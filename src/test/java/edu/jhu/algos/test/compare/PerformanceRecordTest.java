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
        int expectedSize = 128;
        long expectedNaiveTime = 500;
        long expectedStrassenTime = 320;
        long expectedNaiveCount = 100000;
        long expectedStrassenCount = 70000;
        double expectedNaiveConstant = 0.005;
        double expectedStrassenConstant = 0.002;

        // Corrected parameter order
        PerformanceRecord record = new PerformanceRecord(
                expectedSize,
                expectedNaiveTime, expectedNaiveCount,
                expectedStrassenTime, expectedStrassenCount,
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
        PerformanceRecord record = new PerformanceRecord(
                64, 200, 50000, 150, 35000, 0.01, 0.005
        );

        // Retrieve values
        assertEquals(64, record.getSize());
        assertEquals(200, record.getNaiveTimeMs());
        assertEquals(150, record.getStrassenTimeMs());
        assertEquals(50000, record.getNaiveMultiplications());
        assertEquals(35000, record.getStrassenMultiplications());
        assertEquals(0.01, record.getNaiveConstant(), 1e-6);
        assertEquals(0.005, record.getStrassenConstant(), 1e-6);
    }

    @Test
    void testZeroMultiplications() {
        // Test edge case where multiplications are zero
        PerformanceRecord record = new PerformanceRecord(
                32, 10, 0, 15, 0, 0.0, 0.0
        );

        assertEquals(0, record.getNaiveMultiplications(), "Naive multiplications should be zero.");
        assertEquals(0, record.getStrassenMultiplications(), "Strassen multiplications should be zero.");
        assertEquals(0.0, record.getNaiveConstant(), 1e-6, "Naive O(n^3) constant should be zero.");
        assertEquals(0.0, record.getStrassenConstant(), 1e-6, "Strassen O(n^2.81) constant should be zero.");
    }

    @Test
    void testVeryLargeMatrixSizes() {
        // Edge case for large matrix sizes
        PerformanceRecord record = new PerformanceRecord(
                1024, 2000, 100000000, 1500, 70000000, 0.0001, 0.00005
        );

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
        PerformanceRecord record = new PerformanceRecord(
                64, 500, 10000, 350, 7500, 0.003, 0.0015
        );

        // Expected string format
        String expected = String.format("Size: 64 | Naive Time: 500 ms | Strassen Time: 350 ms | " +
                        "Naive Multiplications: 10000 | Strassen Multiplications: 7500 | " +
                        "Naive O(n^3) Constant: %.6f | Strassen O(n^2.81) Constant: %.6f",
                0.003, 0.0015);

        assertEquals(expected, record.toString(), "toString() output format should match.");
    }
}
