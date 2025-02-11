package edu.jhu.algos.algorithms;

import edu.jhu.algos.models.Matrix;
import edu.jhu.algos.tasks.StrassenTask;
import edu.jhu.algos.tasks.WinogradTask;
import edu.jhu.algos.utils.PerformanceMetrics;

/**
 * Main controller for Strassen’s Algorithm.
 * - Supports standard Strassen or Winograd’s optimized variant.
 * - Toggles parallel execution via CLI.
 * - Tracks execution time & multiplication count.
 */
public class StrassenMultiplication {

    private static final PerformanceMetrics metrics = new PerformanceMetrics(); // Performance tracking
    private static boolean useWinograd = false; // Toggle for Winograd’s optimization
    private static boolean useParallel = false; // Toggle for parallel execution

    /**
     * Enables or disables Winograd’s optimization.
     * @param winograd True to enable Winograd’s method, false for classic Strassen.
     */
    public static void setWinogradOptimization(boolean winograd) {
        useWinograd = winograd;
    }

    /**
     * Enables or disables parallel execution.
     * @param parallel True to enable parallel execution, false for sequential.
     */
    public static void setParallelExecution(boolean parallel) {
        useParallel = parallel;
    }

    /**
     * Multiplies two matrices using Strassen’s Algorithm.
     * - Uses StrassenTask for classic Strassen.
     * - Uses WinogradTask if Winograd optimization is enabled.
     * - Supports parallel execution for large matrices.
     *
     * @param A First square matrix (n × n).
     * @param B Second square matrix (n × n).
     * @return The resulting product matrix.
     */
    public static Matrix multiply(Matrix A, Matrix B) {
        try {
            validateMatrices(A, B); // Ensure valid matrices
            metrics.updateMetrics(0, 0); // Reset performance metrics

            long startTime = System.nanoTime(); // Start execution timer

            // **Choose which method to run based on CLI toggles**
            Matrix result = useWinograd
                    ? new WinogradTask(A, B, useParallel).executeTask()
                    : new StrassenTask(A, B, useParallel).executeTask();

            long endTime = System.nanoTime(); // Stop execution timer
            metrics.updateExecutionTime((endTime - startTime) / 1e6); // Convert to milliseconds

            return result;

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during Strassen multiplication: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves the total multiplication count performed in the last operation.
     * @return The multiplication count.
     */
    public static int getMultiplicationCount() {
        return metrics.getMultiplicationCount();
    }

    /**
     * Retrieves the execution time for the last operation.
     * @return Execution time in milliseconds.
     */
    public static double getExecutionTime() {
        return metrics.getExecutionTime();
    }

    /**
     * Validates that input matrices are suitable for Strassen’s Algorithm.
     * @param A First matrix.
     * @param B Second matrix.
     */
    private static void validateMatrices(Matrix A, Matrix B) {
        if (A == null || B == null)
            throw new IllegalArgumentException("One or both matrices are null.");
        if (A.getSize() != B.getSize())
            throw new IllegalArgumentException("Matrices must have the same size.");
    }
}
