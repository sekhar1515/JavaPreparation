import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Demonstrates the usage of {@link ReentrantLock} in Java.
 * <p>
 * A ReentrantLock allows the same thread to acquire the lock multiple times
 * without causing a deadlock. Other lock implementations generally block
 * a thread indefinitely if it tries to re-acquire a lock it already holds.
 * <p>
 * The constructor accepts a "fairness" flag:
 * - {@code true}  → Fair lock: Threads acquire the lock in the order they requested it (FIFO),
 *   reducing the chance of starvation.
 * - {@code false} → Non-fair lock: No strict ordering; some threads may wait longer.
 * <p>
 * If no value is passed, the default is {@code false} (non-fair).
 *
 * <h3>Popular Lock Methods</h3>
 * <ul>
 *   <li>{@link Lock#lock()} – Acquires the lock, blocking until available. Always release in {@code finally}.</li>
 *   <li>{@link Lock#lockInterruptibly()} – Acquires the lock unless the thread is interrupted while waiting.</li>
 *   <li>{@link Lock#tryLock()} – Attempts to acquire immediately; returns {@code true} if successful.</li>
 *   <li>{@link Lock#tryLock(long, TimeUnit)} – Attempts to acquire within a given time limit.</li>
 *   <li>{@link Lock#unlock()} – Releases the lock; must be called by the thread that holds it.</li>
 *   <li>{@link Lock#newCondition()} – Creates a {@link Condition} for finer thread coordination.</li>
 * </ul>
 */
public class ThreadLockDemo {
    public static void main(String[] args) {
        // Create a non-fair ReentrantLock (default is false if not specified)
        Lock lock = new ReentrantLock(false);

        Runnable runnable = () -> lockUnlockThread(lock, 1000);

        Thread t1 = new Thread(runnable, "Thread1");
        Thread t2 = new Thread(runnable, "Thread2");
        Thread t3 = new Thread(runnable, "Thread3");

        t1.start();
        t2.start();
        t3.start();
    }

    /**
     * Acquires a lock, performs work, and then releases the lock.
     * <p>
     * Steps:
     * 1. {@link Lock#lock()} → Acquires the lock, blocking until available.
     * 2. Simulates work by sleeping for the specified duration.
     * 3. {@link Lock#unlock()} → Releases the lock inside {@code finally} to prevent deadlocks.
     *
     * @param lock         the Lock instance
     * @param milliSeconds how long to hold the lock before releasing
     */
    private static void lockUnlockThread(Lock lock, int milliSeconds) {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + " holds the lock");
            Thread.sleep(milliSeconds); // Simulate work
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted");
        } finally {
            lock.unlock();
        }
    }
}
