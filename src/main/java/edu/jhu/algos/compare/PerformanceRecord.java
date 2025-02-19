package edu.jhu.algos.compare;

import edu.jhu.algos.utils.DebugConfig; // Import DebugConfig

/**
 * Stores performance metrics for a single matrix multiplication comparison.
 * <p>
 * Each record tracks:
 * - Matrix size (n x n),
 * - Execution time for Naive and Strassen algorithms,
 * - Scalar multiplication counts for both algorithms.
 * </p>
 */
public class PerformanceRecord {
    private final int n;                 // Matrix size (must be power of 2)
    private final long naiveTimeMs;      // Execution time for Naive in milliseconds
    private final long strassenTimeMs;   // Execution time for Strassen in milliseconds
    private final long naiveMultiplications;   // Number of multiplications in Naive
    private final long strassenMultiplications; // Number of multiplications in Strassen

    /**
     * Constructs a PerformanceRecord for a specific matrix size.
     *
     * @param n The size of the matrices (must be square and power of 2).
     * @param naiveTimeMs Execution time for Naive algorithm (milliseconds).
     * @param strassenTimeMs Execution time for Strassen algorithm (milliseconds).
     * @param naiveMultiplications Number of scalar multiplications in Naive multiplication.
     * @param strassenMultiplications Number of scalar multiplications in Strassen multiplication.
     */
    public PerformanceRecord(int n, long naiveTimeMs, long naiveMultiplications,
                             long strassenTimeMs, long strassenMultiplications) {
        this.n = n;

        // Ensure execution times are valid and log them for debugging
        this.naiveTimeMs = Math.max(naiveTimeMs, 1);  // Prevent 0ms times
        this.strassenTimeMs = Math.max(strassenTimeMs, 1);

        this.naiveMultiplications = naiveMultiplications;
        this.strassenMultiplications = strassenMultiplications;

        // Debugging logs to track exact values
        DebugConfig.log(String.format(
                "PerformanceRecord Created | Matrix Size: %d | Naive Time: %d ms | Strassen Time: %d ms | " +
                        "Naive Multiplications: %d | Strassen Multiplications: %d",
                n, this.naiveTimeMs, this.strassenTimeMs, naiveMultiplications, strassenMultiplications
        ));

        // Additional debug: Warn if execution time is unexpectedly small
        if (naiveTimeMs <= 0 || strassenTimeMs <= 0) {
            System.err.printf("Warning: Execution time too small for matrix size %d. Adjusting values.%n", n);
        }
    }

    // Getters for all fields (ensures immutability)
    public int getSize() {
        DebugConfig.log("Retrieving Matrix Size: " + n);
        return n;
    }

    public long getNaiveTimeMs() {
        DebugConfig.log("Retrieving Naive Time (ms): " + naiveTimeMs);
        return naiveTimeMs;
    }

    public long getStrassenTimeMs() {
        DebugConfig.log("Retrieving Strassen Time (ms): " + strassenTimeMs);
        return strassenTimeMs;
    }

    public long getNaiveMultiplications() {
        DebugConfig.log("Retrieving Naive Multiplications: " + naiveMultiplications);
        return naiveMultiplications;
    }

    public long getStrassenMultiplications() {
        DebugConfig.log("Retrieving Strassen Multiplications: " + strassenMultiplications);
        return strassenMultiplications;
    }

    /**
     * Returns a formatted string of the performance data.
     * Useful for debugging, logging, or generating CSV-style tables.
     *
     * @return A formatted string containing performance metrics.
     */
    @Override
    public String toString() {
        return String.format("Size: %d | Naive Time: %d ms | Strassen Time: %d ms | " +
                        "Naive Multiplications: %d | Strassen Multiplications: %d",
                n, naiveTimeMs, strassenTimeMs, naiveMultiplications, strassenMultiplications);
    }
}
