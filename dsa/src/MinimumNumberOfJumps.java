public class MinimumNumberOfJumps {
    public static void main(String[] args) {
        int[] arr = {1, 3, 5, 8, 9, 2, 6, 7, 6, 8, 9};
        int n = arr.length;
        System.out.println("Minimum number of jumps to reach the end is: " + minJumps(arr, n));
    }

    //problemLink: https://www.geeksforgeeks.org/problems/minimum-number-of-jumps-1587115620/1
    public static int minJumps(int[] arr, int n) {
        int jumps = 1; // Initialize jumps to 1, as we start at the first element
        int steps = arr[0];  // Initialize steps to the value of the first element, which indicates how far we can jump initially
        int maxReach = arr[0]; // MaxReach is the maximum index we can reach at any point
        // base condition where if the first element is 0, we cannot move anywhere
        if (maxReach == 0) {
            return -1; // If the first element is 0, we cannot move anywhere
        }
        // Iterate through the array starting from the second element
        for (int i = 1; i < n; i++) {
            // if we reach the end then we return the number of jumps
            if (i == n - 1) {
                return jumps; // If we have reached the last element, return the number of jumps
            }
            // Updating the maximum reach at each step
            maxReach = Math.max(maxReach, i + arr[i]);
            // Decrease the number of steps we can take
            steps--;
            // If we have no more steps left, we need to make a jump
            if (steps == 0) {
                // Increment the jump count
                jumps++;
                //if at anypoint the index is greater than the maximum reach, it means we cannot proceed further
                if (i > maxReach) {
                    return -1;
                }
                // adjusting the value of steps to the maximum reach from the current index
                steps = maxReach - i;
            }
        }
        // If we exit the loop without reaching the end, return -1
        return -1;
    }
}
