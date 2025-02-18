package edu.jhu.algos.test.compare;

import edu.jhu.algos.compare.PerformanceRecord;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PerformanceRecord.
 * Ensures that matrix size, execution time, multiplication counts,
 * and estimated Big-O constants are correctly stored and retrieved.
 */
class PerformanceRecordTest {

    @Test
    void testPerformanceRecordInitialization() {
        // Define expected values
        int expectedSize = 128;       // Matrix size
        long expectedNaiveTime = 500; // Naive execution time in ms
        long expectedStrassenTime = 320; // Strassen execution time in ms
        long expectedNaiveCount = 100000; // Naive multiplication count
        long expectedStrassenCount = 70000; // Strassen multiplication count
        double expectedNaiveConstant = 0.005; // Hypothetical curve-fitted constant for O(n^3)
        double expectedStrassenConstant = 0.002; // Hypothetical curve-fitted constant for O(n^2.81)

        // Create PerformanceRecord instance
        PerformanceRecord record = new PerformanceRecord(
                expectedSize, expectedNaiveTime, expectedStrassenTime,
                expectedNaiveCount, expectedStrassenCount,
                expectedNaiveConstant, expectedStrassenConstant
        );

        // Validate stored values match expected values
        assertEquals(expectedSize, record.getSize(), "Matrix size should match expected value.");
        assertEquals(expectedNaiveTime, record.getNaiveTimeMs(), "Naive time should match expected value.");
        assertEquals(expectedStrassenTime, record.getStrassenTimeMs(), "Strassen time should match expected value.");
        assertEquals(expectedNaiveCount, record.getNaiveMultiplications(), "Naive multiplication count should match expected value.");
        assertEquals(expectedStrassenCount, record.getStrassenMultiplications(), "Strassen multiplication count should match expected value.");
        assertEquals(expectedNaiveConstant, record.getNaiveConstant(), 1e-6, "Naive O(n^3) constant should match expected value.");
        assertEquals(expectedStrassenConstant, record.getStrassenConstant(), 1e-6, "Strassen O(n^2.81) constant should match expected value.");
    }

    @Test
    void testImmutability() {
        // Create a record with initial values
        PerformanceRecord record = new PerformanceRecord(64, 200, 150, 50000, 35000, 0.01, 0.005);

        // Retrieve values
        int originalSize = record.getSize();
        long originalNaiveTime = record.getNaiveTimeMs();
        long originalStrassenTime = record.getStrassenTimeMs();
        long originalNaiveCount = record.getNaiveMultiplications();
        long originalStrassenCount = record.getStrassenMultiplications();
        double originalNaiveConstant = record.getNaiveConstant();
        double originalStrassenConstant = record.getStrassenConstant();

        // Ensure values are immutable (do not change after retrieval)
        assertEquals(64, originalSize);
        assertEquals(200, originalNaiveTime);
        assertEquals(150, originalStrassenTime);
        assertEquals(50000, originalNaiveCount);
        assertEquals(35000, originalStrassenCount);
        assertEquals(0.01, originalNaiveConstant, 1e-6);
        assertEquals(0.005, originalStrassenConstant, 1e-6);
    }

    @Test
    void testZeroMultiplications() {
        // Test edge case where multiplications are zero (e.g., multiplying by zero matrix)
        PerformanceRecord record = new PerformanceRecord(32, 10, 15, 0, 0, 0.0, 0.0);

        assertEquals(0, record.getNaiveMultiplications(), "Naive multiplications should be zero.");
        assertEquals(0, record.getStrassenMultiplications(), "Strassen multiplications should be zero.");
        assertEquals(0.0, record.getNaiveConstant(), 1e-6, "Naive O(n^3) constant should be zero.");
        assertEquals(0.0, record.getStrassenConstant(), 1e-6, "Strassen O(n^2.81) constant should be zero.");
    }

    @Test
    void testVeryLargeMatrixSizes() {
        // Edge case for large matrix sizes
        PerformanceRecord record = new PerformanceRecord(1024, 2000, 1500, 100000000, 70000000, 0.0001, 0.00005);

        assertEquals(1024, record.getSize(), "Matrix size should match large input.");
        assertEquals(2000, record.getNaiveTimeMs(), "Naive execution time should match.");
        assertEquals(1500, record.getStrassenTimeMs(), "Strassen execution time should match.");
        assertEquals(100000000, record.getNaiveMultiplications(), "Naive multiplications should match large input.");
        assertEquals(70000000, record.getStrassenMultiplications(), "Strassen multiplications should match large input.");
        assertEquals(0.0001, record.getNaiveConstant(), 1e-6, "Naive O(n^3) constant should match large input.");
        assertEquals(0.00005, record.getStrassenConstant(), 1e-6, "Strassen O(n^2.81) constant should match large input.");
    }

    @Test
    void testToStringFormat() {
        // Create a test record
        PerformanceRecord record = new PerformanceRecord(64, 500, 350, 10000, 7500, 0.003, 0.0015);

        // Expected string format
        String expected = String.format("Size: 64 | Naive Time: 500 ms | Strassen Time: 350 ms | " +
                        "Naive Multiplications: 10000 | Strassen Multiplications: 7500 | " +
                        "Naive O(n^3) Constant: %.6f | Strassen O(n^2.81) Constant: %.6f",
                0.003, 0.0015);

        assertEquals(expected, record.toString(), "toString() output format should match.");
    }
}
