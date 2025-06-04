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
    }
}
