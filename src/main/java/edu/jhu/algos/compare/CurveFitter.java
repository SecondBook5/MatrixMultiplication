package edu.jhu.algos.compare;

import java.util.List;
import edu.jhu.algos.utils.DebugConfig; // Import DebugConfig

/**
 * Fits empirical performance data to theoretical complexity functions.
 * This estimates the best-fit constant `c` for:
 * - Naive multiplication: O(n^3)  →  T(n) ≈ c * n^3
 * - Strassen multiplication: O(n^2.8074)  →  T(n) ≈ c * n^2.8074
 */
public class CurveFitter {

    private static final double EPSILON = 1e-12;  // Small threshold to prevent floating-point issues
    private static final double MIN_VALID_TIME = 0.1; // Ignore execution times smaller than 0.1ms

    /**
     * Computes the best-fit constant `c` for the function f(n) = c * (n^exp)
     * using least-squares estimation.
     *
     * @param records The performance records containing (n, time) data points.
     * @param exponent The exponent `exp` (3.0 for Naive, 2.8074 for Strassen).
     * @param useNaiveTime If true, fits the Naive model (O(n^3)), otherwise fits Strassen (O(n^2.8074)).
     * @return The estimated best-fit constant `c`, or `0` if no valid data is provided.
     */
    public static double fitConstant(List<PerformanceRecord> records, double exponent, boolean useNaiveTime) {
        double numerator = 0.0;
        double denominator = 0.0;
        int validCount = 0; // Track valid entries

        if (records == null || records.isEmpty()) {
            System.err.println("Warning: No valid data provided to CurveFitter, returning 0.");
            return 0;
        }

        for (PerformanceRecord r : records) {
            long time = useNaiveTime ? r.getNaiveTimeMs() : r.getStrassenTimeMs();
            int size = r.getSize();

            // Strictly ignore negative or zero times
            if (time <= 0 || Math.abs(time) < 1e-8) continue;

            double logN = Math.log(size);
            double nPowerExp = Math.exp(exponent * logN);

            numerator += time * nPowerExp;
            denominator += nPowerExp * nPowerExp;
            validCount++;
        }

        // Debugging logs
        DebugConfig.log(String.format("Valid Data Points: %d", validCount));
        DebugConfig.log(String.format("Computed Numerator: %.8e", numerator));
        DebugConfig.log(String.format("Computed Denominator: %.8e", denominator));

        // Ensure a strict threshold on small denominators
        if (validCount == 0 || Math.abs(denominator) < 1e-8) {
            System.err.printf("Warning: Computed denominator too small (denominator=%.12e), returning 0.%n", denominator);
            return 0;
        }

        double result = numerator / denominator;
        DebugConfig.log(String.format("Computed Constant (exp=%.6f): %.8f", exponent, result));
        return result;
    }
}
