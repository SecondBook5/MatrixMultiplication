package edu.jhu.algos.operations;

import edu.jhu.algos.models.Matrix;
import edu.jhu.algos.utils.MatrixValidator;

/**
 * Provides core matrix operations such as addition, subtraction,
 * splitting (for submatrices), and merging of submatrices.
 */
public class MatrixOperations {

    /**
     * Adds two matrices A and B of the same size.
     * @param A First matrix.
     * @param B Second matrix.
     * @return A new Matrix containing the sum of A and B.
     * @throws IllegalArgumentException if matrices are not the same size.
     */
    public static Matrix add(Matrix A, Matrix B) {
        try {
            // Check if A and B have the same size
            if (!MatrixValidator.isSameSize(A, B)) {
                throw new IllegalArgumentException("Matrices must be the same size to add.");
            }

            // Retrieve dimensions from A (and B)
            int size = A.getSize();

            // Create a new matrix to store the result
            Matrix result = new Matrix(size);

            // Loop through each cell in the matrix
            for (int i = 0; i < size; i++) { // Row index
                for (int j = 0; j < size; j++) { // Column index
                    // Sum corresponding values from A and B
                    int sum = A.get(i, j) + B.get(i, j);
                    // Store the result in the new matrix
                    result.set(i, j, sum);
                }
            }

            // Return the resulting matrix
            return result;

        } catch (IllegalArgumentException e) {
            // Throw an IllegalArgumentException if sizes differ or other errors occur
            throw new IllegalArgumentException("Error in add(): " + e.getMessage());
        }
    }

    /**
     * Subtracts matrix B from matrix A (A - B).
     * @param A First matrix (minuend).
     * @param B Second matrix (subtrahend).
     * @return A new Matrix containing the difference (A - B).
     * @throws IllegalArgumentException if matrices are not the same size.
     */
    public static Matrix subtract(Matrix A, Matrix B) {
        try {
            // Check if A and B have the same size
            if (!MatrixValidator.isSameSize(A, B)) {
                throw new IllegalArgumentException("Matrices must be the same size to subtract.");
            }

            // Retrieve dimensions from A (and B)
            int size = A.getSize();

            // Create a new matrix to store the result
            Matrix result = new Matrix(size);

            // Loop through each cell in the matrix
            for (int i = 0; i < size; i++) { // Row index
                for (int j = 0; j < size; j++) { // Column index
                    // Subtract corresponding values from A and B
                    int difference = A.get(i, j) - B.get(i, j);
                    // Store the result in the new matrix
                    result.set(i, j, difference);
                }
            }

            // Return the resulting matrix
            return result;

        } catch (IllegalArgumentException e) {
            // Throw an IllegalArgumentException if sizes differ or other errors occur
            throw new IllegalArgumentException("Error in subtract(): " + e.getMessage());
        }
    }

    /**
     * Splits a matrix into four submatrices (A11, A12, A21, A22).
     * Commonly used in Strassen's Algorithm for matrix multiplication.
     * @param original The original matrix to split.
     * @return An array of four Matrices: [A11, A12, A21, A22].
     * @throws IllegalArgumentException if the matrix size is not divisible by 2.
     */
    public static Matrix[] split(Matrix original) {
        try {
            // Check if matrix is at least size 2 (so we can split)
            int n = original.getSize();

            // If n=1, can't split meaningfully
            if (n < 2) {
                throw new IllegalArgumentException("Matrix too small to split (must be at least 2x2).");
            }

            // halfSize is the dimension of each submatrix
            int halfSize = n / 2;

            // Create four submatrices
            Matrix A11 = new Matrix(halfSize);
            Matrix A12 = new Matrix(halfSize);
            Matrix A21 = new Matrix(halfSize);
            Matrix A22 = new Matrix(halfSize);

            // Populate each submatrix from original
            for (int i = 0; i < halfSize; i++) { // Rows in submatrix
                for (int j = 0; j < halfSize; j++) { // Cols in submatrix

                    // A11 -> top-left
                    A11.set(i, j, original.get(i, j));

                    // A12 -> top-right
                    A12.set(i, j, original.get(i, j + halfSize));

                    // A21 -> bottom-left
                    A21.set(i, j, original.get(i + halfSize, j));

                    // A22 -> bottom-right
                    A22.set(i, j, original.get(i + halfSize, j + halfSize));
                }
            }

            // Return array of submatrices
            return new Matrix[]{ A11, A12, A21, A22 };

        } catch (IllegalArgumentException e) {
            // Throw if matrix is too small or other errors occur
            throw new IllegalArgumentException("Error in split(): " + e.getMessage());
        }
    }

    /**
     * Merges four submatrices (A11, A12, A21, A22) into a single matrix.
     * Commonly used in Strassen's Algorithm to reassemble sub-results.
     * @param A11 Top-left submatrix.
     * @param A12 Top-right submatrix.
     * @param A21 Bottom-left submatrix.
     * @param A22 Bottom-right submatrix.
     * @return A new Matrix representing the merged result.
     * @throws IllegalArgumentException if submatrices are not the same size.
     */
    public static Matrix merge(Matrix A11, Matrix A12, Matrix A21, Matrix A22) {
        try {
            // Validate submatrices have the same size
            if (!MatrixValidator.isSameSize(A11, A12) ||
                    !MatrixValidator.isSameSize(A11, A21) ||
                    !MatrixValidator.isSameSize(A11, A22)) {
                throw new IllegalArgumentException("All submatrices must be the same size to merge.");
            }

            // Each submatrix is half the final dimension
            int halfSize = A11.getSize();
            int fullSize = halfSize * 2;

            // Create a new matrix to store the merged result
            Matrix merged = new Matrix(fullSize);

            // Fill top-left, top-right, bottom-left, bottom-right
            for (int i = 0; i < halfSize; i++) {
                for (int j = 0; j < halfSize; j++) {
                    // Place A11 in top-left
                    merged.set(i, j, A11.get(i, j));
                    // Place A12 in top-right
                    merged.set(i, j + halfSize, A12.get(i, j));
                    // Place A21 in bottom-left
                    merged.set(i + halfSize, j, A21.get(i, j));
                    // Place A22 in bottom-right
                    merged.set(i + halfSize, j + halfSize, A22.get(i, j));
                }
            }

            // Return the newly merged matrix
            return merged;

        } catch (IllegalArgumentException e) {
            // Throw if submatrices are not the same size or other issues arise
            throw new IllegalArgumentException("Error in merge(): " + e.getMessage());
        }
    }
}
