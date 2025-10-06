package threadpoolexecutor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This file explains ThreadPoolExecutor behavior in detail, straight in comments.
 *
 * POOL CONFIG USED HERE
 * ------------------------------------------------------------------------------
 * corePoolSize = 2
 * maximumPoolSize = 4
 * keepAliveTime = 10 MINUTES (applies to non-core threads by default)
 * workQueue = ArrayBlockingQueue<>(2)
 * threadFactory = CustomThreadFactory("demo-pool")        // custom naming, priority, daemon, etc.
 * rejectedHandler = CustomRejectedHandler()                // logs rejections; does NOT throw
 *
 * DECISION RULES WHEN SUBMITTING A TASK (execute/submit both delegate to execute)
 * ------------------------------------------------------------------------------
 * 1) If runningThreads < corePoolSize        -> create a new thread and run task.
 * 2) Else if queue not full                  -> enqueue task.
 * 3) Else if runningThreads < maximumPoolSize-> create a new (non-core) thread and run task.
 * 4) Else                                    -> reject task via RejectedExecutionHandler.
 *
 * WHAT HAPPENS IN THIS PROGRAM WHEN WE RAPIDLY SUBMIT 10 TASKS (2s each):
 * ------------------------------------------------------------------------------
 * - Tasks 1–2: create threads T1, T2 (fill core).
 * - Tasks 3–4: go into queue (queue capacity = 2).
 * - Tasks 5–6: queue full, but threads < max (=4) -> create T3, T4.
 * - Tasks 7–10: threads at max AND queue full -> REJECTED (4 rejections).
 *   (Because all submissions happen quickly; none finish before we finish submitting.)
 *
 * KEEP-ALIVE + THREAD LIFETIME
 * ------------------------------------------------------------------------------
 * - Non-core threads (T3, T4) may time out after 10 minutes of idleness.
 * - Core threads DO NOT time out unless you call allowCoreThreadTimeOut(true).
 *
 * IMPORTANT: submit() vs execute()
 * ------------------------------------------------------------------------------
 * - execute(Runnable) -> on reject:
 *     * AbortPolicy throws RejectedExecutionException in the calling thread.
 *     * Other handlers do what they say (run in caller / drop / drop oldest / custom).
 * - submit(Callable/Runnable) wraps tasks in a Future and calls execute().
 *     * If the handler DOES NOT throw (e.g., DiscardPolicy or your custom logger),
 *       submit() will return a Future that will NEVER complete because the task never ran.
 *       -> Easy to miss rejections if you don’t check futures!
 *     * If the handler throws (AbortPolicy), submit() will throw RejectedExecutionException.
 *
 * REJECTION POLICIES — WHEN THE POOL IS "FULL"
 * ------------------------------------------------------------------------------
 * 1) AbortPolicy (default):
 *    - Behavior: throws RejectedExecutionException immediately.
 *    - Use when: you want to FAIL FAST and be forced to handle overload properly
 *      (e.g., shed load upstream, open circuit, return HTTP 503, etc.).
 *
 * 2) CallerRunsPolicy:
 *    - Behavior: runs the task in the calling thread (the thread that called submit/execute).
 *    - Effect: provides **backpressure** — the submitter slows down and stops flooding the pool.
 *    - Caveat: if the caller is the request thread (e.g., servlet), it may BLOCK handling other
 *      requests; use carefully to avoid head-of-line blocking or latency spikes.
 *
 * 3) DiscardPolicy:
 *    - Behavior: silently drops the task.
 *    - Danger: when used with submit(), you might never notice rejections unless you check the Future.
 *    - Use when: you can **safely drop** work (e.g., telemetry sampling) and you monitor drops.
 *
 * 4) DiscardOldestPolicy:
 *    - Behavior: removes the **oldest** task from the queue (likely a stale one), then retries execute().
 *    - Good for: keeping queue "fresh" for newest data (e.g., UIs) when older tasks have little value.
 *    - Caveat: a second rejection is still possible if the conditions haven’t improved.
 *
 * CUSTOM RejectedExecutionHandler (this demo):
 * ------------------------------------------------------------------------------
 * - We log the rejected task (by default you'll see a FutureTask toString).
 * - We DO NOT throw — so submit() calls will NOT fail fast. If you care,
 *   either throw or track/alert on rejections.
 *
 * CUSTOM ThreadFactory — why and what to set:
 * ------------------------------------------------------------------------------
 * - Naming: critical for debugging. We add a prefix and an incrementing thread number.
 * - Daemon: false (non-daemon) so the JVM waits for tasks to finish on shutdown.
 * - Priority: normally Thread.NORM_PRIORITY. Avoid MAX unless you *really* mean it.
 * - UncaughtExceptionHandler: log or report crashes that happen inside pool threads.
 * - You can also set ThreadGroup, contextClassLoader, MDC, etc., as needed.
 *
 * QUEUE CHOICE IMPACTS SCALING:
 * ------------------------------------------------------------------------------
 * - ArrayBlockingQueue(capacity N)      : bounded; once full, pool can grow up to max, then reject.
 * - LinkedBlockingQueue(unbounded)      : effectively disables maxPoolSize growth (pool stays at core).
 * - SynchronousQueue (capacity 0)       : no queue; either hand off to a thread or create a new one
 *                                         (up to max), else reject. Great for direct handoff patterns.
 *
 * GRACEFUL SHUTDOWN PATTERN:
 * ------------------------------------------------------------------------------
 * pool.shutdown();                        // stop accepting new tasks
 * if (!pool.awaitTermination(…)) {        // wait for in-flight to finish
 *     pool.shutdownNow();                 // cancel queued / interrupt workers
 * }
 */
public class ThreadPoolExecutorSampleDemo {
    public static void main(String[] args) throws InterruptedException {
        // Choose ONE rejection policy. We’ll wire our custom logger by default.
        RejectedExecutionHandler rejectionHandler = new CustomRejectedHandler();

        // If you want classic policies instead, uncomment ONE of these:
        // rejectionHandler = new ThreadPoolExecutor.AbortPolicy();        // fail fast
        // rejectionHandler = new ThreadPoolExecutor.CallerRunsPolicy();   // backpressure
        // rejectionHandler = new ThreadPoolExecutor.DiscardPolicy();      // drop silently
        // rejectionHandler = new ThreadPoolExecutor.DiscardOldestPolicy();// drop oldest, then retry

        ThreadFactory threadFactory = new CustomThreadFactory("demo-pool");

        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(
                        2,                       // corePoolSize: always kept alive (unless you allow timeouts)
                        4,                       // maximumPoolSize
                        10, TimeUnit.MINUTES,    // keepAlive (for NON-core threads)
                        new ArrayBlockingQueue<>(2), // bounded queue -> triggers growth, then rejection
                        threadFactory,
                        rejectionHandler
                );

        // Optional: prestart core threads so the first tasks don’t pay thread-creation cost.
        // threadPoolExecutor.prestartAllCoreThreads();

        // SUBMIT 10 quick tasks that each take ~2s, which will trigger rejections as explained above.
        // NOTE: Using submit() with a non-throwing handler means rejections may be silent unless you check futures.
        // To *see* rejections clearly, either:
        //   (A) use execute(), which won’t give you a Future but pairs nicely with logging handlers, OR
        //   (B) keep Futures and inspect them.
        for (int i = 0; i < 10; i++) {
            final int taskId = i + 1;
            // ---- Option A: clearer for demos (no Future); good with logging/custom handlers:
            threadPoolExecutor.execute(() -> doWork(taskId));

            // ---- Option B: if you need a Future, also handle possible silent rejections:
            // Future<?> f = threadPoolExecutor.submit(() -> doWork(taskId));
            // // If you used a non-throwing handler, rejected tasks will return a Future that never completes.
            // // You could detect that later with timeouts, metrics, or by wrapping the handler to mark rejected Futures.
        }

        // Initiate a graceful shutdown
        threadPoolExecutor.shutdown(); // stop accepting new tasks, finish what’s queued/running

        // Wait for completion (good hygiene for demos/tools; in servers you often rely on lifecycle hooks)
        if (!threadPoolExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
            // If tasks are still not finished, force an aggressive shutdown:
            threadPoolExecutor.shutdownNow();
        }
    }

    private static void doWork(int taskId) {
        try {
            // Simulate some work
            Thread.sleep(2000);
            System.out.printf("Task #%d is executing on %s%n", taskId, Thread.currentThread().getName());
        } catch (InterruptedException e) {
            // If shutdownNow() interrupts us, we should handle it gracefully.
            Thread.currentThread().interrupt();
            System.out.printf("Task #%d interrupted on %s%n", taskId, Thread.currentThread().getName());
        }
    }
}

/**
 * Custom ThreadFactory:
 * - Gives threads readable names like "demo-pool-1", "demo-pool-2", ...
 * - Sets daemon=false so the JVM waits for tasks to complete on shutdown.
 * - Sets a sensible priority and an UncaughtExceptionHandler.
 *
 * TIP: Naming is GOLD when debugging pool behavior in logs and thread dumps.
 */
class CustomThreadFactory implements ThreadFactory {
    private final String namePrefix;
    private final AtomicInteger count = new AtomicInteger(1);

    CustomThreadFactory(String namePrefix) {
        this.namePrefix = (namePrefix == null || namePrefix.isBlank()) ? "pool" : namePrefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, namePrefix + "-" + count.getAndIncrement());
        t.setDaemon(false);                         // non-daemon so app doesn’t exit early
        t.setPriority(Thread.NORM_PRIORITY);        // avoid starvation of other threads
        t.setUncaughtExceptionHandler((thr, ex) -> {
            // Log/report: unhandled exceptions inside pool tasks
            System.err.println("Uncaught in " + thr.getName() + ": " + ex);
        });
        return t;
    }
}

/**
 * Custom RejectedExecutionHandler:
 * - Logs the rejected task and current pool/queue state for visibility.
 * - DOES NOT throw. This means submit() won’t throw, and you could miss the rejection
 *   unless you watch logs/metrics. If you want fail-fast, throw a RejectedExecutionException here.
 *
 * Consider augmenting this to:
 * - increment a metric
 * - enqueue to a secondary buffer / dead-letter queue
 * - trigger circuit-breaking / backpressure upstream
 */
class CustomRejectedHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        int active = executor.getActiveCount();
        int poolSize = executor.getPoolSize();
        int queued = executor.getQueue().size();
        int remaining = executor.getQueue().remainingCapacity();

        System.err.println(
                "REJECTED task: " + r +
                        " | active=" + active +
                        " | poolSize=" + poolSize +
                        " | queued=" + queued +
                        " | queueRemainingCapacity=" + remaining
        );

        // If you prefer fail-fast, uncomment:
        // throw new RejectedExecutionException("Task rejected: " + r);
    }
}
