package edu.jhu.algos.test.utils;

import edu.jhu.algos.utils.PerformanceMetrics;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the PerformanceMetrics class.
 *
 * Ensures:
 * - Correct initialization of metrics.
 * - Proper updates to multiplication count and execution time.
 * - Accurate retrieval of performance data.
 * - Correct behavior of incrementing multiplication count.
 */
public class PerformanceMetricsTest {

    /**
     * Tests the default initialization of PerformanceMetrics.
     * Expected: multiplicationCount = 0, executionTime = 0.0
     */
    @Test
    void testInitialization() {
        PerformanceMetrics metrics = new PerformanceMetrics();
        assertEquals(0, metrics.getMultiplicationCount(), "Multiplication count should be initialized to 0.");
        assertEquals(0.0, metrics.getExecutionTime(), "Execution time should be initialized to 0.0 ms.");
    }

    /**
     * Tests updating performance metrics and verifies correct storage.
     */
    @Test
    void testUpdateMetrics() {
        PerformanceMetrics metrics = new PerformanceMetrics();
        metrics.updateMetrics(120, 5.67); // Update values

        assertEquals(120, metrics.getMultiplicationCount(), "Multiplication count should be updated correctly.");
        assertEquals(5.67, metrics.getExecutionTime(), 0.001, "Execution time should be updated correctly.");
    }

    /**
     * Tests retrieving multiplication count after multiple updates.
     */
    @Test
    void testGetMultiplicationCount() {
        PerformanceMetrics metrics = new PerformanceMetrics();
        metrics.updateMetrics(250, 10.25);
        assertEquals(250, metrics.getMultiplicationCount(), "Multiplication count should return the latest update.");
    }

    /**
     * Tests retrieving execution time after multiple updates.
     */
    @Test
    void testGetExecutionTime() {
        PerformanceMetrics metrics = new PerformanceMetrics();
        metrics.updateMetrics(300, 15.88);
        assertEquals(15.88, metrics.getExecutionTime(), 0.001, "Execution time should return the latest update.");
    }

    /**
     * Tests printing performance results to the console.
     * (This ensures no exceptions occur during print operation)
     */
    @Test
    void testPrintPerformanceResults() {
        PerformanceMetrics metrics = new PerformanceMetrics();
        metrics.updateMetrics(400, 20.42);

        // Expecting no exception when calling the print method
        assertDoesNotThrow(() -> metrics.printPerformanceResults("Naive Algorithm"));
    }

    /**
     * Tests incrementing multiplication count.
     * Ensures that the count increases correctly.
     */
    @Test
    void testIncrementMultiplicationCount() {
        PerformanceMetrics metrics = new PerformanceMetrics();

        // Increment count multiple times
        metrics.incrementMultiplicationCount();
        metrics.incrementMultiplicationCount();
        metrics.incrementMultiplicationCount();

        assertEquals(3, metrics.getMultiplicationCount(), "Multiplication count should correctly increment.");
    }

    /**
     * Tests updating only execution time without affecting multiplication count.
     */
    @Test
    void testUpdateExecutionTime() {
        PerformanceMetrics metrics = new PerformanceMetrics();
        metrics.updateMetrics(100, 5.0); // Initial values

        // Update only execution time
        metrics.updateExecutionTime(10.5);

        assertEquals(100, metrics.getMultiplicationCount(), "Multiplication count should remain unchanged.");
        assertEquals(10.5, metrics.getExecutionTime(), 0.001, "Execution time should be updated correctly.");
    }
}
