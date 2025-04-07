import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MajorityElement2 {

    //Finding the elements that have occured more than n/3 times
    // TC: O(N) SC: O(N)
    public List<Integer> majorityElement(int[] nums) {
        Map<Integer,Integer> mapSum = new HashMap<>();
        for(int i: nums){
            mapSum.put(i, mapSum.getOrDefault(i, 0) + 1);
        }
        int size = nums.length, n = size/3;
        List<Integer> result = new ArrayList<>();
        for(Map.Entry<Integer, Integer> entry: mapSum.entrySet()){
            if(entry.getValue() > n){
                result.add(entry.getKey());
            }
        }
        return result;
    }

}
