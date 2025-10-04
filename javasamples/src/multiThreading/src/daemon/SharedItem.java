package daemon;

public class SharedItem {
    boolean isItemAvailable = false;

    public synchronized void getItem(){
        System.out.println("Making the item available : " + Thread.currentThread().getName());
        isItemAvailable = true;
        try{
            System.out.println("Setting the thread to sleep mode ");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Notifying other threads");
        notify();
    }
}
