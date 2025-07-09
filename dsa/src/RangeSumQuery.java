/**
 * RangeSumQuery class allows querying the sum of a rectangular submatrix.
 * It supports both brute-force and optimized (prefix sum) approaches.
 */
public class RangeSumQuery {

    // Original matrix
    int[][] matrixValues;

    // Prefix sum matrix (extra row and column for easier calculations)
    int[][] prefixSum;

    /**
     * Constructor initializes the matrix and precomputes prefix sums.
     *
     * @param matrix The 2D array input matrix
     *
     * Time Complexity: O(m * n) where m is number of rows and n is number of columns
     * Space Complexity: O(m * n) for the prefixSum array
     */
    public RangeSumQuery(int[][] matrix) {
        this.matrixValues = matrix;
        int rows = matrix.length;
        int cols = matrix[0].length;

        // Initializing prefix sum matrix with one extra row and column (for easier math)
        prefixSum = new int[rows + 1][cols + 1];

        // Compute prefix sums
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                prefixSum[i][j] = prefixSum[i - 1][j]
                        + prefixSum[i][j - 1]
                        - prefixSum[i - 1][j - 1]
                        + matrix[i - 1][j - 1];
            }
        }
    }

    /**
     * Brute-force method to compute the sum of a submatrix from (row1, col1) to (row2, col2).
     *
     * @param row1 Starting row index
     * @param col1 Starting column index
     * @param row2 Ending row index
     * @param col2 Ending column index
     * @return Sum of submatrix
     *
     * Time Complexity: O((row2 - row1 + 1) * (col2 - col1 + 1))
     * Space Complexity: O(1)
     */
    public int sumRegionBruteForce(int row1, int col1, int row2, int col2) {
        int sum = 0;
        for (int i = row1; i <= row2; i++) {
            for (int j = col1; j <= col2; j++) {
                sum += matrixValues[i][j];
            }
        }
        return sum;
    }

    /**
     * Optimized method using prefix sum to compute the sum of submatrix.
     *
     * @param row1 Starting row index
     * @param col1 Starting column index
     * @param row2 Ending row index
     * @param col2 Ending column index
     * @return Sum of submatrix
     *
     * Time Complexity: O(1)
     * Space Complexity: O(1) (uses precomputed prefixSum)
     */
    public int sumRegionOptimal(int row1, int col1, int row2, int col2) {
        return prefixSum[row2 + 1][col2 + 1]
                - prefixSum[row1][col2 + 1]
                - prefixSum[row2 + 1][col1]
                + prefixSum[row1][col1];
    }

    /**
     * Main method to demonstrate both brute-force and optimized approaches.
     */
    public static void main(String[] args) {
        // Sample 2D matrix
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6}
        };

        // Instantiate the RangeSumQuery object
        RangeSumQuery obj = new RangeSumQuery(matrix);

        // Brute-force result
        int bruteForceResult = obj.sumRegionBruteForce(0, 0, 1, 1); // Expected: 1+2+4+5 = 12
        System.out.println("Brute-force sum of region (0,0) to (1,1): " + bruteForceResult);

        // Optimized prefix sum result
        int optimalResult = obj.sumRegionOptimal(0, 0, 1, 1); // Expected: 12
        System.out.println("Optimized sum of region (0,0) to (1,1): " + optimalResult);
    }
}
