import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCacheImplementation {

    public static void main(String[] args) {
        // Create an LRU cache with a capacity of 4
        LruUsingLinkedHashMap<String, Integer> lruUsingLinkedHashMap = new LruUsingLinkedHashMap<>(4);

        // Adding entries
        lruUsingLinkedHashMap.put("hi", 1);
        lruUsingLinkedHashMap.put("Hello", 2);
        lruUsingLinkedHashMap.put("Hola", 3);
        lruUsingLinkedHashMap.put("love", 4);

        // Accessing "love" makes it most recently used
        lruUsingLinkedHashMap.get("love");

        // Adding "mike" exceeds capacity → least recently used ("hi") will be removed
        lruUsingLinkedHashMap.put("mike", 5);

        // Access "Hello" → makes it recently used
        lruUsingLinkedHashMap.get("Hello");

        // Adding "newEntry" exceeds capacity → next least recently used ("Hola") will be removed
        lruUsingLinkedHashMap.put("newEntry", 6);

        // Print final contents of cache
        for (Map.Entry<String, Integer> mapValue : lruUsingLinkedHashMap.entrySet()) {
            System.out.println(mapValue.getKey() + " " + mapValue.getValue());
        }
    }
}

/**
 * LRU (Least Recently Used) Cache implementation
 * using LinkedHashMap in access-order mode.
 */
class LruUsingLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    private int capacity; // Maximum capacity of the cache

    public LruUsingLinkedHashMap(int capacity) {
        // accessOrder = true → maintains order based on access (get/put)
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    /**
     * Automatically removes the eldest entry
     * once the cache exceeds the defined capacity.
     */
    @Override
    public boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}
