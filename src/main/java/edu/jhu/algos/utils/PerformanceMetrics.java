package edu.jhu.algos.utils;

/**
 * A utility class to measure performance data for matrix operations,
 * including timing and counting multiplications.
 * <p>
 * The class is designed to be used by any matrix multiplication algorithm
 * (e.g., Naive, Strassen), so that you can consistently track the number
 * of multiplications and the elapsed time in milliseconds.
 * </p>
 */
public class PerformanceMetrics {

    // Stores the system time (in nanoseconds) when the operation starts.
    private long startTime;

    // Stores the system time (in nanoseconds) when the operation ends.
    private long endTime;

    // Tracks the total count of multiplications performed in the operation.
    private long multiplicationCount;

    /**
     * Default constructor initializes all fields to zero.
     * The user should call startTimer() before an operation,
     * incrementMultiplicationCount() during the operation,
     * and stopTimer() after the operation is complete.
     */
    public PerformanceMetrics() {
        // Set everything to 0 by default
        this.startTime = 0;            // No start time yet
        this.endTime = 0;              // No end time yet
        this.multiplicationCount = 0;  // No multiplications counted yet
    }

    /**
     * Records the current system time as the start time for measuring an operation.
     * <p>
     * Must be called before the operation begins. If it is called again before
     * stopTimer(), the old start time is overwritten.
     * </p>
     */
    public void startTimer() {
        // Capture the current time in nanoseconds
        this.startTime = System.nanoTime();
    }

    /**
     * Records the current system time as the end time for measuring an operation.
     * <p>
     * Must be called after the operation is finished. If startTimer() was never
     * called, this method will throw an IllegalStateException.
     * </p>
     * @throws IllegalStateException if called before startTimer() was ever called.
     */
    public void stopTimer() {
        // Check if startTimer() was invoked
        if (startTime == 0) {
            throw new IllegalStateException("Error: stopTimer() invoked without a valid startTimer() call.");
        }
        // Capture the current time in nanoseconds
        this.endTime = System.nanoTime();
    }

    /**
     * Increments the multiplication counter by one.
     * <p>
     * Call this each time a single scalar multiplication is performed, for
     * example, every time you do A[i][k] * B[k][j].
     * </p>
     */
    public void incrementMultiplicationCount() {
        this.multiplicationCount++; // Increase count by 1
    }

    /**
     * Adds a specified amount to the current multiplication counter.
     * @param amount The number of multiplications to add.
     * @throws IllegalArgumentException If amount is negative.
     */
    public void addMultiplications(long amount) {
        // Validate that amount is not negative
        if (amount < 0) {
            throw new IllegalArgumentException("Error: Cannot add a negative number of multiplications.");
        }
        // Increase the multiplication count by the given amount
        this.multiplicationCount += amount;
    }

    /**
     * Retrieves the total multiplication count.
     * @return The total number of scalar multiplications recorded.
     */
    public long getMultiplicationCount() {
        return this.multiplicationCount; // Return the stored count
    }

    /**
     * Resets the multiplication count to zero.
     * <p>
     * This can be called if you want to reuse the same object for multiple
     * distinct operations without constructing a new PerformanceMetrics.
     * </p>
     */
    public void resetMultiplicationCount() {
        this.multiplicationCount = 0; // Reset to 0
    }

    /**
     * Retrieves the elapsed time in milliseconds between the last startTimer() and stopTimer() calls.
     * @return The time in milliseconds, or 0 if stopTimer() hasn't been called.
     */
    public long getElapsedTimeMs() {
        // If stopTimer() was never called, or startTimer() is 0, return 0
        if (this.startTime == 0 || this.endTime == 0) {
            return 0;
        }
        // Convert nanoseconds to milliseconds
        return (endTime - startTime) / 1_000_000;
    }

    /**
     * Resets all recorded performance data.
     * <p>
     * This sets the timer and multiplication counts back to zero,
     * allowing reuse of the same PerformanceMetrics instance.
     * </p>
     */
    public void resetAll() {
        // Reset both timers and the multiplication count
        this.startTime = 0;
        this.endTime = 0;
        this.multiplicationCount = 0;
    }
}
