package edu.jhu.algos.test.utils;

import edu.jhu.algos.utils.MatrixValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the MatrixValidator class.
 */
public class MatrixValidatorTest {

    /**
     * Tests whether a valid matrix passes validation.
     */
    @Test
    void testValidMatrix() {
        double[][] validMatrix = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };
        assertDoesNotThrow(() -> MatrixValidator.validateMatrix(3, 3, validMatrix));
    }

    /**
     * Tests whether a null matrix throws an exception.
     */
    @Test
    void testNullMatrix() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                MatrixValidator.validateMatrix(3, 3, null)
        );
        assertEquals("Matrix data cannot be null.", exception.getMessage());
    }

    /**
     * Tests whether a matrix with incorrect row count throws an exception.
     */
    @Test
    void testMismatchedRowCount() {
        double[][] invalidMatrix = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0}  // Only 2 rows instead of 3
        };
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                MatrixValidator.validateMatrix(3, 3, invalidMatrix)
        );
        assertEquals("Expected 3 rows but found 2", exception.getMessage());
    }

    /**
     * Tests whether a matrix with inconsistent column sizes throws an exception.
     */
    @Test
    void testInconsistentColumnSize() {
        double[][] invalidMatrix = {
                {1.0, 2.0},  // Only 2 columns instead of 3
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                MatrixValidator.validateMatrix(3, 3, invalidMatrix)
        );
        assertEquals("Row 0 must have exactly 3 columns.", exception.getMessage());
    }

    /**
     * Tests whether a square matrix passes validation.
     */
    @Test
    void testValidSquareMatrix() {
        assertDoesNotThrow(() -> MatrixValidator.validateSquareMatrix(4, 4));
    }

    /**
     * Tests whether a non-square matrix throws an exception.
     */
    @Test
    void testNonSquareMatrix() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                MatrixValidator.validateSquareMatrix(3, 4)  // 3 × 4 is not square
        );
        assertEquals("Matrix must be square (`n × n`), but got 3 × 4", exception.getMessage());
    }

    /**
     * Tests whether a valid power-of-two matrix size passes validation.
     */
    @Test
    void testValidPowerOfTwoSize() {
        assertDoesNotThrow(() -> MatrixValidator.validatePowerOfTwoSize(8));  // 8 is 2^3
    }

    /**
     * Tests whether a non-power-of-two matrix size throws an exception.
     */
    @Test
    void testInvalidPowerOfTwoSize() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                MatrixValidator.validatePowerOfTwoSize(10)  // 10 is not a power of two
        );
        assertEquals("Matrix size must be a power of two, but got 10", exception.getMessage());
    }

    /**
     * Tests whether the isPowerOfTwo method correctly identifies power-of-two values.
     */
    @Test
    void testIsPowerOfTwo() {
        assertTrue(MatrixValidator.isPowerOfTwo(1));  // 2^0
        assertTrue(MatrixValidator.isPowerOfTwo(2));  // 2^1
        assertTrue(MatrixValidator.isPowerOfTwo(4));  // 2^2
        assertTrue(MatrixValidator.isPowerOfTwo(8));  // 2^3
        assertTrue(MatrixValidator.isPowerOfTwo(16)); // 2^4
        assertFalse(MatrixValidator.isPowerOfTwo(3)); // Not a power of two
        assertFalse(MatrixValidator.isPowerOfTwo(6)); // Not a power of two
        assertFalse(MatrixValidator.isPowerOfTwo(10)); // Not a power of two
        assertFalse(MatrixValidator.isPowerOfTwo(-8)); // Negative numbers should return false
        assertFalse(MatrixValidator.isPowerOfTwo(0)); // Zero is not a power of two
    }
}
