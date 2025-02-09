package edu.jhu.algos.test.algorithms;

import edu.jhu.algos.algorithms.NaiveMultiplication;
import edu.jhu.algos.models.Matrix;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NaiveMultiplicationTest {

    @Test
    void testValidMultiplication() {
        double[][] dataA = {{1, 2}, {3, 4}};
        double[][] dataB = {{5, 6}, {7, 8}};
        double[][] expectedProduct = {{19, 22}, {43, 50}};

        Matrix A = new Matrix(2, dataA);
        Matrix B = new Matrix(2, dataB);
        Matrix product = NaiveMultiplication.multiply(A, B);

        assertArrayEquals(expectedProduct, product.retrieveRowMajorAs2D());
    }

    @Test
    void testIncompatibleMultiplication() {
        double[][] dataA = {{1, 2}, {3, 4}};
        double[][] dataB = {{5, 6, 7}, {8, 9, 10}}; // 2x3 matrix is invalid

        Matrix A = new Matrix(2, dataA);
        assertThrows(IllegalArgumentException.class, () -> new Matrix(3, dataB)); // 3x3 is required, not 2x3
    }

    @Test
    void testNullMatrixMultiplication() {
        Matrix A = new Matrix(2, new double[][]{{1, 2}, {3, 4}});
        assertThrows(IllegalArgumentException.class, () -> NaiveMultiplication.multiply(A, null));
        assertThrows(IllegalArgumentException.class, () -> NaiveMultiplication.multiply(null, A));
    }

    @Test
    void testEmptyMatrixMultiplication() {
        assertThrows(IllegalArgumentException.class, () -> new Matrix(0, new double[0][0]));
    }

    @Test
    void testMultiplicationWithIdentity() {
        double[][] dataA = {{3, 4}, {5, 6}};
        double[][] identityMatrix = {{1, 0}, {0, 1}};

        Matrix A = new Matrix(2, dataA);
        Matrix I = new Matrix(2, identityMatrix);
        Matrix product = NaiveMultiplication.multiply(A, I);

        assertArrayEquals(dataA, product.retrieveRowMajorAs2D());
    }

    @Test
    void testMultiplicationWithZeroMatrix() {
        double[][] dataA = {{3, 4}, {5, 6}};
        Matrix zeroMatrix = Matrix.zeroMatrix(2);
        double[][] expectedProduct = {{0, 0}, {0, 0}};

        Matrix A = new Matrix(2, dataA);
        Matrix Z = zeroMatrix;
        Matrix product = NaiveMultiplication.multiply(A, Z);

        assertArrayEquals(expectedProduct, product.retrieveRowMajorAs2D());
        assertEquals(0, NaiveMultiplication.getMultiplicationCount()); // Should be 0 since early exit occurs
    }

    @Test
    void testMultiplicationCount() {
        double[][] dataA = {{1, 2}, {3, 4}};
        double[][] dataB = {{5, 6}, {7, 8}};

        Matrix A = new Matrix(2, dataA);
        Matrix B = new Matrix(2, dataB);
        NaiveMultiplication.multiply(A, B);

        assertEquals(8, NaiveMultiplication.getMultiplicationCount()); // 2×2×2 = 8 multiplications
    }

    @Test
    void testExecutionTimeTracking() {
        double[][] dataA = {{1, 2}, {3, 4}};
        double[][] dataB = {{5, 6}, {7, 8}};

        Matrix A = new Matrix(2, dataA);
        Matrix B = new Matrix(2, dataB);
        NaiveMultiplication.multiply(A, B);

        assertTrue(NaiveMultiplication.getExecutionTime() > 0, "Execution time should be greater than 0.");
    }

    @Test
    void testLargeMatrixMultiplication() {
        int size = 128;
        double[][] dataA = new double[size][size];
        double[][] dataB = new double[size][size];

        for (int i = 0; i < size; i++) {
            dataA[i][i] = 1; // Identity-like
            dataB[i][i] = 1;
        }

        Matrix A = new Matrix(size, dataA);
        Matrix B = new Matrix(size, dataB);
        Matrix product = NaiveMultiplication.multiply(A, B);

        assertArrayEquals(A.retrieveRowMajorAs2D(), product.retrieveRowMajorAs2D());
    }

    @Test
    void testParallelExecutionToggle() {
        int size = 128;
        double[][] dataA = new double[size][size];
        double[][] dataB = new double[size][size];

        for (int i = 0; i < size; i++) {
            dataA[i][i] = 1;
            dataB[i][i] = 1;
        }

        Matrix A = new Matrix(size, dataA);
        Matrix B = new Matrix(size, dataB);

        NaiveMultiplication.setParallelExecution(true);
        Matrix parallelResult = NaiveMultiplication.multiply(A, B);
        NaiveMultiplication.setParallelExecution(false);
        Matrix sequentialResult = NaiveMultiplication.multiply(A, B);

        assertArrayEquals(parallelResult.retrieveRowMajorAs2D(), sequentialResult.retrieveRowMajorAs2D());
    }

    @Test
    void testParallelVsSequentialExecutionTime() {
        int size = 512;
        double[][] dataA = new double[size][size];
        double[][] dataB = new double[size][size];

        for (int i = 0; i < size; i++) {
            dataA[i][i] = 1;
            dataB[i][i] = 1;
        }

        Matrix A = new Matrix(size, dataA);
        Matrix B = new Matrix(size, dataB);

        NaiveMultiplication.setParallelExecution(false);
        NaiveMultiplication.multiply(A, B);
        double sequentialTime = NaiveMultiplication.getExecutionTime();

        NaiveMultiplication.setParallelExecution(true);
        NaiveMultiplication.multiply(A, B);
        double parallelTime = NaiveMultiplication.getExecutionTime();

        assertTrue(parallelTime < sequentialTime, "Parallel execution should be faster than sequential.");
    }

    @Test
    void testPerformanceMetricsIntegration() {
        double[][] dataA = {{1, 2}, {3, 4}};
        double[][] dataB = {{5, 6}, {7, 8}};

        Matrix A = new Matrix(2, dataA);
        Matrix B = new Matrix(2, dataB);
        NaiveMultiplication.multiply(A, B);

        assertTrue(NaiveMultiplication.getMultiplicationCount() > 0);
        assertTrue(NaiveMultiplication.getExecutionTime() > 0);
    }
}
