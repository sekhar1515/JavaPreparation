import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RotateMatrix {
    // TC: O(N^2) S.C : No Space complexity since we are not using any space
    // Optimal solution
    public static void rotate(int[][] matrix) {
        int rows = matrix.length;
        for (int i = 0; i < rows; i++) {
            for (int j = i; j < rows; j++) {
                swap(matrix, i, j);
            }
        }
        for(int i = 0; i < rows; i++){
            reverseRows(matrix[i]);
        }
    }

    private static void reverseRows(int[] arr) {
        int low = 0, high = arr.length - 1;
        while (low <= high) {
            int temp = arr[low];
            arr[low] = arr[high];
            arr[high] = temp;
            low++;
            high--;
        }
    }

    private static void swap(int[][] matrix, int i, int j) {
        int temp = matrix[j][i];
        matrix[j][i] = matrix[i][j];
        matrix[i][j] = temp;
    }

    // T.C : O(N^2) S.C : O(N^2)
    // Implementation of code using the extra space using list
    public static void rotateUsingSpace(int[][] matrix) {
        int rows = matrix.length;
        List<List<Integer>> rowValues = new ArrayList<>();
        for (int[] ints : matrix) {
            List<Integer> row = Arrays.stream(ints)
                    .boxed()
                    .collect(Collectors.toList());
            rowValues.add(0, row);
        }
        int k = 0;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < rows; j++){
                matrix[i][j] = rowValues.get(j).get(k);
            }
            k++;
        }
    }
    public static void main(String[] args) {
    int[][] matrix = {{1,2,3},{4,5,6},{7,8,9}};
    System.out.println("Using Brute force ");
    int[][] matrix2 = {{1,2,3},{4,5,6},{7,8,9}};
    rotateUsingSpace(matrix);
    for (int[] rows : matrix) {
        System.out.println(Arrays.toString(rows));
    }
    rotate(matrix2);
    System.out.println("Optimal solution : ");
    for (int[] rows : matrix2) {
        System.out.println(Arrays.toString(rows));
    }

    }
}
