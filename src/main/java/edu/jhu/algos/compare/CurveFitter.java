package edu.jhu.algos.compare;

import java.util.List;

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

        // Debugging logs
        System.out.printf("Valid Data Points: %d%n", validCount);
        System.out.printf("Computed Numerator: %.8e%n", numerator);
        System.out.printf("Computed Denominator: %.8e%n", denominator);

        // Edge case: Prevent division by zero
        if (denominator == 0) {
            System.err.println("Warning: Computed denominator is zero. Returning 0.");
            return 0;
        }

        double result = numerator / denominator;
        System.out.printf("Computed Constant (exp=%.6f): %.8f%n", exponent, result);
        return result;
    }
}
