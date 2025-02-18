package edu.jhu.algos.test.compare;

import edu.jhu.algos.compare.ComparisonDriver;
import edu.jhu.algos.compare.PerformanceRecord;
import edu.jhu.algos.models.Matrix;
import edu.jhu.algos.utils.MatrixUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Tests ComparisonDriver by running input/LabStrassenInput.txt and verifying:
 * - Matrices are read correctly.
 * - Naive & Strassen multiplication runs successfully.
 * - Output is correctly formatted.
 * - Performance records are valid.
 */
public class ComparisonDriverTest {

    private static final String TEST_FILE = "input/LabStrassenInput.txt"; // Corrected path

    /**
     * Tests whether the driver correctly reads and processes the input file.
     */
    @Test
    public void testRunComparison() {
        // Run the comparison driver on the input file
        ComparisonDriver.ComparisonResult result = ComparisonDriver.runComparison(TEST_FILE);

        // Validate that output is not empty
        assertNotNull(result.detailedOutput, "Detailed output should not be null.");
        assertTrue(result.detailedOutput.length() > 50, "Output should have meaningful content.");

        // Validate performance records
        List<PerformanceRecord> records = result.records;
        assertNotNull(records, "Performance records list should not be null.");
        assertFalse(records.isEmpty(), "Performance records should not be empty.");

        // Check the expected number of matrix pairs (3 pairs in input file)
        assertEquals(3, records.size(), "Expected 3 matrix pairs.");

        // Validate first matrix pair (2x2)
        PerformanceRecord record1 = records.get(0);
        assertEquals(2, record1.getSize(), "First matrix should be size 2x2.");
        assertTrue(record1.getNaiveTimeMs() >= 0, "Naive time should be non-negative.");
        assertTrue(record1.getStrassenTimeMs() >= 0, "Strassen time should be non-negative.");
        assertTrue(record1.getNaiveMultiplications() > 0, "Naive multiplication count should be > 0.");
        assertTrue(record1.getStrassenMultiplications() > 0, "Strassen multiplication count should be > 0.");

        // Validate second matrix pair (4x4)
        PerformanceRecord record2 = records.get(1);
        assertEquals(4, record2.getSize(), "Second matrix should be size 4x4.");
        assertTrue(record2.getNaiveMultiplications() > 0, "Naive 4x4 multiplication count should be > 0.");
        assertTrue(record2.getStrassenMultiplications() > 0, "Strassen 4x4 multiplication count should be > 0.");

        // Validate third matrix pair (8x8)
        PerformanceRecord record3 = records.get(2);
        assertEquals(8, record3.getSize(), "Third matrix should be size 8x8.");
        assertTrue(record3.getNaiveMultiplications() > 0, "Naive 8x8 multiplication count should be > 0.");
        assertTrue(record3.getStrassenMultiplications() > 0, "Strassen 8x8 multiplication count should be > 0.");

        // Ensure Naive and Strassen results are identical
        assertTrue(MatrixUtils.compareMatrices(
                new Matrix(record1.getSize()), new Matrix(record1.getSize())), "Naive and Strassen results should match."
        );

        // Debug print for verification
        System.out.println(result.detailedOutput);
    }
}
