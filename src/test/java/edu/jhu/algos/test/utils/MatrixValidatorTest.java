package edu.jhu.algos.test.utils;

import edu.jhu.algos.utils.MatrixValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import edu.jhu.algos.models.Matrix;

/**
 * Unit tests for the MatrixValidator class.
 * Ensures all validation functions work correctly, including edge cases.
 */
class MatrixValidatorTest {

    /**
     * Tests if a number is correctly identified as a power of 2.
     */
    @Test
    void testIsPowerOfTwo() {
        assertTrue(MatrixValidator.isPowerOfTwo(1), "1 should be a power of 2.");
        assertTrue(MatrixValidator.isPowerOfTwo(2), "2 should be a power of 2.");
        assertTrue(MatrixValidator.isPowerOfTwo(16), "16 should be a power of 2.");
        assertTrue(MatrixValidator.isPowerOfTwo(1024), "1024 should be a power of 2.");
        assertFalse(MatrixValidator.isPowerOfTwo(3), "3 should NOT be a power of 2.");
        assertFalse(MatrixValidator.isPowerOfTwo(10), "10 should NOT be a power of 2.");
        assertFalse(MatrixValidator.isPowerOfTwo(0), "0 should NOT be a power of 2.");
        assertFalse(MatrixValidator.isPowerOfTwo(-8), "-8 should NOT be a power of 2.");
    }

    /**
     * Tests if an empty or null matrix is correctly identified.
     */
    @Test
    void testIsNonEmptyMatrix() {
        int[][] emptyMatrix = {};
        assertFalse(MatrixValidator.isNonEmptyMatrix(emptyMatrix), "Empty matrix should return false.");

        int[][] nullMatrix = null;
        assertFalse(MatrixValidator.isNonEmptyMatrix(nullMatrix), "Null matrix should return false.");

        int[][] validMatrix = {{1}};
        assertTrue(MatrixValidator.isNonEmptyMatrix(validMatrix), "1x1 matrix should be non-empty.");
    }

    /**
     * Tests if a square matrix is correctly identified.
     */
    @Test
    void testIsSquareMatrix() {
        int[][] squareMatrix = {
                {1, 2},
                {3, 4}
        };
        assertTrue(MatrixValidator.isSquareMatrix(squareMatrix), "2x2 matrix should be square.");

        int[][] nonSquareMatrix = {
                {1, 2, 3},
                {4, 5, 6}
        };
        assertFalse(MatrixValidator.isSquareMatrix(nonSquareMatrix), "2x3 matrix should NOT be square.");
    }

    /**
     * Tests if two matrices have the same size.
     */
    @Test
    void testIsSameSize() {
        Matrix A = new Matrix(4);
        Matrix B = new Matrix(4);
        Matrix C = new Matrix(2);

        assertTrue(MatrixValidator.isSameSize(A, B), "Both 4x4 matrices should be the same size.");
        assertFalse(MatrixValidator.isSameSize(A, C), "4x4 and 2x2 matrices should NOT be the same size.");
    }

    /**
     * Tests if a matrix is valid (square and a power of 2).
     */
    @Test
    void testIsValidMatrix() {
        int[][] validMatrix = {
                {1, 2},
                {3, 4}
        };
        assertTrue(MatrixValidator.isValidMatrix(validMatrix), "Valid 2x2 matrix should pass.");

        int[][] invalidMatrix = {
                {1, 2, 3},
                {4, 5, 6}
        };
        assertFalse(MatrixValidator.isValidMatrix(invalidMatrix), "Non-square matrix should fail.");

        int[][] nonPowerOfTwoMatrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        assertFalse(MatrixValidator.isValidMatrix(nonPowerOfTwoMatrix), "3x3 matrix (not power of 2) should fail.");
    }

    /**
     * Tests if matrix indices are within bounds.
     */
    @Test
    void testIsValidIndex() {
        assertTrue(MatrixValidator.isValidIndex(1, 1, 4), "Index (1,1) should be valid in a 4x4 matrix.");
        assertFalse(MatrixValidator.isValidIndex(4, 4, 4), "Index (4,4) should be out of bounds in a 4x4 matrix.");
        assertFalse(MatrixValidator.isValidIndex(-1, 0, 4), "Negative index should be invalid.");
    }

    /**
     * Tests if submatrix extraction is valid.
     */
    @Test
    void testIsValidSubMatrix() {
        assertTrue(MatrixValidator.isValidSubMatrix(0, 0, 2, 4), "Valid 2x2 submatrix in 4x4 should pass.");
        assertFalse(MatrixValidator.isValidSubMatrix(3, 3, 2, 4), "Invalid submatrix exceeding 4x4 bounds should fail.");
    }

    /**
     * Tests if a matrix is a zero matrix.
     */
    @Test
    void testIsZeroMatrix() {
        Matrix zeroMatrix = new Matrix(2);
        assertTrue(MatrixValidator.isZeroMatrix(zeroMatrix), "Empty 2x2 matrix should be a zero matrix.");

        Matrix nonZeroMatrix = new Matrix(2);
        nonZeroMatrix.set(0, 0, 1);
        assertFalse(MatrixValidator.isZeroMatrix(nonZeroMatrix), "Matrix with nonzero values should NOT be a zero matrix.");
    }

    /**
     * Tests if a matrix is an identity matrix.
     */
    @Test
    void testIsIdentityMatrix() {
        Matrix identityMatrix = new Matrix(2);
        identityMatrix.set(0, 0, 1);
        identityMatrix.set(1, 1, 1);
        assertTrue(MatrixValidator.isIdentityMatrix(identityMatrix), "2x2 identity matrix should be valid.");

        Matrix nonIdentityMatrix = new Matrix(2);
        nonIdentityMatrix.set(0, 0, 1);
        nonIdentityMatrix.set(1, 1, 2);
        assertFalse(MatrixValidator.isIdentityMatrix(nonIdentityMatrix), "Matrix with incorrect diagonal should fail.");
    }

    /**
     * Tests if a matrix contains only positive integers.
     */
    @Test
    void testIsPositiveMatrix() {
        Matrix positiveMatrix = new Matrix(2);
        positiveMatrix.set(0, 0, 1);
        positiveMatrix.set(0, 1, 2);
        positiveMatrix.set(1, 0, 3);
        positiveMatrix.set(1, 1, 4);
        assertTrue(MatrixValidator.isPositiveMatrix(positiveMatrix), "Matrix with only positive numbers should pass.");

        Matrix mixedMatrix = new Matrix(2);
        mixedMatrix.set(0, 0, -1);
        assertFalse(MatrixValidator.isPositiveMatrix(mixedMatrix), "Matrix with a negative number should fail.");
    }

    /**
     * Tests if a matrix is symmetric.
     */
    @Test
    void testIsSymmetric() {
        Matrix symmetricMatrix = new Matrix(2);
        symmetricMatrix.set(0, 0, 1);
        symmetricMatrix.set(0, 1, 2);
        symmetricMatrix.set(1, 0, 2);
        symmetricMatrix.set(1, 1, 1);
        assertTrue(MatrixValidator.isSymmetric(symmetricMatrix), "Symmetric matrix should pass.");

        Matrix nonSymmetricMatrix = new Matrix(2);
        nonSymmetricMatrix.set(0, 1, 3);
        nonSymmetricMatrix.set(1, 0, 2);
        assertFalse(MatrixValidator.isSymmetric(nonSymmetricMatrix), "Non-symmetric matrix should fail.");
    }
}
