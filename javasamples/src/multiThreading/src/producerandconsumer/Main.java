package producerandconsumer;

public class Main {
    public static void main(String[] args) {
        SharedResource sharedResource = new SharedResource();
        Thread producerThread = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sharedResource.produceItem();
        });

        Thread consumerThread = new Thread(sharedResource::consumeItem);
        producerThread.start();
        consumerThread.start();
    }
}
