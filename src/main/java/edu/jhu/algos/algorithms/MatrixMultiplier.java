package edu.jhu.algos.algorithms;

import edu.jhu.algos.models.Matrix;

/**
 * Provides a contract for multiplying two square matrices of size 2^n x 2^n.
 * <p>
 * Implementations (e.g., NaiveMultiplication, StrassenMultiplication) must:
 * 1) Perform the multiplication in their preferred manner,
 * 2) Track the scalar multiplication count,
 * 3) Track the elapsed time of the multiply() call.
 * </p>
 */
public interface MatrixMultiplier {

    /**
     * Multiplies two matrices A and B and returns the resulting matrix.
     * @param A The first matrix (2^n x 2^n).
     * @param B The second matrix (2^n x 2^n).
     * @return The product matrix A x B.
     * @throws IllegalArgumentException if the sizes of A and B don't match
     *         or if they are invalid for multiplication.
     */
    Matrix multiply(Matrix A, Matrix B);

    /**
     * Retrieves the total number of scalar multiplications performed
     * during the most recent multiply() operation.
     * @return The multiplication count from the last multiply() call.
     */
    long getMultiplicationCount();

    /**
     * Retrieves the elapsed time (in milliseconds) spent in the most recent
     * multiply() operation.
     * @return The elapsed time in ms from the last multiply() call.
     */
    long getElapsedTimeMs();
}
