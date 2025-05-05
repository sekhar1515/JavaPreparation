public class MininmumInRotatedSortedArray {
    public int findMin(int[] nums) {
        int low = 0, high = nums.length - 1;
        int minValue = Integer.MAX_VALUE;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            minValue = Math.min(minValue, nums[mid]);
            // checking where we can get minimum in the array
            if (nums[mid] < nums[high]) {
                high = mid - 1;
            } else {
                // elimintating the left half
                low = mid + 1;
            }
        }
        return minValue;
    }

    public static void main(String[] args) {
        MininmumInRotatedSortedArray mininmumInRotatedSortedArray = new MininmumInRotatedSortedArray();
        int minimum = mininmumInRotatedSortedArray.findMin(new int[]{9, 8, 7, 1, 2, 3, 4, 5, 6});
        System.out.println(minimum);
    }
}
