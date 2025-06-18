import java.util.HashMap;
import java.util.Map;

public class TwoSum {


    //leetcode problem link: https://leetcode.com/problems/two-sum/description/
    // Finds two indices such that the numbers at those indices add up to the target.
    // Returns an array with the indices, or \{-1, -1\} if no such pair exists.
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int diff = target - nums[i];
            // Check if the complement exists in the map
            if (countMap.containsKey(diff)) {
                return new int[]{countMap.get(diff), i};
            }
            // Store the current number and its index
            countMap.put(nums[i], i);
        }
        // Return \{-1, -1\} if no valid pair is found
        return new int[]{-1, -1};
    }

    public static void main(String[] args) {
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        int[] result = twoSum(nums, target);
        if (result[0] != -1) {
            System.out.println("Indices of the two numbers that add up to " + target + " are: " + result[0] + " and " + result[1]);
        } else {
            System.out.println("No two numbers add up to " + target);
        }
    }
}
