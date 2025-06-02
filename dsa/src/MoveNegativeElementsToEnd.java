import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoveNegativeElementsToEnd {
    public static void main(String[] args) {
        MoveNegativeElementsToEnd sol = new MoveNegativeElementsToEnd();
        int[] arr = {1, -2, 3, -4, 5, -6};
        sol.segregateElements(arr);
        System.out.println(Arrays.toString(arr));
    }

    public void segregateElements(int[] arr) {
        // Your code goes here
        List<Integer> negativeValues = new ArrayList<>();
        List<Integer> positiveValues = new ArrayList<>();
        Arrays.stream(arr).forEach(i -> {
            if (i >= 0) {
                positiveValues.add(i);
            } else {
                negativeValues.add(i);
            }
        });
        int j = 0;
        for (int i : positiveValues) {
            arr[j++] = i;
        }
        for (int i : negativeValues) {
            arr[j++] = i;
        }
    }
}
