package edu.jhu.algos.algorithms;

import edu.jhu.algos.models.Matrix;

/**
 * Implements naive O(n³) matrix multiplication with defensive programming.
 * - Uses loop reordering to improve cache efficiency.
 * - Unrolls loops to reduce loop overhead.
 * - Implements blocking (tiling) to optimize large matrix multiplication.
 * - Includes robust error handling for invalid operations.
 */
public class NaiveMultiplication {

    // Counter to track the number of scalar multiplications performed
    private static int multiplicationCount = 0;

    /**
     * Multiplies two matrices using the optimized naive O(n³) algorithm.
     *
     * **Algorithm:**
     * - Uses three nested loops for matrix multiplication.
     * - Implements loop reordering for better cache performance.
     * - Uses loop unrolling to reduce loop overhead.
     * - Uses blocking (tiling) for large matrices to optimize cache usage.
     *
     * **Defensive Programming Measures:**
     * - Checks for null matrices to prevent `NullPointerException`.
     * - Ensures matrices are not empty.
     * - Ensures valid dimensions for multiplication (`A.cols == B.rows`).
     *
     * @param A The first matrix (m × n).
     * @param B The second matrix (n × p).
     * @return The product matrix (m × p).
     * @throws IllegalArgumentException If matrices cannot be multiplied.
     */
    public static Matrix multiply(Matrix A, Matrix B) {
        // Validate input matrices before proceeding
        validateMatrices(A, B);

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

        try {
            // Blocked matrix multiplication with loop unrolling
            for (int bi = 0; bi < m; bi += BLOCK_SIZE) { // Process row blocks
                for (int bj = 0; bj < p; bj += BLOCK_SIZE) { // Process column blocks
                    for (int bk = 0; bk < n; bk += BLOCK_SIZE) { // Process depth blocks

                        // Compute each block of the result matrix
                        for (int i = bi; i < Math.min(bi + BLOCK_SIZE, m); i++) {
                            for (int k = bk; k < Math.min(bk + BLOCK_SIZE, n); k++) {
                                double a_ik = A.getData(false)[i][k]; // Cache A[i][k] to reduce memory accesses
                                for (int j = bj; j < Math.min(bj + BLOCK_SIZE, p); j += 2) {
                                    result[i][j] += a_ik * B.getData(false)[k][j]; // Standard multiplication
                                    if (j + 1 < p) {
                                        result[i][j + 1] += a_ik * B.getData(false)[k][j + 1]; // Loop unrolling
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
            // Catch-all block for unexpected runtime errors
            System.err.println("Unexpected error during matrix multiplication: " + e.getMessage());
            throw new RuntimeException("Matrix multiplication failed due to an unexpected error.", e);
        }
    }

    /**
     * Validates input matrices before multiplication.
     * Ensures the matrices are not null, not empty, and have valid dimensions.
     *
     * @param A The first matrix.
     * @param B The second matrix.
     * @throws IllegalArgumentException If matrices are invalid for multiplication.
     */
    private static void validateMatrices(Matrix A, Matrix B) {
        // Check if either matrix is null
        if (A == null || B == null) {
            throw new IllegalArgumentException("Matrix multiplication error: One or both matrices are null.");
        }

        // Check if either matrix is empty
        if (A.getRows() == 0 || A.getCols() == 0 || B.getRows() == 0 || B.getCols() == 0) {
            throw new IllegalArgumentException("Matrix multiplication error: One or both matrices are empty.");
        }

        // Ensure that the number of columns in A matches the number of rows in B
        if (A.getCols() != B.getRows()) {
            throw new IllegalArgumentException("Matrix multiplication error: "
                    + "A has " + A.getCols() + " columns, but B has " + B.getRows() + " rows.");
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
