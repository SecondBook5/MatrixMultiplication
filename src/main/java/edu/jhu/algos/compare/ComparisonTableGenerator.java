package edu.jhu.algos.compare;

import java.util.List;

/**
 * Generates textual representations (ASCII, CSV) of performance records.
 * <p>
 * Supports:
 *  - Console-friendly ASCII table
 *  - CSV format for spreadsheet use
 *  - Empirical Big-O estimations using runtime scaling
 * </p>
 */
public class ComparisonTableGenerator {

    /**
     * Returns an ASCII table representation of the performance records.
     * @param records List of performance records.
     * @return Formatted ASCII table as a string.
     */
    public static String toAsciiTable(List<PerformanceRecord> records) {
        if (records == null || records.isEmpty()) {
            return "No performance data available.\n";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("-------------------------------------------------------------------------------------------------------------\n");
        sb.append(String.format("| %10s | %15s | %15s | %15s | %15s | %10s |\n",
                "Matrix n", "Naive Time (ms)", "Naive Count", "Strassen Time (ms)", "Strassen Count", "Big-O"));
        sb.append("-------------------------------------------------------------------------------------------------------------\n");

        // Compute scaling constants
        double cNaive = estimateScaling(records, 3.0, true);
        double cStrassen = estimateScaling(records, Math.log(7) / Math.log(2), false);

        for (PerformanceRecord r : records) {
            double naiveFit = cNaive * Math.pow(r.getSize(), 3.0);
            double strassenFit = cStrassen * Math.pow(r.getSize(), Math.log(7) / Math.log(2));

            String bigONotation = determineBigONotation(r.getNaiveTimeMs(), naiveFit, strassenFit);

            sb.append(String.format("| %10d | %15d | %15d | %15d | %15d | %10s |\n",
                    r.getSize(),
                    r.getNaiveTimeMs(),
                    r.getNaiveMultiplications(),
                    r.getStrassenTimeMs(),
                    r.getStrassenMultiplications(),
                    bigONotation));
        }
        sb.append("-------------------------------------------------------------------------------------------------------------\n");

        return sb.toString();
    }

    /**
     * Returns a CSV-formatted string of the performance records.
     * @param records List of performance records.
     * @return CSV-formatted data.
     */
    public static String toCsv(List<PerformanceRecord> records) {
        if (records == null || records.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("n,naiveTimeMs,naiveMultiplications,strassenTimeMs,strassenMultiplications,bigONotation\n");

        // Compute scaling constants
        double cNaive = estimateScaling(records, 3.0, true);
        double cStrassen = estimateScaling(records, Math.log(7) / Math.log(2), false);

        for (PerformanceRecord r : records) {
            double naiveFit = cNaive * Math.pow(r.getSize(), 3.0);
            double strassenFit = cStrassen * Math.pow(r.getSize(), Math.log(7) / Math.log(2));

            String bigONotation = determineBigONotation(r.getNaiveTimeMs(), naiveFit, strassenFit);

            sb.append(String.format("%d,%d,%d,%d,%d,%s\n",
                    r.getSize(),
                    r.getNaiveTimeMs(),
                    r.getNaiveMultiplications(),
                    r.getStrassenTimeMs(),
                    r.getStrassenMultiplications(),
                    bigONotation));
        }

        return sb.toString();
    }

    /**
     * Estimates the scaling constant C for an empirical runtime ~ O(n^exp).
     * Uses least-squares fitting to determine the best-fit C.
     *
     * @param records List of performance records.
     * @param exp Theoretical exponent (3.0 for naive, log2(7) for Strassen).
     * @param useNaiveTime True if fitting for naive, false if fitting for Strassen.
     * @return Best-fit scaling constant C.
     */
    private static double estimateScaling(List<PerformanceRecord> records, double exp, boolean useNaiveTime) {
        double sumNumerator = 0;
        double sumDenominator = 0;

        for (PerformanceRecord r : records) {
            long time = useNaiveTime ? r.getNaiveTimeMs() : r.getStrassenTimeMs();
            double nPowerExp = Math.pow(r.getSize(), exp);

            sumNumerator += time;
            sumDenominator += nPowerExp;
        }

        return (sumDenominator == 0) ? 0 : (sumNumerator / sumDenominator);
    }

    /**
     * Determines an empirical complexity estimate based on observed runtime scaling.
     *
     * @param actualTime The observed runtime.
     * @param naiveFit The expected runtime based on O(n^3).
     * @param strassenFit The expected runtime based on O(n^2.81).
     * @return String representing the best-fit complexity notation.
     */
    private static String determineBigONotation(long actualTime, double naiveFit, double strassenFit) {
        double naiveRatio = actualTime / naiveFit;
        double strassenRatio = actualTime / strassenFit;

        // Use a **wider range** to account for variability in timing
        if (naiveRatio > 0.7 && naiveRatio < 1.3) {
            return "O(n^3)";
        } else if (strassenRatio > 0.7 && strassenRatio < 1.3) {
            return "O(n^2.81)";
        } else if (actualTime < naiveFit && actualTime > strassenFit) {
            return "Θ(n^2.9)";
        } else if (actualTime < strassenFit * 0.9) {
            return "Ω(n^2.8)";
        } else {
            return "Uncertain";
        }
    }
}
