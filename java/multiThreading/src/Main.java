import creation.CreationByExtendingThread;
import creation.CreationByImplementingRunnable;
import synchronization.NonSynchronized;
import synchronization.Synchronized;

public class Main {
    public static void main(String[] args) {
//        CreationByExtendingThread creationByExtendingThread = new CreationByExtendingThread();
//        creationByExtendingThread.start();


        // We can't directly create a new class that is implementing the Runnable interface
        //Since running this will run the class in main thread instead of creating the new thread
        // So we should work on creating the thread and then calling start method
//        CreationByImplementingRunnable creationByImplementingRunnable = new CreationByImplementingRunnable();
//        creationByImplementingRunnable.run();

//        Thread thread = new Thread(new CreationByImplementingRunnable());
//        thread.start();


        //Creation of threads without synchronized method will lead to a race condition
        // Where we don't know which thread will be executed so we need to be careful of things
        NonSynchronized nonSynchronized = new NonSynchronized();
        Thread t1 = new Thread(nonSynchronized);
        Thread t2 = new Thread(nonSynchronized);
        t1.start();
        t2.start();
        System.out.println(Thread.currentThread().getName() + ": " + nonSynchronized.getCount());

        //Usage of synchronized in real life that for a banking application
        Synchronized synchronizedBankAppl = new Synchronized(0);
//        Thread threadBanking = new Thread(() -> synchronizedBankAppl.withdraw(800), "Thread 1");
//        Thread threadBanking2 = new Thread(() -> synchronizedBankAppl.withdraw(100), "Thread 2");
//        Thread threadBanking3 = new Thread(() -> synchronizedBankAppl.withdraw(200), "Thread 3");
//        threadBanking.start();
//        threadBanking2.start();
//        threadBanking3.start();

        Thread threadForSyncBlock = new Thread(() -> synchronizedBankAppl.withdrawBlockLevel(100), "Thread at block level");
        Thread threadForSyncBlock2 = new Thread(() -> synchronizedBankAppl.withdrawBlockLevel(100), "Thread at block level2");
        threadForSyncBlock.start();
        threadForSyncBlock2.start();



    }
}