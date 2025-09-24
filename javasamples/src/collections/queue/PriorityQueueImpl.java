package collections.queue;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * Demonstrates usage of Java's {@link java.util.PriorityQueue} and differences with
 * {@link java.util.concurrent.PriorityBlockingQueue}.
 *
 * <h2>PriorityQueue Internals</h2>
 * <ul>
 *   <li>Backed by a resizable {@code Object[]} array, storing a <b>binary heap</b>.</li>
 *   <li>Default ordering: <b>min-heap</b> (head is smallest element by natural order or Comparator).</li>
 *   <li>Index rules: root at 0, children at {@code 2*i+1}, {@code 2*i+2}, parent at {@code (i-1)/2}.</li>
 *   <li>Not thread-safe; must wrap with external synchronization if shared across threads.</li>
 *   <li>Not stable: equal elements may be dequeued in any order.</li>
 *   <li>Resizing: internal array grows automatically (roughly 1.5x); constructor can pre-size capacity.
 *       {@code clear()} drops logical size but retains the array for reuse (no shrink).</li>
 *   <li>Mutating element priority after insertion <b>corrupts heap order</b>. Remove & reinsert instead.</li>
 * </ul>
 *
 * <h2>Common Operations & Time Complexity</h2>
 * <ul>
 *   <li>{@code add(e)} / {@code offer(e)} – Insert element, O(log n).</li>
 *   <li>{@code poll()} – Remove and return head, O(log n).</li>
 *   <li>{@code peek()} / {@code element()} – Return head without removing, O(1).</li>
 *   <li>{@code remove(Object)} – Linear search (O(n)) + sift (O(log n)), effectively O(n).</li>
 *   <li>{@code contains(Object)} – Linear scan, O(n).</li>
 *   <li>{@code size()} / {@code isEmpty()} – O(1).</li>
 *   <li>{@code clear()} – O(n).</li>
 *   <li>Bulk constructor from Collection – heapify in O(n) (Floyd’s algorithm).</li>
 *   <li>Iteration – O(n), but order is unspecified (not sorted).</li>
 * </ul>
 *
 * <h2>PriorityBlockingQueue</h2>
 * <ul>
 *   <li>Thread-safe, blocking variant in {@code java.util.concurrent}.</li>
 *   <li>Still backed by a binary heap (same ordering rules).</li>
 *   <li>{@code put(e)} never blocks (queue is unbounded).</li>
 *   <li>{@code take()} blocks until an element is available.</li>
 *   <li>{@code poll(timeout, unit)} waits up to a timeout.</li>
 *   <li>Iterators are <i>weakly consistent</i> (no CME, may not reflect all updates).</li>
 *   <li>{@code drainTo(Collection, n)} efficiently removes multiple elements at once.</li>
 *   <li>{@code size()} is approximate under concurrency.</li>
 * </ul>
 *
 * <h2>Use Cases</h2>
 * <ul>
 *   <li>Scheduling tasks by priority.</li>
 *   <li>Top-K or streaming leaderboard (bounded heap pattern).</li>
 *   <li>Producer/consumer models – use {@code PriorityBlockingQueue} for safety.</li>
 *   <li>When strict order iteration is required, consider {@link java.util.TreeSet} instead.</li>
 * </ul>
 */
public class PriorityQueueImpl {
    public static void main(String[] args) throws InterruptedException {
        // Min-heap (default): head is the smallest element.
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();

        // offer/add: O(log n) per insertion (heapify-up).
        priorityQueue.add(1);
        priorityQueue.add(10);
        priorityQueue.add(5);
        priorityQueue.add(3);
        priorityQueue.add(2);
        priorityQueue.add(7);
        priorityQueue.add(4);

        // poll(): O(log n) — removes and returns the head (re-heapify-down).
        // peek(): O(1) — returns head without removing it.
        // Print in ascending order because it's a min-heap.
        while (!priorityQueue.isEmpty()) {
            System.out.print(priorityQueue.poll() + " ");
        }
        System.out.println();

        // Max-heap: supply a Comparator that reverses the natural order.
        // Avoid "b - a" because it can overflow; use Integer.compare(b, a) or Comparator.reverseOrder().
        PriorityQueue<Integer> maxQueue = new PriorityQueue<>(Comparator.reverseOrder());

        maxQueue.add(1);
        maxQueue.add(4);
        maxQueue.add(6);
        maxQueue.add(7);
        maxQueue.add(10);
        maxQueue.add(8);
        maxQueue.add(22);

        // Prints in descending order because head is the largest.
        while (!maxQueue.isEmpty()) {
            System.out.print(maxQueue.poll() + " ");
        }
        System.out.println();

        // ❗️IMPORTANT: The following producer/consumer sample is UNSAFE.
        // PriorityQueue is not thread-safe; concurrent add/poll can corrupt the heap or throw.
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                // Unsafe without synchronization! Use a lock or a PriorityBlockingQueue instead.
                priorityQueue.add(i);
            }
        });

        Thread consumer = new Thread(() -> {
            // Also unsafe: 'isEmpty' + 'poll' is a race; the queue may become empty between the checks.
            while (!priorityQueue.isEmpty()) {
                priorityQueue.poll();
            }
        });

        producer.start();
        consumer.start();
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        PriorityBlockingQueue<Integer> q = new PriorityBlockingQueue<>(11, Comparator.reverseOrder());

        Thread producer1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                q.add(i); // never blocks (unbounded)
            }
        });

        Thread consumer1 = new Thread(() -> {
            // use take() to block when empty (no racy isEmpty/poll loop)
            for (int i = 0; i < 90; i++) {
                Integer head = q.poll(); // waits if empty
                // process head ...
            }
        });

        producer1.start();
        consumer1.start();
        producer1.join();
        consumer1.join();

        // Example: timed poll (returns null if no element arrives in time)
        Integer maybe = q.poll(100, TimeUnit.MILLISECONDS);
        System.out.println("Timed poll -> " + maybe);
    }
}
