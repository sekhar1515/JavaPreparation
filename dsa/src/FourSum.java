import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FourSum {
    public static List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        // base case when the array length is less than 4 we just skip that
        if (nums.length < 4) {
            return result;
        }
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            // Process for avoiding duplicates
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            for (int j = i + 1; j < nums.length; j++) {
                // Need to skip consecutive elements to avoid duplicates
                if (j > i + 1 && nums[j] == nums[j - 1]) {
                    continue;
                }
                int k = j + 1, right = nums.length - 1;
                while (k < right) {
                    // we need to cater here as long since we might the value which is greater than int
                    long currTarget = (long) nums[i] + nums[j] + nums[k] + nums[right];
                    if (currTarget == target) {
                        result.add(Arrays.asList(nums[i], nums[j], nums[k], nums[right]));
                        k++;
                        right--;
                        while (k < right && nums[k] == nums[k - 1]) {
                            k++;
                        }
                        while (k < right && nums[right] == nums[right + 1]) {
                            right--;
                        }
                    } else if (currTarget > target) {
                        right--;
                    } else {
                        k++;
                    }
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(fourSum(new int[]{-2, -1, -1, 1, 1, 2, 2}, 0));
    }
}
