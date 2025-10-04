package daemon;

/*
-------------------------
🔹 Daemon Threads:
- Used for background tasks, e.g., garbage collection, housekeeping.
- JVM does NOT wait for daemon threads when shutting down.
- Example: Thread.setDaemon(true);

🔹 User (Non-Daemon) Threads:
- Normal threads which JVM waits for before shutting down.
- Example: Main thread, worker threads.

🔹 join() Method:
- Causes current thread to wait until the target thread completes.
- If the target is a daemon, and JVM exits before it completes, it may never join properly.

-------------------------
⚠️ Deprecated Thread Methods (not recommended):
1. stop():
   - Forcibly terminates a thread.
   - Dangerous because it can leave shared objects in an inconsistent state.
   - Deprecated since JDK 1.2.

2. suspend():
   - Suspends a thread’s execution.
   - Deprecated because it can cause deadlocks (if the suspended thread holds a lock,
     no one else can acquire it, and it never resumes).

3. resume():
   - Resumes a suspended thread.
   - Deprecated for same reason as suspend().

✅ Instead of these, use:
- For stopping: a flag variable checked inside the thread loop.
- For suspending/pausing: use wait()/notify() or higher-level concurrency utilities.

-------------------------
*/
public class Main {
    public static void main(String[] args) throws InterruptedException {
        SharedItem sharedItem = new SharedItem();

        // Creating Thread t1 as a daemon thread
        // Daemon threads are "background service" threads, like GC, that do not prevent
        // the JVM from exiting. If only daemon threads are running, JVM terminates.
        Thread t1 = new Thread(() -> {
            sharedItem.getItem();
        }, "Thread 1");

        t1.setDaemon(true); // Marking t1 as a daemon thread
        t1.start();

        System.out.println("Main Thread run is finished ");

        // Creating Thread t2 as a normal (user) thread
        // User threads are the "main" work threads. JVM waits for them to finish before exiting.
        Thread t2 = new Thread(() -> {
            sharedItem.getItem();
        }, "Thread 2");

        t2.start();

        // join() makes the calling thread (here, main thread) wait until t1 terminates.
        // BUT since t1 is a daemon, it may never complete if JVM exits earlier.
        t1.join();
    }
}
