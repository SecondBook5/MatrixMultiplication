package edu.jhu.algos.test.algorithms;

import edu.jhu.algos.algorithms.NaiveMultiplication;
import edu.jhu.algos.models.Matrix;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the NaiveMultiplication class.
 * Ensures correctness, error handling, and multiplication count tracking.
 */
public class NaiveMultiplicationTest {

    /**
     * Tests multiplication of two valid matrices.
     * Expected result:
     *      [1  2]   ×   [5  6]   =   [19  22]
     *      [3  4]       [7  8]       [43  50]
     */
    @Test
    void testValidMultiplication() {
        double[][] dataA = {
                {1, 2},
                {3, 4}
        };
        double[][] dataB = {
                {5, 6},
                {7, 8}
        };
        double[][] expectedProduct = {
                {19, 22},
                {43, 50}
        };

        Matrix A = new Matrix(2, 2, dataA);
        Matrix B = new Matrix(2, 2, dataB);
        Matrix product = NaiveMultiplication.multiply(A, B);

        assertArrayEquals(expectedProduct, product.getData(true));
    }

    /**
     * Tests multiplication of matrices with incompatible dimensions.
     * A (2×3) cannot be multiplied by B (2×2), should throw an IllegalArgumentException.
     */
    @Test
    void testIncompatibleMultiplication() {
        double[][] dataA = {
                {1, 2, 3},
                {4, 5, 6}
        };
        double[][] dataB = {
                {7, 8},
                {9, 10}
        };

        Matrix A = new Matrix(2, 3, dataA);
        Matrix B = new Matrix(2, 2, dataB);

        // Expect an exception due to mismatched dimensions
        assertThrows(IllegalArgumentException.class, () -> NaiveMultiplication.multiply(A, B));
    }

    /**
     * Tests multiplication with a null matrix.
     * Multiplication should throw an IllegalArgumentException.
     */
    @Test
    void testNullMatrixMultiplication() {
        Matrix A = new Matrix(2, 2, new double[][]{{1, 2}, {3, 4}});

        // Both cases should throw an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> NaiveMultiplication.multiply(A, null));
        assertThrows(IllegalArgumentException.class, () -> NaiveMultiplication.multiply(null, A));
    }

    /**
     * Tests multiplication with an empty matrix.
     * Multiplication should throw an IllegalArgumentException.
     */
    @Test
    void testEmptyMatrixMultiplication() {
        Matrix emptyMatrix = new Matrix(0, 0, new double[0][0]);
        Matrix A = new Matrix(2, 2, new double[][]{{1, 2}, {3, 4}});

        // Both cases should throw an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> NaiveMultiplication.multiply(A, emptyMatrix));
        assertThrows(IllegalArgumentException.class, () -> NaiveMultiplication.multiply(emptyMatrix, A));
    }

    /**
     * Tests multiplication with an identity matrix.
     * Multiplying by an identity matrix should return the same matrix.
     * Expected result:
     *      [3  4]   ×   [1  0]   =   [3  4]
     *      [5  6]       [0  1]       [5  6]
     */
    @Test
    void testMultiplicationWithIdentity() {
        double[][] dataA = {
                {3, 4},
                {5, 6}
        };
        double[][] identityMatrix = {
                {1, 0},
                {0, 1}
        };
        double[][] expectedProduct = {
                {3, 4},
                {5, 6}
        };

        Matrix A = new Matrix(2, 2, dataA);
        Matrix I = new Matrix(2, 2, identityMatrix);
        Matrix product = NaiveMultiplication.multiply(A, I);

        assertArrayEquals(expectedProduct, product.getData(true));
    }

    /**
     * Tests multiplication with a zero matrix.
     * The result should be a zero matrix.
     * Expected result:
     *      [3  4]   ×   [0  0]   =   [0  0]
     *      [5  6]       [0  0]       [0  0]
     */
    @Test
    void testMultiplicationWithZeroMatrix() {
        double[][] dataA = {
                {3, 4},
                {5, 6}
        };
        double[][] zeroMatrix = {
                {0, 0},
                {0, 0}
        };
        double[][] expectedProduct = {
                {0, 0},
                {0, 0}
        };

        Matrix A = new Matrix(2, 2, dataA);
        Matrix Z = new Matrix(2, 2, zeroMatrix);
        Matrix product = NaiveMultiplication.multiply(A, Z);

        assertArrayEquals(expectedProduct, product.getData(true));
    }

    /**
     * Tests multiplication count tracking.
     * Since A (2×2) and B (2×2) are multiplied,
     * the expected multiplication count should be 2 × 2 × 2 = 8.
     */
    @Test
    void testMultiplicationCount() {
        double[][] dataA = {
                {1, 2},
                {3, 4}
        };
        double[][] dataB = {
                {5, 6},
                {7, 8}
        };

        Matrix A = new Matrix(2, 2, dataA);
        Matrix B = new Matrix(2, 2, dataB);
        NaiveMultiplication.multiply(A, B);

        // Expect 2 * 2 * 2 = 8 multiplications
        assertEquals(8, NaiveMultiplication.getMultiplicationCount());
    }
}
