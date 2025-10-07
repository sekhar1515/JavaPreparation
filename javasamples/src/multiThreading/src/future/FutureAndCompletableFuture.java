package future;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class FutureAndCompletableFuture {

    public static void main(String[] args) throws Exception {
        // ---------- 1) Custom thread pool ----------
        // core=3 so we can truly run 3 tasks concurrently without queueing.
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                3, 3, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), // unbounded queue (fine for demo)
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );

        try {
            /* ===============================================================
             * 2) Classic Future: Runnable vs Callable (blocking style)
             * =============================================================== */
            // Runnable -> Future<?> returns null on get()
            Future<?> runnableFuture = executor.submit(() -> {
                sleep(500);
                System.out.println("[Future Runnable] work done on " + Thread.currentThread().getName());
            });

            // Callable -> Future<T> returns a value (blocking get)
            Future<Integer> callableFuture = executor.submit(() -> {
                sleep(800);
                System.out.println("[Future Callable] work done on " + Thread.currentThread().getName());
                return 10;
            });

            // Blocking waits
            runnableFuture.get(); // returns null
            System.out.println("[Future Callable] result = " + callableFuture.get()); // 10

            /* ===============================================================
             * 3) CompletableFuture: supplyAsync -> thenApplyAsync
             *    (ALL stages pinned to our executor)
             * =============================================================== */
            CompletableFuture<Integer> cfApply = CompletableFuture
                    .supplyAsync(() -> {
                        System.out.println("[CF thenApply] supply on " + Thread.currentThread().getName());
                        return 10;
                    }, executor)
                    .thenApplyAsync(i -> {
                        System.out.println("[CF thenApply] transform on " + Thread.currentThread().getName());
                        return i + 20; // 30
                    }, executor);

            System.out.println("[CF thenApply] result = " + cfApply.get()); // 30

            /* ===============================================================
             * 4) thenComposeAsync: chain DEPENDENT async steps (flat-map)
             *    (keep both inner & outer on our executor)
             * =============================================================== */
            CompletableFuture<Integer> cfCompose = CompletableFuture
                    .supplyAsync(() -> {
                        System.out.println("[CF thenCompose] stage-1 on " + Thread.currentThread().getName());
                        return 10;
                    }, executor)
                    .thenComposeAsync(i -> CompletableFuture.supplyAsync(() -> {
                        System.out.println("[CF thenCompose] stage-2 on " + Thread.currentThread().getName());
                        return i * 100; // 1000
                    }, executor), executor);

            System.out.println("[CF thenCompose] result = " + cfCompose.get()); // 1000

            /* ===============================================================
             * 5) thenCombineAsync: combine INDEPENDENT futures
             *    (both run in parallel, then combine on our executor)
             * =============================================================== */
            CompletableFuture<List<Integer>> cfList = CompletableFuture.supplyAsync(() -> {
                System.out.println("[CF thenCombine] list supplier on " + Thread.currentThread().getName());
                return Arrays.asList(1, 2, 3);
            }, executor);

            CompletableFuture<Integer> cfCombined = cfCompose.thenCombineAsync(cfList, (i1, list) -> {
                System.out.println("[CF thenCombine] combiner on " + Thread.currentThread().getName());
                int sum = list.stream().reduce(0, Integer::sum); // 6
                return sum * i1; // 6 * 1000 = 6000
            }, executor);

            System.out.println("[CF thenCombine] result = " + cfCombined.get()); // 6000

            /* ===============================================================
             * 6) allOf + collecting results:
             *    - launch 3 independent tasks truly in parallel (core=3)
             *    - wait for all
             *    - collect each result into a List<Integer>
             * =============================================================== */
            CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> {
                System.out.println("[allOf] f1 on " + Thread.currentThread().getName());
                sleep(1000);
                return 100;
            }, executor);

            CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(() -> {
                System.out.println("[allOf] f2 on " + Thread.currentThread().getName());
                sleep(1000);
                return 200;
            }, executor);

            CompletableFuture<Integer> f3 = CompletableFuture.supplyAsync(() -> {
                System.out.println("[allOf] f3 on " + Thread.currentThread().getName());
                sleep(1000);
                return 300;
            }, executor);

            // Barrier that completes when ALL are done.
            CompletableFuture<Void> all = CompletableFuture.allOf(f1, f2, f3);

            // Option A: Side-effect logging + wait
            all.whenCompleteAsync((v, ex) -> {
                if (ex == null) {
                    System.out.println("[allOf] All completed successfully");
                } else {
                    System.err.println("[allOf] At least one failed: " + ex);
                }
            }, executor).join();

            // Option B: After 'all' completes, COLLECT individual results
            List<Integer> results = Stream.of(f1, f2, f3)
                    .map(CompletableFuture::join) // safe here because 'all' already completed
                    .toList();

            System.out.println("[allOf] results = " + results);           // [100, 200, 300]
            System.out.println("[allOf] sum     = " + results.stream().mapToInt(Integer::intValue).sum()); // 600

            /* ===============================================================
             * 7) Bonus: error handling with exceptionally / handle / whenComplete
             * =============================================================== */
            CompletableFuture<Integer> cfErrorHandled = CompletableFuture
                    .supplyAsync(() -> {
                        System.out.println("[Error] risky on " + Thread.currentThread().getName());
                        if (true) throw new RuntimeException("Boom!");
                        return 42;
                    }, executor)
                    .exceptionally(ex -> {
                        System.out.println("[Error] handled: " + ex.getMessage());
                        return -1; // fallback
                    });

            System.out.println("[Error] result = " + cfErrorHandled.get()); // -1

        } finally {
            // ---------- graceful shutdown ----------
            executor.shutdown();
        }
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }
}
