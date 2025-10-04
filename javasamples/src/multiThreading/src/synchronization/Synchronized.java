package synchronization;

public class Synchronized implements Runnable {

    private int balance = 1000;
    private int amount;

    public Synchronized(int amount) {
        this.amount = amount;
    }

    @Override
    public void run() {
        withdraw(amount);
    }

    //Synchronization at method level where actually only one thread is allowed to modify the data
    //So that user will be able to see the correct balance
    public synchronized void withdraw(int amount) {
        if (amount > balance) {
            System.out.println("amount exceeds balance" + Thread.currentThread().getName() + ": " + amount);
        } else {
            balance -= amount;
            System.out.println("updated Balance = " + balance + Thread.currentThread().getName());
        }
    }

    // Synchronization at block level
    // Synchronization can also happen at block level where the logic is restricted to block only
    public void withdrawBlockLevel(int amount) {
        synchronized (this) {
            if (amount > balance) {
                System.out.println("amount exceeds balance" + Thread.currentThread().getName() + ": " + amount);
            } else {
                balance -= amount;
                System.out.println("updated Balance = " + balance + Thread.currentThread().getName());
            }
        }
    }
}
