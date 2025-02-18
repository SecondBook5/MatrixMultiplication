package edu.jhu.algos.compare;

import java.util.List;

/**
 * Fits empirical performance data to theoretical complexity functions
 * (O(n^3) for Naive, O(n^2.8074) for Strassen).
 * Uses least-squares fitting to estimate the best-fit constants.
 */
public class CurveFitter {

    /**
     * Fits a function f(n) = c * (n^exp) to the data
     * by minimizing sum of squared errors in a naive way.
     *
     * @param records The performance records (n, time).
     * @param exponent The exponent (3.0 for Naive, 2.8074 for Strassen).
     * @param useNaiveTime Whether to fit based on Naive (true) or Strassen (false) execution time.
     * @return The best-fit constant c, which minimizes the squared error.
     */
    public static double fitConstant(List<PerformanceRecord> records, double exponent, boolean useNaiveTime) {
        double numerator = 0.0;  // Stores sum(time(n) * n^exp)
        double denominator = 0.0; // Stores sum(n^(2exp))

        for (PerformanceRecord r : records) {
            long time = useNaiveTime ? r.getNaiveTimeMs() : r.getStrassenTimeMs(); // Select correct execution time
            double nPowerExp = Math.pow(r.getSize(), exponent); // Compute n^exp
            numerator += time * nPowerExp;  // Accumulate numerator sum
            denominator += Math.pow(r.getSize(), 2.0 * exponent); // Accumulate denominator sum
        }

        // Prevent division by zero in edge cases (e.g., empty input)
        if (denominator == 0) {
            return 0;
        }

        return numerator / denominator;  // Compute best-fit constant c
    }
}
