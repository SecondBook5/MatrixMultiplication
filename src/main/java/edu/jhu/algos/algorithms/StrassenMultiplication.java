package edu.jhu.algos.algorithms;

import edu.jhu.algos.models.Matrix;
import edu.jhu.algos.operations.MatrixOperations;
import edu.jhu.algos.utils.PerformanceMetrics;
import edu.jhu.algos.utils.MatrixValidator;

/**
 * Implements Strassen's Algorithm for square matrix multiplication.
 * <p>
 * Strassen's Algorithm reduces the multiplication complexity from O(n^3) (Naive) to O(n^(log2(7))) ≈ O(n^2.81).
 * It recursively splits the matrices into submatrices, computes 7 intermediate matrices (M1..M7),
 * and then combines them to form the result.
 * <p>
 * **Key Properties:**
 * - Works only on square matrices of size 2^n × 2^n.
 * - More efficient than the naive approach for large matrices, but introduces recursive overhead.
 * <p>
 * **Performance Tracking:**
 * - The number of scalar multiplications performed.
 * - The total execution time in milliseconds.
 */
public class StrassenMultiplication implements MatrixMultiplier {

    private final PerformanceMetrics metrics;  // Tracks execution time and multiplication count

    /**
     * Default constructor initializes a fresh PerformanceMetrics object
     * for each StrassenMultiplication instance.
     */
    public StrassenMultiplication() {
        this.metrics = new PerformanceMetrics();
    }

    /**
     * Multiplies two matrices A and B using Strassen's Algorithm.
     *
     * @param A The first matrix (must be square and a power of 2).
     * @param B The second matrix (must be square and a power of 2).
     * @return A new Matrix containing A × B.
     * @throws IllegalArgumentException if A and B are not the same size.
     */
    @Override
    public Matrix multiply(Matrix A, Matrix B) {
        if (A.getSize() != B.getSize()) {  // Ensure both matrices have the same dimensions
            throw new IllegalArgumentException("Matrices must be the same size for Strassen multiplication.");
        }

        metrics.resetAll();  // Reset multiplication count and execution time
        metrics.startTimer();  // Start timing the multiplication process

        // Check for zero matrices to avoid unnecessary computation
        if (MatrixValidator.isZeroMatrix(A) || MatrixValidator.isZeroMatrix(B)) {
            metrics.stopTimer();
            return new Matrix(A.getSize());
        }

        Matrix result = strassenRecursive(A, B);  // Recursively compute the product

        metrics.stopTimer();  // Stop the timer after multiplication
        return result;
    }

    /**
     * Retrieves the number of scalar multiplications performed
     * during the last multiply() call.
     *
     * @return The multiplication count from PerformanceMetrics.
     */
    @Override
    public long getMultiplicationCount() {
        return metrics.getMultiplicationCount();
    }

    /**
     * Retrieves the time in milliseconds for the last multiply() call.
     *
     * @return The elapsed time in milliseconds from PerformanceMetrics.
     */
    @Override
    public long getElapsedTimeMs() {
        return metrics.getElapsedTimeMs();
    }

    /**
     * Recursively computes the product of two matrices A and B using Strassen's Algorithm.
     * <p>
     * This method follows the recursive breakdown of Strassen's approach:
     * 1. Split A and B into 4 submatrices each.
     * 2. Compute 7 intermediate matrices (M1..M7).
     * 3. Compute the final submatrices of C.
     * 4. Merge the submatrices into a full result matrix.
     *
     * @param A The first matrix.
     * @param B The second matrix.
     * @return The product matrix A × B.
     */
    private Matrix strassenRecursive(Matrix A, Matrix B) {
        int n = A.getSize();  // Get matrix dimension

        // Base case: Direct scalar multiplication for 1x1 matrix
        if (n == 1) {
            int val = A.get(0, 0) * B.get(0, 0);
            metrics.incrementMultiplicationCount();  // Count this multiplication

            Matrix singleCell = new Matrix(1);  // Create a 1x1 matrix for the result
            singleCell.set(0, 0, val);
            return singleCell;
        }

        // Step 1: Split matrices into four submatrices
        Matrix[] AParts = MatrixOperations.split(A);
        Matrix[] BParts = MatrixOperations.split(B);

        // Assign meaningful names to submatrices
        Matrix A11 = AParts[0], A12 = AParts[1], A21 = AParts[2], A22 = AParts[3];
        Matrix B11 = BParts[0], B12 = BParts[1], B21 = BParts[2], B22 = BParts[3];

        // Step 2: Compute 7 intermediary matrices using Strassen's formula
        Matrix M1 = strassenRecursive(MatrixOperations.add(A11, A22), MatrixOperations.add(B11, B22));
        Matrix M2 = strassenRecursive(MatrixOperations.add(A21, A22), B11);
        Matrix M3 = strassenRecursive(A11, MatrixOperations.subtract(B12, B22));
        Matrix M4 = strassenRecursive(A22, MatrixOperations.subtract(B21, B11));
        Matrix M5 = strassenRecursive(MatrixOperations.add(A11, A12), B22);
        Matrix M6 = strassenRecursive(MatrixOperations.subtract(A21, A11), MatrixOperations.add(B11, B12));
        Matrix M7 = strassenRecursive(MatrixOperations.subtract(A12, A22), MatrixOperations.add(B21, B22));

        // Step 3: Compute final submatrices of C

        // Compute C11 = M1 + M4 - M5 + M7
        Matrix temp1 = MatrixOperations.add(M1, M4);
        Matrix temp2 = MatrixOperations.subtract(temp1, M5);
        Matrix C11 = MatrixOperations.add(temp2, M7);

        // Compute C12 = M3 + M5
        Matrix C12 = MatrixOperations.add(M3, M5);

        // Compute C21 = M2 + M4
        Matrix C21 = MatrixOperations.add(M2, M4);

        // Compute C22 = M1 + M3 - M2 + M6
        Matrix temp3 = MatrixOperations.add(M1, M3);
        Matrix temp4 = MatrixOperations.subtract(temp3, M2);
        Matrix C22 = MatrixOperations.add(temp4, M6);

        // Step 4: Merge submatrices into the final n x n result matrix
        return MatrixOperations.merge(C11, C12, C21, C22);
    }
}
