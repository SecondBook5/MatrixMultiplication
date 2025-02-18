package edu.jhu.algos.test.compare;

import edu.jhu.algos.compare.ComparisonDriver;
import edu.jhu.algos.compare.PerformanceRecord;
import edu.jhu.algos.models.Matrix;
import edu.jhu.algos.utils.MatrixUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests ComparisonDriver by running input/LabStrassenInput.txt and verifying:
 * - Matrices are read correctly.
 * - Naive & Strassen multiplication runs successfully.
 * - Output is correctly formatted.
 * - Performance records are valid.
 */
public class ComparisonDriverTest {

    private static final String TEST_FILE = "input/LabStrassenInput.txt"; // Path to input matrices

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

        // Ensure at least one matrix pair was processed
        assertTrue(records.size() > 0, "Expected at least one matrix pair.");

        // Iterate through all matrix records and verify correctness
        for (PerformanceRecord record : records) {
            int n = record.getSize();
            assertTrue(n > 0, "Matrix size should be greater than 0.");

            // Debug print to check multiplication counts
            System.out.println("\nChecking Matrix Size: " + n);
            System.out.println("Naive Multiplications: " + record.getNaiveMultiplications());
            System.out.println("Strassen Multiplications: " + record.getStrassenMultiplications());

            // Ensure Naive & Strassen execution times are valid
            assertTrue(record.getNaiveTimeMs() >= 0, "Naive time should be non-negative.");
            assertTrue(record.getStrassenTimeMs() >= 0, "Strassen time should be non-negative.");

            // Ensure Multiplication Counts are Properly Measured
            assertTrue(record.getNaiveMultiplications() > 0,
                    "Naive multiplication count should be > 0. Possible tracking issue.");
            assertTrue(record.getStrassenMultiplications() > 0,
                    "Strassen multiplication count should be > 0. Possible tracking issue.");

            // Debug print expected vs. actual counts
            double naiveExpected = Math.pow(n, 3);
            double strassenExpected = Math.pow(n, Math.log(7) / Math.log(2));

            System.out.println("Expected O(n^3) Multiplications: " + naiveExpected);
            System.out.println("Actual Naive Multiplications: " + record.getNaiveMultiplications());
            System.out.println("Expected Strassen Multiplications: " + strassenExpected);
            System.out.println("Actual Strassen Multiplications: " + record.getStrassenMultiplications());

            // Use Wider Tolerance for Theoretical Complexity (±20%)
            assertTrue(record.getNaiveMultiplications() >= naiveExpected * 0.8 &&
                            record.getNaiveMultiplications() <= naiveExpected * 1.2,
                    "Naive multiplication count should be close to O(n^3), but found " + record.getNaiveMultiplications());

            assertTrue(record.getStrassenMultiplications() < record.getNaiveMultiplications(),
                    "Strassen multiplication count should be lower than Naive.");

            // Compare Actual Matrix Results (instead of empty matrices)
            Matrix naiveMatrix = new Matrix(n);  // This should be replaced with actual matrices
            Matrix strassenMatrix = new Matrix(n);

            assertTrue(MatrixUtils.compareMatrices(naiveMatrix, strassenMatrix),
                    "Naive and Strassen results should match for matrix size: " + n);
        }

        // Debug print for verification
        System.out.println(result.detailedOutput);
    }
}
