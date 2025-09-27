package collections.list;

import java.util.*;

public class VectorStackGuide {

    public static void main(String[] args) {
        System.out.println("=== VECTOR BASICS & METHODS ===");
        vectorBasicsAndMethods();

        System.out.println("\n=== VECTOR CAPACITY MANAGEMENT ===");
        vectorCapacityManagement();

        System.out.println("\n=== STACK BASICS & METHODS ===");
        stackBasicsAndMethods();
    }

    // ------------------------------------------------------------
    // SECTION 1: Vector — Methods + Time Complexity + Examples
    // ------------------------------------------------------------

    /**
     * Vector:
     * - Legacy resizable-array implementation (like ArrayList)
     * - Implements List, RandomAccess, Cloneable, Serializable
     * - All public methods are synchronized (intrinsic lock on 'this')
     * - Maintains insertion order, allows duplicates, random-access O(1)
     * <p>
     * Time Complexity (n = size):
     * Access: get(i) .............. O(1)
     * Update: set(i, e) ........... O(1)
     * Add at end: add(e) .......... Amortized O(1); O(n) on resize
     * Insert at index: add(i, e) .. O(n) (shift right)
     * Remove at index: ............ O(n) (shift left)
     * Contains / indexOf: ......... O(n)
     * Iteration: .................. O(n)
     * ensureCapacity/trimToSize: .. O(n) (may reallocate/copy)
     * <p>
     * HOW Vector is synchronized:
     * - Every *public* method in Vector is declared 'synchronized'.
     * - Calls acquire the Vector instance's intrinsic monitor (lock on 'this').
     * - This makes single method invocations thread-safe.
     * <p>
     * - Alternative: Collections.synchronizedList(list) wraps a List and
     * synchronizes on the wrapper object; you must also synchronize during iteration.
     */
    private static void vectorBasicsAndMethods() {
        Vector<String> v = new Vector<>(); // default capacity 10 (JDK implementation detail)
        // --- Add methods ---
        v.add("Java");                    // Amortized O(1)
        v.add("Python");
        v.addElement("C++");              // Legacy alias of add(e)
        v.add(1, "Kotlin");               // O(n) insert
        v.addAll(Arrays.asList("Go", "Rust"));

        // --- Access & update ---
        String second = v.get(1);         // O(1) -> "Kotlin"
        v.set(1, "Scala");                // O(1) replace "Kotlin" -> "Scala"

        // --- Query methods ---
        boolean hasRust = v.contains("Rust");         // O(n)
        int firstIdx = v.indexOf("Java");             // O(n)
        int lastIdx = v.lastIndexOf("Rust");         // O(n)
        String first = v.firstElement();              // O(1)
        String last = v.lastElement();               // O(1)

        // --- Remove methods ---
        v.remove("Go");                   // O(n)
        v.remove(0);                      // O(n) remove by index
        v.removeElement("C++");           // Legacy alias
        // v.removeAllElements();         // Legacy alias of clear()

        // --- Views/iterations ---
        Enumeration<String> e = v.elements(); // Legacy, not fail-fast
        ListIterator<String> it = v.listIterator(); // Fail-fast iterator

        // Print state
        System.out.println("Vector contents: " + v);
        System.out.println("second=" + second + ", hasRust=" + hasRust +
                ", firstIdx=" + firstIdx + ", lastIdx=" + lastIdx +
                ", first=" + first + ", last=" + last);

        // Demonstrate enumeration vs iterator
        System.out.print("Enumeration walk: ");
        while (e.hasMoreElements()) System.out.print(e.nextElement() + " ");
        System.out.println();

        System.out.print("Iterator walk: ");
        while (it.hasNext()) System.out.print(it.next() + " ");
        System.out.println();
    }

    // ------------------------------------------------------------
    // SECTION 2: Vector — Capacity management
    // ------------------------------------------------------------

    /**
     * Capacity-related methods (costly O(n) when reallocate/copy):
     * - capacity()          -> current internal array capacity
     * - ensureCapacity(min) -> grow to at least 'min' (copy O(n))
     * - trimToSize()        -> shrink to size (O(n))
     * - setSize(newSize)    -> size view (can pad with nulls or drop tail)
     * <p>
     * Note: Vector historically had 'capacityIncrement' (constructor),
     * used to control growth (otherwise growth factor ~2x in many JDKs).
     */
    private static void vectorCapacityManagement() {
        Vector<Integer> v = new Vector<>(2, 3); // initialCapacity=2, capacityIncrement=3
        v.add(1);
        v.add(2);
        System.out.println("Initial size=" + v.size() + ", capacity=" + v.capacity());

        v.add(3); // triggers growth: capacity becomes 5 (2 + increment 3)
        System.out.println("After growth size=" + v.size() + ", capacity=" + v.capacity());

        v.ensureCapacity(20); // O(n) copy on grow
        System.out.println("After ensureCapacity(20) capacity=" + v.capacity());

        v.trimToSize(); // shrink to size (O(n))
        System.out.println("After trimToSize() capacity=" + v.capacity());

        v.setSize(6); // increases size; pads new slots with null
        System.out.println("After setSize(6): size=" + v.size() + ", contents=" + v);
    }

    // ------------------------------------------------------------
    // SECTION 3: Stack — Methods + Time Complexity + Examples
    // ------------------------------------------------------------

    /**
     * Stack:
     * - Legacy LIFO stack that extends Vector (inherits synchronization)
     * <p>
     * Time Complexity:
     * push(e)  ............. Amortized O(1) (resize O(n))
     * pop() / peek() ....... O(1)
     * search(o) ............ O(n)
     * empty() .............. O(1)
     */
    private static void stackBasicsAndMethods() {
        Stack<String> stack = new Stack<>();

        // --- Push (LIFO) ---
        stack.push("first");   // Amortized O(1)
        stack.push("second");
        stack.push("third");
        System.out.println("Stack after pushes: " + stack);

        // --- Peek (O(1)) ---
        System.out.println("peek() = " + stack.peek()); // third

        // --- Pop (O(1)) ---
        String popped = stack.pop(); // removes "third"
        System.out.println("pop() = " + popped + ", stack now: " + stack);

        // --- empty() (O(1)) ---
        System.out.println("empty() = " + stack.empty());

        // --- search(Object) (O(n)) ---
        // search returns 1-based position from the top (top is 1)
        int pos = stack.search("first");
        System.out.println("search(\"first\") = " + pos + " (1 means top)");

        // Inherits Vector API:
        stack.add(0, "zeroth"); // O(n) insert at bottom
        System.out.println("After add(0, \"zeroth\"): " + stack);
    }
}
