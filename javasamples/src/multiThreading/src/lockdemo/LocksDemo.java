package lockdemo;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

/**
 * Tiny, readable demos for:
 * 1) synchronized + wait/notify (intrinsic monitor)
 * 2) ReentrantLock + tryLock
 * 3) ReentrantReadWriteLock (many readers, one writer)
 * 4) StampedLock (optimistic read)
 * 5) Semaphore (limit concurrency)
 * 6) ReentrantLock + Condition (notEmpty / notFull)  <-- NEW
 */
public class LocksDemo {

    public static void main(String[] args) {
        title("1) synchronized + wait/notify (intrinsic monitor)");
        SynchronizedPingPong.demo();

        title("2) ReentrantLock + tryLock");
        ReentrantLockCounter.demo();

        title("3) ReentrantReadWriteLock (many readers, one writer)");
        RWLDemo.demo();

        title("4) StampedLock (optimistic read)");
        StampedLockPointDemo.demo();

        title("5) Semaphore (limit concurrency)");
        SemaphoreParkingLot.demo();

        title("6) ReentrantLock + Condition (bounded queue with notEmpty/notFull)");
        ConditionBoundedQueueDemo.demo();

        title("Done.");
    }

    // ---------- small helpers ----------
    static void title(String s) {
        System.out.println();
        System.out.println(repeat("-", s.length()));
        System.out.println(s);
        System.out.println(repeat("-", s.length()));
    }

    static String repeat(String c, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(c);
        return sb.toString();
    }

    static void nap(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }

    static void startAndJoin(Thread... ts) {
        for (Thread t : ts) t.start();
        for (Thread t : ts)
            try {
                t.join();
            } catch (InterruptedException ignored) {
            }
    }

    // =====================================================================================
    // 1) INTRINSIC MONITOR: synchronized + wait/notify (ping-pong)
    // =====================================================================================
    static class SynchronizedPingPong {
        static class PingPong {
            private boolean pingTurn = true; // start with ping

            public synchronized void ping() throws InterruptedException {
                while (!pingTurn) wait();     // wait until it's ping's turn
                System.out.println("ping");
                pingTurn = false;
                notifyAll();                  // wake pong
            }

            public synchronized void pong() throws InterruptedException {
                while (pingTurn) wait();      // wait until it's pong's turn
                System.out.println("pong");
                pingTurn = true;
                notifyAll();                  // wake ping
            }
        }

        static void demo() {
            PingPong pp = new PingPong();
            Thread t1 = new Thread(() -> {
                for (int i = 0; i < 3; i++) {
                    try {
                        pp.ping();
                    } catch (Exception ignored) {
                    }
                    nap(25);
                }
            }, "pinger");
            Thread t2 = new Thread(() -> {
                for (int i = 0; i < 3; i++) {
                    try {
                        pp.pong();
                    } catch (Exception ignored) {
                    }
                    nap(25);
                }
            }, "ponger");
            startAndJoin(t1, t2);
        }
    }

    // =====================================================================================
    // 2) REENTRANTLOCK: basic mutex + tryLock
    // =====================================================================================
    static class ReentrantLockCounter {
        static class Counter {
            private final ReentrantLock lock = new ReentrantLock();
            private int value = 0;

            public void incBlocking() {
                lock.lock();
                try {
                    value++;
                    System.out.println(Thread.currentThread().getName() + ": inc => " + value);
                } finally {
                    lock.unlock();
                }
            }

            public void incIfFree() {
                if (lock.tryLock()) {
                    try {
                        value++;
                        System.out.println(Thread.currentThread().getName() + ": tryLock inc => " + value);
                    } finally {
                        lock.unlock();
                    }
                } else {
                    System.out.println(Thread.currentThread().getName() + ": tryLock skipped (busy)");
                }
            }
        }

        static void demo() {
            Counter c = new Counter();
            Thread blocker = new Thread(() -> {
                for (int i = 0; i < 4; i++) {
                    c.incBlocking();
                    nap(20);
                }
            }, "blocker");
            Thread opportunist = new Thread(() -> {
                for (int i = 0; i < 6; i++) {
                    c.incIfFree();
                    nap(15);
                }
            }, "opportunist");
            startAndJoin(blocker, opportunist);
        }
    }

    // =====================================================================================
    // 3) REENTRANTREADWRITELOCK: shared reads, exclusive write
    // =====================================================================================
    static class RWLDemo {
        static class SharedName {
            private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock(true);
            private final Lock r = rwl.readLock();
            private final Lock w = rwl.writeLock();
            private String name = "alpha";

            public String get() {
                r.lock();
                try {
                    return name;
                } finally {
                    r.unlock();
                }
            }

            public void set(String n) {
                w.lock();
                try {
                    name = n;
                } finally {
                    w.unlock();
                }
            }
        }

        static void demo() {
            SharedName sn = new SharedName();
            Runnable readerTask = () -> {
                for (int i = 0; i < 5; i++) {
                    System.out.println(Thread.currentThread().getName() + ": read => " + sn.get());
                    nap(18);
                }
            };
            Thread r1 = new Thread(readerTask, "reader-1");
            Thread r2 = new Thread(readerTask, "reader-2");
            Thread r3 = new Thread(readerTask, "reader-3");
            Thread writer = new Thread(() -> {
                nap(50);
                sn.set("beta");
                System.out.println("writer: set => beta");
            }, "writer");
            startAndJoin(r1, r2, r3, writer);
        }
    }

    // =====================================================================================
    // 4) STAMPEDLOCK: optimistic read + fallback to read lock
    // =====================================================================================
    static class StampedLockPointDemo {
        static class Point {
            private double x, y;
            private final StampedLock sl = new StampedLock();

            public void move(double dx, double dy) {
                long s = sl.writeLock();
                try {
                    x += dx;
                    y += dy;
                } finally {
                    sl.unlockWrite(s);
                }
            }

            public double distance() {
                long s = sl.tryOptimisticRead();
                double lx = x, ly = y;               // read without blocking
                if (!sl.validate(s)) {               // writer interfered: get consistent snapshot
                    s = sl.readLock();
                    try {
                        lx = x;
                        ly = y;
                    } finally {
                        sl.unlockRead(s);
                    }
                }
                return Math.hypot(lx, ly);
            }
        }

        static void demo() {
            Point p = new Point();
            Thread mover = new Thread(() -> {
                for (int i = 0; i < 3; i++) {
                    p.move(1, 1);
                    System.out.println("mover: moved");
                    nap(28);
                }
            }, "mover");
            Thread measurer = new Thread(() -> {
                for (int i = 0; i < 5; i++) {
                    System.out.println("measurer: distance ~ " + String.format(java.util.Locale.ROOT, "%.2f", p.distance()));
                    nap(16);
                }
            }, "measurer");
            startAndJoin(mover, measurer);
        }
    }

    // =====================================================================================
    // 5) SEMAPHORE: limit concurrent access (counting semaphore)
    // =====================================================================================
    static class SemaphoreParkingLot {
        static class ParkingLot {
            private final Semaphore spots = new Semaphore(2); // two permits (spots)

            public void tryPark(String carName) {
                try {
                    if (spots.tryAcquire(1, 500, TimeUnit.MILLISECONDS)) {
                        System.out.println(carName + " parked");
                        nap(60);
                        System.out.println(carName + " leaving");
                        spots.release();
                    } else {
                        System.out.println(carName + " gave up (no spot)");
                    }
                } catch (InterruptedException ignored) {
                }
            }
        }

        static void demo() {
            ParkingLot lot = new ParkingLot();
            startAndJoin(
                    new Thread(() -> lot.tryPark("car-1")),
                    new Thread(() -> lot.tryPark("car-2")),
                    new Thread(() -> lot.tryPark("car-3")),
                    new Thread(() -> lot.tryPark("car-4"))
            );
        }
    }

    // =====================================================================================
    // 6) REENTRANTLOCK + CONDITION: bounded queue with notEmpty/notFull
    //
    // Why Conditions?
    //  - Multiple, named wait-sets per lock (here: notFull, notEmpty).
    //  - await() releases the lock while waiting and re-acquires it before returning.
    //  - Always: while (!predicate) await();  // handle spurious wakeups.
    //
    // Demo flow:
    //  - Producer adds 0..4 into a size-2 queue (blocks when full).
    //  - Consumer takes 0..4 (blocks when empty).
    //  - Output shows producers/consumers waking each other via signal().
    // =====================================================================================
    static class ConditionBoundedQueueDemo {
        static class BoundedQueue<T> {
            private final Object[] buf;
            private int head = 0, tail = 0, size = 0;

            private final ReentrantLock lock = new ReentrantLock(true);
            private final Condition notFull = lock.newCondition();
            private final Condition notEmpty = lock.newCondition();

            BoundedQueue(int capacity) {
                if (capacity <= 0) throw new IllegalArgumentException("capacity must be > 0");
                this.buf = new Object[capacity];
            }

            public void put(T item) throws InterruptedException {
                lock.lock();
                try {
                    while (size == buf.length) {
                        System.out.println("producer: waiting (full)");
                        notFull.await(); // releases lock & parks; re-locks on wake
                    }
                    buf[tail] = item;
                    tail = (tail + 1) % buf.length;
                    size++;
                    System.out.println("producer: put " + item + " (size=" + size + ")");
                    notEmpty.signal(); // wake a consumer
                } finally {
                    lock.unlock();
                }
            }

            @SuppressWarnings("unchecked")
            public T take() throws InterruptedException {
                lock.lock();
                try {
                    while (size == 0) {
                        System.out.println("consumer: waiting (empty)");
                        notEmpty.await();
                    }
                    T x = (T) buf[head];
                    buf[head] = null;
                    head = (head + 1) % buf.length;
                    size--;
                    System.out.println("consumer: took " + x + " (size=" + size + ")");
                    notFull.signal(); // wake a producer
                    return x;
                } finally {
                    lock.unlock();
                }
            }
        }

        static void demo() {
            BoundedQueue<Integer> q = new BoundedQueue<>(2);

            Thread producer = new Thread(() -> {
                try {
                    for (int i = 0; i < 5; i++) {
                        q.put(i);
                        nap(30);
                    }
                } catch (InterruptedException ignored) {
                }
            }, "producer");

            Thread consumer = new Thread(() -> {
                try {
                    for (int i = 0; i < 5; i++) {
                        q.take();
                        nap(50);
                    }
                } catch (InterruptedException ignored) {
                }
            }, "consumer");

            startAndJoin(producer, consumer);
        }
    }
}
