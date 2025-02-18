package edu.jhu.algos.test.utils;

import edu.jhu.algos.models.Matrix;
import edu.jhu.algos.utils.MatrixUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Unit tests for MatrixUtils, ensuring that all utility methods
 * (deepCopy, fillRandom, createZeroMatrix, createIdentityMatrix,
 *  printMatrix, toString, compareMatrices) work as expected.
 */
class MatrixUtilsTest {

    /**
     * Tests deepCopy(int[][]) by verifying that the returned array
     * is a distinct object but has the same contents.
     */
    @Test
    void testDeepCopy() {
        // Create a 2D array
        int[][] original = {
                {1, 2},
                {3, 4}
        };

        // Perform a deep copy
        int[][] copy = MatrixUtils.deepCopy(original);

        // Check that copy is not the same reference
        assertNotSame(original, copy, "The copied array should be a different object reference.");

        // Check that the contents match
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[0].length; j++) {
                assertEquals(original[i][j], copy[i][j], "Elements should be identical.");
            }
        }

        // Modify the copy and ensure it doesn't affect the original
        copy[0][0] = 99;
        assertEquals(1, original[0][0], "Changing copy should not affect the original array.");
    }

    /**
     * Tests fillRandom(Matrix, int, int) by verifying values are in the expected range.
     */
    @Test
    void testFillRandom() {
        // Create a 4x4 Matrix
        Matrix matrix = new Matrix(4);
        // Fill with random values in [5, 10]
        MatrixUtils.fillRandom(matrix, 5, 10);

        // Verify each element is within [5, 10]
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int val = matrix.get(i, j);
                assertTrue(val >= 5 && val <= 10,
                        "Random value should be between 5 and 10 inclusive.");
            }
        }
    }

    /**
     * Tests createZeroMatrix(int) by confirming size and content.
     */
    @Test
    void testCreateZeroMatrix() {
        // Create a zero 4x4 matrix
        Matrix zeroMat = MatrixUtils.createZeroMatrix(4);

        // Check size is 4
        assertEquals(4, zeroMat.getSize(), "Zero matrix should be 4x4.");

        // Check all values are 0
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(0, zeroMat.get(i, j), "All values in the zero matrix should be 0.");
            }
        }
    }

    /**
     * Tests createIdentityMatrix(int) by confirming size, diagonals, and off-diagonals.
     */
    @Test
    void testCreateIdentityMatrix() {
        // Create an identity 3x3 matrix.
        // (Even though we typically say power of 2, let's check 2^1=2^2=4, or 3 is not power of 2?
        //  The assignment says matrix sizes are 2^n, but let's just demonstrate.)
        // If your assignment strictly wants 2^n, you can change to 4.
        Matrix identityMat = MatrixUtils.createIdentityMatrix(2);

        // Check size
        assertEquals(2, identityMat.getSize(), "Identity matrix should be 2x2 for this test.");

        // Check diagonal is 1, off-diagonal is 0
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (i == j) {
                    // Diagonal
                    assertEquals(1, identityMat.get(i, j), "Diagonal elements should be 1.");
                } else {
                    // Off-diagonal
                    assertEquals(0, identityMat.get(i, j), "Off-diagonal elements should be 0.");
                }
            }
        }
    }

    /**
     * Tests printMatrix(Matrix) by capturing console output and verifying it matches expectation.
     */
    @Test
    void testPrintMatrix() {
        // Create a small 2x2 matrix
        Matrix matrix = new Matrix(2);
        matrix.set(0, 0, 1);
        matrix.set(0, 1, 2);
        matrix.set(1, 0, 3);
        matrix.set(1, 1, 4);

        // Capture console output using ByteArrayOutputStream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Print the matrix
        MatrixUtils.printMatrix(matrix);

        // Restore original System.out
        System.setOut(originalOut);

        // The printed result should contain "1" "2" in the first line, "3" "4" in the second
        String output = outContent.toString();
        assertTrue(output.contains("   1"), "Expected '1' in output.");
        assertTrue(output.contains("   2"), "Expected '2' in output.");
        assertTrue(output.contains("   3"), "Expected '3' in output.");
        assertTrue(output.contains("   4"), "Expected '4' in output.");
    }

    /**
     * Tests toString(Matrix) by verifying the returned string matches expected rows and columns.
     */
    @Test
    void testToString() {
        // Create a small 2x2 matrix
        Matrix matrix = new Matrix(2);
        matrix.set(0, 0, 10);
        matrix.set(0, 1, 20);
        matrix.set(1, 0, 30);
        matrix.set(1, 1, 40);

        // Convert matrix to string
        String matrixStr = MatrixUtils.toString(matrix);

        // Check that it contains the correct values
        // NOTE: The string format might have spacing like "  10" or "  20".
        //       We'll do a simple contains check.
        assertTrue(matrixStr.contains("  10"), "Expected '10' in string representation.");
        assertTrue(matrixStr.contains("  20"), "Expected '20' in string representation.");
        assertTrue(matrixStr.contains("  30"), "Expected '30' in string representation.");
        assertTrue(matrixStr.contains("  40"), "Expected '40' in string representation.");
    }

    /**
     * Tests compareMatrices(Matrix, Matrix) by checking identical and different matrices.
     */
    @Test
    void testCompareMatrices() {
        // Create matrix A
        Matrix A = new Matrix(2);
        A.set(0, 0, 1);
        A.set(0, 1, 2);
        A.set(1, 0, 3);
        A.set(1, 1, 4);

        // Create matrix B with same values
        Matrix B = new Matrix(2);
        B.set(0, 0, 1);
        B.set(0, 1, 2);
        B.set(1, 0, 3);
        B.set(1, 1, 4);

        // Create matrix C with different values
        Matrix C = new Matrix(2);
        C.set(0, 0, 5);
        C.set(0, 1, 6);
        C.set(1, 0, 7);
        C.set(1, 1, 8);

        // Should be true for A and B
        assertTrue(MatrixUtils.compareMatrices(A, B), "A and B should be identical.");

        // Should be false for A and C
        assertFalse(MatrixUtils.compareMatrices(A, C), "A and C should differ.");

        // Check dimension mismatch
        Matrix D = new Matrix(4);
        assertFalse(MatrixUtils.compareMatrices(A, D), "2x2 vs. 4x4 should not match.");
    }
}
