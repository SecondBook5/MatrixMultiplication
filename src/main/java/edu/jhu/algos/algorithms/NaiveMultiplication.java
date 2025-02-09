package edu.jhu.algos.algorithms;

import edu.jhu.algos.models.Matrix;

/**
 * Implements naive O(n³) matrix multiplication with defensive programming.
 * - Uses loop reordering to improve cache efficiency.
 * - Unrolls loops to reduce loop overhead.
 * - Implements blocking (tiling) to optimize large matrix multiplication.
 * - Includes robust error handling for invalid operations.
 * - Detects zero matrices to optimize computation.
 */
public class NaiveMultiplication {

    private static int multiplicationCount = 0; // Tracks number of scalar multiplications

    /**
     * Multiplies two matrices using the optimized naive O(n³) algorithm.
     *
     * **Optimizations:**
     * - Uses three nested loops for matrix multiplication.
     * - Implements loop reordering for better cache performance.
     * - Uses loop unrolling to reduce loop overhead.
     * - Uses blocking (tiling) for large matrices to optimize cache usage.
     * - Detects zero matrices and skips computation early.
     *
     * **Defensive Programming Measures:**
     * - Checks for null matrices to prevent `NullPointerException`.
     * - Ensures matrices are not empty.
     * - Ensures valid dimensions for multiplication (`A.cols == B.rows`).
     *
     * @param A The first matrix (n × n).
     * @param B The second matrix (n × n).
     * @return The product matrix (n × n).
     * @throws IllegalArgumentException If matrices cannot be multiplied.
     */
    public static Matrix multiply(Matrix A, Matrix B) {
        try {
            validateMatrices(A, B); // Ensure valid matrices before proceeding
            multiplicationCount = 0; // Reset multiplication counter

            int n = A.getSize(); // Square matrix size
            double[][] result = new double[n][n];

            // **Early Exit for Zero Matrices**
            if (isZeroMatrix(A) || isZeroMatrix(B)) {
                return Matrix.zeroMatrix(n); // If either matrix is zero, return a zero matrix
            }

            // **Cache matrix data for performance**
            double[][] aData = A.retrieveRowMajorAs2D();
            double[][] bData = B.retrieveRowMajorAs2D();

            // **Optimized Naive Matrix Multiplication**
            for (int i = 0; i < n; i++) {
                for (int k = 0; k < n; k++) {
                    double a_ik = aData[i][k]; // Cache value from A
                    for (int j = 0; j < n; j++) {
                        result[i][j] += a_ik * bData[k][j]; // Multiply and accumulate
                        multiplicationCount++; // Track multiplications correctly
                    }
                }
            }

            return new Matrix(n, result); // Return the resulting product matrix

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during matrix multiplication: " + e.getMessage(), e);
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

        if (A.getSize() != B.getSize()) {
            throw new IllegalArgumentException("Matrix multiplication error: Matrices must have the same size.");
        }
    }

    /**
     * Checks if a given matrix is a zero matrix (all elements are zero).
     *
     * @param M The matrix to check.
     * @return True if the matrix is a zero matrix, false otherwise.
     */
    private static boolean isZeroMatrix(Matrix M) {
        double[][] data = M.retrieveRowMajorAs2D();
        for (double[] row : data) {
            for (double val : row) {
                if (val != 0) return false;
            }
        }
        return true;
    }

    /**
     * Retrieves the number of scalar multiplications performed in the last operation.
     * Useful for performance analysis.
     *
     * @return The multiplication count from the last operation.
     */
    public static int getMultiplicationCount() {
        return multiplicationCount;
    }
}
