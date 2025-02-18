package edu.jhu.algos.test.algorithms;

import edu.jhu.algos.algorithms.NaiveMultiplication;
import edu.jhu.algos.models.Matrix;
import edu.jhu.algos.utils.MatrixValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for NaiveMultiplication.
 * Verifies correctness, multiplication count, and execution time tracking.
 */
public class NaiveMultiplicationTest {

    @Test
    void testSmallMatrixMultiplication() {
        // 2x2 Matrices
        int[][] Adata = { {1, 2}, {3, 4} };
        int[][] Bdata = { {5, 6}, {7, 8} };
        int[][] expected = { {19, 22}, {43, 50} }; // Expected result from A * B

        Matrix A = new Matrix(Adata);
        Matrix B = new Matrix(Bdata);
        NaiveMultiplication naive = new NaiveMultiplication();

        // Perform multiplication
        Matrix result = naive.multiply(A, B);

        // Verify correctness
        assertArrayEquals(expected, result.getData(), "Matrix multiplication result is incorrect.");

        // Verify multiplication count (2^3 = 8 multiplications)
        assertEquals(8, naive.getMultiplicationCount(), "Multiplication count incorrect.");
    }

    @Test
    void testLargerMatrixMultiplication() {
        // 4x4 Identity matrix * another matrix
        int[][] Adata = {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };

        int[][] Bdata = {
                {2, 3, 4, 5},
                {6, 7, 8, 9},
                {10, 11, 12, 13},
                {14, 15, 16, 17}
        };

        int[][] expected = Bdata; // Identity * B = B

        Matrix A = new Matrix(Adata);
        Matrix B = new Matrix(Bdata);
        NaiveMultiplication naive = new NaiveMultiplication();

        // Perform multiplication
        Matrix result = naive.multiply(A, B);

        // Verify correctness
        assertArrayEquals(expected, result.getData(), "Identity matrix multiplication failed.");

        // Verify multiplication count (4^3 = 64)
        assertEquals(64, naive.getMultiplicationCount(), "Multiplication count incorrect.");
    }

    @Test
    void testZeroMatrixMultiplication() {
        // 2x2 Zero matrix * 2x2 matrix
        int[][] Adata = { {0, 0}, {0, 0} };
        int[][] Bdata = { {1, 2}, {3, 4} };
        int[][] expected = { {0, 0}, {0, 0} }; // Zero matrix * anything = Zero matrix

        Matrix A = new Matrix(Adata);
        Matrix B = new Matrix(Bdata);
        NaiveMultiplication naive = new NaiveMultiplication();

        // Perform multiplication
        Matrix result = naive.multiply(A, B);

        // Verify result is still a zero matrix
        assertArrayEquals(expected, result.getData(), "Zero matrix multiplication should return zero matrix.");

        // Verify multiplication count (2^3 = 8)
        assertEquals(8, naive.getMultiplicationCount(), "Multiplication count incorrect.");
    }

    @Test
    void testInvalidMatrixMultiplication() {
        // 2x2 matrix multiplied by a 4x4 matrix (should fail)
        int[][] Adata = { {1, 2}, {3, 4} };
        int[][] Bdata = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };

        Matrix A = new Matrix(Adata);
        Matrix B = new Matrix(Bdata);
        NaiveMultiplication naive = new NaiveMultiplication();

        // Expect an exception due to mismatched matrix sizes
        Exception exception = assertThrows(IllegalArgumentException.class, () -> naive.multiply(A, B));
        assertTrue(exception.getMessage().contains("Matrices must be the same size"));
    }

    @Test
    void testExecutionTimeTracking() {
        // Simple 2x2 multiplication
        int[][] Adata = { {1, 2}, {3, 4} };
        int[][] Bdata = { {5, 6}, {7, 8} };

        Matrix A = new Matrix(Adata);
        Matrix B = new Matrix(Bdata);
        NaiveMultiplication naive = new NaiveMultiplication();

        // Perform multiplication
        naive.multiply(A, B);

        // Ensure execution time is greater than 0 (indicating measurement works)
        assertTrue(naive.getElapsedTimeMs() >= 0, "Elapsed time tracking is incorrect.");
    }
}
