import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MergeIntervals {
    public static int[][] merge(int[][] intervals) {
        List<Integer> result = new ArrayList<>();
        //Sorting the array based on the first element so that we will start from beginning
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        int i = 0;
        // Iterating over the intervals
        for (int[] interval : intervals) {
            if (i == 0) {
                result.add(interval[0]);
                result.add(interval[1]);
            } else {
                // retreiving the last element to check if the last element is greater than current interval
                int lastVal = result.get(result.size() - 1);
                if (lastVal >= interval[0]) {
                    if (lastVal >= interval[1]) {
                        continue;
                    } else {
                        result.remove(result.size() - 1);
                        result.add(interval[1]);
                    }
                } else {
                    result.add(interval[0]);
                    result.add(interval[1]);
                }
            }
            i++;
        }
        int length = result.size() / 2;
        int[][] resultArray = new int[length][2];
    // storing the final result here
        for (int j = 0; j < length; j++) {
            resultArray[j][0] = result.get(j * 2);
            resultArray[j][1] = result.get(j * 2 + 1);
        }
        return resultArray;
    }

    public static void main(String[] args) {
        int[][] result = merge(new int[][]{{1, 3}, {2, 4}, {5, 6}, {7, 8}});
        System.out.println(Arrays.deepToString(result));
    }
}
