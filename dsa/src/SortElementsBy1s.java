import java.util.*;

public class SortElementsBy1s {

    /* =========================================================
       APPROACH 1: TreeMap + Brian Kernighan’s Algorithm
       ========================================================= */
    public static int[] sortByBitsUsingTreeMap(int[] arr) {

        // TreeMap keeps keys (bit counts) sorted automatically
        Map<Integer, List<Integer>> mapList = new TreeMap<>();

        // Step 1: Sort array numerically
        // This ensures numbers with same bit count stay sorted
        Arrays.sort(arr);

        // Step 2: Group numbers by their bit count
        for (int num : arr) {
            int bitCount = getNumberOfOnesInBinary(num);
            mapList.computeIfAbsent(bitCount, k -> new ArrayList<>()).add(num);
        }

        // Step 3: Flatten map into result array
        int[] result = new int[arr.length];
        int index = 0;

        for (List<Integer> list : mapList.values()) {
            for (int num : list) {
                result[index++] = num;
            }
        }

        return result;
    }

    // Brian Kernighan’s Algorithm to count set bits
    private static int getNumberOfOnesInBinary(int number) {
        int count = 0;

        while (number != 0) {
            number = number & (number - 1); // removes lowest set bit
            count++;
        }

        return count;
    }


    /* =========================================================
       APPROACH 2: Custom Comparator + Integer.bitCount()
       ========================================================= */
    public static int[] sortByBitsUsingComparator(int[] arr) {

        // Convert int[] to Integer[] (Comparator doesn't work on primitive int[])
        Integer[] boxed = new Integer[arr.length];

        for (int i = 0; i < arr.length; i++) {
            boxed[i] = arr[i];
        }

        // Sort using custom comparator
        Arrays.sort(boxed, (a, b) -> {
            int bitCompare = Integer.bitCount(a) - Integer.bitCount(b);

            // If bit counts differ → sort by bit count
            // Else → sort numerically
            return bitCompare != 0 ? bitCompare : a - b;
        });

        // Convert back to int[]
        for (int i = 0; i < arr.length; i++) {
            arr[i] = boxed[i];
        }

        return arr;
    }


    /* =========================================================
       MAIN METHOD
       ========================================================= */
    public static void main(String[] args) {

        int[] arr1 = {3, 5, 7, 8, 9};
        int[] arr2 = {3, 5, 7, 8, 9};

        System.out.println("Using TreeMap Approach:");
        int[] result1 = sortByBitsUsingTreeMap(arr1);
        Arrays.stream(result1).forEach(i -> System.out.print(i + " "));

        System.out.println("\nUsing Comparator Approach:");
        int[] result2 = sortByBitsUsingComparator(arr2);
        Arrays.stream(result2).forEach(i -> System.out.print(i + " "));
    }
}