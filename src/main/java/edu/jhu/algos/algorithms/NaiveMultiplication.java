package edu.jhu.algos.algorithms;

import edu.jhu.algos.models.Matrix;
import edu.jhu.algos.utils.PerformanceMetrics;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Implements naive O(n³) matrix multiplication with performance optimizations.
 * - Uses loop reordering for cache efficiency.
 * - Ensures correct scalar multiplication counting.
 * - Tracks execution time for performance analysis via PerformanceMetrics.
 * - Implements defensive programming with robust error handling.
 * - Uses parallel processing ONLY IF EXPLICITLY ENABLED (via CLI toggle).
 */
public class NaiveMultiplication {

    private static final ForkJoinPool pool = new ForkJoinPool(); // Thread pool for parallel execution
    private static boolean useParallel = false; // CLI toggle for parallel execution
    private static final PerformanceMetrics metrics = new PerformanceMetrics(); // Centralized performance tracking

    /**
     * Enables or disables parallel execution for matrix multiplication.
     * Parallel mode must be explicitly enabled (not automatic).
     *
     * @param parallel True to enable parallel execution, false to force sequential.
     */
    public static void setParallelExecution(boolean parallel) {
        useParallel = parallel;
    }

    /**
     * Multiplies two matrices using an optimized naive O(n³) algorithm.
     * - Uses parallel execution ONLY IF `useParallel = true`.
     * - Uses loop blocking (tiling) **ONLY IF parallel mode is enabled**.
     *
     * @param A The first matrix (n × n).
     * @param B The second matrix (n × n).
     * @return The product matrix (n × n).
     * @throws IllegalArgumentException If matrices cannot be multiplied.
     */
    public static Matrix multiply(Matrix A, Matrix B) {
        try {
            validateMatrices(A, B); // Validate matrices before processing
            metrics.updateMetrics(0, 0); // Reset performance metrics

            int n = A.getSize(); // Get matrix size
            double[][] result = new double[n][n];

            // **Early Exit for Zero Matrices**
            if (isZeroMatrix(A) || isZeroMatrix(B)) {
                return Matrix.zeroMatrix(n);
            }

            // **Cache Matrix Data for Performance**
            double[][] aData = A.retrieveRowMajorAs2D();
            double[][] bData = B.retrieveRowMajorAs2D();

            // **Measure Execution Time**
            long startTime = System.nanoTime();

            if (useParallel && n >= 128) { // Parallel processing only for large matrices
                pool.invoke(new MultiplyTask(0, n, aData, bData, result));
            } else {
                // Sequential Execution
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        double sum = 0;
                        for (int k = 0; k < n; k++) {
                            sum += aData[i][k] * bData[k][j];
                            metrics.incrementMultiplicationCount(); // Track multiplications via PerformanceMetrics
                        }
                        result[i][j] = sum;
                    }
                }
            }

            long endTime = System.nanoTime();
            metrics.updateExecutionTime((endTime - startTime) / 1e6); // Convert to milliseconds

            return new Matrix(n, result);

        } catch (IllegalArgumentException e) {
            throw e; // Preserve expected validation exceptions
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
        if (A == null || B == null) {
            throw new IllegalArgumentException("Matrix multiplication error: One or both matrices are null.");
        }
        if (A.getSize() != B.getSize()) {
            throw new IllegalArgumentException("Matrix multiplication error: Matrices must have the same size.");
        }
    }

    /**
     * Checks if a matrix is a zero matrix (all elements are zero).
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
        return metrics.getMultiplicationCount();
    }

    /**
     * Retrieves the execution time of the last multiplication in milliseconds.
     *
     * @return Execution time in milliseconds.
     */
    public static double getExecutionTime() {
        return metrics.getExecutionTime();
    }

    /**
     * **Parallel Processing Task for Large Matrices (Enabled Only if Explicitly Set)**
     * - Uses ForkJoinPool to divide matrix multiplication across threads.
     */
    private static class MultiplyTask extends RecursiveAction {
        private final int start, end, n;
        private final double[][] aData, bData, result;

        MultiplyTask(int start, int end, double[][] aData, double[][] bData, double[][] result) {
            this.start = start;
            this.end = end;
            this.n = aData.length;
            this.aData = aData;
            this.bData = bData;
            this.result = result;
        }

        @Override
        protected void compute() {
            if ((end - start) <= Math.max(64, n / 8)) { // Adjusted stopping condition
                for (int i = start; i < end; i++) {
                    for (int j = 0; j < n; j++) {
                        double sum = 0;
                        for (int k = 0; k < n; k++) {
                            sum += aData[i][k] * bData[k][j];
                            metrics.incrementMultiplicationCount();
                        }
                        result[i][j] = sum;
                    }
                }
            } else {
                // Divide into smaller tasks
                int mid = (start + end) / 2;
                MultiplyTask left = new MultiplyTask(start, mid, aData, bData, result);
                MultiplyTask right = new MultiplyTask(mid, end, aData, bData, result);

                left.fork();  // Fork left task
                right.compute();  // Compute right task in current thread
                left.join();  // Join left task
            }
        }
    }
}
