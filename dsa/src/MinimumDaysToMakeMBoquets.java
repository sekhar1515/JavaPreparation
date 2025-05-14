import java.util.Arrays;

public class MinimumDaysToMakeMBoquets {
    public int minDays(int[] bloomDay, int m, int k) {
        //Checking the overflow condition
        if ((long) m * k > bloomDay.length) {
            return -1;
        }
        int min = Arrays.stream(bloomDay).min().getAsInt();
        int max = Arrays.stream(bloomDay).max().getAsInt();
        while (min < max) {
            int mid = min + (max - min) / 2;
            // Helper function to check if the value is enough to make m boquets with adjacent flowers
            if (canGather(bloomDay, m, k, mid)) {
                max = mid;
            } else {
                min = mid + 1;
            }
        }
        return min;
    }

    private boolean canGather(int[] bloomDay, int m, int k, int min) {
        int bouquets = 0;
        int flowers = 0;
        for (int day : bloomDay) {
            if (day <= min) {
                flowers++;
                if (flowers == k) {
                    bouquets++;
                    flowers = 0;
                    if (bouquets == m) {
                        return true;
                    }
                }
            } else {
                flowers = 0;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        MinimumDaysToMakeMBoquets minDaysToMakeMBoquets = new MinimumDaysToMakeMBoquets();
        int[] bloomDay = {1, 10, 3, 10, 2};
        int m = 3, k = 1;
        System.out.println(minDaysToMakeMBoquets.minDays(bloomDay, m, k));
    }
}
