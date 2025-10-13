package executorservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
Here we go dive deep into
 *   1) SingleThreadExecutor
 *   2) FixedThreadPool
 *   3) CachedThreadPool
 *   4) WorkStealingPool + Fork/Join RecursiveTask
 *
 * The emphasis is on INTERNALS — how each executor is implemented on top of
 * ThreadPoolExecutor (except work-stealing, which uses ForkJoinPool),
 * and what that means for performance, queuing, back-pressure, and safety.
 *
 * Build & Run (Java 11+):
 *   javac ExecutorsExplained.java && java ExecutorsExplained
 */
public class ExecutorsExplained {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Executors Explained (run order matters only for sections) ===");

        sectionSingleThreadExecutor();
        sectionFixedThreadPool();
        sectionCachedThreadPool();
        sectionWorkStealingAndForkJoin();

        System.out.println("\n=== Done ===");
    }

    // -------------------------------------------------------------------------
    // SECTION 1: SingleThreadExecutor
    // -------------------------------------------------------------------------
    private static void sectionSingleThreadExecutor() throws Exception {
        banner("1) SingleThreadExecutor — sequential execution, ordered FIFO");

        /*
         * WHAT IT IS:
         *  - An ExecutorService that guarantees a single worker thread processes tasks
         *    in submission order (FIFO), replacing the thread if it dies.
         *
         * INTERNALS:
         *  - It's a thin wrapper around ThreadPoolExecutor with:
         *        corePoolSize = 1
         *        maximumPoolSize = 1
         *        keepAliveTime = 0
         *        workQueue = LinkedBlockingQueue<Runnable> (unbounded)
         *    Equivalent to:
         *        new ThreadPoolExecutor(1, 1, 0L, MILLISECONDS, new LinkedBlockingQueue<>());
         *
         * IMPLICATIONS:
         *  - No parallelism; tasks queue up while one thread runs them.
         *  - Unbounded queue means: if submitter is faster than consumer forever,
         *    memory can grow (back-pressure is not automatic).
         *  - NEVER block inside a task waiting for another task on the same executor
         *    (self-deadlock risk).
         */
        ExecutorService single = Executors.newSingleThreadExecutor(namedFactory("single"));

        Future<Integer> f1 = single.submit(() -> {
            log("Task A start");
            sleep(150);
            log("Task A end");
            return 1;
        });
        Future<Integer> f2 = single.submit(() -> { log("Task B"); return 2; });
        Future<Integer> f3 = single.submit(() -> { log("Task C"); return 3; });

        log("Results: " + f1.get() + ", " + f2.get() + ", " + f3.get());

        shutdownAndAwait(single);
    }

    // -------------------------------------------------------------------------
    // SECTION 2: FixedThreadPool
    // -------------------------------------------------------------------------
    private static void sectionFixedThreadPool() throws Exception {
        banner("2) FixedThreadPool(n) — bounded concurrency with an unbounded queue");

        /*
         * WHAT IT IS:
         *  - A pool with exactly N worker threads; if all are busy, tasks are queued.
         *
         * INTERNALS:
         *  - Built on ThreadPoolExecutor with:
         *        corePoolSize = n
         *        maximumPoolSize = n
         *        keepAliveTime = 0
         *        workQueue = LinkedBlockingQueue<Runnable> (unbounded)
         *    Equivalent to:
         *        new ThreadPoolExecutor(n, n, 0L, MILLISECONDS, new LinkedBlockingQueue<>());
         *
         * IMPLICATIONS:
         *  - Parallelism capped at n.
         *  - Unbounded queue can hide overload (latency grows, memory grows).
         *  - Good default for CPU-ish workloads where you want a fixed degree of parallelism.
         */
        int n = Math.max(4, Runtime.getRuntime().availableProcessors());
        ExecutorService pool = Executors.newFixedThreadPool(n, namedFactory("fixed"));

        List<Future<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < 2 * n; i++) {
            int taskId = i;
            futures.add(pool.submit(() -> {
                log("fixed: task " + taskId + " running");
                sleep(120);
                return taskId * taskId;
            }));
        }
        for (Future<Integer> f : futures) f.get(); // wait all

        shutdownAndAwait(pool);
    }

    // -------------------------------------------------------------------------
    // SECTION 3: CachedThreadPool
    // -------------------------------------------------------------------------
    private static void sectionCachedThreadPool() throws Exception {
        banner("3) CachedThreadPool — elastic threads, no queue (SynchronousQueue)");

        /*
         * WHAT IT IS:
         *  - A pool that creates threads on demand and reuses idle threads for up to 60s.
         *  - No task buffering: if an idle thread is not available, a new thread is created.
         *
         * INTERNALS:
         *  - ThreadPoolExecutor with:
         *        corePoolSize = 0
         *        maximumPoolSize = Integer.MAX_VALUE   (practically unbounded)
         *        keepAliveTime = 60s
         *        workQueue = SynchronousQueue<Runnable> (a handoff queue; no capacity)
         *
         * IMPLICATIONS:
         *  - Throughput-friendly for short, bursty tasks.
         *  - Can grow to very large thread counts under load (risk of CPU thrash / memory pressure)
         *    if submit rate is high and tasks block or are slow.
         *  - Use when you really want “no queuing” and fast handoff, and trust the workload shape.
         */
        ExecutorService cached = Executors.newCachedThreadPool(namedFactory("cached"));

        List<Future<?>> tasks = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            int taskId = i;
            tasks.add(cached.submit(() -> {
                log("cached: task " + taskId + " running");
                // Simulate short task; cached pools shine here
                sleep(80 + (taskId % 3) * 20);
            }));
        }
        for (Future<?> f : tasks) f.get();

        shutdownAndAwait(cached);
    }

    // -------------------------------------------------------------------------
    // SECTION 4: Work-Stealing Pool + Fork/Join RecursiveTask
    // -------------------------------------------------------------------------
    private static void sectionWorkStealingAndForkJoin() {
        banner("4) WorkStealingPool + ForkJoin RecursiveTask — divide & conquer, work-stealing deques");

        /*
         * WHAT IT IS:
         *  - Executors.newWorkStealingPool() returns an ExecutorService backed by a ForkJoinPool.
         *  - Parallelism defaults to Runtime.availableProcessors().
         *
         * FORK/JOIN INTERNALS (high level):
         *  - Each worker thread maintains a double-ended queue (deque) of tasks.
         *  - A worker executes its own tasks LIFO (pop from tail) to improve cache locality.
         *  - When idle, a worker steals from the HEAD of another worker’s deque (work-stealing).
         *  - Designed for fine-grained, CPU-bound, recursive “split-apply-combine” tasks.
         *  - Blocking calls should be avoided; or use ForkJoinPool.ManagedBlocker if necessary.
         *
         * API SHAPES:
         *  - RecursiveTask<V>: returns V
         *  - RecursiveAction:  returns void
         *
         * DEMO:
         *  - We compute the sum of a large array using a RecursiveTask, with threshold cutoff
         *    for the “leaf” computation size.
         */
        int size = 1_000_00; // 100k
        long[] data = new Random(42).longs(size, 1, 10).toArray();
        SumTask root = new SumTask(data, 0, data.length);

        // You can use the commonPool() or a custom ForkJoinPool:
        ForkJoinPool fjp = ForkJoinPool.commonPool();
        long sum = fjp.invoke(root);
        log("ForkJoin sum = " + sum + " (expected ~ " + approxExpected(data) + ")");
    }

    // A RecursiveTask that sums a slice of the array; demonstrates fork/join
    static class SumTask extends RecursiveTask<Long> {
        private static final int THRESHOLD = 5_000; // tune cutoff: too small -> overhead; too big -> less parallelism
        private final long[] arr;
        private final int lo, hi;

        SumTask(long[] arr, int lo, int hi) {
            this.arr = arr; this.lo = lo; this.hi = hi;
        }

        @Override
        protected Long compute() {
            int len = hi - lo;
            if (len <= THRESHOLD) {
                long s = 0;
                for (int i = lo; i < hi; i++) s += arr[i];
                return s;
            }
            // Split the task; a common, efficient pattern is: fork left, compute right, join left
            int mid = lo + len / 2;
            SumTask left  = new SumTask(arr, lo,  mid);
            SumTask right = new SumTask(arr, mid, hi);

            left.fork();               // enqueue 'left' for potential stealing
            long r = right.compute();  // compute right in current thread
            long l = left.join();      // wait for the forked left half
            return l + r;
        }
    }

    // -------------------------------------------------------------------------
    // =============== ThreadPoolExecutor INTERNALS CHEAT SHEET =================
    // -------------------------------------------------------------------------
    /*
     * ThreadPoolExecutor core concepts (applies to Single/Fixed/Cached):
     *
     *  execute(command):
     *    1) If workerCount < corePoolSize -> start a new worker to run command.
     *    2) Else, try to enqueue command in workQueue.
     *    3) If queue is full AND workerCount < maximumPoolSize -> start non-core worker.
     *    4) Else -> reject via RejectedExecutionHandler.
     *
     *  Work queues:
     *    - LinkedBlockingQueue (unbounded): queues tasks; back-pressure only via memory/latency.
     *    - ArrayBlockingQueue (bounded): enables explicit back-pressure via rejection.
     *    - SynchronousQueue: zero-capacity handoff; prefers spawning up to maximumPoolSize.
     *    - PriorityBlockingQueue: tasks ordered by priority (Comparable/Comparator).
     *
     *  Lifecycle states: RUNNING -> SHUTDOWN -> STOP -> TIDYING -> TERMINATED
     *    - shutdown(): reject new tasks; process queued; no interrupts.
     *    - shutdownNow(): attempt to cancel in-flight (interrupt), drain queue and return the tasks.
     *
     *  KeepAlive:
     *    - Non-core threads time out after keepAliveTime (Cached uses 60s). You can allow core
     *      threads to time out via allowCoreThreadTimeOut(true).
     *
     * Best practices:
     *    - Choose queueing strategy by workload shape.
     *    - Name your threads for observability (thread dumps, logs, metrics).
     *    - Always shut down executors to avoid leaks / hanging JVMs.
     *    - CPU-bound: parallelism ≈ #cores; IO-bound: higher parallelism; avoid blocking in FJP.
     */

    // -------------------------------------------------------------------------
    // Utilities
    // -------------------------------------------------------------------------
    private static ThreadFactory namedFactory(String prefix) {
        AtomicInteger seq = new AtomicInteger(1);
        return r -> {
            Thread t = new Thread(r);
            t.setName(prefix + "-" + seq.getAndIncrement());
            t.setDaemon(false);
            return t;
        };
    }

    private static void shutdownAndAwait(ExecutorService svc) throws InterruptedException {
        svc.shutdown(); // stop accepting new tasks; let queued run
        if (!svc.awaitTermination(5, TimeUnit.SECONDS)) {
            // If tasks are stuck, try to forcefully stop
            List<Runnable> dropped = svc.shutdownNow();
            log("Forced shutdown; dropped queued tasks = " + dropped.size());
            // Best-effort wait again
            svc.awaitTermination(3, TimeUnit.SECONDS);
        }
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    private static void log(String msg) {
        System.out.printf("[%s] %s%n", Thread.currentThread().getName(), msg);
    }

    private static void banner(String title) {
        System.out.println("\n-----------------------------");
        System.out.println(title);
        System.out.println("-----------------------------");
    }

    private static long approxExpected(long[] arr) {
        long min = Long.MAX_VALUE, max = Long.MIN_VALUE, sum = 0;
        for (long v : arr) { min = Math.min(min, v); max = Math.max(max, v); sum += v; }
        return sum; // exact sum (used just for sanity in this demo)
    }
}
