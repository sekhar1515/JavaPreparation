package dsasamples;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
//leetcode link: https://leetcode.com/problems/print-in-order/description/
public class FooTesting {
    public static void main(String[] args) {
        Foo foo = new Foo();
        Thread t1 = new Thread(() -> {
            try {
                foo.first(() -> {
                    System.out.println("First");
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                foo.second(() -> {
                    System.out.println("Second");
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread t3 = new Thread(() -> {
            try {
                foo.third(() -> {
                    System.out.println("Third");
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        t3.start();
        t2.start();
        t1.start();
    }
}

class Foo {
    private final ReentrantLock lock;
    private final Condition firstCondition;
    private final Condition secondCondition;
    private boolean isFirstDone;
    private boolean isSecondDone;

    public Foo() {
        lock = new ReentrantLock();
        firstCondition = lock.newCondition();
        secondCondition = lock.newCondition();
        isFirstDone = false;
        isSecondDone = false;
    }

    public void first(Runnable printFirst) throws InterruptedException {

        // printFirst.run() outputs "first". Do not change or remove this line.

        lock.lock();
        try {
            printFirst.run();
            isFirstDone = true;
            firstCondition.signal();
        } finally {
            lock.unlock();
        }

    }

    public void second(Runnable printSecond) throws InterruptedException {

        // printSecond.run() outputs "second". Do not change or remove this line.
        lock.lock();
        try {
            while (!isFirstDone) {
                firstCondition.await();
            }
            printSecond.run();
            isSecondDone = true;
            secondCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public void third(Runnable printThird) throws InterruptedException {

        // printThird.run() outputs "third". Do not change or remove this line.
        lock.lock();
        try {
            while (!isSecondDone) {
                secondCondition.await();
            }
            printThird.run();
        } finally {
            lock.unlock();
        }
    }
}

class FooSemaphore {
    private final Semaphore second;  // Controls access to second()
    private final Semaphore third;   // Controls access to third()

    public FooSemaphore() {
        second = new Semaphore(0);   // Locked initially (no permit)
        third = new Semaphore(0);    // Locked initially (no permit)
    }

    // Runs first
    public void first(Runnable printFirst) throws InterruptedException {
        printFirst.run();             // Print "first"
        second.release();             // Allow second() to proceed
    }

    // Runs second
    public void second(Runnable printSecond) throws InterruptedException {
        second.acquire();             // Wait for first() to finish
        printSecond.run();            // Print "second"
        third.release();              // Allow third() to proceed
    }

    // Runs third
    public void third(Runnable printThird) throws InterruptedException {
        third.acquire();              // Wait for second() to finish
        printThird.run();             // Print "third"
    }
}