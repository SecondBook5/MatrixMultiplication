package edu.jhu.algos.compare;

import java.util.List;

/**
 * Generates textual representations (ASCII, CSV) of performance records.
 * <p>
 * Supports:
 *  - Console-friendly ASCII table
 *  - CSV format for spreadsheet use
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
        sb.append("---------------------------------------------------------------------------------------------------\n");
        sb.append(String.format("| %10s | %15s | %15s | %15s | %15s |\n",
                "Matrix n", "Naive Time (ms)", "Naive Count", "Strassen Time (ms)", "Strassen Count"));
        sb.append("---------------------------------------------------------------------------------------------------\n");

        for (PerformanceRecord r : records) {
            sb.append(String.format("| %10d | %15d | %15d | %15d | %15d |\n",
                    r.getSize(),
                    r.getNaiveTimeMs(),
                    r.getNaiveMultiplications(),
                    r.getStrassenTimeMs(),
                    r.getStrassenMultiplications()));
        }
        sb.append("---------------------------------------------------------------------------------------------------\n");

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
        sb.append("n,naiveTimeMs,naiveMultiplications,strassenTimeMs,strassenMultiplications\n");

        for (PerformanceRecord r : records) {
            sb.append(String.format("%d,%d,%d,%d,%d\n",
                    r.getSize(),
                    r.getNaiveTimeMs(),
                    r.getNaiveMultiplications(),
                    r.getStrassenTimeMs(),
                    r.getStrassenMultiplications()));
        }

        return sb.toString();
    }
}
