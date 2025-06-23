public class DesigningHashMap {
    public static void main(String[] args) {
        MyHashMapUsingLinkedList myHashMapUsingLinkedList = new MyHashMapUsingLinkedList();
        myHashMapUsingLinkedList.put(1, 10);
        System.out.println(myHashMapUsingLinkedList.get(1));
        myHashMapUsingLinkedList.remove(1);
        System.out.println(myHashMapUsingLinkedList.get(1));
    }

}
// Implementation of a simple HashMap using an array for integer keys in the range [0, 1000000]
class MyHashMapUsingArray {
    // Array to store values, index represents the key
    private int[] values;

    // Constructor initializes the array and sets all values to -1 (indicating no value)
    public MyHashMapUsingArray() {
        values = new int[1000001];
        for (int i = 0; i < values.length; i++) {
            values[i] = -1;
        }
    }

    // Associates the specified value with the specified key
    public void put(int key, int value) {
        values[key] = value;
    }

    // Returns the value to which the specified key is mapped, or -1 if not present
    public int get(int key) {
        return values[key];
    }

    // Removes the mapping for the specified key by setting it to -1
    public void remove(int key) {
        values[key] = -1;
    }
}

// Implementation of a simple HashMap using separate chaining with linked lists
class MyHashMapUsingLinkedList {

    // Node class for the linked list
    class LinkedListNode {
        int key;
        int value;
        LinkedListNode next;

        // Constructor to initialize node with key and value
        LinkedListNode(int key, int value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private LinkedListNode[] buckets; // Array of bucket heads (dummy nodes)
    private static final int SIZE = 1000; // Number of buckets

    // Constructor initializes buckets with dummy nodes
    public MyHashMapUsingLinkedList() {
        this.buckets = new LinkedListNode[SIZE];
        for (int i = 0; i < SIZE; i++) {
            buckets[i] = new LinkedListNode(-1, -1); // Dummy node for each bucket
        }
    }

    // Hash function to map key to bucket index
    public int hash(int key) {
        return key % SIZE; // Simple modulus hash function
    }

    // Returns the value associated with the key, or -1 if not found
    public int get(int key) {
        int hashIndex = hash(key);
        LinkedListNode current = buckets[hashIndex];
        while (current.next != null) {
            if (current.next.key == key) {
                return current.value; // Return value if key found
            }
            current = current.next;
        }
        return -1; // Key not found
    }

    // Associates the specified value with the specified key
    public void put(int key, int value) {
        int hashIndex = hash(key);
        LinkedListNode current = buckets[hashIndex];
        while (current.next != null) {
            if (current.next.key == key) {
                current.next.value = value; // Update existing value
                return;
            }
            current = current.next;
        }
        // If key not found, add new node at the end
        LinkedListNode newNode = new LinkedListNode(key, value);
        current.next = newNode;
    }

    // Removes the mapping for the specified key if present
    public void remove(int key) {
        int hashIndex = hash(key);
        LinkedListNode current = buckets[hashIndex];
        while (current.next != null) {
            if (current.next.key == key) {
                current.next = current.next.next; // Remove the node
                return;
            }
            current = current.next;
        }
    }
}