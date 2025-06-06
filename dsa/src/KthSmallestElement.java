import java.util.PriorityQueue;

public class KthSmallestElement {

    public int findKthSmallest(int[] nums, int k) {
        // Using a max-heap to keep track of the k smallest elements
        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> b - a);
        // Add elements to the max-heap
        for (int i : nums) {
            pq.add(i);
            // If the size of the heap exceeds k, remove the largest element
            if (pq.size() > k) {
                pq.poll();
            }
        }
        // The root of the max-heap is the k-th smallest element
        return pq.peek();
    }

    public static void main(String[] args) {
        int[] nums = {3, 2, 1, 3, 1, 5, 6, 4};
        int k = 2;
        KthSmallestElement kthSmallestElement = new KthSmallestElement();
        System.out.println(kthSmallestElement.findKthSmallest(nums, k));
        System.out.println(kthSmallest(nums, k));
    }

    // Finds the k-th smallest element in the array using QuickSelect
    public static int kthSmallest(int[] arr, int k) {
        // k is 1-based, so pass k-1 as the index
        return quickSort(arr, 0, arr.length - 1, k - 1);
    }

    // QuickSelect algorithm to find the k-th smallest element
    public static int quickSort(int[] arr, int low, int high, int smallestIndex) {
        if (low <= high) {
            int partitionIndex = getPartitionIndex(arr, low, high);
            // If partition index matches the k-th smallest index, return the element
            if (partitionIndex == smallestIndex) return arr[partitionIndex];
                // If partition index is greater, search left part
            else if (partitionIndex > smallestIndex) return quickSort(arr, low, partitionIndex - 1, smallestIndex);
                // If partition index is smaller, search right part
            else return quickSort(arr, partitionIndex + 1, high, smallestIndex);
        }
        // Should not reach here if input is valid
        return -1;
    }

    // Partitions the array and returns the partition index
    public static int getPartitionIndex(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        // Move elements smaller than pivot to the left
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                int temp = arr[j];
                arr[j] = arr[i];
                arr[i] = temp;
            }
        }
        // Place pivot in the correct position
        int temp = arr[i + 1];
        arr[i + 1] = pivot;
        arr[high] = temp;
        return i + 1;
    }
}
