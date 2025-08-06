public class ThreadSample {
    public static void main(String[] args) {
        Runnable runnable = () -> {
            for (int i = 0; i < 5; i++) {
                sleep(1000);
                System.out.println("Thread daemon");
                System.out.println("Thread name : " + Thread.currentThread().getName());
            }
        };

        // Since this is set as Daemon it doesn't wait for execution of this thread and doesn't print
        // anything
        Thread t1 = new Thread(runnable, "Daemon Thread");
        t1.setDaemon(true);
        t1.start();

        // Daemon threads are light weight threads that will be executed seperately and will stop if the main thread terminates.
        // For the daemon thread to execute with the main thread we should use join method.

        Thread t2 = new Thread(runnable, "Thread t2");
        t2.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sleep(int milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
