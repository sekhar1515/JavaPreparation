/**
 * Implements the QuickSort algorithm for sorting integer arrays.
 * Uses randomized pivot selection to improve average performance.
 */
public class QuickSort {

    /**
     * Sorts the given array in ascending order using the QuickSort algorithm.
     *
     * @param array the array to be sorted
     * @param low the starting index of the subarray to sort
     * @param high the ending index of the subarray to sort
     */
    public void quickSort(int[] array, int low, int high) {
        // Base case: if the array has 1 or fewer elements, it's already sorted
        if (array.length <= 1) {
            return;
        }
        // If the subarray has more than one element
        if (low < high) {
            // Partition the array and get the pivot's final index
            int paritionIndex = partition(array, low, high);
            // Recursively sort the left subarray (elements less than or equal to pivot)
            quickSort(array, low, paritionIndex - 1);
            // Recursively sort the right subarray (elements greater than pivot)
            quickSort(array, paritionIndex + 1, high);
        }
    }

    /**
     * Partitions the subarray around a randomly chosen pivot.
     * Elements less than or equal to the pivot are moved to the left,
     * and elements greater than the pivot are moved to the right.
     *
     * @param array the array to partition
     * @param low the starting index of the subarray
     * @param high the ending index of the subarray
     * @return the index of the pivot after partitioning
     */
    private int partition(int[] array, int low, int high) {
        int i = low - 1, j = low;
        // Randomly select a pivot and move it to the end
        int randomIndex = low + (int) (Math.random() * (high - low + 1));
        swap(array, randomIndex, high);
        int pivot = array[high];
        // Rearrange elements based on pivot
        while (j < high) {
            if (array[j] <= pivot) {
                i++;
                swap(array, i, j);
            }
            j++;
        }
        // Place pivot in its correct position
        swap(array, i + 1, high);
        return i + 1;
    }

    /**
     * Swaps two elements in the array.
     *
     * @param array the array in which to swap elements
     * @param i the index of the first element
     * @param j the index of the second element
     */
    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * Demonstrates the usage of the QuickSort algorithm.
     * Prints the original and sorted arrays.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        QuickSort quickSort = new QuickSort();
        int[] array = {38, 27, 43, 3, 9, 82, 10};
        System.out.println("Original Array: " + java.util.Arrays.toString(array));
        quickSort.quickSort(array, 0, array.length - 1);
        System.out.println("Sorted Array: " + java.util.Arrays.toString(array));
    }
}