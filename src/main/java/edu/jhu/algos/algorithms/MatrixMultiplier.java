//package edu.jhu.algos.algorithms;
//
//import edu.jhu.algos.models.Matrix;
//import edu.jhu.algos.utils.PerformanceMetrics;
//
///**
// * Abstract class providing shared functionality for matrix multiplication methods.
// * - Tracks execution time and multiplication counts.
// * - Handles validation of matrices before multiplication.
// */
//public abstract class MatrixMultiplier {
//
//    protected final PerformanceMetrics metrics = new PerformanceMetrics(); // Performance tracker
//
//    /**
//     * Tracks execution time for matrix multiplication.
//     *
//     * @param A The first matrix.
//     * @param B The second matrix.
//     * @return The result matrix after multiplication.
//     */
//    public final Matrix multiply(Matrix A, Matrix B) {
//        validateMatrices(A, B); // Validate matrices before processing
//        metrics.resetMetrics(); // Reset performance metrics
//
//        long startTime = System.nanoTime(); // Start timing
//        Matrix result = performMultiplication(A, B);
//        long endTime = System.nanoTime(); // End timing
//
//        metrics.updateExecutionTime((endTime - startTime) / 1e6); // Convert to milliseconds
//        return result;
//    }
//
//    /**
//     * Implemented by subclasses to define multiplication logic.
//     *
//     * @param A The first matrix.
//     * @param B The second matrix.
//     * @return The resulting matrix.
//     */
//    protected abstract Matrix performMultiplication(Matrix A, Matrix B);
//
//    /**
//     * Validates input matrices before multiplication.
//     *
//     * @param A The first matrix.
//     * @param B The second matrix.
//     * @throws IllegalArgumentException If matrices are invalid for multiplication.
//     */
//    protected void validateMatrices(Matrix A, Matrix B) {
//        if (A == null || B == null) {
//            throw new IllegalArgumentException("Matrix multiplication error: One or both matrices are null.");
//        }
//        if (A.getSize() != B.getSize()) {
//            throw new IllegalArgumentException("Matrix multiplication error: Matrices must have the same size.");
//        }
//    }
//
//    /**
//     * Retrieves the number of scalar multiplications performed in the last operation.
//     *
//     * @return The multiplication count from the last operation.
//     */
//    public int getMultiplicationCount() {
//        return metrics.getMultiplicationCount();
//    }
//
//    /**
//     * Retrieves the execution time of the last multiplication in milliseconds.
//     *
//     * @return Execution time in milliseconds.
//     */
//    public double getExecutionTime() {
//        return metrics.getExecutionTime();
//    }
//}
