package future;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FutureAndCompletableFuture {
    public static void main(String[] args) throws Exception {

        /* ===============================================================
         * THREAD POOL CREATION
         * ===============================================================
         * - Creating a custom ThreadPoolExecutor so we can control
         *   concurrency and avoid using the common ForkJoinPool.
         * - corePoolSize = 3 → three threads are always available
         * - maximumPoolSize = 3 → fixed pool of size 3 (no extra threads)
         * - keepAliveTime = 0 → not relevant since pool size is fixed
         * - LinkedBlockingQueue → holds excess tasks if all threads are busy
         * =============================================================== */
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                3, 3, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );

        try {
            /* ===============================================================
             * PART 1: CLASSIC FUTURE (introduced in Java 5)
             * ===============================================================
             * - Represents the result of an asynchronous computation.
             * - Can only:
             *     ✅ start a task
             *     ✅ block and get the result
             *     ✅ cancel the task
             *     ✅ check if it’s done
             * - Cannot:
             *     ❌ chain tasks
             *     ❌ handle errors gracefully
             *     ❌ run callbacks on completion
             * - It’s basically a “receipt” for a background computation.
             * =============================================================== */

            // Runnable example: no return value
            Future<?> runnableFuture = executor.submit(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("[Future Runnable] Executing on " + Thread.currentThread().getName());
            });

            // Callable example: has a return value
            Future<Integer> callableFuture = executor.submit(() -> {
                Thread.sleep(800);
                System.out.println("[Future Callable] Executing on " + Thread.currentThread().getName());
                return 10;
            });

            // Blocking call — waits until runnableFuture completes.
            // For Runnable, the Future’s get() always returns null.
            runnableFuture.get();

            // Blocking call — waits for result (approx. 800 ms)
            // get() throws checked exceptions: InterruptedException, ExecutionException
            System.out.println("[Future Callable] Result returned = " + callableFuture.get());

            /* ===============================================================
             * KEY POINTS ABOUT FUTURE:
             * ===============================================================
             * - The main thread blocks on get(), which defeats full asynchrony.
             * - No easy way to chain logic like “then do this next”.
             * - No built-in way to handle errors or timeouts gracefully.
             * - Future is good for simple, one-off background tasks.
             * =============================================================== */


            /* ===============================================================
             * PART 2: COMPLETABLEFUTURE (introduced in Java 8)
             * ===============================================================
             * - Extends Future but adds powerful composition and reactive APIs.
             * - Supports:
             *     ✅ non-blocking chaining (thenApply, thenAccept, etc.)
             *     ✅ async variants that can run on any Executor
             *     ✅ combination of multiple tasks (thenCombine, allOf, etc.)
             *     ✅ exception handling (exceptionally, handle, whenComplete)
             *     ✅ manual completion (complete(), completeExceptionally())
             * - It implements both Future and CompletionStage.
             * =============================================================== */


            /* ---------------------------------------------------------------
             * Example 1: supplyAsync() → thenApplyAsync()
             * ---------------------------------------------------------------
             * - supplyAsync() runs supplier code asynchronously.
             * - thenApplyAsync() transforms the result in another async stage.
             * - All tasks here are executed using our custom executor.
             * - Shows how to compose non-blocking tasks without calling get().
             * --------------------------------------------------------------- */
            CompletableFuture<Integer> cfApply = CompletableFuture
                    .supplyAsync(() -> {
                        System.out.println("[CF thenApply] Supplying value 10 on " + Thread.currentThread().getName());
                        return 10;
                    }, executor)
                    .thenApplyAsync(i -> {
                        System.out.println("[CF thenApply] Transforming value on " + Thread.currentThread().getName());
                        return i + 20;  // return 30
                    }, executor);

            System.out.println("[CF thenApply] Final result = " + cfApply.get()); // blocking here just for demo


            /* ---------------------------------------------------------------
             * Example 2: thenComposeAsync()
             * ---------------------------------------------------------------
             * - Used for dependent tasks (the second depends on the first).
             * - Similar to flatMap in streams.
             * - Unwraps the nested CompletableFuture automatically.
             * --------------------------------------------------------------- */
            CompletableFuture<Integer> cfCompose = CompletableFuture
                    .supplyAsync(() -> {
                        System.out.println("[CF thenCompose] Stage-1 producing 10 on " + Thread.currentThread().getName());
                        return 10;
                    }, executor)
                    .thenComposeAsync(i -> CompletableFuture.supplyAsync(() -> {
                        System.out.println("[CF thenCompose] Stage-2 consuming value on " + Thread.currentThread().getName());
                        return i * 100; // returns 1000
                    }, executor), executor);

            System.out.println("[CF thenCompose] Final result = " + cfCompose.get()); // 1000


            /* ---------------------------------------------------------------
             * Example 3: thenCombineAsync()
             * ---------------------------------------------------------------
             * - Used to combine two INDEPENDENT futures once both are complete.
             * - Useful when both tasks can run in parallel and their results
             *   are combined afterwards.
             * --------------------------------------------------------------- */
            CompletableFuture<List<Integer>> cfList = CompletableFuture.supplyAsync(() -> {
                System.out.println("[CF thenCombine] Creating list on " + Thread.currentThread().getName());
                return Arrays.asList(1, 2, 3);
            }, executor);

            CompletableFuture<Integer> cfCombined = cfCompose.thenCombineAsync(cfList, (num, list) -> {
                System.out.println("[CF thenCombine] Combining results on " + Thread.currentThread().getName());
                int sum = list.stream().reduce(0, Integer::sum); // 6
                return num * sum; // 1000 * 6 = 6000
            }, executor);

            System.out.println("[CF thenCombine] Final result = " + cfCombined.get()); // 6000


            /* ---------------------------------------------------------------
             * Example 4: allOf()
             * ---------------------------------------------------------------
             * - Used to wait for completion of multiple independent futures.
             * - allOf returns CompletableFuture<Void>
             * - It completes when *all* provided futures complete.
             * - If any fail, allOf completes exceptionally.
             * - We use whenComplete() to observe completion or failure.
             * --------------------------------------------------------------- */
            CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> {
                System.out.println("[CF allOf] f1 running on " + Thread.currentThread().getName());
                sleep(1000);
                return 100;
            }, executor);

            CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(() -> {
                System.out.println("[CF allOf] f2 running on " + Thread.currentThread().getName());
                sleep(1000);
                return 200;
            }, executor);

            CompletableFuture<Integer> f3 = CompletableFuture.supplyAsync(() -> {
                System.out.println("[CF allOf] f3 running on " + Thread.currentThread().getName());
                sleep(1000);
                return 300;
            }, executor);

            // Barrier for all tasks
            CompletableFuture<Void> all = CompletableFuture.allOf(f1, f2, f3);

            // whenComplete() runs after allOf completes (success or failure)
            all.whenCompleteAsync((v, ex) -> {
                if (ex == null)
                    System.out.println("[CF allOf] All tasks completed successfully!");
                else
                    System.err.println("[CF allOf] One of the tasks failed: " + ex);
            }, executor).join(); // join() waits for completion without checked exceptions

            // Collect individual results from futures since allOf() returns Void
            List<Integer> results = Stream.of(f1, f2, f3)
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());

            System.out.println("[CF allOf] Results: " + results); // [100, 200, 300]
            System.out.println("[CF allOf] Sum of results: " + results.stream().mapToInt(Integer::intValue).sum());


            /* ---------------------------------------------------------------
             * Example 5: Exception handling
             * ---------------------------------------------------------------
             * - exceptionally(): provide fallback value if exception occurs.
             * - handle(): runs for both success and failure, can recover.
             * - whenComplete(): observe result or exception (side-effect).
             * --------------------------------------------------------------- */
            CompletableFuture<Object> cfErrorHandled = CompletableFuture
                    .supplyAsync(() -> {
                        System.out.println("[CF Error] Running risky computation on " + Thread.currentThread().getName());
                        throw new RuntimeException("Simulated failure!");
                    }, executor)
                    .exceptionally(ex -> {
                        System.out.println("[CF Error] Exception handled: " + ex.getMessage());
                        return -1; // fallback value
                    });

            System.out.println("[CF Error] Final result (with fallback) = " + cfErrorHandled.get());


            /* ===============================================================
             * KEY POINTS ABOUT COMPLETABLEFUTURE:
             * ===============================================================
             * - Non-blocking and composable; you can chain many stages.
             * - Async variants can use either common ForkJoinPool or custom Executor.
             * - thenCompose() = dependent chaining (flatMap)
             * - thenCombine() = independent merging (zip)
             * - allOf() / anyOf() = coordination utilities for multiple tasks.
             * - Built-in error recovery (exceptionally, handle).
             * - You can manually complete() or cancel() a CompletableFuture.
             * =============================================================== */

        } finally {
            // Always shut down the executor to release threads gracefully.
            executor.shutdown();
        }
    }

    // Helper method to simplify Thread.sleep()
    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }
}
