package edu.jhu.algos.test.models;

import edu.jhu.algos.models.Matrix;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Matrix class.
 */
public class MatrixTest {

    /**
     * Tests whether a valid matrix is created successfully.
     */
    @Test
    void testMatrixCreation() {
        double[][] data = {
                {1.0, 2.0},
                {3.0, 4.0}
        };
        Matrix matrix = new Matrix(2, 2, data);
        assertEquals(2, matrix.getRows());
        assertEquals(2, matrix.getCols());
    }

    /**
     * Tests whether the deep copy mechanism prevents external modification.
     */
    @Test
    void testDeepCopy() {
        double[][] data = {
                {1.0, 2.0},
                {3.0, 4.0}
        };
        Matrix matrix = new Matrix(2, 2, data);

        // Modify original array after creation
        data[0][0] = 99;

        // Ensure the matrix remains unchanged
        assertEquals(1.0, matrix.getData(true)[0][0]);
    }

    /**
     * Tests whether the matrix correctly identifies square matrices.
     */
    @Test
    void testIsSquareMatrix() {
        Matrix squareMatrix = new Matrix(3, 3, new double[3][3]);
        Matrix nonSquareMatrix = new Matrix(3, 4, new double[3][4]);

        assertTrue(squareMatrix.isSquare());
        assertFalse(nonSquareMatrix.isSquare());
    }

    /**
     * Tests whether the matrix correctly identifies power-of-two sizes.
     */
    @Test
    void testIsPowerOfTwoSquare() {
        Matrix powerOfTwoMatrix = new Matrix(8, 8, new double[8][8]);
        Matrix nonPowerOfTwoMatrix = new Matrix(5, 5, new double[5][5]);

        assertTrue(powerOfTwoMatrix.isPowerOfTwoSquare());
        assertFalse(nonPowerOfTwoMatrix.isPowerOfTwoSquare());
    }

    /**
     * Tests whether matrix addition is computed correctly.
     */
    @Test
    void testMatrixAddition() {
        double[][] dataA = {
                {1.0, 2.0},
                {3.0, 4.0}
        };
        double[][] dataB = {
                {5.0, 6.0},
                {7.0, 8.0}
        };
        double[][] expectedSum = {
                {6.0, 8.0},
                {10.0, 12.0}
        };

        Matrix A = new Matrix(2, 2, dataA);
        Matrix B = new Matrix(2, 2, dataB);
        Matrix sum = A.add(B);

        assertArrayEquals(expectedSum, sum.getData(true));
    }

    /**
     * Tests whether matrix subtraction is computed correctly.
     */
    @Test
    void testMatrixSubtraction() {
        double[][] dataA = {
                {5.0, 6.0},
                {7.0, 8.0}
        };
        double[][] dataB = {
                {1.0, 2.0},
                {3.0, 4.0}
        };
        double[][] expectedDiff = {
                {4.0, 4.0},
                {4.0, 4.0}
        };

        Matrix A = new Matrix(2, 2, dataA);
        Matrix B = new Matrix(2, 2, dataB);
        Matrix diff = A.subtract(B);

        assertArrayEquals(expectedDiff, diff.getData(true));
    }

    /**
     * Tests matrix equality based on identical values.
     */
    @Test
    void testMatrixEquality() {
        double[][] dataA = {
                {1.0, 2.0},
                {3.0, 4.0}
        };
        double[][] dataB = {
                {1.0, 2.0},
                {3.0, 4.0}
        };
        double[][] dataC = {
                {5.0, 6.0},
                {7.0, 8.0}
        };

        Matrix A = new Matrix(2, 2, dataA);
        Matrix B = new Matrix(2, 2, dataB);
        Matrix C = new Matrix(2, 2, dataC);

        assertEquals(A, B); // Matrices with identical values should be equal
        assertNotEquals(A, C); // Different matrices should not be equal
    }

    /**
     * Tests matrix hashing to ensure consistent hash codes for identical matrices.
     */
    @Test
    void testMatrixHashing() {
        double[][] dataA = {
                {1.0, 2.0},
                {3.0, 4.0}
        };
        double[][] dataB = {
                {1.0, 2.0},
                {3.0, 4.0}
        };

        Matrix A = new Matrix(2, 2, dataA);
        Matrix B = new Matrix(2, 2, dataB);

        assertEquals(A.hashCode(), B.hashCode()); // Identical matrices should have the same hash
    }

    /**
     * Tests whether the matrix `toString()` method formats correctly.
     */
    @Test
    void testMatrixToString() {
        double[][] data = {
                {1.0, 2.0},
                {3.0, 4.0}
        };

        Matrix matrix = new Matrix(2, 2, data);
        String expectedOutput = "|  1.00  2.00 |\n|  3.00  4.00 |\n";

        assertEquals(expectedOutput.trim(), matrix.toString().trim());
    }
}
