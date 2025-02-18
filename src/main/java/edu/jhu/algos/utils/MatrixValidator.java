package edu.jhu.algos.utils;

import edu.jhu.algos.models.Matrix;

/**
 * Provides validation methods for matrix operations.
 * Ensures matrices meet constraints like size, bounds, and compatibility.
 */
public class MatrixValidator {

    /**
     * Checks if a number is a power of 2.
     * @param n The number to check.
     * @return True if n is a power of 2, otherwise false.
     */
    public static boolean isPowerOfTwo(int n) {
        return (n & (n - 1)) == 0 && n > 0; // Uses bitwise trick: Powers of 2 have only one bit set
    }

    /**
     * Ensures a matrix is not null or empty.
     * @param matrix The matrix to check.
     * @return True if matrix is not null and has at least one row.
     */
    public static boolean isNonEmptyMatrix(int[][] matrix) {
        return matrix != null && matrix.length > 0; // Ensures matrix exists and isn't empty
    }

    /**
     * Checks if a matrix is square (equal rows and columns).
     * @param matrix The matrix to check.
     * @return True if matrix is square, false otherwise.
     */
    public static boolean isSquareMatrix(int[][] matrix) {
        return isNonEmptyMatrix(matrix) && matrix.length == matrix[0].length; // Ensures row count matches column count
    }

    /**
     * Checks if two matrices have the same size.
     * Used for operations like addition, subtraction.
     * @param A First matrix.
     * @param B Second matrix.
     * @return True if both matrices have the same size, false otherwise.
     */
    public static boolean isSameSize(Matrix A, Matrix B) {
        return A.getSize() == B.getSize(); // Ensures both matrices have identical dimensions
    }

    /**
     * Checks if a matrix is valid (square and its size is a power of 2).
     * @param matrix The 2D array representing the matrix.
     * @return True if the matrix is valid, false otherwise.
     */
    public static boolean isValidMatrix(int[][] matrix) {
        return isNonEmptyMatrix(matrix) && isSquareMatrix(matrix) && isPowerOfTwo(matrix.length);
        // Ensures matrix is non-empty, square, and size is power of 2
    }

    /**
     * Checks if a given index is within matrix bounds.
     * @param row The row index.
     * @param col The column index.
     * @param size The matrix size.
     * @return True if the index is valid, false otherwise.
     */
    public static boolean isValidIndex(int row, int col, int size) {
        return row >= 0 && row < size && col >= 0 && col < size;
        // Ensures row and column indices are within matrix bounds
    }

    /**
     * Checks if a submatrix extraction is valid.
     * Ensures the submatrix does not exceed original matrix bounds.
     * @param rowOffset The starting row index.
     * @param colOffset The starting column index.
     * @param newSize The size of the submatrix.
     * @param matrixSize The size of the original matrix.
     * @return True if the submatrix extraction is valid, false otherwise.
     */
    public static boolean isValidSubMatrix(int rowOffset, int colOffset, int newSize, int matrixSize) {
        return rowOffset >= 0 && colOffset >= 0 &&
                rowOffset + newSize <= matrixSize && colOffset + newSize <= matrixSize;
        // Ensures submatrix does not go out of bounds
    }

    /**
     * Checks if a matrix is a zero matrix (all elements are 0).
     * @param matrix The matrix to check.
     * @return True if all elements are zero, false otherwise.
     */
    public static boolean isZeroMatrix(Matrix matrix) {
        int[][] data = matrix.getData(); // Get matrix data
        for (int[] row : data) { // Iterate over rows
            for (int val : row) { // Iterate over columns
                if (val != 0) {
                    return false; // If any value is not zero, return false
                }
            }
        }
        return true; // If all values are zero, return true
    }

    /**
     * Checks if a matrix is an identity matrix (diagonal elements are 1, others are 0).
     * @param matrix The matrix to check.
     * @return True if the matrix is an identity matrix, false otherwise.
     */
    public static boolean isIdentityMatrix(Matrix matrix) {
        int size = matrix.getSize(); // Get matrix size
        int[][] data = matrix.getData(); // Get matrix data

        for (int i = 0; i < size; i++) { // Iterate through rows
            for (int j = 0; j < size; j++) { // Iterate through columns
                if (i == j && data[i][j] != 1) { // Diagonal elements should be 1
                    return false;
                } else if (i != j && data[i][j] != 0) { // Non-diagonal elements should be 0
                    return false;
                }
            }
        }
        return true; // If all conditions match, return true
    }

    /**
     * Checks if a matrix contains only positive integers.
     * @param matrix The matrix to check.
     * @return True if all elements are positive, false otherwise.
     */
    public static boolean isPositiveMatrix(Matrix matrix) {
        int[][] data = matrix.getData(); // Get matrix data
        for (int[] row : data) { // Iterate over rows
            for (int val : row) { // Iterate over columns
                if (val <= 0) {
                    return false; // If any value is non-positive, return false
                }
            }
        }
        return true; // If all values are positive, return true
    }

    /**
     * Checks if a matrix is symmetric (A[i][j] == A[j][i] for all elements).
     * @param matrix The matrix to check.
     * @return True if symmetric, false otherwise.
     */
    public static boolean isSymmetric(Matrix matrix) {
        int size = matrix.getSize(); // Get matrix size
        int[][] data = matrix.getData(); // Get matrix data

        for (int i = 0; i < size; i++) { // Iterate through rows
            for (int j = i + 1; j < size; j++) { // Only check upper triangle
                if (data[i][j] != data[j][i]) { // Symmetric property
                    return false;
                }
            }
        }
        return true; // If all checks pass, return true
    }
}
