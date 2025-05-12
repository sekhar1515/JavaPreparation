public class FindingPeakElement {
    //problem link : https://leetcode.com/problems/find-peak-element/description/
    // linear way of finding the value
    public int findPeakElement(int[] nums) {
        // first we handle edge cases where we can find the solution
        int length = nums.length;
        if (nums.length == 1) {
            return 0;
        } else if (nums[0] > nums[1]) {
            return 0;
        } else if (nums[length - 1] > nums[length - 2]) {
            return length - 1;
        } else {
            for (int i = 1; i < length - 1; i++) {
                if (nums[i] > nums[i - 1] && nums[i] > nums[i + 1]) {
                    return i;
                }
            }
            return 0;
        }
    }

    public int findPeakElementUsingBinarySearch(int[] nums) {
        int length = nums.length;
        if (nums.length == 1) {
            return 0;
        } else if (nums[0] > nums[1]) {
            return 0;
        } else if (nums[length - 1] > nums[length - 2]) {
            return length - 1;
        } else {
            int low = 0, high = length - 1;
            while (low <= high) {
                int mid = low + (high - low) / 2;
                if (nums[mid] > nums[mid + 1] && nums[mid] > nums[mid - 1]) {
                    return mid;
                } else if (nums[mid] > nums[mid + 1]) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            }
            return 0;
        }
    }

    public static void main(String[] args) {
        FindingPeakElement peakElement = new FindingPeakElement();
        int indexValueUsingLinearSearch = peakElement.findPeakElement(new int[]{1, 2, 1, 3, 5, 6, 4});
        int index = peakElement.findPeakElementUsingBinarySearch(new int[]{1, 2, 1, 3, 5, 6, 4});
        System.out.println(index);
        System.out.println(indexValueUsingLinearSearch);
    }
}
