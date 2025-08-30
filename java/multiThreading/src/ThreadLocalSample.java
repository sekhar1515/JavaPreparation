/**
 * ThreadLocalSample demonstrates how each thread in Java
 * can maintain its own independent copy of a variable
 * using ThreadLocal.
 *
 * How it works internally:
 * ------------------------
 * 1. Each Thread object in Java has an internal ThreadLocalMap.
 * 2. When you call threadLocal.set(value):
 *      - The current thread is identified.
 *      - That thread’s ThreadLocalMap stores an entry:
 *        (key = current ThreadLocal instance, value = value you set).
 * 3. When you call threadLocal.get():
 *      - The current thread’s ThreadLocalMap is checked.
 *      - The value associated with the current ThreadLocal instance is returned.
 * 4. When you call threadLocal.remove():
 *      - The current thread’s ThreadLocalMap entry for this ThreadLocal is removed.
 *
 * Key points:
 * - Each thread gets a completely isolated value.
 * - Changes in one thread do not affect others.
 * - Important to call remove() when done, to prevent memory leaks (especially in thread pools).
 *
 * Common Use Cases:
 * -----------------
 * 1. Storing per-thread session or user data in web applications.
 * 2. Keeping thread-specific database connection objects.
 * 3. Providing each thread its own instance of a non-thread-safe class (e.g., SimpleDateFormat).
 * 4. Avoiding passing the same contextual data through multiple method parameters.
 * 5. Maintaining transaction contexts in frameworks like Spring or Hibernate.
 */
public class ThreadLocalSample {
    public static void main(String[] args) {

        // Create a ThreadLocal variable to store an Integer for each thread
        ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

        // First thread
        Thread t1 = new Thread(() -> {
            threadLocal.set(10); // For thread t1, store (threadLocal -> 10) in its ThreadLocalMap

            // Uncomment to pause the thread and simulate delay
            // try {
            //     Thread.sleep(2000);
            // } catch (InterruptedException ie) {
            //     System.out.println(ie);
            // }

            threadLocal.set(200); // Overwrite t1's value in its own ThreadLocalMap
            System.out.println("Value for thread 1 is : " + threadLocal.get()); // Reads from t1's map

            threadLocal.remove(); // Remove this entry from t1's ThreadLocalMap
            threadLocal.set(20);  // Add a new entry after removal
            System.out.println("Removal of value from thread local and adding it again: "
                    + threadLocal.get());
        });

        // Second thread
        Thread t2 = new Thread(() -> {
            threadLocal.set(100); // For thread t2, store (threadLocal -> 100) in its own ThreadLocalMap
            System.out.println("Value is : " + threadLocal.get()); // Reads from t2's map
        });

        // Start both threads
        t1.start();
        t2.start();
    }
}
