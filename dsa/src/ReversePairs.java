import java.util.Arrays;

public class ReversePairs {

    // if we are about to find the values that i > anyIndex then we make use of merge Sort
    public static int reversePairs(int[] nums) {
        return mergeSort(nums, 0, nums.length - 1);
    }

    public static int mergeSort(int[] nums, int low, int high) {
        // To count the values of the indexes that are i > j
        int count = 0;
        if (low < high) {
            int mid = low + (high - low) / 2;
            count += mergeSort(nums, low, mid);
            count += mergeSort(nums, mid + 1, high);
            count += merge(nums, low, mid, high);
        }
        return count;
    }

    // Merging process
    public static int merge(int[] nums, int low, int mid, int high) {
        // Initiating the left array
        int[] leftArray = Arrays.copyOfRange(nums, low, mid + 1);
        int[] rightArray = Arrays.copyOfRange(nums, mid + 1, high + 1);
        int j = 0;
        int count = 0, k = low, i = 0;
        // predetermined check for checking if the values of i are greater than 2*j elements
        for (i = 0; i < leftArray.length; i++) {
            // doing the check for the same
            while (j < rightArray.length && (long) leftArray[i] > 2L * rightArray[j]) {
                j++;
            }
            count += j;
        }
        j = 0;
        i = 0;
        // merging the elements in the array
        while (i < leftArray.length && j < rightArray.length) {
            if (leftArray[i] <= rightArray[j]) {
                nums[k++] = leftArray[i++];
            } else {
                nums[k++] = rightArray[j++];
            }
        }
        while (i < leftArray.length) {
            nums[k++] = leftArray[i++];
        }
        while (j < rightArray.length) {
            nums[k++] = rightArray[j++];
        }
        // returning the count;
        return count;

    }

    public static void main(String[] args) {
        int[] nums = {1, 3, 2, 3, 1};
        System.out.println(reversePairs(nums));
    }

}
