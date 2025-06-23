
/**
 * A simple implementation of a HashSet for integer keys using a boolean array.
 * Supports add, remove, and contains operations in O(1) time.
 */
class DesignHashSetUsingBooleanArray {
    // Boolean array to represent the presence of keys in the set.
    private boolean[] dataSet;

    /**
     * Initializes the HashSet with a fixed size to accommodate keys from 0 to 1,000,000.
     */
    public DesignHashSetUsingBooleanArray() {
        dataSet = new boolean[1000001];
    }

    /**
     * Adds a key to the set.
     *
     * @param key the integer key to add
     */
    public void add(int key) {
        dataSet[key] = true;
    }

    /**
     * Removes a key from the set.
     *
     * @param key the integer key to remove
     */
    public void remove(int key) {
        dataSet[key] = false;
    }

    /**
     * Checks if the set contains the specified key.
     *
     * @param key the integer key to check
     * @return true if the key is present, false otherwise
     */
    public boolean contains(int key) {
        return dataSet[key];
    }
}

class MyHashSetUsingLinkedList {
    /**
     * Node class for the linked list used in the hash set buckets.
     */
    class LinkedList {
        int key;
        LinkedList next;

        LinkedList(int key) {
            this.key = key;
            this.next = null;
        }
    }

    // Array of linked lists to store the hash set buckets.
    private final LinkedList[] setValues;

    /**
     * Initializes the hash set with a fixed number of buckets.
     * Each bucket is initialized with a dummy head node.
     */
    public MyHashSetUsingLinkedList() {
        setValues = new LinkedList[1000001];
        for (int i = 0; i < setValues.length; i++) {
            setValues[i] = new LinkedList(0); // Dummy head node
        }
    }

    /**
     * Adds a key to the hash set.
     * If the key already exists, it does nothing.
     *
     * @param key the integer key to add
     */
    public void add(int key) {
        int index = key % setValues.length;
        LinkedList current = setValues[index];
        while (current.next != null) {
            if (current.next.key == key) {
                return; // Key already exists
            }
            current = current.next;
        }
        current.next = new LinkedList(key); // Add new key at the end
    }

    /**
     * Removes a key from the hash set.
     * If the key does not exist, it does nothing.
     *
     * @param key the integer key to remove
     */
    public void remove(int key) {
        int index = key % setValues.length;
        LinkedList current = setValues[index];
        while (current.next != null) {
            if (current.next.key == key) {
                current.next = current.next.next; // Remove the node
                return;
            }
            current = current.next;
        }
    }

    /**
     * Checks if the hash set contains the specified key.
     *
     * @param key the integer key to check
     * @return true if the key is present, false otherwise
     */
    public boolean contains(int key) {
        int index = key % setValues.length;
        LinkedList current = setValues[index];
        while (current.next != null) {
            if (current.next.key == key) {
                return true;
            }
            current = current.next;
        }
        return false;
    }
}

public class DesignHashSet {
    public static void main(String[] args) {
        MyHashSetUsingLinkedList myHashSet = new MyHashSetUsingLinkedList();
        myHashSet.add(1); // set = [1]
        myHashSet.add(2); // set = [1, 2]
        System.out.println(myHashSet.contains(1)); // return True
        myHashSet.contains(3); // return False, (not found)
        myHashSet.add(2); // set = [1, 2]
        myHashSet.contains(2); // return True
        myHashSet.remove(2); // set = [1]
        myHashSet.contains(2);
    }

}
