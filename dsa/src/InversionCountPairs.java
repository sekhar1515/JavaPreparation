public class InversionCountPairs {
    // Brute force where we count the pairs by iterating the loops internally
//    TC : O(N^2)
    static int inversionCountBruteForce(int arr[]) {
        // Your Code Here
        int count = 0;
        for (int i = arr.length - 1; i >= 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                if (arr[j] > arr[i]) {
                    count++;
                }
            }
        }
        return count;
    }

    // Optimal approach where we make use of mergeSort with a slight modification where
    // we check the values in the left array is greater than elements in the right array
    // TC: O(NLogn)
    // SC: O(N)
//    Problem link : https://takeuforward.org/data-structure/count-inversions-in-an-array/
    static int findInversionCountPairs(int[] arr) {
        return countUsingMergeSort(arr, 0, arr.length - 1);
    }

    private static int countUsingMergeSort(int[] arr, int low, int high) {
        // mergesort call to check the values that are greater than element in the right array
        int count = 0;
        if (low < high) {
            int mid = low + (high - low) / 2;
            count += countUsingMergeSort(arr, low, mid);
            count += countUsingMergeSort(arr, mid + 1, high);
            count += mergeSort(arr, low, mid, high);
        }
        return count;
    }

    private static int mergeSort(int[] arr, int low, int mid, int high) {
        // Merge sort algo
        int[] left = new int[mid - low + 1];
        int[] right = new int[high - mid];
        for (int i = 0; i < left.length; i++) {
            left[i] = arr[i + low];
        }
        for (int i = 0; i < right.length; i++) {
            right[i] = arr[mid + 1 + i];
        }
        int i = 0, j = 0, k = low, swaps = 0;
        // we start the arranging the elements in the array
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                arr[k++] = left[i++];
            } else {
                // if the element is greater than right element
                // we calculate the other elements in the left array will be greater than right element at current point
                arr[k++] = right[j++];
                swaps += left.length - i;
            }
        }
        while (i < left.length) {
            arr[k++] = left[i++];
        }
        while (j < right.length) {
            arr[k++] = right[j++];
        }
        // returning the values
        return swaps;
    }

    public static void main(String[] args) {
        System.out.println(findInversionCountPairs(new int[]{2, 4, 1, 3, 5}));
    }
}
