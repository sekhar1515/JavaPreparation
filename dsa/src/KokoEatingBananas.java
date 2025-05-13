import java.util.Arrays;

public class KokoEatingBananas {


    // Since the answer will always lies in the range of 1 and maximum element in that array
    // use binary search on that range
    public int minEatingSpeed(int[] piles, int h) {
        int length = piles.length;
        int max = Arrays.stream(piles).max().orElse(1);
        int result = 0;
        if (length == 1 && piles[0] < h) {
            return 1;
        } else {
            int low = 1, high = max;
            while (low <= high) {
                int mid = low + (high - low) / 2;
                // Long to handle overflow scenarios
                long timeForCompletingBananas = canCompleteBananas(mid, piles);
                if (timeForCompletingBananas <= h) {
                    result = mid;
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            }
        }
        return result;
    }
    // helper function to calculate the speed for eating bananas
    public long canCompleteBananas(int mid, int[] piles) {
        long time = 0;
        for (int i : piles) {
            time+= (i + mid - 1)/mid;
        }
        return time;
    }

    public static void main(String[] args) {
        KokoEatingBananas kokoEatingBananas = new KokoEatingBananas();
        int minSpeed = kokoEatingBananas.minEatingSpeed(new int[]{805306368,805306368,805306368}, 1000000000);
        System.out.println(minSpeed);
    }
}
