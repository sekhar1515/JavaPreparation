package producerandconsumer;

public class SharedResource {
    boolean isItemAvailable;

    public synchronized void produceItem() {
        isItemAvailable = true;
        System.out.println("producer making the item available");
        notifyAll();
    }

    public synchronized void consumeItem() {
        while (!isItemAvailable) {
            try {
                System.out.println("Waiting for consuming the item since it is not available ");
                wait(); // releases all monitor locks.
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Item is consumed ");
        isItemAvailable = false;
    }
}
