import java.util.Arrays;


public class LongestCommonPrefix {

    /**
     * This implementation uses a two-pass technique to find the longest common prefix.
     * <p>
     * Strategy:
     * 1. Sort strings by length (shortest first)
     * 2. Try decreasing prefix lengths of the shortest string
     * 3. Return the first prefix that matches across all strings
     * <p>
     * Time Complexity: O(n * m^2)
     * - Sorting: O(n log n)
     * - Prefix comparison: O(n * m^2), where n = number of strings, m = length of shortest string
     */
    private static String longestCommonPrefixUsingTwoPass(String[] strs) {
        // Sort the array by string length so the shortest string comes first
        // This ensures we never check beyond the possible max length of any common prefix
        Arrays.sort(strs, (a, b) -> a.length() - b.length());

        String shortestWord = strs[0];  // Candidate prefix base
        boolean foundString = true;

        // Start with the full length of the shortest word, and reduce until a common prefix is found
        for (int i = shortestWord.length() - 1; i >= 0; i--) {
            for (int j = 1; j < strs.length; j++) {
                // Check if the current prefix of length i+1 is common across all strings
                if (!shortestWord.substring(0, i + 1).equals(strs[j].substring(0, i + 1))) {
                    foundString = false;
                    break;
                }
                foundString = true;
            }

            // If a common prefix of this length is found, return it immediately
            if (foundString) {
                return shortestWord.substring(0, i + 1);
            }
        }

        // No common prefix found
        return "";
    }

    /**
     * Finds the longest common prefix among all strings in the input array using a one-pass approach.
     * The method sorts the array lexicographically, then compares only the first and last strings.
     * Because sorting places similar prefixes together, comparing the first and last string gives the LCP for all.
     *
     * Example:
     * Input: ["flower", "flow", "flight"]
     * After sorting: ["flight", "flow", "flower"]
     * Longest common prefix = common prefix between "flight" and "flower" = "fl"
     *
     * @param strs Array of input strings
     * @return The longest common prefix shared by all strings in the array
     *
     * Time Complexity: O(n log n * m)
     * - Sorting: O(n log n * m), where n = number of strings, m = average string length
     * - Prefix comparison: O(m) in the worst case
     *
     * Space Complexity: O(1) extra space (not including input)
     */
    private static String longestCommonPrefixUsingOnePass(String[] strs) {
        // Sort the array lexicographically
        java.util.Arrays.sort(strs);

        // The common prefix must be shared between the smallest and largest string
        String shortestWord = strs[0];
        String longestWord = strs[strs.length - 1];

        int i = 0;

        // Compare characters one-by-one until mismatch is found
        while (i < shortestWord.length() && shortestWord.charAt(i) == longestWord.charAt(i)) {
            i++;
        }

        // Return the common prefix substring
        return shortestWord.substring(0, i);
    }

    public static void main(String[] args) {
        String[] strs = {"flower", "flow", "flight"};
        String result = longestCommonPrefixUsingTwoPass(strs);
        System.out.println("Longest Common Prefix: " + result);
        String resultOnePass = longestCommonPrefixUsingOnePass(strs);
        System.out.println("Longest Common Prefix using One Pass: " + resultOnePass);
    }
}
