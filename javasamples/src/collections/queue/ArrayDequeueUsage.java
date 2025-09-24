package collections.queue;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * ArrayDeque in Java (java.util.ArrayDeque)
 * -----------------------------------------
 * 1. Internal Data Structure:
 * - Backed by a resizable array.
 * - Implements a circular buffer using two pointers:
 * head → index of the first element.
 * tail → index after the last element (next free slot).
 * - Wrap-around is achieved using modulo arithmetic.
 * For example: (index + 1) % array.length.
 * <p>
 * 2. Resizing:
 * - If the deque becomes full (head == tail after insertion),
 * the internal array is doubled in size.
 * - Elements are copied into the new array in correct order.
 * - Array never shrinks automatically (only grows).
 * <p>
 * 3. Time Complexity:
 * - Insertion/removal at head or tail → O(1) amortized.
 * - Peek operations (first/last) → O(1).
 * - Resizing (rare) → O(n) when it happens, but amortized O(1).
 * - No random access by index (unlike ArrayList).
 * <p>
 * 4. Null Handling:
 * - Null elements are NOT allowed.
 * - Any attempt to insert null throws NullPointerException.
 * <p>
 * 5. Capacity:
 * - Initial capacity = 16 (default).
 * - Always power of 2 internally, to optimize modulo operations.
 * <p>
 * 6. Thread Safety:
 * - NOT synchronized (like ArrayList).
 * - For multi-threaded usage, use external synchronization
 * or a concurrent alternative (ConcurrentLinkedDeque).
 * <p>
 * 7. Advantages:
 * - Faster than LinkedList for deque operations (less memory,
 * no node object overhead).
 * - More versatile than Stack (supports double-ended operations).
 * <p>
 * 8. Common Uses:
 * - As a Queue (FIFO): offerLast(), pollFirst().
 * - As a Stack (LIFO): push(), pop().
 * - Double-ended data processing where additions/removals can
 * happen at both ends.
 * <p>
 * Example:
 * ArrayDeque<Integer> dq = new ArrayDeque<>();
 * dq.addFirst(1);  dq.addLast(2);
 * dq.removeFirst(); dq.removeLast();
 */

public class ArrayDequeueUsage {
    public static void main(String[] args) {
        // Create an ArrayDeque of integers
        Deque<Integer> deque = new ArrayDeque<>();

        // Adding elements at the end (like a queue)
        deque.add(10);   // same as addLast()
        deque.addLast(20);
        deque.offerLast(30); // offer does not throw exception on failure

        // Adding elements at the front
        deque.addFirst(5);
        deque.offerFirst(1);

        System.out.println("Initial Deque: " + deque);

        // Removing elements
        int first = deque.removeFirst();  // throws exception if empty
        int last = deque.pollLast();      // returns null if empty

        System.out.println("Removed First: " + first);
        System.out.println("Removed Last: " + last);
        System.out.println("Deque after removals: " + deque);

        // Peeking (just viewing without removal)
        System.out.println("Peek First: " + deque.peekFirst());
        System.out.println("Peek Last: " + deque.peekLast());

        // Using as Stack (LIFO)
        deque.push(100); // push = addFirst
        deque.push(200);
        System.out.println("After Stack Pushes: " + deque);

        int popped = deque.pop(); // pop = removeFirst
        System.out.println("Popped Element: " + popped);

        // Iteration
        System.out.print("Iterating: ");
        for (int num : deque) {
            System.out.print(num + " ");
        }
    }
}
