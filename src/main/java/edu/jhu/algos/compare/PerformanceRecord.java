package edu.jhu.algos.compare;

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
     * @param n The size of the matrices (must be square and power of 2).
     * @param naiveTimeMs Execution time for Naive algorithm (milliseconds).
     * @param strassenTimeMs Execution time for Strassen algorithm (milliseconds).
     * @param naiveMultiplications Number of scalar multiplications in Naive multiplication.
     * @param strassenMultiplications Number of scalar multiplications in Strassen multiplication.
     */
    public PerformanceRecord(int n, long naiveTimeMs, long strassenTimeMs,
                             long naiveMultiplications, long strassenMultiplications) {
        this.n = n;
        this.naiveTimeMs = naiveTimeMs;
        this.strassenTimeMs = strassenTimeMs;
        this.naiveMultiplications = naiveMultiplications;
        this.strassenMultiplications = strassenMultiplications;
    }

    // Getters for all fields (ensures immutability)
    public int getSize() { return n; }
    public long getNaiveTimeMs() { return naiveTimeMs; }  // Matches NaiveMultiplication.getElapsedTimeMs()
    public long getStrassenTimeMs() { return strassenTimeMs; }  // Matches StrassenMultiplication.getElapsedTimeMs()
    public long getNaiveMultiplications() { return naiveMultiplications; }  // Matches getMultiplicationCount()
    public long getStrassenMultiplications() { return strassenMultiplications; }  // Matches getMultiplicationCount()

    /**
     * Returns a formatted string of the performance data.
     * Useful for debugging, logging, or generating CSV-style tables.
     * @return A formatted string containing performance metrics.
     */
    @Override
    public String toString() {
        return String.format("Size: %d | Naive Time: %d ms | Strassen Time: %d ms | " +
                        "Naive Multiplications: %d | Strassen Multiplications: %d",
                n, naiveTimeMs, strassenTimeMs, naiveMultiplications, strassenMultiplications);
    }
}
