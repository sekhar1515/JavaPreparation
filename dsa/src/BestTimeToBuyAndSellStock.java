public class BestTimeToBuyAndSellStock {

    // TC: O(N^2)
    public int maxProfitWithSubArrayBruteForce(int[] prices){
        int maxProfit = 0;
        for (int i = 0; i < prices.length; i++) {
            for (int j = i + 1; j < prices.length; j++) {
                maxProfit = Math.max(maxProfit, prices[j] - prices[i]);
            }
        }
        return maxProfit;
    }

    //    TC : O(N) SC : O(N)
    // Using an array to store the values for the consecutive difference of elements and then using
    // MaxSubArray Sum to find the maxSum;
    public int maxProfitWithSubArray(int[] prices) {
        int maxSum = 0;
        int[] newArr = new int[prices.length - 1];
        for (int i = 0; i < prices.length - 1; i++) {
            newArr[i] = prices[i + 1] - prices[i];
        }
        int sum = 0;
        for (int i : newArr) {
            sum += i;
            maxSum = Math.max(sum, maxSum);
            if (sum < 0) {
                sum = 0;
            }
        }
        return maxSum;
    }

    // Keeping the track of minimum element till now and calcultating maxProfit every instance
    public int maxProfitOptimal(int[] prices) {
        int maxProfit = 0, minSoFar = Integer.MAX_VALUE;
        for(int i: prices){
            minSoFar = Math.min(minSoFar, i);
            maxProfit = Math.max(i - minSoFar, maxProfit);
        }
        return maxProfit;
    }

}
