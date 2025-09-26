package collections.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/*
==============================================================
   ArrayList vs LinkedList — Methods, TC, and Internals
==============================================================

🔹 ArrayList (backed by dynamic array: Object[] elementData)
--------------------------------------------------------------
- add(E e)                → Amortized O(1)   → Append at end; grows array if full (1.5×)
- add(int i, E e)         → O(n)             → Shifts elements right
- get(int i)              → O(1)             → Direct array index
- set(int i, E e)         → O(1)             → Replace at index
- remove(int i)           → O(n)             → Shifts elements left
- remove(Object o)        → O(n)             → Find element + shift
- contains(o)             → O(n)             → Linear scan with equals()
- indexOf(o)              → O(n)             → First occurrence
- lastIndexOf(o)          → O(n)             → Last occurrence
- size(), isEmpty()       → O(1)
- clear()                 → O(n)             → Null out all slots
- iterator()              → O(1) to create; O(n) to traverse (fail-fast)
- sort(Comparator)        → O(n log n)       → TimSort
- replaceAll(fn)          → O(n)             → Apply function to all

⚠️ Trick:
- remove(1) calls remove(int index)
- remove(Integer.valueOf(1)) removes the value 1

--------------------------------------------------------------
🔹 LinkedList (backed by doubly-linked nodes)
--------------------------------------------------------------
- add(E e)                → O(1)             → Link new node at end
- add(int i, E e)         → O(n)             → Traverse to index + O(1) link
- get(int i)              → O(n)             → Traverse (head/tail whichever closer)
- set(int i, E e)         → O(n)             → Traverse, update node
- remove(int i)           → O(n)             → Traverse, unlink node
- remove(Object o)        → O(n)             → Traverse, unlink node
- contains(o)             → O(n)             → Linear scan
- indexOf(o), lastIndexOf → O(n)
- size(), isEmpty()       → O(1)
- clear()                 → O(n)             → Null node refs
- iterator()              → O(1) create; O(n) walk nodes
- Deque ops (addFirst, addLast, removeFirst, removeLast, peekFirst, peekLast) → O(1)

--------------------------------------------------------------
Comparison
--------------------------------------------------------------
- Random access: ArrayList O(1), LinkedList O(n)
- Insert/remove at end: ArrayList O(1)*, LinkedList O(1)
- Insert/remove in middle: Both O(n) (shift vs traversal)
- Insert/remove at head: ArrayList O(n), LinkedList O(1)
- Memory: ArrayList compact, LinkedList heavier (next/prev per node)
- Iteration: ArrayList cache-friendly, LinkedList pointer chasing
==============================================================
*/

public class ArrayListImpl {
    public static void main(String[] args) {
        // 1) Create
        List<Integer> list = new ArrayList<>();
        System.out.println("Initial: " + list);

        // 2) add(E e)
        list.add(10);
        list.add(20);
        list.add(30);
        System.out.println("After add: " + list); // [10, 20, 30]

        // 3) add(int index, E e)
        list.add(1, 15); // insert at index 1, shifts right
        System.out.println("After insert at index 1: " + list); // [10, 15, 20, 30]

        // 4) get(int index)
        System.out.println("Element at index 2: " + list.get(2)); // 20

        // 5) set(int index, E e)
        list.set(2, 25); // replace element at index 2
        System.out.println("After set index 2 -> 25: " + list); // [10, 15, 25, 30]

        // 6) remove(int index)  <-- tricky: removes by position
        list.remove(1); // removes element at index 1 (15)
        System.out.println("After remove index 1: " + list); // [10, 25, 30]

        // 7) remove(Object o)  <-- tricky: removes by value
        list.remove(Integer.valueOf(25)); // removes the object 25
        System.out.println("After remove value 25: " + list); // [10, 30]

        // 8) size() and isEmpty()
        System.out.println("Size: " + list.size());      // 2
        System.out.println("Is empty? " + list.isEmpty()); // false

        // 9) contains(Object o)
        System.out.println("Contains 10? " + list.contains(10)); // true
        System.out.println("Contains 50? " + list.contains(50)); // false

        // 10) indexOf / lastIndexOf
        list.add(10);  // duplicate value
        list.add(30);
        System.out.println("Now: " + list); // [10, 30, 10, 30]
        System.out.println("First index of 10: " + list.indexOf(10)); // 0
        System.out.println("Last index of 10: " + list.lastIndexOf(10)); // 2

        // 11) clear()
        list.clear();
        System.out.println("After clear: " + list); // []

        // 12) Iteration (rebuild list)
        list.addAll(Arrays.asList(1, 2, 3, 4, 5));
        System.out.println("List for iteration: " + list);

        System.out.println("For-each loop:");
        for (int num : list) {
            System.out.println(num);
        }

        System.out.println("Iterator (safe remove if needed):");
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            int val = it.next();
            if (val % 2 == 0) it.remove(); // remove evens safely
        }
        System.out.println("After iterator removal: " + list); // [1, 3, 5]
    }
}
