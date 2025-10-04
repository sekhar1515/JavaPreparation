package collections.set;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;
import java.util.Set;

public class JavaSetInternalsDemo {
    public static void main(String[] args) {
        // ------------------- HashSet -------------------
        // HashSet internally uses a HashMap to store elements.
        // Each element in the HashSet is stored as a key in the HashMap,
        // and a constant dummy value is associated with each key.
        // HashMap organizes these keys into buckets based on their hashCode().
        // If multiple elements have the same hash, they form a linked list or balanced tree
        // inside that bucket. Since keys are unique in maps, duplicates are naturally prevented.
        Set<String> hashSet = new HashSet<>();
        hashSet.add("Apple");
        hashSet.add("Banana");
        hashSet.add("Orange");
        hashSet.add("Apple"); // Duplicate, will not be added

        System.out.println("HashSet Example (No order, no duplicates):");
        System.out.println(hashSet); // Output order is NOT predictable

        // ------------------- LinkedHashSet -------------------
        // LinkedHashSet extends HashSet, and so also uses a LinkedHashMap internally.
        // Like HashMap, elements are stored as keys with a dummy value.
        // Additionally, LinkedHashMap maintains a doubly linked list connecting all entries,
        // preserving insertion order when iterating over the set.
        Set<String> linkedSet = new LinkedHashSet<>();
        linkedSet.add("Red");
        linkedSet.add("Green");
        linkedSet.add("Blue");
        linkedSet.add("Green"); // Duplicate, ignored

        System.out.println("\nLinkedHashSet Example (Order preserved, no duplicates):");
        System.out.println(linkedSet); // Output: [Red, Green, Blue]

        // ------------------- TreeSet -------------------
        // TreeSet does NOT use a map internally. Instead, it uses a Red-Black tree (a balanced
        // binary search tree) to store elements in sorted order.
        // Elements themselves are stored as nodes in the tree.
        // When adding an element, the tree performs comparisons to place it correctly,
        // ensuring sorted and unique elements, with O(log n) insert, remove, and search.
        Set<Integer> treeSet = new TreeSet<>();
        treeSet.add(10);
        treeSet.add(5);
        treeSet.add(20);
        treeSet.add(10); // Duplicate, ignored

        System.out.println("\nTreeSet Example (Sorted, no duplicates):");
        System.out.println(treeSet); // Output: [5, 10, 20]

        // ------------------- Set Operations -------------------
        Set<Integer> setA = new HashSet<>();
        Set<Integer> setB = new HashSet<>();
        setA.add(1);
        setA.add(2);
        setA.add(3);
        setA.add(4);
        setB.add(3);
        setB.add(4);
        setB.add(5);
        setB.add(6);

        // Set Union (A ∪ B)
        Set<Integer> union = new HashSet<>(setA);
        union.addAll(setB); // Adds all from setB
        System.out.println("\nSet Union:");
        System.out.println(union); // [1, 2, 3, 4, 5, 6]

        // Set Intersection (A ∩ B)
        Set<Integer> intersection = new HashSet<>(setA);
        intersection.retainAll(setB); // Keeps only common elements
        System.out.println("Set Intersection:");
        System.out.println(intersection); // [3, 4]

        // Set Difference (A - B)
        Set<Integer> difference = new HashSet<>(setA);
        difference.removeAll(setB); // Removes elements present in setB
        System.out.println("Set Difference:");
        System.out.println(difference); // [1, 2]
    }
}
