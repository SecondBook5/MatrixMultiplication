package edu.jhu.algos.algorithms;

import edu.jhu.algos.models.Matrix;

/**
 * Implements naive O(n³) matrix multiplication with defensive programming.
 * - Uses **loop reordering** to improve cache efficiency.
 * - **Unrolls loops** to reduce loop overhead.
 * - Implements **blocking (tiling)** to optimize large matrix multiplication.
 * - Includes **robust error handling** for invalid operations.
 */
public class NaiveMultiplication {

    // Counter to track the number of scalar multiplications performed
    private static int multiplicationCount = 0;

    /**
     * Multiplies two matrices using the optimized naive O(n³) algorithm.
     *
     * **Algorithm:**
     * - Uses three nested loops for matrix multiplication.
     * - Implements **loop reordering** for better cache performance.
     * - Uses **loop unrolling** to reduce loop overhead.
     * - Uses **blocking (tiling)** for large matrices to optimize cache usage.
     *
     * **Defensive Programming Measures:**
     * - Checks for **null matrices** to prevent `NullPointerException`.
     * - Ensures matrices **are not empty**.
     * - Ensures **valid dimensions** for multiplication (`A.cols == B.rows`).
     * - Uses **try-catch** blocks to gracefully handle unexpected errors.
     *
     * @param A The first matrix (m × n).
     * @param B The second matrix (n × p).
     * @return The product matrix (m × p).
     * @throws IllegalArgumentException If matrices cannot be multiplied.
     */
    public static Matrix multiply(Matrix A, Matrix B) {
        try {
            // Check for null matrices
            if (A == null || B == null) {
                throw new IllegalArgumentException("Matrix multiplication error: One or both matrices are null.");
            }

            // Check if matrices are empty (zero size)
            if (A.getRows() == 0 || A.getCols() == 0 || B.getRows() == 0 || B.getCols() == 0) {
                throw new IllegalArgumentException("Matrix multiplication error: One or both matrices are empty.");
            }

            // Ensure that A's column count matches B's row count
            if (A.getCols() != B.getRows()) {
                throw new IllegalArgumentException("Matrix multiplication error: "
                        + "A has " + A.getCols() + " columns, but B has " + B.getRows() + " rows.");
            }

            // Reset multiplication counter for tracking performance
            multiplicationCount = 0;

            // Get matrix dimensions
            int m = A.getRows(); // Number of rows in A
            int n = A.getCols(); // Number of columns in A (must match rows in B)
            int p = B.getCols(); // Number of columns in B

            // Initialize result matrix with zero values
            double[][] result = new double[m][p];

            // Define block size for tiling (adjust based on CPU cache size)
            int BLOCK_SIZE = 64 / Double.BYTES; // Assumes a 64-byte cache line

            // **Blocked matrix multiplication with loop unrolling**
            for (int bi = 0; bi < m; bi += BLOCK_SIZE) { // Iterate over row blocks
                for (int bj = 0; bj < p; bj += BLOCK_SIZE) { // Iterate over column blocks
                    for (int bk = 0; bk < n; bk += BLOCK_SIZE) { // Iterate over depth blocks

                        // Perform multiplication for each block
                        for (int i = bi; i < Math.min(bi + BLOCK_SIZE, m); i++) { // Rows of A
                            for (int k = bk; k < Math.min(bk + BLOCK_SIZE, n); k++) { // Common dimension
                                double a_ik = A.getData(false)[i][k]; // Cache value from A
                                for (int j = bj; j < Math.min(bj + BLOCK_SIZE, p); j += 2) { // Columns of B with unrolling
                                    result[i][j] += a_ik * B.getData(false)[k][j]; // Standard multiplication
                                    if (j + 1 < p) {
                                        result[i][j + 1] += a_ik * B.getData(false)[k][j + 1]; // Unrolled computation
                                    }
                                    multiplicationCount += 2; // Track scalar multiplications
                                }
                            }
                        }
                    }
                }
            }

            // Return the resulting product matrix
            return new Matrix(m, p, result);

        } catch (Exception e) {
            // Catch-all block to log unexpected runtime errors
            System.err.println("Unexpected error during matrix multiplication: " + e.getMessage());
            throw new RuntimeException("Matrix multiplication failed due to an unexpected error.", e);
        }
    }

    /**
     * Retrieves the number of scalar multiplications performed in the last multiplication.
     * Useful for performance analysis.
     *
     * @return The multiplication count from the last operation.
     */
    public static int getMultiplicationCount() {
        return multiplicationCount;
    }
}
