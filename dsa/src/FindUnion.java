import java.util.HashSet;
import java.util.Set;

public class FindUnion {


    public static int findUnion(int a[], int b[]) {
        // code here
        Set<Integer> setValues = new HashSet<>();
        for (int i : a) {
            setValues.add(i);
        }
        for (int i : b) {
            setValues.add(i);
        }
        return setValues.size();

    }

    public static void main(String[] args) {
        int[] a = {1, 2, 3, 4, 5};
        int[] b = {5, 4, 2, 3, 8};
        int result = findUnion(a, b);
        System.out.println("The number of unique elements in the union is: " + result);
    }

}
