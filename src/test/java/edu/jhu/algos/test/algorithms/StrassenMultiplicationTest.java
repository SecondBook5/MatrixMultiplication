package edu.jhu.algos.test.algorithms;

import edu.jhu.algos.algorithms.StrassenMultiplication;
import edu.jhu.algos.models.Matrix;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StrassenMultiplication.
 * - Tests standard Strassen multiplication.
 * - Tests Winograd-optimized multiplication.
 * - Verifies parallel execution correctness.
 * - Ensures valid multiplication count & execution time.
 */
public class StrassenMultiplicationTest {

    /**
     * Tests Strassen multiplication on a small valid matrix.
     */
    @Test
    void testValidStrassenMultiplication() {
        double[][] dataA = {{1, 2}, {3, 4}};
        double[][] dataB = {{5, 6}, {7, 8}};
        double[][] expectedProduct = {{19, 22}, {43, 50}};

        Matrix A = new Matrix(2, dataA);
        Matrix B = new Matrix(2, dataB);

        StrassenMultiplication.setWinogradOptimization(false);
        Matrix product = StrassenMultiplication.multiply(A, B);

        assertArrayEquals(expectedProduct, product.retrieveRowMajorAs2D());
    }

    /**
     * Tests Winograd-optimized multiplication.
     */
    @Test
    void testValidWinogradMultiplication() {
        double[][] dataA = {{1, 2}, {3, 4}};
        double[][] dataB = {{5, 6}, {7, 8}};
        double[][] expectedProduct = {{19, 22}, {43, 50}};

        Matrix A = new Matrix(2, dataA);
        Matrix B = new Matrix(2, dataB);

        StrassenMultiplication.setWinogradOptimization(true);
        Matrix product = StrassenMultiplication.multiply(A, B);

        assertArrayEquals(expectedProduct, product.retrieveRowMajorAs2D());
    }

    /**
     * Tests Strassen multiplication with an identity matrix.
     */
    @Test
    void testMultiplicationWithIdentityMatrix() {
        double[][] dataA = {{3, 4}, {5, 6}};
        double[][] identity = {{1, 0}, {0, 1}};

        Matrix A = new Matrix(2, dataA);
        Matrix I = new Matrix(2, identity);

        StrassenMultiplication.setWinogradOptimization(false);
        Matrix product = StrassenMultiplication.multiply(A, I);

        assertArrayEquals(dataA, product.retrieveRowMajorAs2D());
    }

    /**
     * Tests multiplication with a zero matrix.
     */
    @Test
    void testMultiplicationWithZeroMatrix() {
        double[][] dataA = {{3, 4}, {5, 6}};
        Matrix zeroMatrix = Matrix.zeroMatrix(2);
        double[][] expectedProduct = {{0, 0}, {0, 0}};

        Matrix A = new Matrix(2, dataA);
        Matrix Z = zeroMatrix;

        StrassenMultiplication.setWinogradOptimization(false);
        Matrix product = StrassenMultiplication.multiply(A, Z);

        assertArrayEquals(expectedProduct, product.retrieveRowMajorAs2D());
    }

    /**
     * Tests invalid matrix sizes for multiplication.
     */
    @Test
    void testInvalidMatrixMultiplication() {
        double[][] dataA = {{1, 2}, {3, 4}};
        double[][] dataB = {{1, 2, 3}, {4, 5, 6}}; // 2×3 matrix (invalid)

        Matrix A = new Matrix(2, dataA);
        assertThrows(IllegalArgumentException.class, () -> new Matrix(3, dataB)); // 3×3 is required, not 2×3
    }

    /**
     * Tests parallel vs sequential execution.
     */
    @Test
    void testParallelVsSequentialExecution() {
        int size = 128; // Medium-sized matrix
        double[][] dataA = new double[size][size];
        double[][] dataB = new double[size][size];

        for (int i = 0; i < size; i++) {
            dataA[i][i] = 1;
            dataB[i][i] = 1;
        }

        Matrix A = new Matrix(size, dataA);
        Matrix B = new Matrix(size, dataB);

        // Sequential Execution
        StrassenMultiplication.setParallelExecution(false);
        Matrix sequentialResult = StrassenMultiplication.multiply(A, B);
        double sequentialTime = StrassenMultiplication.getExecutionTime();

        // Parallel Execution
        StrassenMultiplication.setParallelExecution(true);
        Matrix parallelResult = StrassenMultiplication.multiply(A, B);
        double parallelTime = StrassenMultiplication.getExecutionTime();

        assertArrayEquals(sequentialResult.retrieveRowMajorAs2D(), parallelResult.retrieveRowMajorAs2D());
        assertTrue(parallelTime < sequentialTime, "Parallel execution should be faster than sequential.");
    }

    /**
     * Tests execution time tracking.
     */
    @Test
    void testExecutionTimeTracking() {
        double[][] dataA = {{1, 2}, {3, 4}};
        double[][] dataB = {{5, 6}, {7, 8}};

        Matrix A = new Matrix(2, dataA);
        Matrix B = new Matrix(2, dataB);
        StrassenMultiplication.setWinogradOptimization(false);
        StrassenMultiplication.multiply(A, B);

        assertTrue(StrassenMultiplication.getExecutionTime() > 0, "Execution time should be greater than 0.");
    }

    /**
     * Tests multiplication count tracking.
     */
    @Test
    void testMultiplicationCountTracking() {
        double[][] dataA = {{1, 2}, {3, 4}};
        double[][] dataB = {{5, 6}, {7, 8}};

        Matrix A = new Matrix(2, dataA);
        Matrix B = new Matrix(2, dataB);
        StrassenMultiplication.setWinogradOptimization(false);
        StrassenMultiplication.multiply(A, B);

        assertTrue(StrassenMultiplication.getMultiplicationCount() > 0, "Multiplication count should be greater than 0.");
    }

    /**
     * Tests performance metrics integration.
     */
    @Test
    void testPerformanceMetricsIntegration() {
        double[][] dataA = {{1, 2}, {3, 4}};
        double[][] dataB = {{5, 6}, {7, 8}};

        Matrix A = new Matrix(2, dataA);
        Matrix B = new Matrix(2, dataB);
        StrassenMultiplication.setWinogradOptimization(false);
        StrassenMultiplication.multiply(A, B);

        assertTrue(StrassenMultiplication.getMultiplicationCount() > 0);
        assertTrue(StrassenMultiplication.getExecutionTime() > 0);
    }
}
