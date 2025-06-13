import java.util.Arrays;

public class Anagram {
    // Problem link: https://leetcode.com/problems/valid-anagram/description

    /**
     * Checks if two strings are anagrams of each other.
     * Time Complexity: O(n * log n), where n is the length of the strings (due to sorting).
     * Space Complexity: O(n), for the character arrays created from the input strings.
     *
     * @param s the first string
     * @param t the second string
     * @return true if s and t are anagrams, false otherwise
     */
    public boolean isAnagramUsingSort(String s, String t) {
        char[] sArray = s.toCharArray();
        char[] tArray = t.toCharArray();
        Arrays.sort(sArray);
        Arrays.sort(tArray);
        return Arrays.equals(sArray, tArray);
    }

    /**
     * Checks if two strings are anagrams of each other.
     * Time Complexity: O(n), where n is the length of the strings for checking character counts.
     * Space Complexity: O(n), for the character arrays created from the input strings.
     *
     * @param s the first string
     * @param t the second string
     * @return true if s and t are anagrams, false otherwise
     */
    public boolean isAnagramUsingHCount(String s, String t) {
        // If lengths are not equal, they cannot be anagrams
        if (s.length() != t.length()) {
            return false;
        }

        int[] count = new int[26]; // Assuming only lowercase letters a-z
        for (int i = 0; i < s.length(); i++) {
            count[s.charAt(i) - 'a']++;
            count[t.charAt(i) - 'a']--;
        }

        // Check if all counts are zero
        return Arrays.stream(count).allMatch(i -> i == 0);
    }

    public static void main(String[] args) {
        String a = "abcd";
        String b = "dabc";
        Anagram anagram = new Anagram();
        boolean isAnagramUsingSort = anagram.isAnagramUsingSort(a, b);
        boolean isAnagramUsingHCount = anagram.isAnagramUsingHCount(a, b);
        System.out.println("Are the strings anagrams using sort? " + isAnagramUsingSort);
        System.out.println("Are the strings anagrams using character count? " + isAnagramUsingHCount);
    }
}
