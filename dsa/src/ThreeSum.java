import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreeSum {
    public static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        for(int i = 0; i < nums.length ; i++){
            if(i > 0 && nums[i] == nums[i-1]){
                continue;
            }
            int reqSum = -1*nums[i];
            int k = i + 1, right = nums.length - 1;
            while(k < right){
                int sum = nums[k] + nums[right];
                if(sum == reqSum){
                    result.add(List.of(nums[i], nums[k], nums[right]));
                    k++;
                    right--;
                    while (k < right && nums[k] == nums[k - 1]) k++;
                    while (k < right && nums[right] == nums[right + 1]) right--;
                }else if(sum > reqSum){
                    right--;
                }else{
                    k++;
                }
            }
        }
        return result;
    }
    public static void main(String[] args) {
        System.out.println(threeSum(new int[]{-1, 0, 1, 2, -1, -4}));
    }
}
