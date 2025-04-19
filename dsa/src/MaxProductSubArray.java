public class MaxProductSubArray {
    public int maxProduct(int[] nums) {
        if (nums.length == 1) {
            return nums[0];
        }
        int maxProduct = 0, currProduct = 0;
        for (int i = 0; i < nums.length; i++) {
            currProduct = 1;
            for (int j = i; j < nums.length; j++) {
                currProduct *= nums[j];
                maxProduct = Math.max(currProduct, maxProduct);
            }
        }
        return maxProduct;
    }

    public int maxProduct2(int[] nums) {
        if (nums.length == 1) {
            return nums[0];
        }
        // Optimal approach where we keep track of prefixProduct and suffixProduct from the beginnning
        // And from the end if we come across any instance where the product is zero we just omit that
        // make the prefixProduct and SuffixProduct back to 1 again
        // we store the max value at every level in the maxProduct value and we return that
        int prefixProduct = 1, suffixProduct = 1, maxProduct = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            if (prefixProduct == 0) prefixProduct = 1;
            if (suffixProduct == 0) suffixProduct = 1;
            prefixProduct *= nums[i];
            suffixProduct *= nums[nums.length - 1 - i];
            maxProduct = Math.max(maxProduct, Math.max(prefixProduct, suffixProduct));
        }
        return maxProduct;

    }

    // Modified Kadane's algorithm
    public int maxProduct3(int[] nums) {
        int prod1 = nums[0], prod2 = nums[0], result = nums[0];
        for (int i = 1; i < nums.length; i++) {
            int temp = Math.max(nums[i], Math.max(prod2 * nums[i], nums[i] * prod1));
            prod2 = Math.min(nums[i], Math.min(prod1 * nums[i], nums[i] * prod2));
            prod1 = temp;
            result = Math.max(result, prod1);
        }
        return result;
    }

    public static void main(String[] args) {
        MaxProductSubArray maxProductSubArray = new MaxProductSubArray();
        System.out.println(maxProductSubArray.maxProduct(new int[]{-2, 1, -3, 4}));
        System.out.println(maxProductSubArray.maxProduct2(new int[]{-2, 1, -3, 4}));
        System.out.println(maxProductSubArray.maxProduct3(new int[]{-2, 1, -3, 4}));
    }
}
