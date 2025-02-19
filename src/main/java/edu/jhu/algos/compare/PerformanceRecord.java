package edu.jhu.algos.compare;

import edu.jhu.algos.utils.DebugConfig; // Import DebugConfig

/**
 * Stores performance metrics for a single matrix multiplication comparison.
 * <p>
 * Each record tracks:
 * - Matrix size (n x n),
 * - Execution time for Naive and Strassen algorithms,
 * - Scalar multiplication counts for both algorithms,
 * - Estimated Big-O constants for Naive (O(n^3)) and Strassen (O(n^2.8074)).
 * </p>
 */
public class PerformanceRecord {
    private final int n;                 // Matrix size (must be power of 2)
    private final long naiveTimeMs;      // Execution time for Naive in milliseconds
    private final long strassenTimeMs;   // Execution time for Strassen in milliseconds
    private final long naiveMultiplications;   // Number of multiplications in Naive
    private final long strassenMultiplications; // Number of multiplications in Strassen
    private final double naiveConstant;    // Experimentally determined constant for O(n^3)
    private final double strassenConstant; // Experimentally determined constant for O(n^2.8074)

    /**
     * Constructs a PerformanceRecord for a specific matrix size.
     *
     * @param n The size of the matrices (must be square and power of 2).
     * @param naiveTimeMs Execution time for Naive algorithm (milliseconds).
     * @param strassenTimeMs Execution time for Strassen algorithm (milliseconds).
     * @param naiveMultiplications Number of scalar multiplications in Naive multiplication.
     * @param strassenMultiplications Number of scalar multiplications in Strassen multiplication.
     * @param naiveConstant Estimated constant for O(n^3) using curve fitting.
     * @param strassenConstant Estimated constant for O(n^2.8074) using curve fitting.
     */
    public PerformanceRecord(int n, long naiveTimeMs, long naiveMultiplications,
                             long strassenTimeMs, long strassenMultiplications,
                             double naiveConstant, double strassenConstant) {
        this.n = n;
        this.naiveTimeMs = naiveTimeMs;
        this.strassenTimeMs = strassenTimeMs;
        this.naiveMultiplications = naiveMultiplications;
        this.strassenMultiplications = strassenMultiplications;
        this.naiveConstant = naiveConstant;
        this.strassenConstant = strassenConstant;

        // Debugging to track exact values
        DebugConfig.log("PerformanceRecord Created - Naive Multiplications = " + naiveMultiplications +
                ", Strassen Multiplications = " + strassenMultiplications);
    }

    // Getters for all fields (ensures immutability)
    public int getSize() {
        DebugConfig.log("Retrieving Matrix Size: " + n);
        return n;
    }

    public long getNaiveTimeMs() {
        DebugConfig.log("Retrieving Naive Time (ms): " + naiveTimeMs);
        return naiveTimeMs;
    }  // Matches NaiveMultiplication.getElapsedTimeMs()

    public long getStrassenTimeMs() {
        DebugConfig.log("Retrieving Strassen Time (ms): " + strassenTimeMs);
        return strassenTimeMs;
    }  // Matches StrassenMultiplication.getElapsedTimeMs()

    public long getNaiveMultiplications() {
        DebugConfig.log("Retrieving Naive Multiplications: " + naiveMultiplications);
        return naiveMultiplications;
    }  // Matches getMultiplicationCount()

    public long getStrassenMultiplications() {
        DebugConfig.log("Retrieving Strassen Multiplications: " + strassenMultiplications);
        return strassenMultiplications;
    }  // Matches getMultiplicationCount()

    public double getNaiveConstant() {
        DebugConfig.log("Retrieving Naive Constant: " + naiveConstant);
        return naiveConstant;
    }  // Experimentally fitted constant for O(n^3)

    public double getStrassenConstant() {
        DebugConfig.log("Retrieving Strassen Constant: " + strassenConstant);
        return strassenConstant;
    }  // Experimentally fitted constant for O(n^2.8074)

    /**
     * Returns a formatted string of the performance data.
     * Useful for debugging, logging, or generating CSV-style tables.
     *
     * @return A formatted string containing performance metrics.
     */
    @Override
    public String toString() {
        return String.format("Size: %d | Naive Time: %d ms | Strassen Time: %d ms | " +
                        "Naive Multiplications: %d | Strassen Multiplications: %d | " +
                        "Naive O(n^3) Constant: %.6f | Strassen O(n^2.81) Constant: %.6f",
                n, naiveTimeMs, strassenTimeMs, naiveMultiplications, strassenMultiplications,
                naiveConstant, strassenConstant);
    }
}
