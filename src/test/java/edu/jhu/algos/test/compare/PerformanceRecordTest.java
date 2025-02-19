package edu.jhu.algos.test.compare;

import edu.jhu.algos.compare.PerformanceRecord;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PerformanceRecord.
 * Ensures that matrix size, execution time, and multiplication counts are correctly stored and retrieved.
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

        // Corrected parameter order
        PerformanceRecord record = new PerformanceRecord(
                expectedSize,
                expectedNaiveTime, expectedNaiveCount,
                expectedStrassenTime, expectedStrassenCount
        );

        // Validate stored values match expected values
        assertEquals(expectedSize, record.getSize(), "Matrix size should match expected value.");
        assertEquals(expectedNaiveTime, record.getNaiveTimeMs(), "Naive time should match expected value.");
        assertEquals(expectedStrassenTime, record.getStrassenTimeMs(), "Strassen time should match expected value.");
        assertEquals(expectedNaiveCount, record.getNaiveMultiplications(), "Naive multiplication count should match expected value.");
        assertEquals(expectedStrassenCount, record.getStrassenMultiplications(), "Strassen multiplication count should match expected value.");
    }

    @Test
    void testImmutability() {
        // Create a record with initial values
        PerformanceRecord record = new PerformanceRecord(
                64, 200, 50000, 150, 35000
        );

        // Retrieve values
        assertEquals(64, record.getSize());
        assertEquals(200, record.getNaiveTimeMs());
        assertEquals(150, record.getStrassenTimeMs());
        assertEquals(50000, record.getNaiveMultiplications());
        assertEquals(35000, record.getStrassenMultiplications());
    }

    @Test
    void testZeroMultiplications() {
        // Test edge case where multiplications are zero
        PerformanceRecord record = new PerformanceRecord(
                32, 10, 0, 15, 0
        );

        assertEquals(0, record.getNaiveMultiplications(), "Naive multiplications should be zero.");
        assertEquals(0, record.getStrassenMultiplications(), "Strassen multiplications should be zero.");
    }

    @Test
    void testVeryLargeMatrixSizes() {
        // Edge case for large matrix sizes
        PerformanceRecord record = new PerformanceRecord(
                1024, 2000, 100000000, 1500, 70000000
        );

        assertEquals(1024, record.getSize(), "Matrix size should match large input.");
        assertEquals(2000, record.getNaiveTimeMs(), "Naive execution time should match.");
        assertEquals(1500, record.getStrassenTimeMs(), "Strassen execution time should match.");
        assertEquals(100000000, record.getNaiveMultiplications(), "Naive multiplications should match large input.");
        assertEquals(70000000, record.getStrassenMultiplications(), "Strassen multiplications should match large input.");
    }

    @Test
    void testToStringFormat() {
        // Create a test record
        PerformanceRecord record = new PerformanceRecord(
                64, 500, 10000, 350, 7500
        );

        // Expected string format
        String expected = String.format("Size: 64 | Naive Time: 500 ms | Strassen Time: 350 ms | " +
                "Naive Multiplications: 10000 | Strassen Multiplications: 7500");

        assertEquals(expected, record.toString(), "toString() output format should match.");
    }
}
