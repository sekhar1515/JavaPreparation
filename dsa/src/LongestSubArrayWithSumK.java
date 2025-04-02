import java.util.HashMap;
import java.util.Map;

public class LongestSubArrayWithSumK {
    //Brute force Approach which takes o(n^2)
    public static int longestSubarray(int[] arr, int k) {
        // code here
        int i = 0, j = 0, maxLengthOfSubArray = 0;
        for (i = 0; i < arr.length; i++) {
            int sum = 0;
            for (j = i; j < arr.length; j++) {
                sum += arr[j];
                if (sum == k) {
                    maxLengthOfSubArray = Math.max(maxLengthOfSubArray, j - i + 1);
                }
            }
        }
        return maxLengthOfSubArray;
    }

    // PrefixSumApproach using hashMap
    // We iterate checking the prefix Sum and computing the values as needed
    // TC: O(N) SC: O(N)
    public static int longestSubArrayUsingPrefix(int[] arr, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        int maxLengthOfSubArray = 0, sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            if (sum == k) {
                maxLengthOfSubArray = Math.max(maxLengthOfSubArray, i + 1);
            }
            if (map.containsKey(sum - k)) {
                maxLengthOfSubArray = Math.max(maxLengthOfSubArray, i - map.get(sum - k));
            }
            map.putIfAbsent(sum, i);
        }
        return maxLengthOfSubArray;
    }

    public static void main(String[] args) {
        int ans = longestSubarray(new int[]{94, -33, -13, 40, -82, 94, -33, -13, 40, -82
        }, 52);
        System.out.println(ans);
        int ansWithPrefixSum = longestSubArrayUsingPrefix(new int[]{94, -33, -13, 40, -82, 94, -33, -13, 40, -82
        }, 52);
        System.out.println(ansWithPrefixSum);
// There is another approach that will only work for positives in the array which is two pointer approach.

    }
}
