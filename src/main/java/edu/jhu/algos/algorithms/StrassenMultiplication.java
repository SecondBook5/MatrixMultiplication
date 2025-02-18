package edu.jhu.algos.algorithms;

import edu.jhu.algos.models.Matrix;
import edu.jhu.algos.operations.MatrixOperations;
import edu.jhu.algos.utils.PerformanceMetrics;

/**
 * Implements Strassen's Algorithm for square matrices of size 2^n x 2^n.
 * <p>
 * The Strassen algorithm recursively splits each matrix into four submatrices,
 * computes 7 intermediate products (M1..M7), then merges them to form the result.
 * Compared to the naive O(n^3) approach, Strassen runs in O(n^(log2(7))) ~ O(n^2.81).
 * </p>
 * <p>
 * This class uses PerformanceMetrics to track:
 * 1) Scalar multiplication count,
 * 2) Elapsed time in milliseconds.
 * </p>
 */
public class StrassenMultiplication implements MatrixMultiplier {

    // PerformanceMetrics tracks time + multiplication count
    private final PerformanceMetrics metrics;

    /**
     * Default constructor initializes a fresh PerformanceMetrics object
     * for each StrassenMultiplication instance.
     */
    public StrassenMultiplication() {
        this.metrics = new PerformanceMetrics();
    }

    /**
     * Multiplies two matrices A and B using Strassen's Algorithm.
     * @param A The first matrix (2^n x 2^n).
     * @param B The second matrix (2^n x 2^n).
     * @return A new Matrix containing A x B.
     * @throws IllegalArgumentException if A and B are not the same size.
     */
    @Override
    public Matrix multiply(Matrix A, Matrix B) {
        // Ensure both matrices have the same dimensions
        if (A.getSize() != B.getSize()) {
            throw new IllegalArgumentException("Matrices must be the same size for Strassen multiplication.");
        }

        // Reset and start performance tracking
        metrics.resetAll();
        metrics.startTimer();

        // Recursively do the Strassen algorithm
        Matrix result = strassenRecursive(A, B);

        // Stop timing
        metrics.stopTimer();
        return result;
    }

    /**
     * Retrieves the number of scalar multiplications performed
     * during the last multiply() call.
     * @return The multiplication count from PerformanceMetrics.
     */
    @Override
    public long getMultiplicationCount() {
        return metrics.getMultiplicationCount();
    }

    /**
     * Retrieves the time in milliseconds for the last multiply() call.
     * @return The elapsed time in ms from PerformanceMetrics.
     */
    @Override
    public long getElapsedTimeMs() {
        return metrics.getElapsedTimeMs();
    }

    /**
     * Recursively computes the product of two matrices A and B
     * using Strassen's divide-and-conquer approach.
     * @param A The first matrix.
     * @param B The second matrix.
     * @return The product matrix of A x B.
     */
    private Matrix strassenRecursive(Matrix A, Matrix B) {
        int n = A.getSize();

        // Base case: If matrix is 1x1, just do the scalar multiply
        if (n == 1) {
            // A.get(0,0) * B.get(0,0)
            int val = A.get(0, 0) * B.get(0, 0);
            // Count this scalar multiplication
            metrics.incrementMultiplicationCount();

            // Build a 1x1 result matrix
            Matrix singleCell = new Matrix(1);
            singleCell.set(0, 0, val);
            return singleCell;
        }

        // Split each matrix into four submatrices: A11, A12, A21, A22 and B11, B12, B21, B22
        Matrix[] AParts = MatrixOperations.split(A);
        Matrix[] BParts = MatrixOperations.split(B);

        // Extract submatrices for clarity
        Matrix A11 = AParts[0]; // top-left
        Matrix A12 = AParts[1]; // top-right
        Matrix A21 = AParts[2]; // bottom-left
        Matrix A22 = AParts[3]; // bottom-right

        Matrix B11 = BParts[0]; // top-left
        Matrix B12 = BParts[1]; // top-right
        Matrix B21 = BParts[2]; // bottom-left
        Matrix B22 = BParts[3]; // bottom-right

        // According to Strassen's formula:
        // M1 = Strassen((A11 + A22), (B11 + B22))
        // M2 = Strassen((A21 + A22), B11)
        // M3 = Strassen(A11, (B12 - B22))
        // M4 = Strassen(A22, (B21 - B11))
        // M5 = Strassen((A11 + A12), B22)
        // M6 = Strassen((A21 - A11), (B11 + B12))
        // M7 = Strassen((A12 - A22), (B21 + B22))

        Matrix M1 = strassenRecursive(
                MatrixOperations.add(A11, A22),
                MatrixOperations.add(B11, B22)
        );

        Matrix M2 = strassenRecursive(
                MatrixOperations.add(A21, A22),
                B11
        );

        Matrix M3 = strassenRecursive(
                A11,
                MatrixOperations.subtract(B12, B22)
        );

        Matrix M4 = strassenRecursive(
                A22,
                MatrixOperations.subtract(B21, B11)
        );

        Matrix M5 = strassenRecursive(
                MatrixOperations.add(A11, A12),
                B22
        );

        Matrix M6 = strassenRecursive(
                MatrixOperations.subtract(A21, A11),
                MatrixOperations.add(B11, B12)
        );

        Matrix M7 = strassenRecursive(
                MatrixOperations.subtract(A12, A22),
                MatrixOperations.add(B21, B22)
        );

        // Now compute the resulting 4 submatrices of C
        // According to Strassen's combination formula:
        // C11 = M1 + M4 - M5 + M7
        // C12 = M3 + M5
        // C21 = M2 + M4
        // C22 = M1 + M3 - M2 + M6

        Matrix C11 = MatrixOperations.add(
                MatrixOperations.subtract(
                        MatrixOperations.add(M1, M4),
                        M5
                ),
                M7
        );

        Matrix C12 = MatrixOperations.add(M3, M5);
        Matrix C21 = MatrixOperations.add(M2, M4);

        Matrix C22 = MatrixOperations.add(
                MatrixOperations.subtract(
                        MatrixOperations.add(M1, M3),
                        M2
                ),
                M6
        );

        // Merge submatrices back into a single n x n matrix
        Matrix result = MatrixOperations.merge(C11, C12, C21, C22);
        return result;
    }
}
