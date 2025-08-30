public class SynchronizationSample {
    private int count;

    private int getCount() {
        synchronized (this) {
            return this.count;
        }
    }

    private void incCount() {
        synchronized (this) {
            this.count++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizationSample synchronizationSample = new SynchronizationSample();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                synchronizationSample.incCount();
            }
            System.out.println(synchronizationSample.getCount());
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                synchronizationSample.incCount();
            }
            System.out.println(synchronizationSample.getCount());
        });
//        System.out.println(synchronizationSample.getCount());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
