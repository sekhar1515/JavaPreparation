package executorservice;/*
DIFFERENCE: scheduleAtFixedRate vs scheduleWithFixedDelay

- scheduleAtFixedRate(task, initialDelay, period, unit)
  * Tries to run the task at a CONSTANT RATE.
  * Next start time = previous SCHEDULED start time + period (not based on finish time).
  * If a run takes longer than the period, the next run starts immediately when the previous one finishes
    (it "catches up") but executions never overlap on the same ScheduledThreadPoolExecutor worker.
  * Useful when you want a steady heartbeat (e.g., "run every 5s") regardless of task duration.
  * If the task throws an exception, future executions are cancelled.

- scheduleWithFixedDelay(task, initialDelay, delay, unit)
  * Runs the task with a CONSTANT GAP BETWEEN FINISH AND NEXT START.
  * Next start time = time when the LAST RUN FINISHED + delay.
  * If a run takes longer, the overall cadence slows down naturally—no catch-up.
  * Useful when you want "rest time" between runs (e.g., polling with a cool-down).
  * If the task throws an exception, future executions are cancelled.
*/

import java.util.concurrent.*;

public class ScheduledExecutor {
    public static void main(String[] args) {
        // ========= 1) Regular fixed thread pool example =========
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        // Submit one task that sleeps for 5 seconds
        executorService.submit(() -> {
            try {
                Thread.sleep(5000); // simulate work
            } catch (InterruptedException e) {
                // If shutdownNow() interrupts this thread, we land here.
                // Throwing RuntimeException will mark this task as failed and stop further execution.
                throw new RuntimeException(e);
            }
            System.out.println("Task completed");
        });

        // Graceful shutdown: no new tasks accepted, existing tasks are allowed to complete.
        executorService.shutdown();

        // FORCEFUL shutdown immediately after graceful one:
        // - Attempts to interrupt running tasks.
        // - Returns a list of tasks that were submitted but never started (not used here).
        // NOTE: Doing shutdownNow() right after shutdown() will likely interrupt the sleeping task above,
        // so "Task completed" may NEVER print.
        executorService.shutdownNow();

        try {
            // Wait up to 2 seconds for the pool to TERMINATE (i.e., finish or abort all tasks and exit).
            // Returns true if terminated within the timeout, false otherwise.
            boolean isExecutorTerminated = executorService.awaitTermination(2, TimeUnit.SECONDS);
            System.out.println("Did executor terminate within 2s? " + isExecutorTerminated);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Main thread after first pool logic");

        // ========= 2) Scheduled executor: one-shot + periodic =========
        ScheduledExecutorService executorServiceScheduled = Executors.newScheduledThreadPool(5);

        // (A) One-shot task: runs once after 1 second
        executorServiceScheduled.schedule(() -> {
            System.out.println("Inside one-shot scheduled task");
        }, 1000, TimeUnit.MILLISECONDS);

        // (B) Fixed-Rate task: start after 2s, then every 5s by SCHEDULE
        // Returns a ScheduledFuture<?> that can be cancelled. get() never completes for periodic tasks.
        ScheduledFuture<?> fixedRateFuture = executorServiceScheduled.scheduleAtFixedRate(() -> {
            System.out.println("Scheduled at a FIXED RATE");
        }, 2, 5, TimeUnit.SECONDS);

        try {
            // Let the fixed-rate task run for ~10s total:
            // Approximate run starts: t≈2s, t≈7s ... (next would be t≈12s).
            Thread.sleep(10000);
            // Cancel future executions. The 'true' requests interruption if currently running.
            fixedRateFuture.cancel(true);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // (C) Fixed-Delay task: start after 2s, then wait 5s AFTER EACH COMPLETION
        ScheduledFuture<?> fixedDelayFuture = executorServiceScheduled.scheduleWithFixedDelay(() -> {
            System.out.println("Scheduled with a FIXED DELAY");
        }, 2, 5, TimeUnit.SECONDS);

        try {
            // Let the fixed-delay task run for ~20s, then cancel it.
            Thread.sleep(20000);
            fixedDelayFuture.cancel(true);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Clean shutdown of the scheduled executor.
        executorServiceScheduled.shutdown();
        try {
            // Wait for scheduled executor to stop cleanly.
            executorServiceScheduled.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Main thread completed");
    }
}
