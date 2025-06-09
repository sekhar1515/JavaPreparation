import java.util.Arrays;

public class MinimizeTheHeights {

    //problem link : https://www.geeksforgeeks.org/problems/minimize-the-heights3351/1
    public int getMinDiff(int[] arr, int k) {
        // Sort the array to make it easier to compare adjacent elements
        Arrays.sort(arr);
        int length = arr.length - 1;
        // Initial max and min values after sorting
        int max = arr[length], min = arr[0];
        // Initial result is the difference between the largest and smallest elements
        int result = max - min;
        // Iterate through the array and try to minimize the difference
        for (int i = 1; i <= length; i++) {
            if (arr[i] - k < 0) {
                // If the current element minus k is negative, skip this iteration
                continue;
            }
            // Calculate the minimum value after increasing the smallest and decreasing the current element
            int currMin = Math.min(arr[i] - k, arr[0] + k);
            // Calculate the maximum value after increasing the previous element and decreasing the largest
            int currMax = Math.max(arr[i - 1] + k, arr[length] - k);
            // Update the result with the minimum difference found so far
            result = Math.min(result, currMax - currMin);
        }
        // Return the minimized difference
        return result;
    }

    public static void main(String[] args) {
        MinimizeTheHeights minimizeTheHeights = new MinimizeTheHeights();
        int[] arr = {1, 5, 8, 10};
        int k = 2;
        int minDiff = minimizeTheHeights.getMinDiff(arr, k);
        System.out.println("The minimum difference is: " + minDiff);
    }
}
