import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LongestConsecutiveSubsequence {
    public static void main(String[] args) {

        int[] nums = {100, 4, 200, 1, 3, 2};
        System.out.println("Using sorting " + longestConsecutiveSort(nums));
        System.out.println("Using HashSet " + longestConsecutive(nums));
    }

    // Sorting the elements and eleminating the duplicates and
    // finding the maxSize
    // TC ; O(Nlogn);
    public static int longestConsecutiveSort(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        Arrays.sort(nums);
        int maxLength = 0, size = 1;
        for (int i = 0; i < nums.length - 1; i++) {
            // System.out.println(nums[i] + " " + nums[i+1]);
            if (nums[i] + 1 == nums[i + 1]) {
                size++;
            } else if (nums[i] == nums[i + 1]) {
                continue;
            } else {
                maxLength = Math.max(maxLength, size);
                size = 1;
            }
        }
        maxLength = Math.max(maxLength, size);
        return maxLength;
    }

    // O(n) complexity code where we use hashSet
    // checking the elements and finding if the current element is the starting element in
    // sequence and iterating the hashSet for getting maxSequence
    public static int longestConsecutive(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        Set<Integer> setValues = new HashSet<>();
        int result = 0, maxSequence = 0;
        for (int i : nums) {
            setValues.add(i);
        }
        for (int i : setValues) {
            result = 1;
            if (!setValues.contains(i - 1)) {
                while (setValues.contains(i + 1)) {
                    i++;
                    result++;
                }
            }
            maxSequence = Math.max(maxSequence, result);
        }
        return maxSequence;
    }
}
