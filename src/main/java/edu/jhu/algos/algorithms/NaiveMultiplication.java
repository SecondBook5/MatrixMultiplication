package edu.jhu.algos.algorithms;

import edu.jhu.algos.models.Matrix;
import edu.jhu.algos.utils.PerformanceMetrics;
import edu.jhu.algos.utils.MatrixValidator;
import edu.jhu.algos.utils.DebugConfig; // Import DebugConfig

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
    private final PerformanceMetrics metrics; // Each instance has its own metrics

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
        // Validate input matrices
        if (!MatrixValidator.isSameSize(A, B)) {
            throw new IllegalArgumentException("Matrices must be the same size for naive multiplication.");
        }

        // Reset all metrics: time and multiplication count
        DebugConfig.log("Resetting metrics before multiplication...");
        metrics.resetAll();
        DebugConfig.log("Multiplication count after reset: " + metrics.getMultiplicationCount());

        metrics.startTimer(); // Start timing

        int n = A.getSize();             // The dimension of the matrices
        Matrix result = new Matrix(n);   // Prepare an empty matrix of size n x n

        // Triple nested loop for naive O(n^3) multiplication
        for (int i = 0; i < n; i++) {               // Rows of A
            for (int j = 0; j < n; j++) {           // Columns of B
                int sum = 0;                        // Accumulate A[i,k]*B[k,j]
                for (int k = 0; k < n; k++) {       // Loop over 'k'
                    int aVal = A.get(i, k);        // Cache A[i, k]
                    int bVal = B.get(k, j);        // Cache B[k, j]
                    sum += aVal * bVal;            // Multiply and sum
                    metrics.incrementMultiplicationCount(); // Track scalar multiplication

                    // Debugging: Print after every multiplication
                    DebugConfig.log("Multiplication (" + i + ", " + j + ", " + k + ") | A: " + aVal + " * B: " + bVal +
                            " | Total Count: " + metrics.getMultiplicationCount());
                }
                result.set(i, j, sum); // Store computed sum
            }
        }

        metrics.stopTimer(); // Stop timing

        // Debugging: Final multiplication count check
        DebugConfig.log("Final Multiplication Count: " + metrics.getMultiplicationCount());
        DebugConfig.log("Total Time: " + metrics.getElapsedTimeMs() + " ms");

        return result;
    }

    /**
     * Retrieves the total number of scalar multiplications performed
     * in the last multiply() operation.
     * @return The multiplication count.
     */
    @Override
    public long getMultiplicationCount() {
        long count = metrics.getMultiplicationCount();
        DebugConfig.log("Retrieving Multiplication Count: " + count);
        return count;
    }

    /**
     * Retrieves the elapsed time (in ms) for the last multiply() call.
     * @return The time in milliseconds.
     */
    @Override
    public long getElapsedTimeMs() {
        long time = metrics.getElapsedTimeMs();
        DebugConfig.log("Retrieving Elapsed Time: " + time + " ms");
        return time;
    }
}
