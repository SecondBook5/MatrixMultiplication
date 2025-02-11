package edu.jhu.algos.utils;

import edu.jhu.algos.models.Matrix;

/**
 * Utility class for matrix operations used in Strassen’s Algorithm.
 * - Provides methods for matrix addition & subtraction.
 * - Supports efficient matrix splitting & merging for divide-and-conquer algorithms.
 * - Fully integrates with Matrix.java, reducing redundant code.
 */
public class MatrixOperations {

    /**
     * Adds two matrices element-wise using Matrix's built-in method.
     * @param A First matrix.
     * @param B Second matrix.
     * @return Sum matrix (A + B).
     */
    public static Matrix add(Matrix A, Matrix B) {
        return A.add(B);  // Uses Matrix.java's internal method
    }

    /**
     * Subtracts one matrix from another using Matrix's built-in method.
     * @param A First matrix.
     * @param B Second matrix.
     * @return Difference matrix (A - B).
     */
    public static Matrix subtract(Matrix A, Matrix B) {
        return A.subtract(B);  // Uses Matrix.java's internal method
    }

    /**
     * Splits a source matrix into a submatrix.
     * @param source The original matrix.
     * @param rowOffset Row starting index.
     * @param colOffset Column starting index.
     * @param newSize Size of the submatrix.
     * @return Extracted submatrix.
     */
    public static Matrix split(Matrix source, int rowOffset, int colOffset, int newSize) {
        double[][] sourceData = source.retrieveRowMajorAs2D();
        double[][] subMatrix = new double[newSize][newSize];

        for (int i = 0; i < newSize; i++) {
            System.arraycopy(sourceData[i + rowOffset], colOffset, subMatrix[i], 0, newSize);
        }

        return new Matrix(newSize, subMatrix);
    }

    /**
     * Merges a submatrix into a larger matrix at a given offset.
     * @param target Target matrix to merge into.
     * @param source Submatrix to merge.
     * @param rowOffset Row index to start merging.
     * @param colOffset Column index to start merging.
     */
    public static void merge(Matrix target, Matrix source, int rowOffset, int colOffset) {
        double[][] targetData = target.retrieveRowMajorAs2D();
        double[][] sourceData = source.retrieveRowMajorAs2D();

        for (int i = 0; i < source.getSize(); i++) {
            System.arraycopy(sourceData[i], 0, targetData[i + rowOffset], colOffset, source.getSize());
        }
    }
}
