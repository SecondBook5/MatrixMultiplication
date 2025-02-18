package edu.jhu.algos.algorithms;

import edu.jhu.algos.models.Matrix;
import edu.jhu.algos.utils.PerformanceMetrics;

/**
 * Implements the O(n³) naive matrix multiplication algorithm.
 * <p>
 * This class uses PerformanceMetrics to track:
 * 1) The total number of scalar multiplications,
 * 2) The execution time in milliseconds.
 * </p>
 */
public class NaiveMultiplication implements MatrixMultiplier {

    // PerformanceMetrics tracks both time and multiplication count
    private final PerformanceMetrics metrics; // Final so each instance has its own metrics

    /**
     * Default constructor initializes a new PerformanceMetrics object.
     */
    public NaiveMultiplication() {
        // Create a fresh PerformanceMetrics for each NaiveMultiplication instance
        this.metrics = new PerformanceMetrics();
    }

    /**
     * Multiplies two matrices A and B using triple nested loops (O(n^3)).
     * @param A The first matrix.
     * @param B The second matrix.
     * @return A new Matrix containing A x B.
     * @throws IllegalArgumentException if A and B have different sizes.
     */
    @Override
    public Matrix multiply(Matrix A, Matrix B) {
        // Validate that A and B have the same dimension
        if (A.getSize() != B.getSize()) {
            throw new IllegalArgumentException("Matrices must be the same size for naive multiplication.");
        }

        // Reset all metrics: time and multiplication count
        metrics.resetAll();
        // Start timing
        metrics.startTimer();

        int n = A.getSize();             // The dimension of the matrices
        Matrix result = new Matrix(n);   // Prepare an empty matrix of size n x n

        // Triple nested loop for naive O(n^3) multiplication
        for (int i = 0; i < n; i++) {                 // Loop over rows of A
            for (int j = 0; j < n; j++) {             // Loop over columns of B
                int sum = 0;                          // Accumulate A[i,k]*B[k,j]
                for (int k = 0; k < n; k++) {         // Loop over 'k'
                    sum += A.get(i, k) * B.get(k, j); // Multiply A[i,k]*B[k,j]
                    metrics.incrementMultiplicationCount(); // Count each scalar multiplication
                }
                result.set(i, j, sum); // Place the computed sum into the result matrix
            }
        }

        // Stop timing
        metrics.stopTimer();
        // Return the final result matrix
        return result;
    }

    /**
     * Retrieves the total number of scalar multiplications performed
     * in the last multiply() operation.
     * @return The multiplication count.
     */
    @Override
    public long getMultiplicationCount() {
        return metrics.getMultiplicationCount();
    }

    /**
     * Retrieves the elapsed time (in ms) for the last multiply() call.
     * @return The time in milliseconds.
     */
    @Override
    public long getElapsedTimeMs() {
        return metrics.getElapsedTimeMs();
    }
}
