package edu.jhu.algos.test.utils;

import edu.jhu.algos.utils.PerformanceMetrics;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the PerformanceMetrics class.
 * Ensures correct handling of timing, multiplication counting,
 * and invalid usage scenarios.
 */
class PerformanceMetricsTest {

    /**
     * Tests startTimer() and stopTimer() usage,
     * verifying that elapsed time is captured.
     */
    @Test
    void testTimingValidUsage() {
        PerformanceMetrics metrics = new PerformanceMetrics();

        // Call startTimer() before the operation
        metrics.startTimer();

        // Simulate some short operation
        for (int i = 0; i < 1000000; i++) {
            // Doing some trivial loop to use a little time
        }

        // Stop timer
        metrics.stopTimer();

        // Check that getElapsedTimeMs() is > 0
        // since we definitely spent some time in the loop
        assertTrue(metrics.getElapsedTimeMs() > 0,
                "Elapsed time should be greater than zero after a small loop.");
    }

    /**
     * Tests that calling stopTimer() without startTimer()
     * throws an IllegalStateException.
     */
    @Test
    void testStopTimerWithoutStart() {
        PerformanceMetrics metrics = new PerformanceMetrics();
        // We do NOT call metrics.startTimer() here

        // Expect an IllegalStateException
        Exception ex = assertThrows(IllegalStateException.class, metrics::stopTimer);
        assertTrue(ex.getMessage().contains("stopTimer() invoked without a valid startTimer()"),
                "Expected error message about stopTimer() being called prematurely.");
    }

    /**
     * Tests incrementMultiplicationCount() and getMultiplicationCount().
     */
    @Test
    void testIncrementMultiplicationCount() {
        PerformanceMetrics metrics = new PerformanceMetrics();

        // Initially should be 0
        assertEquals(0, metrics.getMultiplicationCount(),
                "Initial multiplication count should be 0.");

        // Increment by 1
        metrics.incrementMultiplicationCount();
        assertEquals(1, metrics.getMultiplicationCount(),
                "After one increment, count should be 1.");

        // Increment multiple times
        for (int i = 0; i < 9; i++) {
            metrics.incrementMultiplicationCount();
        }
        // Now total should be 10
        assertEquals(10, metrics.getMultiplicationCount(),
                "After 10 increments, total count should be 10.");
    }

    /**
     * Tests addMultiplications(long amount) with a positive amount.
     */
    @Test
    void testAddMultiplicationsValid() {
        PerformanceMetrics metrics = new PerformanceMetrics();

        // Add 5 multiplications
        metrics.addMultiplications(5);
        assertEquals(5, metrics.getMultiplicationCount(),
                "Multiplication count should be 5 after adding 5.");

        // Add 10 more
        metrics.addMultiplications(10);
        assertEquals(15, metrics.getMultiplicationCount(),
                "Multiplication count should be 15 after adding 10 more.");
    }

    /**
     * Tests addMultiplications(long amount) with a negative amount,
     * expecting an IllegalArgumentException.
     */
    @Test
    void testAddMultiplicationsInvalid() {
        PerformanceMetrics metrics = new PerformanceMetrics();

        // Attempt to add negative number
        Exception ex = assertThrows(IllegalArgumentException.class, () -> metrics.addMultiplications(-1));
        assertTrue(ex.getMessage().contains("Cannot add a negative"),
                "Expected error message about negative multiplications.");
    }

    /**
     * Tests resetMultiplicationCount() to ensure it clears the count.
     */
    @Test
    void testResetMultiplicationCount() {
        PerformanceMetrics metrics = new PerformanceMetrics();
        // Increment some
        metrics.addMultiplications(10);
        assertEquals(10, metrics.getMultiplicationCount(), "Count should be 10.");

        // Now reset just the multiplication count
        metrics.resetMultiplicationCount();
        assertEquals(0, metrics.getMultiplicationCount(), "Count should be reset to 0.");
    }

    /**
     * Tests resetAll() to ensure it resets both the time and multiplication count.
     */
    @Test
    void testResetAll() {
        PerformanceMetrics metrics = new PerformanceMetrics();

        // 1) Start/Stop timer
        metrics.startTimer();
        metrics.stopTimer(); // Should record some time
        long timeMs = metrics.getElapsedTimeMs();
        assertTrue(timeMs >= 0, "Time should be captured here.");

        // 2) Add some multiplication count
        metrics.addMultiplications(5);

        // Now reset all
        metrics.resetAll();

        // Timer should be 0 => so getElapsedTimeMs() should be 0
        assertEquals(0, metrics.getElapsedTimeMs(), "Time should be reset to 0 after resetAll().");

        // Multiplication count should be 0
        assertEquals(0, metrics.getMultiplicationCount(), "Multiplication count should be reset to 0.");
    }

    /**
     * Tests that if we never call stopTimer(), getElapsedTimeMs() returns 0.
     */
    @Test
    void testNoStopTimerTime() {
        PerformanceMetrics metrics = new PerformanceMetrics();
        metrics.startTimer(); // no stopTimer() call
        assertEquals(0, metrics.getElapsedTimeMs(),
                "Elapsed time should be 0 if stopTimer() was never called.");
    }
}
