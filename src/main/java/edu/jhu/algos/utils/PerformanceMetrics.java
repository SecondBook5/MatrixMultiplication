package edu.jhu.algos.utils;

/**
 * Utility class for tracking performance metrics (multiplication count & execution time)
 * for both Naive and Strassen Matrix Multiplication.
 */
public class PerformanceMetrics {

    private int multiplicationCount; // Tracks total scalar multiplications
    private double executionTime;    // Tracks execution time in milliseconds

    /**
     * Initializes performance metrics with default values.
     */
    public PerformanceMetrics() {
        this.multiplicationCount = 0;
        this.executionTime = 0.0;
    }

    /**
     * Updates performance metrics with new values.
     *
     * @param multiplicationCount The total number of scalar multiplications performed.
     * @param executionTime The execution time in milliseconds.
     */
    public void updateMetrics(int multiplicationCount, double executionTime) {
        this.multiplicationCount = multiplicationCount;
        this.executionTime = executionTime;
    }

    /**
     * Prints the performance results for easy comparison.
     *
     * @param algorithmName The name of the algorithm being benchmarked (e.g., "Naive", "Strassen").
     */
    public void printPerformanceResults(String algorithmName) {
        System.out.println("\n **Performance Metrics for " + algorithmName + " Multiplication**");
        System.out.println("   - Total Scalar Multiplications: " + multiplicationCount);
        System.out.printf("   - Execution Time: %.3f ms%n", executionTime);
        System.out.println("──────────────────────────────────\n");
    }

    /**
     * Gets the multiplication count.
     *
     * @return The multiplication count.
     */
    public int getMultiplicationCount() {
        return multiplicationCount;
    }

    /**
     * Gets the execution time in milliseconds.
     *
     * @return The execution time.
     */
    public double getExecutionTime() {
        return executionTime;
    }
}
