package edu.jhu.algos.test.algorithms;

import edu.jhu.algos.algorithms.StrassenMultiplication;
import edu.jhu.algos.algorithms.NaiveMultiplication;
import edu.jhu.algos.models.Matrix;
import edu.jhu.algos.utils.MatrixValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StrassenMultiplication.
 * Ensures correctness, multiplication count tracking, and execution time tracking.
 */
public class StrassenMultiplicationTest {

    @Test
    void testSmallMatrixMultiplication() {
        // 2x2 Matrices
        int[][] Adata = { {1, 2}, {3, 4} };
        int[][] Bdata = { {5, 6}, {7, 8} };
        int[][] expected = { {19, 22}, {43, 50} }; // Expected result from A * B

        Matrix A = new Matrix(Adata);
        Matrix B = new Matrix(Bdata);
        StrassenMultiplication strassen = new StrassenMultiplication();

        // Perform multiplication
        Matrix result = strassen.multiply(A, B);

        // Verify correctness
        assertArrayEquals(expected, result.getData(), "Matrix multiplication result is incorrect.");

        // Ensure multiplication count is < naive's O(n^3)
        assertTrue(strassen.getMultiplicationCount() < 8, "Multiplication count should be lower than naive O(n^3).");
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
        StrassenMultiplication strassen = new StrassenMultiplication();

        // Perform multiplication
        Matrix result = strassen.multiply(A, B);

        // Verify correctness
        assertArrayEquals(expected, result.getData(), "Identity matrix multiplication failed.");

        // Ensure multiplication count is < naive's O(n^3)
        assertTrue(strassen.getMultiplicationCount() < 64, "Multiplication count should be lower than naive O(n^3).");
    }

    @Test
    void testZeroMatrixMultiplication() {
        // 2x2 Zero matrix * 2x2 matrix
        int[][] Adata = { {0, 0}, {0, 0} };
        int[][] Bdata = { {1, 2}, {3, 4} };
        int[][] expected = { {0, 0}, {0, 0} }; // Zero matrix * anything = Zero matrix

        Matrix A = new Matrix(Adata);
        Matrix B = new Matrix(Bdata);
        StrassenMultiplication strassen = new StrassenMultiplication();

        // Perform multiplication
        Matrix result = strassen.multiply(A, B);

        // Verify result is still a zero matrix
        assertArrayEquals(expected, result.getData(), "Zero matrix multiplication should return zero matrix.");

        // Ensure no unnecessary multiplications were performed
        assertEquals(0, strassen.getMultiplicationCount(), "Multiplication count should be zero for zero matrix.");
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
        StrassenMultiplication strassen = new StrassenMultiplication();

        // Expect an exception due to mismatched matrix sizes
        Exception exception = assertThrows(IllegalArgumentException.class, () -> strassen.multiply(A, B));
        assertTrue(exception.getMessage().contains("Matrices must be the same size"));
    }

    @Test
    void testExecutionTimeTracking() {
        // Simple 2x2 multiplication
        int[][] Adata = { {1, 2}, {3, 4} };
        int[][] Bdata = { {5, 6}, {7, 8} };

        Matrix A = new Matrix(Adata);
        Matrix B = new Matrix(Bdata);
        StrassenMultiplication strassen = new StrassenMultiplication();

        // Perform multiplication
        strassen.multiply(A, B);

        // Ensure execution time is greater than 0 (indicating measurement works)
        assertTrue(strassen.getElapsedTimeMs() >= 0, "Elapsed time tracking is incorrect.");
    }

    @Test
    void testCompareWithNaiveMultiplication() {
        // 4x4 random matrices
        int[][] Adata = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };

        int[][] Bdata = {
                {17, 18, 19, 20},
                {21, 22, 23, 24},
                {25, 26, 27, 28},
                {29, 30, 31, 32}
        };

        Matrix A = new Matrix(Adata);
        Matrix B = new Matrix(Bdata);
        NaiveMultiplication naive = new NaiveMultiplication();
        StrassenMultiplication strassen = new StrassenMultiplication();

        // Perform both multiplications
        Matrix naiveResult = naive.multiply(A, B);
        Matrix strassenResult = strassen.multiply(A, B);

        // Ensure both methods produce the same result
        assertArrayEquals(naiveResult.getData(), strassenResult.getData(), "Strassen's result should match Naive multiplication.");

        // Strassen should perform fewer multiplications than naive
        assertTrue(strassen.getMultiplicationCount() < naive.getMultiplicationCount(), "Strassen should use fewer multiplications than Naive.");
    }
}
