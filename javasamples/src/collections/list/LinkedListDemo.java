package collections.list;

import java.util.*;

/*
================================================================================
                Internal Working of LinkedList in Java
================================================================================

1) Data Structure:
   - LinkedList is implemented as a **doubly-linked list**.
   - Each element (called a "node") contains:
        • data (the element itself)
        • reference to the previous node
        • reference to the next node
   - Head points to the first node, Tail points to the last node.

   Node structure (simplified):
       class Node<E> {
           E item;
           Node<E> prev;
           Node<E> next;
       }

2) Characteristics:
   - Implements **List, Deque, Queue** interfaces → works as List, Queue, Stack.
   - Allows **duplicate elements**.
   - Allows **null elements** (unlike ArrayDeque).
   - Not synchronized (must use external synchronization if shared across threads).

3) Time Complexity:
   - addFirst(), addLast(), removeFirst(), removeLast() → O(1)
   - offer(), poll(), peek() (queue-style ops) → O(1)
   - push(), pop() (stack-style ops) → O(1)
   - add(E) at end → O(1)
   - remove(Object o) or remove(index) → O(n) in worst case (search required)
   - get(int index), set(int index) → O(n) (traverses from head or tail)

   ⚠️ Unlike ArrayList:
   - Random access is **slow** (O(n)).
   - Insert/delete at ends is **fast** (O(1)).

4) Queue/Deque behavior:
   - As Queue:
        • offer(e) → add at tail
        • poll() / remove() → remove from head
        • peek() / element() → view head
   - As Deque:
        • addFirst(e), addLast(e)
        • removeFirst(), removeLast()
        • getFirst(), getLast()
        • removeFirstOccurrence(o), removeLastOccurrence(o)
   - As Stack:
        • push(e) → addFirst(e)
        • pop()   → removeFirst()

5) Iterator:
   - Provides fail-fast iterators (throws ConcurrentModificationException if modified structurally during iteration).
   - Supports descendingIterator() for reverse traversal.

6) When to use LinkedList:
   - Best when frequent insertions/removals at beginning or middle are needed.
   - Avoid if random access (get by index) is common → use ArrayList instead.

================================================================================
*/

public class LinkedListDemo {
    public static void main(String[] args) {
        // LinkedList implements List, Deque, Queue
        LinkedList<Integer> dq = new LinkedList<>();
        System.out.println("Initial: " + dq); // []

        // 1) add(E e): as List/Deque tail-append
        dq.add(10);
        dq.add(20);
        dq.add(30);
        System.out.println("After add: " + dq); // [10, 20, 30]

        // 2) List ops: add(int index, E e), get, set, remove(int index)
        dq.add(1, 15); 
        System.out.println("After insert at index 1: " + dq); // [10, 15, 20, 30]

        System.out.println("Element at index 2: " + dq.get(2)); // 20

        dq.set(2, 25);
        System.out.println("After set index 2 -> 25: " + dq); // [10, 15, 25, 30]

        dq.remove(1); // remove by position
        System.out.println("After remove index 1: " + dq); // [10, 25, 30]

        // 3) remove(Object o): remove by value (first occurrence)
        dq.remove(Integer.valueOf(25));
        System.out.println("After remove value 25: " + dq); // [10, 30]

        // 4) size(), isEmpty(), contains(), indexOf/lastIndexOf
        System.out.println("Size: " + dq.size()); // 2
        System.out.println("Is empty? " + dq.isEmpty()); // false
        System.out.println("Contains 10? " + dq.contains(10)); // true
        dq.add(10);
        dq.add(30);
        System.out.println("Now: " + dq); // [10, 30, 10, 30]
        System.out.println("First index of 10: " + dq.indexOf(10)); // 0
        System.out.println("Last index of 10: " + dq.lastIndexOf(10)); // 2

        // 5) Deque endpoints: addFirst/addLast, getFirst/getLast, removeFirst/removeLast
        dq.addFirst(5);
        dq.addLast(40);
        System.out.println("After addFirst(5), addLast(40): " + dq); // [5, 10, 30, 10, 30, 40]
        System.out.println("getFirst: " + dq.getFirst()); // 5
        System.out.println("getLast: " + dq.getLast());   // 40
        dq.removeFirst(); // removes 5
        dq.removeLast();  // removes 40
        System.out.println("After removeFirst/Last: " + dq); // [10, 30, 10, 30]

        // 6) Queue-style ops (head = front/first)
        // add/offer -> enqueue; element/peek -> inspect head; remove/poll -> dequeue
        // TRICKY: remove() with no args removes HEAD (like removeFirst)
        dq.offer(50); // add at tail; returns boolean, never throws on capacity (LL has no cap)
        System.out.println("After offer(50): " + dq); // [10, 30, 10, 30, 50]
        System.out.println("peek(): " + dq.peek());   // 10  (head)
        System.out.println("element(): " + dq.element()); // 10 (throws if empty; peek returns null)
        System.out.println("poll(): " + dq.poll());   // 10  (removes head or null if empty)
        System.out.println("After poll: " + dq); // [30, 10, 30, 50]

        // 7) Stack-style ops via Deque: push/pop (LIFO at head)
        dq.push(99); // push to head
        System.out.println("After push(99): " + dq); // [99, 30, 10, 30, 50]
        System.out.println("pop(): " + dq.pop());    // 99 (throws if empty)
        System.out.println("After pop: " + dq);      // [30, 10, 30, 50]

        // 8) Occurrence-based removal (useful with duplicates)
        dq.removeFirstOccurrence(30); // removes first 30 (at index 0)
        System.out.println("After removeFirstOccurrence(30): " + dq); // [10, 30, 50]
        dq.removeLastOccurrence(30);  // removes last 30
        System.out.println("After removeLastOccurrence(30): " + dq);  // [10, 50]

        // 9) Nulls allowed (LinkedList permits a null element)
        dq.add(null);
        System.out.println("After adding null: " + dq); // [10, 50, null]
        System.out.println("peek(): " + dq.peek());     // 10

        // 10) Iteration (supports fail-fast iterator)
        dq.addAll(Arrays.asList(2, 4, 6, 7));
        System.out.println("For iteration: " + dq);
        System.out.println("Descending iterator:");
        Iterator<Integer> desc = dq.descendingIterator();
        while (desc.hasNext()) System.out.print(desc.next() + " ");
        System.out.println();

        System.out.println("Iterator (safe remove evens):");
        Iterator<Integer> it = dq.iterator();
        while (it.hasNext()) {
            Integer v = it.next();
            if (v != null && v % 2 == 0) it.remove();
        }
        System.out.println("After iterator removal evens: " + dq);

        // 11) clear()
        dq.clear();
        System.out.println("After clear: " + dq); // []
    }
}
