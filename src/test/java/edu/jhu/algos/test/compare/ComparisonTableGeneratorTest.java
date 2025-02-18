package edu.jhu.algos.test.compare;

import edu.jhu.algos.compare.ComparisonTableGenerator;
import edu.jhu.algos.compare.PerformanceRecord;
import edu.jhu.algos.compare.CurveFitter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for ComparisonTableGenerator.
 * Ensures that performance records are correctly formatted into tables (ASCII and CSV),
 * and verifies scaling estimation.
 */
class ComparisonTableGeneratorTest {

    /**
     * Tests that the ASCII table is correctly generated for valid input records.
     */
    @Test
    void testAsciiTableGeneration() {
        List<PerformanceRecord> records = generateTestRecords();

        // Generate ASCII table
        String table = ComparisonTableGenerator.toAsciiTable(records);

        // Validate table contains key fields
        assertTrue(table.contains("Matrix n"), "Table should contain header row.");
        assertTrue(table.contains("Naive Time (ms)"), "Table should list Naive time.");
        assertTrue(table.contains("Strassen Time (ms)"), "Table should list Strassen time.");

        // Validate table contains matrix sizes
        for (PerformanceRecord record : records) {
            assertTrue(table.contains(String.format("| %10d", record.getSize())),
                    "Table should contain matrix size " + record.getSize());
        }
    }

    /**
     * Tests that an empty input list returns a proper message instead of a table.
     */
    @Test
    void testAsciiTableEmptyInput() {
        List<PerformanceRecord> emptyRecords = new ArrayList<>();
        String table = ComparisonTableGenerator.toAsciiTable(emptyRecords);

        assertEquals("No performance data available.\n", table, "Table should indicate no data is available.");
    }

    /**
     * Tests that the CSV output is correctly formatted and contains expected values.
     */
    @Test
    void testCsvGeneration() {
        List<PerformanceRecord> records = generateTestRecords();

        // Generate CSV
        String csv = ComparisonTableGenerator.toCsv(records);

        // Check that CSV contains correct headers
        assertTrue(csv.startsWith("n,naiveTimeMs,naiveMultiplications,strassenTimeMs,strassenMultiplications,bigONotation"),
                "CSV should start with correct headers.");

        // Check that CSV contains expected values
        for (PerformanceRecord record : records) {
            assertTrue(csv.contains(String.format("%d,%d,%d,%d,%d",
                            record.getSize(),
                            record.getNaiveTimeMs(),
                            record.getNaiveMultiplications(),
                            record.getStrassenTimeMs(),
                            record.getStrassenMultiplications())),
                    "CSV should contain performance data for size " + record.getSize());
        }
    }

    /**
     * Tests whether Big-O notation estimation is assigned correctly based on empirical scaling.
     */
    @Test
    void testBigONotationEstimation() {
        List<PerformanceRecord> records = generateTestRecords();

        // Generate ASCII table with empirical scaling
        String table = ComparisonTableGenerator.toAsciiTable(records);

        // Validate Big-O predictions
        assertTrue(table.contains("O(n^3)"), "Big-O should recognize O(n^3) for naive case.");
        assertTrue(table.contains("O(n^2.81)"), "Big-O should recognize O(n^2.81) for Strassen case.");
    }

    /**
     * Helper method to generate test performance records with calculated constants.
     *
     * @return List of performance records with precomputed constants.
     */
    private List<PerformanceRecord> generateTestRecords() {
        List<PerformanceRecord> records = new ArrayList<>();

        // Create sample performance records (Initial constants set to 0)
        records.add(new PerformanceRecord(32, 32768, 16384, 10240, 5000, 0, 0));
        records.add(new PerformanceRecord(64, 1048576, 500000, 300000, 240000, 0, 0));
        records.add(new PerformanceRecord(128, 8388608, 4000000, 1200000, 980000, 0, 0));

        // Compute Big-O constants using CurveFitter
        double naiveConstant = CurveFitter.fitConstant(records, 3.0, true);
        double strassenConstant = CurveFitter.fitConstant(records, Math.log(7) / Math.log(2), false);

        // Update records with correct constants
        for (int i = 0; i < records.size(); i++) {
            PerformanceRecord r = records.get(i);
            records.set(i, new PerformanceRecord(r.getSize(), r.getNaiveTimeMs(), r.getNaiveMultiplications(),
                    r.getStrassenTimeMs(), r.getStrassenMultiplications(), naiveConstant, strassenConstant));
        }

        return records;
    }
}
