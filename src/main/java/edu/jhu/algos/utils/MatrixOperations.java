package edu.jhu.algos.utils;

import edu.jhu.algos.models.Matrix;

/**
 * Utility class for optimized matrix operations.
 * - Provides methods for matrix addition, subtraction, splitting, and merging.
 * - Uses `Matrix.java` methods to ensure efficiency and avoid redundancy.
 * - Fully supports method chaining.
 */
public final class MatrixOperations {

    /**
     * Adds two matrices element-wise.
     * - Uses the `add()` method from `Matrix.java` for optimized computation.
     * - Supports method chaining.
     *
     * @param A First matrix.
     * @param B Second matrix.
     * @return Sum matrix (A + B).
     * @throws IllegalArgumentException If matrices have mismatched dimensions.
     */
    public static Matrix add(Matrix A, Matrix B) {
        return A.add(B);  // Directly calls Matrix.java's built-in `add` method
    }

    /**
     * Subtracts one matrix from another element-wise.
     * - Uses the `subtract()` method from `Matrix.java` for optimized computation.
     * - Supports method chaining.
     *
     * @param A First matrix.
     * @param B Second matrix.
     * @return Difference matrix (A - B).
     * @throws IllegalArgumentException If matrices have mismatched dimensions.
     */
    public static Matrix subtract(Matrix A, Matrix B) {
        return A.subtract(B);  // Directly calls Matrix.java's built-in `subtract` method
    }

    /**
     * Extracts a submatrix from the source matrix.
     * - Used in divide-and-conquer algorithms like Strassen’s.
     *
     * @param source The original matrix.
     * @param rowOffset Row starting index.
     * @param colOffset Column starting index.
     * @param newSize Size of the submatrix.
     * @return Extracted submatrix.
     * @throws IllegalArgumentException If the requested submatrix is out of bounds.
     */
    public static Matrix split(Matrix source, int rowOffset, int colOffset, int newSize) {
        double[][] sourceData = source.retrieveRowMajorAs2D();
        double[][] subMatrix = new double[newSize][newSize];

        try {
            for (int i = 0; i < newSize; i++) {
                System.arraycopy(sourceData[i + rowOffset], colOffset, subMatrix[i], 0, newSize);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Matrix split failed: Requested submatrix is out of bounds.", e);
        }

        return new Matrix(newSize, subMatrix);
    }

    /**
     * Merges a submatrix into a larger matrix at a given offset.
     * - Used for combining submatrices in Strassen’s Algorithm.
     *
     * @param target Target matrix to merge into.
     * @param source Submatrix to merge.
     * @param rowOffset Row index to start merging.
     * @param colOffset Column index to start merging.
     * @throws IllegalArgumentException If the submatrix exceeds the bounds of the target matrix.
     */
    public static void merge(Matrix target, Matrix source, int rowOffset, int colOffset) {
        double[][] targetData = target.retrieveRowMajorAs2D();
        double[][] sourceData = source.retrieveRowMajorAs2D();

        try {
            for (int i = 0; i < source.getSize(); i++) {
                System.arraycopy(sourceData[i], 0, targetData[i + rowOffset], colOffset, source.getSize());
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Matrix merge failed: Submatrix exceeds target matrix bounds.", e);
        }
    }
}
