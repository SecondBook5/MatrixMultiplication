package edu.jhu.algos.test.compare;

import edu.jhu.algos.compare.PerformanceRecord;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PerformanceRecord.
 * Ensures that matrix size, execution time, and multiplication counts
 * are correctly stored and retrieved.
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

        // Create PerformanceRecord instance
        PerformanceRecord record = new PerformanceRecord(
                expectedSize, expectedNaiveTime, expectedStrassenTime,
                expectedNaiveCount, expectedStrassenCount
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
        PerformanceRecord record = new PerformanceRecord(64, 200, 150, 50000, 35000);

        // Retrieve values
        int originalSize = record.getSize();
        long originalNaiveTime = record.getNaiveTimeMs();
        long originalStrassenTime = record.getStrassenTimeMs();
        long originalNaiveCount = record.getNaiveMultiplications();
        long originalStrassenCount = record.getStrassenMultiplications();

        // Ensure values are immutable (do not change after retrieval)
        assertEquals(64, originalSize);
        assertEquals(200, originalNaiveTime);
        assertEquals(150, originalStrassenTime);
        assertEquals(50000, originalNaiveCount);
        assertEquals(35000, originalStrassenCount);
    }

    @Test
    void testZeroMultiplications() {
        // Test edge case where multiplications are zero (e.g., multiplying by zero matrix)
        PerformanceRecord record = new PerformanceRecord(32, 10, 15, 0, 0);

        assertEquals(0, record.getNaiveMultiplications(), "Naive multiplications should be zero.");
        assertEquals(0, record.getStrassenMultiplications(), "Strassen multiplications should be zero.");
    }

    @Test
    void testVeryLargeMatrixSizes() {
        // Edge case for large matrix sizes
        PerformanceRecord record = new PerformanceRecord(1024, 2000, 1500, 100000000, 70000000);

        assertEquals(1024, record.getSize(), "Matrix size should match large input.");
        assertEquals(2000, record.getNaiveTimeMs(), "Naive execution time should match.");
        assertEquals(1500, record.getStrassenTimeMs(), "Strassen execution time should match.");
        assertEquals(100000000, record.getNaiveMultiplications(), "Naive multiplications should match large input.");
        assertEquals(70000000, record.getStrassenMultiplications(), "Strassen multiplications should match large input.");
    }

    @Test
    void testToStringFormat() {
        // Create a test record
        PerformanceRecord record = new PerformanceRecord(64, 500, 350, 10000, 7500);

        // Expected string format
        String expected = "Size: 64 | Naive Time: 500 ms | Strassen Time: 350 ms | " +
                "Naive Multiplications: 10000 | Strassen Multiplications: 7500";

        assertEquals(expected, record.toString(), "toString() output format should match.");
    }
}
