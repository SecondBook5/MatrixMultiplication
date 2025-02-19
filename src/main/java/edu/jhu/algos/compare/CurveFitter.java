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
        double numerator = 0.0;  // Σ [T(n) * n^exp]
        double denominator = 0.0; // Σ [(n^exp)²]

        // Edge case: No records
        if (records == null || records.isEmpty()) {
            System.err.println("Warning: No valid data provided to CurveFitter, returning 0.");
            return 0;
        }

        int validCount = 0; // Count valid entries

        for (PerformanceRecord r : records) {
            long time = useNaiveTime ? r.getNaiveTimeMs() : r.getStrassenTimeMs();
            int size = r.getSize();

            // Skip invalid or zero execution times
            if (time <= 0) continue;

            double nPowerExp = Math.pow(size, exponent);
            numerator += time * nPowerExp;
            denominator += nPowerExp * nPowerExp; // Square the value instead of using 2*exp
            validCount++;
        }

        // Debugging logs using String.format()
        DebugConfig.log(String.format("Valid Data Points: %d", validCount));
        DebugConfig.log(String.format("Computed Numerator: %.8e", numerator));
        DebugConfig.log(String.format("Computed Denominator: %.8e", denominator));

        // Edge case: Prevent division by zero or extremely small values
        if (Math.abs(denominator) < 1e-12) {  // Adjust threshold if needed
            System.err.printf("Warning: Computed denominator is near zero (denominator=%.12e) in CurveFitter for exponent %.6f. Returning 0.%n", denominator, exponent);
            return 0;
        }


        double result = numerator / denominator;
        DebugConfig.log(String.format("Computed Constant (exp=%.6f): %.8f", exponent, result));
        return result;
    }
}
