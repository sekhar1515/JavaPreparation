import java.util.HashMap;
import java.util.Map;

public class SubArrayWithGivenSum {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3};
        System.out.println("Brute force solution : " + subarraySum(arr, 3));
        System.out.println("Optimal approach using hash set : " + subarraySumOptimal(arr, 3));
    }

    // Implementation using brute force using iteration of arrays internally
    // TC : O(N^2)
    public static int subarraySum(int[] nums, int k) {
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            int sum = nums[i];

            for (int j = i + 1; j < nums.length; j++) {
                if (sum == k) {
                    count++;
                }
                sum += nums[j];
            }
            if (sum == k) {
                count++;
            }
        }
        return count;
    }

    // Using extra space of hashMap to keep track of prefixsum and their occurences
    // TC : O(N)
    // SC: O(N)
    public static int subarraySumOptimal(int[] nums, int k) {
        int count = 0;
        Map<Integer, Integer> mapValues = new HashMap<>();
        mapValues.put(0, 1);
        int prefixSum = 0;
        for (int i : nums) {
            prefixSum += i;
            if (mapValues.containsKey(prefixSum - k)) {
                count += mapValues.get(prefixSum - k);
            }
            mapValues.put(prefixSum, mapValues.getOrDefault(prefixSum, 0) + 1);
        }
        return count;
    }
}
