package dsasamples;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;

public class PrintZeroEvenOdd {
    public static void main(String[] args) {
        ZeroEvenOdd zeroEvenOdd = new ZeroEvenOdd(10);
        Thread t1 = new Thread(() -> {
            try {
                zeroEvenOdd.zero(System.out::print);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                zeroEvenOdd.even(System.out::print);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread t3 = new Thread(() -> {
            try {
                zeroEvenOdd.odd(System.out::print);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        t3.start();
        t2.start();
        t1.start();
    }
}

class ZeroEvenOdd {
    private int n;
    private AtomicInteger i;
    private ReentrantLock lock;
    private Condition evenCondition;
    private Condition oddCondition;
    private Condition zeroCondition;
    private boolean printEvenOddValues; // false -> zero's turn, true -> even/odd's turn

    public ZeroEvenOdd(int n) {
        this.n = n;
        i = new AtomicInteger(1); // start from 1, not 0
        lock = new ReentrantLock();
        evenCondition = lock.newCondition();
        oddCondition = lock.newCondition();
        zeroCondition = lock.newCondition();
        printEvenOddValues = false;
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        for (int count = 0; count < n; count++) { // print zero n times
            lock.lock();
            try {
                while (printEvenOddValues) {
                    zeroCondition.await();
                }
                printNumber.accept(0);
                printEvenOddValues = true;

                // signal either odd or even thread depending on current number
                if (i.get() % 2 == 0) {
                    evenCondition.signal();
                } else {
                    oddCondition.signal();
                }
            } finally {
                lock.unlock();
            }
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        while (true) {
            lock.lock();
            try {
                while (!printEvenOddValues || i.get() % 2 != 0) {
                    if (i.get() > n) return;
                    evenCondition.await();
                }
                if (i.get() > n) return;

                printNumber.accept(i.getAndIncrement());
                printEvenOddValues = false;
                zeroCondition.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        while (true) {
            lock.lock();
            try {
                while (!printEvenOddValues || i.get() % 2 == 0) {
                    if (i.get() > n) return;
                    oddCondition.await();
                }
                if (i.get() > n) return;

                printNumber.accept(i.getAndIncrement());
                printEvenOddValues = false;
                zeroCondition.signal();
            } finally {
                lock.unlock();
            }
        }
    }
}
class ZeroEvenOddSemaphore {
    private int n;
    private Semaphore zeroSemaphore;
    private Semaphore evenSemaphore;
    private Semaphore oddSemaphore;
    public ZeroEvenOddSemaphore(int n) {
        this.n = n;
        zeroSemaphore = new Semaphore(1);
        evenSemaphore = new Semaphore(0);
        oddSemaphore = new Semaphore(0);
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        for(int i = 1; i <= n; i++){
            zeroSemaphore.acquire();
            printNumber.accept(0);
            if(i%2 == 0){
                evenSemaphore.release();
            }else{
                oddSemaphore.release();
            }
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        for(int i = 2; i <= n ; i+=2){
            evenSemaphore.acquire();
            printNumber.accept(i);
            zeroSemaphore.release();
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        for(int i = 1; i <= n ; i+=2){
            oddSemaphore.acquire();
            printNumber.accept(i);
            zeroSemaphore.release();
        }
    }
}

