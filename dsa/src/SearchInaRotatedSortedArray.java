public class SearchInaRotatedSortedArray {
    //    Problem link : https://leetcode.com/problems/search-in-rotated-sorted-array/
    public static void main(String[] args) {
        int[] arr = {4, 5, 6, 7, 0, 1, 2};
        int target = 0;
        SearchInaRotatedSortedArray search = new SearchInaRotatedSortedArray();
        int position = search.findElementInSortedArray(arr, target);
        System.out.println(position);
    }

    private int findElementInSortedArray(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        // Using binary search to find the element
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] == target) {
                return mid;
            }
            // checking if the left part is sorted
            if (arr[low] <= arr[mid]) {
                // if the element lies in the first part
                if (arr[low] <= target && target < arr[mid]) {
                    high = mid - 1;
                } else {
                    // if the element is in right part
                    low = mid + 1;
                }
            } else {
                // If the element is in right half
                if (arr[mid] <= target && target < arr[high]) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }
        // returning -1 if no element found
        return -1;
    }
}
