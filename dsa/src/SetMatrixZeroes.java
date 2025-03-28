import java.util.HashSet;
import java.util.Set;

public class SetMatrixZeroes {

    // we are going to make use of first row and first col for making other rows and
    // cols zero where we don't need to make use of any extra space
    // T.C : O(N^2)
    public static void setZeroes(int[][] matrix) {
        boolean isFirstRowZero = false, isFirstColZero = false;
        int rows = matrix.length, cols = matrix[0].length;
        // System.out.println(" cols " + cols + " rows " + rows);
        for (int[] ints : matrix) {
            if (ints[0] == 0) {
                isFirstRowZero = true;
                break;
            }
        }
        for (int i = 0; i < cols; i++) {
            // System.out.println(i);
            if (matrix[0][i] == 0) {
                isFirstColZero = true;
                break;
            }
        }
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;
                    matrix[0][j] = 0;
                }
            }
        }
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
        }
        if (isFirstRowZero) {
            for (int i = 0; i < rows; i++) {
                matrix[i][0] = 0;
            }
        }
        if (isFirstColZero) {
            for (int i = 0; i < cols; i++) {
                matrix[0][i] = 0;
            }
        }
    }

    // Usage of extra space so that we keep of rows and cols
    // To make their relevant rows and cols to zero
    public static void setZeroesUsingExtraSpace(int[][] matrix) {
        Set<Integer> rows = new HashSet<>();
        Set<Integer> cols = new HashSet<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    rows.add(i);
                    cols.add(j);
                }
            }
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (rows.contains(i) || cols.contains(j)) {
                    matrix[i][j] = 0;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[][] matrix = {{1, 1, 1}, {1, 0, 1}, {1, 0, 1}};
        setZeroesUsingExtraSpace(matrix);

        print(matrix);
        System.out.println();
        int[][] matrix2 = {{1, 1, 1}, {1, 0, 1}, {1, 0, 1}};
        setZeroes(matrix2);
        print(matrix2);
    }

    static void print(int[][] matrix) {
        for (int[] ints : matrix) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(ints[j] + " ");
            }
        }
    }
}
