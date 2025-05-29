public class KthLargestNumber {
    public static void main(String[] args) {
        KthLargestNumber kthLargestNumber = new KthLargestNumber();
        int[] nums = {3, 2, 1, 5, 6, 4};
        int k = 2;
//        System.out.println(kthLargestNumber.findKthLargest(nums, k)); // Output: 5
        System.out.println(kthLargestNumber.findKthLargestUsingQuickSelect(nums, k));
    }

    public int findKthLargest(int[] nums, int k) {
        return quickSort(nums, 0, nums.length - 1, k - 1);
    }

    private int quickSort(int[] nums, int low, int high, int k) {
        if (low <= high) {
            int partition = getPartition(nums, low, high);
            if (partition == k) {
                return nums[k];
            } else if (partition < k) {
                return quickSort(nums, partition + 1, high, k);
            } else {
                return quickSort(nums, low, partition - 1, k);
            }
        }
        return nums[low];
    }

    private int getPartition(int[] nums, int low, int high) {
        int i = low - 1;
        int j = low;
        int pivot = nums[high];
        while (j < high) {
            if (nums[j] >= pivot) {
                i++;
                int temp = nums[i];
                nums[i] = nums[j];
                nums[j] = temp;
            }
            j++;
        }
        int temp = nums[i + 1];
        nums[i + 1] = nums[high];
        nums[high] = temp;
        return i + 1;
    }

    public int findKthLargestUsingQuickSelect(int[] nums, int k) {
        // Set<Integer> setValues = new HashSet<>();
        // for(int i: nums){
        //     setValues.add(i);
        // }
        return quickSelect(nums, 0, nums.length - 1, nums.length - k);
    }

    private int quickSelect(int[] nums, int low, int high, int targetIndex) {
        if (low == high) {
            return nums[low];
        }
        int left = low, right = high;
        int pivot = nums[left];
        while (left <= right) {
            while (left <= right && nums[left] < pivot) {
                left++;
            }

            while (left <= right && nums[right] > pivot) {
                right--;
            }
            if (left <= right) {
                swap(nums, left, right);
                left++;
                right--;
            }
        }
        if (targetIndex <= right) {
            return quickSelect(nums, low, right, targetIndex);
        } else if (targetIndex >= left) {
            return quickSelect(nums, left, high, targetIndex);
        } else {
            return nums[targetIndex];
        }

    }

    private void swap(int[] nums, int left, int right) {
        int temp = nums[left];
        nums[left] = nums[right];
        nums[right] = temp;
    }
}
