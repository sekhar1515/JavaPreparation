import java.util.*;

public class GroupAnagrams {

    /**
     * Groups anagrams using sorted strings as keys.
     * <p>
     * Time Complexity: O(n * k log k)
     * - n = number of strings in input array
     * - k = maximum length of any string
     * - Sorting each string takes O(k log k), and we do this for all n strings.
     * <p>
     * Space Complexity: O(n * k)
     * - Up to n keys in the hashmap
     * - Each key is a string of length k
     * - Each value is a list of original strings, total length of all strings is O(n * k)
     */
    public static List<List<String>> groupAnagramsUsingSorting(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            // Sort characters of the string to form the key
            char[] chars = s.toCharArray();
            Arrays.sort(chars); // O(k log k)
            String key = String.valueOf(chars);
            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(s);
        }
        return new ArrayList<>(map.values());
    }

    /**
     * Groups anagrams using character frequency arrays as keys.
     * <p>
     * Time Complexity: O(n * k)
     * - n = number of strings
     * - k = maximum length of a string
     * - Counting characters takes O(k) per string
     * - Converting count array to string takes O(26) = O(1)
     * <p>
     * Space Complexity: O(n * k)
     * - HashMap stores up to n keys
     * - Each value is a list of strings (total space O(n * k))
     * - Keys are strings of fixed size (O(1)), since character array is of size 26
     */
    public static List<List<String>> groupAnagramsUsingCharCount(String[] strings) {
        Map<String, List<String>> mapValues = new HashMap<>();
        for (String s : strings) {
            int[] charCount = new int[26]; // Assuming input only contains lowercase letters
            for (char ch : s.toCharArray()) {
                charCount[ch - 'a']++; // Count frequency
            }
            // Use the character count array as the key
            String key = Arrays.toString(charCount); // O(1), since size is fixed (26)
            mapValues.putIfAbsent(key, new ArrayList<>());
            mapValues.get(key).add(s);
        }
        return new ArrayList<>(mapValues.values());
    }

    public static void main(String[] args) {
        String[] strs = {"eat", "tea", "tan", "ate", "nat", "bat"};

        // Test using sorting method
        List<List<String>> result = groupAnagramsUsingSorting(strs);
        System.out.println("Using Sorting:");
        System.out.println(result);

        // Test using character count method
        List<List<String>> result2 = groupAnagramsUsingCharCount(strs);
        System.out.println("Using Character Count:");
        System.out.println(result2);
    }
}
