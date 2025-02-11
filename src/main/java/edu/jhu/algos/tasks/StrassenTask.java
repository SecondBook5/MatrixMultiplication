package edu.jhu.algos.tasks;

import edu.jhu.algos.models.Matrix;
import edu.jhu.algos.utils.MatrixOperations;
import edu.jhu.algos.utils.PerformanceMetrics;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Implements Strassen's Algorithm as a recursive parallel task.
 * - Uses divide-and-conquer to reduce multiplication count.
 * - Supports parallel execution using ForkJoinPool.
 * - Optimized for power-of-two sized matrices.
 */
public class StrassenTask extends RecursiveTask<Matrix> {
    private static final int PARALLEL_THRESHOLD = 64;  // Below this, run sequentially.
    private static final ForkJoinPool pool = new ForkJoinPool();  // Thread pool for parallel tasks.
    private static final PerformanceMetrics metrics = new PerformanceMetrics();  // Performance tracking.

    private final Matrix A;
    private final Matrix B;
    private final boolean parallel;

    /**
     * Constructs a Strassen multiplication task.
     * @param A First input matrix.
     * @param B Second input matrix.
     * @param parallel Whether to execute in parallel.
     */
    public StrassenTask(Matrix A, Matrix B, boolean parallel) {
        this.A = A;
        this.B = B;
        this.parallel = parallel;
    }

    /**
     * Executes Strassen multiplication as a recursive task.
     * @return The resulting product matrix.
     */
    @Override
    protected Matrix compute() {
        return strassenRecursive(A, B, parallel);
    }

    /**
     * Public method to execute Strassen multiplication.
     * @return The product matrix.
     */
    public Matrix executeTask() {
        return pool.invoke(this);
    }

    /**
     * Performs matrix multiplication using Strassen’s Algorithm.
     * - Supports both parallel and sequential execution.
     * @param A First matrix.
     * @param B Second matrix.
     * @param parallel Whether to enable parallel execution.
     * @return Product matrix.
     */
    public static Matrix multiply(Matrix A, Matrix B, boolean parallel) {
        validateMatrices(A, B);
        metrics.updateMetrics(0, 0); // Reset performance counters.

        long startTime = System.nanoTime();
        Matrix result = parallel ? pool.invoke(new StrassenTask(A, B, true)) : strassenRecursive(A, B, false);
        long endTime = System.nanoTime();

        metrics.updateExecutionTime((endTime - startTime) / 1e6); // Convert to milliseconds.
        return result;
    }

    /**
     * Recursive Strassen’s Algorithm implementation.
     * - Uses 7 recursive multiplications instead of 8 (reducing O(n³) to O(n^2.81)).
     * - Efficiently handles matrix splitting and merging.
     * - Switches to naive multiplication for very small matrices.
     * @param A First matrix.
     * @param B Second matrix.
     * @param parallel Whether to use parallel execution.
     * @return Resulting product matrix.
     */
    private static Matrix strassenRecursive(Matrix A, Matrix B, boolean parallel) {
        int n = A.getSize();

        // Base case: Single-element multiplication.
        if (n == 1) {
            metrics.incrementMultiplicationCount();
            return new Matrix(1, new double[][]{{A.retrieveRowMajorAs2D()[0][0] * B.retrieveRowMajorAs2D()[0][0]}});
        }

        int newSize = n / 2; // Half-size for submatrices.

        // Split A and B into 4 submatrices each.
        Matrix A11 = MatrixOperations.split(A, 0, 0, newSize);
        Matrix A12 = MatrixOperations.split(A, 0, newSize, newSize);
        Matrix A21 = MatrixOperations.split(A, newSize, 0, newSize);
        Matrix A22 = MatrixOperations.split(A, newSize, newSize, newSize);

        Matrix B11 = MatrixOperations.split(B, 0, 0, newSize);
        Matrix B12 = MatrixOperations.split(B, 0, newSize, newSize);
        Matrix B21 = MatrixOperations.split(B, newSize, 0, newSize);
        Matrix B22 = MatrixOperations.split(B, newSize, newSize, newSize);

        // Compute 7 Strassen products.
        StrassenTask P1 = new StrassenTask(MatrixOperations.add(A11, A22), MatrixOperations.add(B11, B22), parallel);
        StrassenTask P2 = new StrassenTask(MatrixOperations.add(A21, A22), B11, parallel);
        StrassenTask P3 = new StrassenTask(A11, MatrixOperations.subtract(B12, B22), parallel);
        StrassenTask P4 = new StrassenTask(A22, MatrixOperations.subtract(B21, B11), parallel);
        StrassenTask P5 = new StrassenTask(MatrixOperations.add(A11, A12), B22, parallel);
        StrassenTask P6 = new StrassenTask(MatrixOperations.subtract(A21, A11), MatrixOperations.add(B11, B12), parallel);
        StrassenTask P7 = new StrassenTask(MatrixOperations.subtract(A12, A22), MatrixOperations.add(B21, B22), parallel);

        Matrix M1, M2, M3, M4, M5, M6, M7;

        if (parallel && n > PARALLEL_THRESHOLD) {
            // Execute in parallel if matrix is large.
            invokeAll(P1, P2, P3, P4, P5, P6, P7);
            M1 = P1.join();
            M2 = P2.join();
            M3 = P3.join();
            M4 = P4.join();
            M5 = P5.join();
            M6 = P6.join();
            M7 = P7.join();
        } else {
            // Execute sequentially for small matrices.
            M1 = P1.fork().join();
            M2 = P2.fork().join();
            M3 = P3.fork().join();
            M4 = P4.fork().join();
            M5 = P5.fork().join();
            M6 = P6.fork().join();
            M7 = P7.fork().join();
        }

        // Compute 4 submatrices of result matrix.
        Matrix C11 = MatrixOperations.add(MatrixOperations.subtract(MatrixOperations.add(M1, M4), M5), M7);
        Matrix C12 = MatrixOperations.add(M3, M5);
        Matrix C21 = MatrixOperations.add(M2, M4);
        Matrix C22 = MatrixOperations.add(MatrixOperations.subtract(MatrixOperations.add(M1, M3), M2), M6);

        // Merge submatrices into final result.
        Matrix C = new Matrix(n, new double[n][n]);
        MatrixOperations.merge(C, C11, 0, 0);
        MatrixOperations.merge(C, C12, 0, newSize);
        MatrixOperations.merge(C, C21, newSize, 0);
        MatrixOperations.merge(C, C22, newSize, newSize);

        return C;
    }

    /**
     * Validates matrices before multiplication.
     * @param A First matrix.
     * @param B Second matrix.
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
     * Retrieves the multiplication count for performance analysis.
     * @return Total multiplication count.
     */
    public static int getMultiplicationCount() {
        return metrics.getMultiplicationCount();
    }

    /**
     * Retrieves execution time for the last multiplication.
     * @return Execution time in milliseconds.
     */
    public static double getExecutionTime() {
        return metrics.getExecutionTime();
    }
}
