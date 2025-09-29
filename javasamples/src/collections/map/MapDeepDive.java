package collections.map;
/**
 * Java Map Deep‑Dive
 * <p>
 * A comprehensive, single-file reference on Java's Map data structure:
 * - Map interface contracts & operations
 * - Implementations: HashMap, LinkedHashMap, TreeMap, ConcurrentHashMap, WeakHashMap,
 * IdentityHashMap, EnumMap, Hashtable, Collections.synchronizedMap(...)
 * - Internals: hashing, treeification, resizing, R-B trees, iteration behavior
 * - Concurrency specifics & atomic operations
 * - Performance tuning, memory, best practices
 * - Practical recipes & demos in main()
 * <p>
 * NOTE: This file is designed to be readable as a tutorial while remaining compilable and runnable.
 * You can run it to see small demos in stdout.
 */

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public class MapDeepDive {

    /* ================================================================
     * 1) Map Interface — Key Contracts & Essentials
     * ================================================================
     *
     * - Keys are unique; values may repeat.
     * - equals/hashCode contract for hash-based maps:
     *     If a.equals(b) then a.hashCode() == b.hashCode().
     * - Most non-concurrent map iterators are fail-fast (best effort):
     *     Structural modification outside the iterator -> ConcurrentModificationException (CME).
     *
     * Essential Operations:
     *   get(K), put(K,V), remove(K), containsKey(K), containsValue(V)
     *   putIfAbsent, getOrDefault, compute, computeIfAbsent, computeIfPresent, merge, replaceAll
     *   Views: keySet(), values(), entrySet()  (these are backed by the map)
     */

    /* ================================================================
     * 2) Choosing the Right Map — Quick Cheat Sheet
     * ================================================================
     *
     * HashMap            : Unordered, allows 1 null key + many null values, O(1) avg, not thread-safe
     * LinkedHashMap      : Insertion or access order, allows nulls, O(1) avg, not thread-safe
     * TreeMap            : Sorted by key (Comparable/Comparator), null values ok, null key not allowed with natural order, O(log n)
     * ConcurrentHashMap  : Unordered, no nulls, O(1) avg, highly concurrent (weakly consistent iterators)
     * Hashtable (legacy) : Unordered, no nulls, O(1) avg, method-synchronized (coarse), avoid in new code
     * WeakHashMap        : Unordered, GC removes entries when keys are weakly reachable, O(1) avg, not thread-safe
     * IdentityHashMap    : Unordered, identity (==) equality for keys, O(1) avg, not thread-safe, niche
     * EnumMap            : Enum keys only, array-backed, very fast & compact, natural enum order, O(1) avg, not thread-safe
     * Collections.synchronizedMap(map): Wrapper using a single intrinsic lock; iteration requires external sync on the map
     */

    /* ================================================================
     * 3) HashMap Internals (Java 8+)
     * ================================================================
     *
     * - Backed by an array Node<K,V>[] of power-of-two length n.
     * - Bucket index: i = (n - 1) & spreadHash(key.hashCode()), where spreadHash mixes high bits (h ^ (h >>> 16)).
     * - Collision handling:
     *     Initially: singly linked list per bucket.
     *     If bucket size exceeds TREEIFY_THRESHOLD=8 and table capacity >= MIN_TREEIFY_CAPACITY=64, bin treeifies into a red-black tree (TreeNode).
     *     On shrink below UNTREEIFY_THRESHOLD=6, may revert to list.
     * - Resizing:
     *     Triggered when size > threshold (capacity * loadFactor). Capacity doubles; nodes redistributed (low/high bit trick)
     *     into either index i or i + oldCapacity.
     * - Nulls:
     *     Allows one null key and many null values.
     * - Complexity:
     *     Average O(1) for get/put/remove; worst-case O(log n) in tree bins (else O(n) if pathological).
     * - Iteration:
     *     Fail-fast (modCount) best effort.
     */

    /* ================================================================
     * 4) LinkedHashMap Internals
     * ================================================================
     *
     * - Extends HashMap + doubly-linked list across entries to preserve iteration order.
     * - Modes:
     *     Insertion order (default) OR access order (new LinkedHashMap<>(cap, lf, true)).
     * - LRU Cache:
     *     Override removeEldestEntry to evict when size exceeds bound.
     */

    /* ================================================================
     * 5) TreeMap Internals (NavigableMap)
     * ================================================================
     *
     * - Backed by a Red-Black tree -> O(log n) get/put/remove.
     * - Sorted by natural ordering or a provided Comparator.
     * - Null values ok; null key not allowed with natural ordering.
     * - Range views (subMap/headMap/tailMap) & navigation (lower/higher, floor/ceiling, pollFirst/Last).
     * - Prefer over HashMap when you need sorted keys, range queries, or stable ordered traversal.
     */

    /* ================================================================
     * 6) ConcurrentHashMap Internals (Java 8+)
     * ================================================================
     *
     * - High concurrency via per-bin synchronization and CAS; tree bins under high collision like HashMap.
     * - No null keys/values (avoid ambiguity/races in concurrent get).
     * - Iterators are weakly consistent (see some updates, no CME).
     * - Atomic per-key ops: compute*, merge, etc.
     * - Bulk operations: forEach, search, reduce with parallelism hints.
     */

    /* ================================================================
     * 7) Specialized Maps
     * ================================================================
     *
     * WeakHashMap:
     *   - Keys are weakly referenced; if no strong refs exist, entries are removed by GC (non-deterministic timing).
     *   - Good for caches of computed/derived data tied to key lifecycle.
     *
     * IdentityHashMap:
     *   - Keys compared by reference (==), not equals(). Niche.
     *
     * EnumMap:
     *   - Array-backed using enum ordinal. Extremely compact and fast. Keys must be of a single enum type.
     *
     * Hashtable (legacy):
     *   - Method synchronized, no nulls. Prefer ConcurrentHashMap or synchronizedMap wrapper.
     *
     * Collections.synchronizedMap(map):
     *   - Coarse lock wrapper; during iteration, manually synchronize on the map.
     */

    /* ================================================================
     * 8) Functional Ops (Java 8+)
     * ================================================================
     * getOrDefault, putIfAbsent, compute, computeIfAbsent, computeIfPresent, merge, replaceAll
     *
     * Example (memoization): value = map.computeIfAbsent(key, k -> expensive(k));
     */

    /* ================================================================
     * 9) Performance, Memory, and Tuning
     * ================================================================
     *
     * - Load factor & capacity:
     *     Default LF 0.75. Pre-size when size known to reduce resizes:
     *       new HashMap<>((int)(expectedSize / 0.75f) + 1, 0.75f)
     *
     * - Memory footprint:
     *     Each entry carries overhead (node + references). EnumMap is compactest for enum keys.
     *
     * - Hash quality:
     *     Ensure well-distributed hashCode for custom keys; include significant fields.
     *
     * - Iteration:
     *     Use entrySet() when needing both key and value. Avoid repeated map.get(k) inside loops if you already have v.
     *
     * - Bulk ops:
     *     Use putAll for batches; consider pre-sizing.
     */

    /* ================================================================
     * 10) Concurrency Patterns & Pitfalls
     * ================================================================
     * DO:
     *   - Use ConcurrentHashMap for frequent concurrent reads/writes.
     *   - Prefer compute/merge for atomic updates.
     * DO NOT:
     *   - Share a plain HashMap across threads without external synchronization.
     *   - Rely on iteration order for HashMap/CHM.
     *   - Use nulls in CHM.
     *
     * Example synchronized iteration with synchronizedMap:
     *   Map<K,V> m = Collections.synchronizedMap(new HashMap<>());
     *   synchronized (m) { for (Map.Entry<K,V> e : m.entrySet()) { ... } }
     */

    /* ================================================================
     * 11) Practical Recipes (compact)
     * ================================================================
     */

    /**
     * Simple LRU cache using LinkedHashMap (access-order).
     */
    static class LruCache<K, V> extends LinkedHashMap<K, V> {
        private final int cap;

        LruCache(int cap) {
            super(cap, 0.75f, true); // access-order
            this.cap = cap;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > cap;
        }
    }

    /**
     * Demonstrates safe concurrent counters with LongAdder in CHM.
     */
    static class ConcurrentCounters {
        private final ConcurrentHashMap<String, LongAdder> counters = new ConcurrentHashMap<>();

        void inc(String key) {
            counters.computeIfAbsent(key, k -> new LongAdder()).increment();
        }

        long get(String key) {
            LongAdder a = counters.get(key);
            return a == null ? 0L : a.sum();
        }
    }

    /**
     * Enum for EnumMap demo.
     */
    enum Color {RED, GREEN, BLUE}

    /**
     * Record-like user for dedup examples (pre-Java 16 style for wide compatibility).
     */
    static class User {
        final int id;
        final String name;

        User(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return "User{id=" + id + ", name='" + name + "'}";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof User)) return false;
            User u = (User) o;
            return id == u.id && Objects.equals(name, u.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name);
        }
    }

    /* ================================================================
     * 12) Main — Mini Showcase
     * ================================================================
     */
    public static void main(String[] args) {
        System.out.println("=== MapDeepDive: Mini Showcase ===");

        // 1) HashMap basics
        Map<String, Integer> hm = new HashMap<>(32, 0.75f);
        hm.put("a", 1);
        hm.put("b", 2);
        hm.put("c", 3);
        System.out.println("HashMap getOrDefault z -> " + hm.getOrDefault("z", -1));

        // 2) LinkedHashMap LRU
        LruCache<String, Integer> lru = new LruCache<>(2);
        lru.put("x", 1);
        lru.put("y", 2);
        lru.get("x");
        lru.put("z", 3); // evicts "y"
        System.out.println("LinkedHashMap LRU -> " + lru);

        // 3) TreeMap range operations
        NavigableMap<Integer, String> tm = new TreeMap<>();
        tm.put(10, "ten");
        tm.put(5, "five");
        tm.put(20, "twenty");
        System.out.println("TreeMap subMap[5..15) -> " + tm.subMap(5, true, 15, false));
        System.out.println("TreeMap floorEntry(12) -> " + tm.floorEntry(12));
        System.out.println("TreeMap higherEntry(10) -> " + tm.higherEntry(10));

        // 4) ConcurrentHashMap atomic updates
        ConcurrentHashMap<String, Long> chm = new ConcurrentHashMap<>();
        chm.merge("hits", 1L, Long::sum);
        chm.compute("hits", (k, v) -> v == null ? 1L : v + 1);
        System.out.println("ConcurrentHashMap -> " + chm);

        // 5) EnumMap — fast & compact for enum keys
        EnumMap<Color, String> em = new EnumMap<>(Color.class);
        em.put(Color.RED, "#ff0000");
        em.put(Color.GREEN, "#00ff00");
        System.out.println("EnumMap -> " + em);

        // 6) WeakHashMap demo (GC-sensitive keys)
        WeakHashMap<Object, String> whm = new WeakHashMap<>();
        Object key = new Object();
        whm.put(key, "temp");
        System.out.println("WeakHashMap size before GC: " + whm.size());
        key = null; // eligible for GC
        System.gc(); // hint (non-deterministic)
        // Entry may or may not be removed immediately.
        System.out.println("WeakHashMap size after GC hint: " + whm.size());

        // 7) IdentityHashMap (identity semantics)
        IdentityHashMap<String, Integer> ihm = new IdentityHashMap<>();
        String s1 = new String("id"), s2 = new String("id");
        ihm.put(s1, 1);
        ihm.put(s2, 2); // distinct because s1 != s2 (by reference)
        System.out.println("IdentityHashMap -> " + ihm);

        // 8) Collections.synchronizedMap wrapper
        Map<String, Integer> base = new HashMap<>();
        Map<String, Integer> syncMap = Collections.synchronizedMap(base);
        syncMap.put("k", 42);
        synchronized (syncMap) { // required during iteration
            for (Map.Entry<String, Integer> e : syncMap.entrySet()) {
                // safe iteration under intrinsic lock
            }
        }

        // 9) Deduplicate by ID using putIfAbsent (first wins)
        List<User> users = Arrays.asList(new User(1, "Ann"), new User(1, "Ann v2"), new User(2, "Bob"));
        Map<Integer, User> byId = new HashMap<>();
        for (User u : users) byId.putIfAbsent(u.id, u);
        System.out.println("Dedup by ID (first wins) -> " + byId);

        // 10) Concurrent counters with LongAdder via CHM
        ConcurrentCounters counters = new ConcurrentCounters();
        counters.inc("hits");
        counters.inc("hits");
        counters.inc("miss");
        System.out.println("Counters: hits=" + counters.get("hits") + ", miss=" + counters.get("miss"));

        System.out.println("=== End Showcase ===");
    }
}
