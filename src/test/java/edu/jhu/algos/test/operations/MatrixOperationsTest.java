package edu.jhu.algos.test.operations;

import edu.jhu.algos.models.Matrix;
import edu.jhu.algos.operations.MatrixOperations;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the MatrixOperations class.
 * Covers add, subtract, split, and merge functionality.
 */
class MatrixOperationsTest {

    /**
     * Tests the add method with valid matrices.
     */
    @Test
    void testAddValid() {
        // Create a 2x2 Matrix A
        Matrix A = new Matrix(2);
        A.set(0, 0, 1);
        A.set(0, 1, 2);
        A.set(1, 0, 3);
        A.set(1, 1, 4);

        // Create a 2x2 Matrix B
        Matrix B = new Matrix(2);
        B.set(0, 0, 5);
        B.set(0, 1, 6);
        B.set(1, 0, 7);
        B.set(1, 1, 8);

        // Add A and B
        Matrix result = MatrixOperations.add(A, B);

        // Check results
        assertEquals(6, result.get(0, 0), "Result[0,0] should be 6.");
        assertEquals(8, result.get(0, 1), "Result[0,1] should be 8.");
        assertEquals(10, result.get(1, 0), "Result[1,0] should be 10.");
        assertEquals(12, result.get(1, 1), "Result[1,1] should be 12.");
    }

    /**
     * Tests the add method with mismatched sizes (expect exception).
     */
    @Test
    void testAddSizeMismatch() {
        // Create a 2x2 Matrix A
        Matrix A = new Matrix(2);
        // Create a 4x4 Matrix B
        Matrix B = new Matrix(4);

        // Attempt to add should throw exception
        Exception e = assertThrows(IllegalArgumentException.class, () -> MatrixOperations.add(A, B));
        assertTrue(e.getMessage().contains("same size"));
    }

    /**
     * Tests the subtract method with valid matrices.
     */
    @Test
    void testSubtractValid() {
        // Create a 2x2 Matrix A
        Matrix A = new Matrix(2);
        A.set(0, 0, 10);
        A.set(0, 1, 8);
        A.set(1, 0, 6);
        A.set(1, 1, 4);

        // Create a 2x2 Matrix B
        Matrix B = new Matrix(2);
        B.set(0, 0, 1);
        B.set(0, 1, 2);
        B.set(1, 0, 3);
        B.set(1, 1, 4);

        // Subtract B from A
        Matrix result = MatrixOperations.subtract(A, B);

        // Check results
        assertEquals(9, result.get(0, 0), "Result[0,0] should be 9.");
        assertEquals(6, result.get(0, 1), "Result[0,1] should be 6.");
        assertEquals(3, result.get(1, 0), "Result[1,0] should be 3.");
        assertEquals(0, result.get(1, 1), "Result[1,1] should be 0.");
    }

    /**
     * Tests the subtract method with mismatched sizes (expect exception).
     */
    @Test
    void testSubtractSizeMismatch() {
        // Create a 2x2 Matrix A
        Matrix A = new Matrix(2);
        // Create a 4x4 Matrix B
        Matrix B = new Matrix(4);

        // Attempt to subtract should throw exception
        Exception e = assertThrows(IllegalArgumentException.class, () -> MatrixOperations.subtract(A, B));
        assertTrue(e.getMessage().contains("same size"));
    }

    /**
     * Tests the split method with a valid 4x4 matrix.
     */
    @Test
    void testSplitValid() {
        // Create a 4x4 Matrix
        Matrix big = new Matrix(4);
        // Fill with values 1..16
        int value = 1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                big.set(i, j, value++);
            }
        }
        /*
           big =
           1   2   3   4
           5   6   7   8
           9  10  11  12
           13 14  15  16
         */

        // Split
        Matrix[] submatrices = MatrixOperations.split(big);
        // submatrices[0] => A11, submatrices[1] => A12, submatrices[2] => A21, submatrices[3] => A22

        // Each submatrix is 2x2
        assertEquals(2, submatrices[0].getSize(), "Submatrix A11 should be 2x2.");
        // Check some values in A11
        assertEquals(1, submatrices[0].get(0, 0), "A11[0,0] should be 1.");
        assertEquals(6, submatrices[0].get(1, 1), "A11[1,1] should be 6.");

        // Check some values in A12
        assertEquals(3, submatrices[1].get(0, 0), "A12[0,0] should be 3.");
        assertEquals(8, submatrices[1].get(1, 1), "A12[1,1] should be 8.");

        // Check some values in A21
        assertEquals(9, submatrices[2].get(0, 0), "A21[0,0] should be 9.");
        assertEquals(14, submatrices[2].get(1, 1), "A21[1,1] should be 14.");

        // Check some values in A22
        assertEquals(11, submatrices[3].get(0, 0), "A22[0,0] should be 11.");
        assertEquals(16, submatrices[3].get(1, 1), "A22[1,1] should be 16.");
    }

    /**
     * Tests the split method with a 1x1 matrix (expect exception).
     */
    @Test
    void testSplitTooSmall() {
        Matrix tiny = new Matrix(1);
        // Attempt to split a 1x1 matrix
        Exception e = assertThrows(IllegalArgumentException.class, () -> MatrixOperations.split(tiny));
        assertTrue(e.getMessage().contains("too small to split"));
    }

    /**
     * Tests the merge method with valid submatrices.
     */
    @Test
    void testMergeValid() {
        // Create four 2x2 submatrices to merge
        Matrix A11 = new Matrix(2);
        Matrix A12 = new Matrix(2);
        Matrix A21 = new Matrix(2);
        Matrix A22 = new Matrix(2);

        // Fill each submatrix with some distinct values
        // A11
        A11.set(0, 0, 1);
        A11.set(0, 1, 2);
        A11.set(1, 0, 3);
        A11.set(1, 1, 4);

        // A12
        A12.set(0, 0, 5);
        A12.set(0, 1, 6);
        A12.set(1, 0, 7);
        A12.set(1, 1, 8);

        // A21
        A21.set(0, 0, 9);
        A21.set(0, 1, 10);
        A21.set(1, 0, 11);
        A21.set(1, 1, 12);

        // A22
        A22.set(0, 0, 13);
        A22.set(0, 1, 14);
        A22.set(1, 0, 15);
        A22.set(1, 1, 16);

        // Merge
        Matrix merged = MatrixOperations.merge(A11, A12, A21, A22);

        // The merged matrix should be 4x4
        assertEquals(4, merged.getSize(), "Merged matrix should be 4x4.");

        // Check some positions in the merged matrix
        assertEquals(1, merged.get(0, 0), "Top-left corner from A11[0,0]");
        assertEquals(6, merged.get(0, 3), "Top-right corner from A12[0,1]");
        assertEquals(9, merged.get(2, 0), "Bottom-left corner from A21[0,0]");
        assertEquals(16, merged.get(3, 3), "Bottom-right corner from A22[1,1]");
    }

    /**
     * Tests the merge method with submatrices of different sizes (expect exception).
     */
    @Test
    void testMergeSizeMismatch() {
        // Create a 2x2 submatrix
        Matrix A11 = new Matrix(2);
        // Create a 4x4 submatrix
        Matrix A12 = new Matrix(4);
        // We'll just reuse A11 for A21 and A22
        Matrix A21 = A11;
        Matrix A22 = A11;

        // Attempt to merge should fail
        Exception e = assertThrows(IllegalArgumentException.class, () -> MatrixOperations.merge(A11, A12, A21, A22));
        assertTrue(e.getMessage().contains("must be the same size"));
    }
}
