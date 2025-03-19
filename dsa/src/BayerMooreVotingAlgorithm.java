public class BayerMooreVotingAlgorithm {
    //Bayer Moore voting algorithm so that it can be used to find the element that has occured more than n/2 times
    // TC : O(N) SC : O(1)
    public int majorityElement(int[] nums) {
        int count = 0, element = 0;
        for (int num : nums) {
            if (count == 0) {
                element = num;
                count++;
            } else if (element == num) {
                count++;
            } else {
                count--;
            }
        }
        return element;
    }
    public static void main(String[] args) {
        int[] nums = new int[]{1,3,4,5,4,4};
        System.out.println(new BayerMooreVotingAlgorithm().majorityElement(nums));
    }
}
