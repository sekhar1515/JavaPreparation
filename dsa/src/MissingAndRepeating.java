import java.util.ArrayList;
import java.util.List;

public class MissingAndRepeating {

    //finding elements that have occurred twice and one element that is missing
    static ArrayList<Integer> findTwoElement(int arr[]) {
        // code here
        // First we find the xor of all the elements in the array
        // and we do the xor with elements from 1 to n with the xor value
        int xor = 0;
        for (int i : arr) {
            xor ^= i;
        }
        for (int i = 1; i <= arr.length; i++) {
            xor ^= i;
        }
        // Now the xor value will be combination of (missing^repetitive)
        // Now we find the right most set bit of that xor and we divide the elements into buckets
        // one that is having set bit for the right most and other where bit is not set
        // We store them in x and y respectively.
        int rightmostSetBit = xor & (-xor);
        int x = 0, y = 0;
        for (int i : arr) {
            if ((i & rightmostSetBit) == 0) {
                x ^= i;
            } else {
                y ^= i;
            }
        }
        for (int i = 1; i <= arr.length; i++) {
            if ((i & rightmostSetBit) == 0) {
                x ^= i;
            } else {
                y ^= i;
            }
        }
        // we cross check at the last which one is repetitive and which one is missing in the array
        // we return that result.
        ArrayList<Integer> result = new ArrayList<>();
        for (int i : arr) {
            if (i == x) {

                result.add(x);
                result.add(y);
                break;
            }
            if (i == y) {
                result.add(y);
                result.add(x);
                break;
            }

        }
        return result;
    }

    //by using extra space using hashing
    static List<Integer> findElements(int[] arr) {
        int[] hashMap = new int[arr.length + 1];
        for (int i : arr) {
            hashMap[i]++;
        }
        for (int i = 1; i <= arr.length; i++) {
            hashMap[i]--;
        }
        int twiceElement = -1, missing = -1;
        for (int i = 0; i < hashMap.length; i++) {
            if (hashMap[i] == -1) {
                missing = i;
            } else if (hashMap[i] == 1) {
                twiceElement = i;
            }
        }
        return List.of(twiceElement, missing);
    }
    public static void main(String[] args) {
        int[] arr = {1, 2, 2};
        System.out.println(findTwoElement(arr));
        System.out.println(findElements(arr));

    }
}
