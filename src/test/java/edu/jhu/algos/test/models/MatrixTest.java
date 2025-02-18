package edu.jhu.algos.test.models;

import edu.jhu.algos.models.Matrix;
import edu.jhu.algos.utils.MatrixValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the minimal Matrix class.
 * Verifies constructor usage, size checks, set/get, and data handling.
 */
class MatrixTest {

    /**
     * Tests constructing a Matrix with a valid power-of-2 size.
     */
    @Test
    void testConstructorValidSize() {
        // Create a matrix of size 4 (4 is 2^2, a valid power of 2)
        Matrix mat = new Matrix(4);
        // Check if getSize() returns 4
        assertEquals(4, mat.getSize(), "Matrix size should be 4.");
    }

    /**
     * Tests constructing a Matrix with an invalid size (not a power of 2).
     * Should throw IllegalArgumentException.
     */
    @Test
    void testConstructorInvalidSize() {
        // Size 3 is not a power of 2, expect an exception
        Exception ex = assertThrows(IllegalArgumentException.class, () -> new Matrix(3));
        assertTrue(ex.getMessage().contains("power of 2"), "Expected an error mentioning 'power of 2'.");
    }

    /**
     * Tests constructing a Matrix from a valid 2D array.
     */
    @Test
    void testConstructorValid2DArray() {
        // 2D array must be square and size must be power of 2 (e.g., 2x2)
        int[][] validData = {
                {1, 2},
                {3, 4}
        };
        // Construct the matrix
        Matrix mat = new Matrix(validData);
        // Check size
        assertEquals(2, mat.getSize(), "Matrix dimension should be 2.");
        // Check a value
        assertEquals(1, mat.get(0, 0), "Expected value 1 at (0,0).");
        // Check validity
        assertTrue(MatrixValidator.isValidMatrix(mat.getData()), "Should be a valid matrix.");
    }

    /**
     * Tests constructing a Matrix from an invalid 2D array (non-square or dimension not power of 2).
     */
    @Test
    void testConstructorInvalid2DArray() {
        // Non-square array
        int[][] nonSquare = {
                {1, 2, 3},
                {4, 5, 6}
        };
        // 3x3 not power of 2
        int[][] threeByThree = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        // Empty array
        int[][] empty = {};

        // Should all throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> new Matrix(nonSquare));
        assertThrows(IllegalArgumentException.class, () -> new Matrix(threeByThree));
        assertThrows(IllegalArgumentException.class, () -> new Matrix(empty));
    }

    /**
     * Tests getData() to ensure it returns the underlying 2D array.
     * (Currently returns the direct reference. If you do a deep copy, adapt the test.)
     */
    @Test
    void testGetData() {
        // Create a 2x2 matrix
        int[][] arr = {
                {1, 2},
                {3, 4}
        };
        Matrix mat = new Matrix(arr);
        // Retrieve the data
        int[][] fetchedData = mat.getData();
        // Check a sample value
        assertEquals(4, fetchedData[1][1], "Expected value 4 at (1,1).");

        // If direct reference: changes to fetchedData affect the matrix
        fetchedData[0][0] = 99;
        assertEquals(99, mat.get(0, 0), "Because getData() returns the direct reference, changes reflect in mat.");
    }

    /**
     * Tests set(...) and get(...) with valid indices.
     */
    @Test
    void testSetAndGetValid() {
        Matrix mat = new Matrix(2); // 2x2
        mat.set(0, 0, 10);
        mat.set(0, 1, 20);
        mat.set(1, 0, 30);
        mat.set(1, 1, 40);

        // Verify the values
        assertEquals(10, mat.get(0, 0), "Expected 10 at (0,0).");
        assertEquals(20, mat.get(0, 1), "Expected 20 at (0,1).");
        assertEquals(30, mat.get(1, 0), "Expected 30 at (1,0).");
        assertEquals(40, mat.get(1, 1), "Expected 40 at (1,1).");
    }

    /**
     * Tests set(...) and get(...) with out-of-bounds indices, expecting IndexOutOfBoundsException.
     */
    @Test
    void testSetAndGetInvalidIndices() {
        Matrix mat = new Matrix(2); // 2x2
        // Attempt to set using invalid index (2,2) => out of bounds
        assertThrows(IndexOutOfBoundsException.class, () -> mat.set(2, 2, 99));
        // Attempt to get using negative index => out of bounds
        assertThrows(IndexOutOfBoundsException.class, () -> mat.get(-1, 0));
    }
}
