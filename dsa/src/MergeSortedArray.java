import java.util.Arrays;

public class MergeSortedArray {

    // Leetcode link : https://leetcode.com/problems/merge-sorted-array/description

    public static void mergeOptimized(int[] nums1, int m, int[] nums2, int n) {
        // We will start iterating the array from the last and check which one
        // holds the highest value and then we place the values accordingly
        int n1 = m - 1, n2 = n - 1;
        int loop = m + n - 1;
        while (n1 >= 0 && n2 >= 0) {
            if (nums1[n1] >= nums2[n2]) {
                nums1[loop--] = nums1[n1--];
            } else {
                nums1[loop--] = nums2[n2--];
            }
        }
        while (n1 >= 0) {
            nums1[loop--] = nums1[n1--];
        }
        while (n2 >= 0) {
            nums1[loop--] = nums2[n2--];
        }
    }

    // Add the remaining elements at the end and do the sorting of array again
    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        int k = m;
        for (int i = 0; i < n; i++) {
            nums1[k] = nums2[i];
            k++;
        }
        Arrays.sort(nums1);
    }

    public static void main(String[] args) {
        int[] nums1 = {1, 2, 3, 0, 0, 0};
        int[] nums2 = {2, 5, 6};
        merge(nums1, 3, nums2, 3);
        System.out.println(Arrays.toString(nums1));
        int[] nums3 = {1, 2, 3, 4, 5, 0, 0, 0};
        int[] nums4 = {2, 5, 6};
        mergeOptimized(nums3, 5, nums4, 3);
        System.out.println(Arrays.toString(nums3));
    }
}
