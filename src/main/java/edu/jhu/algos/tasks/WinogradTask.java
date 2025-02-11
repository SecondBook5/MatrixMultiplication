package edu.jhu.algos.tasks;

import edu.jhu.algos.models.Matrix;
import edu.jhu.algos.utils.MatrixOperations;
import edu.jhu.algos.utils.PerformanceMetrics;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Implements Winograd’s optimized Strassen Algorithm for matrix multiplication.
 * - Reduces matrix additions/subtractions from 18 to 12.
 * - Uses 7 recursive multiplications instead of 8 (like Strassen).
 * - Supports parallel execution using ForkJoinTask.
 * - Tracks performance (multiplication count & execution time).
 */
public class WinogradTask extends RecursiveTask<Matrix> {
    private static final int PARALLEL_THRESHOLD = 64; // Below this, switch to sequential
    private static final ForkJoinPool pool = new ForkJoinPool();
    private static final PerformanceMetrics metrics = new PerformanceMetrics();

    private final Matrix A;
    private final Matrix B;
    private final boolean parallel;

    /**
     * Constructs a WinogradTask for matrix multiplication.
     * @param A First square matrix (n × n).
     * @param B Second square matrix (n × n).
     * @param parallel If true, enables parallel execution.
     */
    public WinogradTask(Matrix A, Matrix B, boolean parallel) {
        this.A = A;
        this.B = B;
        this.parallel = parallel;
    }

    /**
     * Executes the Winograd multiplication task.
     * @return The product matrix.
     */
    @Override
    protected Matrix compute() {
        return winogradRecursive(A, B, parallel);
    }

    /**
     * Public method to execute Winograd multiplication.
     * @return The product matrix.
     */
    public Matrix executeTask() {
        return pool.invoke(this);
    }

    /**
     * Executes Winograd multiplication (parallel/sequential).
     * @param A First matrix.
     * @param B Second matrix.
     * @param parallel If true, enables parallel execution.
     * @return The resulting product matrix.
     */
    public static Matrix multiply(Matrix A, Matrix B, boolean parallel) {
        validateMatrices(A, B);
        metrics.updateMetrics(0, 0); // Reset performance counters.

        long startTime = System.nanoTime();
        Matrix result = parallel ? pool.invoke(new WinogradTask(A, B, true)) : winogradRecursive(A, B, false);
        long endTime = System.nanoTime();

        metrics.updateExecutionTime((endTime - startTime) / 1e6); // Convert to milliseconds.
        return result;
    }

    /**
     * Recursive Winograd-Optimized Strassen’s Algorithm implementation.
     * @param A First matrix.
     * @param B Second matrix.
     * @param parallel Whether to use parallel execution.
     * @return The product matrix.
     */
    private static Matrix winogradRecursive(Matrix A, Matrix B, boolean parallel) {
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

        // Compute Winograd's reduced S matrices
        Matrix T1 = MatrixOperations.add(A21, A22);
        Matrix T2 = MatrixOperations.subtract(A22, A12);
        Matrix T3 = MatrixOperations.subtract(A22, A11);
        Matrix T4 = MatrixOperations.subtract(B22, B11);
        Matrix T5 = MatrixOperations.add(B21, B22);
        Matrix T6 = MatrixOperations.subtract(B22, B12);

        // Compute 7 recursive multiplications
        WinogradTask M1 = new WinogradTask(A11, B11, parallel);
        WinogradTask M2 = new WinogradTask(A12, B21, parallel);
        WinogradTask M3 = new WinogradTask(A21, T4, parallel);
        WinogradTask M4 = new WinogradTask(A22, B22, parallel);
        WinogradTask M5 = new WinogradTask(T1, T5, parallel);
        WinogradTask M6 = new WinogradTask(T2, T6, parallel);
        WinogradTask M7 = new WinogradTask(T3, B12, parallel);

        Matrix P1, P2, P3, P4, P5, P6, P7;

        if (parallel && n > PARALLEL_THRESHOLD) {
            invokeAll(M1, M2, M3, M4, M5, M6, M7);
            P1 = M1.join();
            P2 = M2.join();
            P3 = M3.join();
            P4 = M4.join();
            P5 = M5.join();
            P6 = M6.join();
            P7 = M7.join();
        } else {
            P1 = M1.fork().join();
            P2 = M2.fork().join();
            P3 = M3.fork().join();
            P4 = M4.fork().join();
            P5 = M5.fork().join();
            P6 = M6.fork().join();
            P7 = M7.fork().join();
        }

        // Compute final C submatrices using optimized formulas
        Matrix C11 = MatrixOperations.add(P1, P2);
        Matrix C12 = MatrixOperations.subtract(P5, P7);
        Matrix C21 = MatrixOperations.add(P3, P6);
        Matrix C22 = MatrixOperations.subtract(MatrixOperations.add(P5, P6), MatrixOperations.add(P2, P4));

        // Merge submatrices into final result matrix
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
