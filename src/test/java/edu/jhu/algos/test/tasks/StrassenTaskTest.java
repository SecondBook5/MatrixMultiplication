package edu.jhu.algos.test.tasks;

import edu.jhu.algos.models.Matrix;
import edu.jhu.algos.tasks.StrassenTask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StrassenTaskTest {

    /**
     * Test standard Strassen multiplication correctness.
     */
    @Test
    void testValidStrassenMultiplication() {
        double[][] dataA = {{1, 2}, {3, 4}};
        double[][] dataB = {{5, 6}, {7, 8}};
        double[][] expected = {{19, 22}, {43, 50}};

        Matrix A = new Matrix(2, dataA);
        Matrix B = new Matrix(2, dataB);
        Matrix result = new StrassenTask(A, B, false).executeTask();

        assertArrayEquals(expected, result.retrieveRowMajorAs2D());
    }

    /**
     * Test multiplication with an identity matrix (A * I = A).
     */
    @Test
    void testMultiplicationWithIdentityMatrix() {
        double[][] dataA = {{3, 4}, {5, 6}};
        double[][] identity = {{1, 0}, {0, 1}};

        Matrix A = new Matrix(2, dataA);
        Matrix I = new Matrix(2, identity);
        Matrix result = new StrassenTask(A, I, false).executeTask();

        assertArrayEquals(dataA, result.retrieveRowMajorAs2D());
    }

    /**
     * Test multiplication with a zero matrix (A * 0 = 0).
     */
    @Test
    void testMultiplicationWithZeroMatrix() {
        double[][] dataA = {{3, 4}, {5, 6}};
        Matrix zeroMatrix = Matrix.zeroMatrix(2);
        double[][] expected = {{0, 0}, {0, 0}};

        Matrix A = new Matrix(2, dataA);
        Matrix result = new StrassenTask(A, zeroMatrix, false).executeTask();

        assertArrayEquals(expected, result.retrieveRowMajorAs2D());
    }

    /**
     * Test multiplication count tracking.
     */
    @Test
    void testMultiplicationCountTracking() {
        double[][] dataA = {{1, 2}, {3, 4}};
        double[][] dataB = {{5, 6}, {7, 8}};

        Matrix A = new Matrix(2, dataA);
        Matrix B = new Matrix(2, dataB);
        StrassenTask.multiply(A, B, false);

        assertTrue(StrassenTask.getMultiplicationCount() > 0, "Multiplication count should be greater than 0.");
    }

    /**
     * Test execution time tracking.
     */
    @Test
    void testExecutionTimeTracking() {
        double[][] dataA = {{1, 2}, {3, 4}};
        double[][] dataB = {{5, 6}, {7, 8}};

        Matrix A = new Matrix(2, dataA);
        Matrix B = new Matrix(2, dataB);
        StrassenTask.multiply(A, B, false);

        assertTrue(StrassenTask.getExecutionTime() > 0, "Execution time should be greater than 0.");
    }

    /**
     * Test large matrix multiplication.
     */
    @Test
    void testLargeMatrixMultiplication() {
        int size = 128;
        double[][] dataA = new double[size][size];
        double[][] dataB = new double[size][size];

        // Initialize diagonal matrices (like identity but non-1 values)
        for (int i = 0; i < size; i++) {
            dataA[i][i] = 2;
            dataB[i][i] = 3;
        }

        Matrix A = new Matrix(size, dataA);
        Matrix B = new Matrix(size, dataB);
        Matrix result = new StrassenTask(A, B, false).executeTask();

        // Expected result should have 6s along the diagonal
        for (int i = 0; i < size; i++) {
            assertEquals(6, result.retrieveRowMajorAs2D()[i][i]);
        }
    }

    /**
     * Test parallel execution vs sequential execution.
     */
    @Test
    void testParallelVsSequentialExecution() {
        int size = 64;
        double[][] dataA = new double[size][size];
        double[][] dataB = new double[size][size];

        // Fill matrices with some values
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                dataA[i][j] = i + j;
                dataB[i][j] = i - j;
            }
        }

        Matrix A = new Matrix(size, dataA);
        Matrix B = new Matrix(size, dataB);

        // Sequential execution
        long startSequential = System.nanoTime();
        Matrix sequentialResult = new StrassenTask(A, B, false).executeTask();
        long endSequential = System.nanoTime();
        double sequentialTime = (endSequential - startSequential) / 1e6;

        // Parallel execution
        long startParallel = System.nanoTime();
        Matrix parallelResult = new StrassenTask(A, B, true).executeTask();
        long endParallel = System.nanoTime();
        double parallelTime = (endParallel - startParallel) / 1e6;

        // Ensure correctness
        assertArrayEquals(sequentialResult.retrieveRowMajorAs2D(), parallelResult.retrieveRowMajorAs2D());

        // Ensure parallel execution is faster
        assertTrue(parallelTime < sequentialTime, "Parallel execution should be faster than sequential.");
    }

    /**
     * Test invalid matrix dimensions (should throw an exception).
     */
    @Test
    void testInvalidMatrixMultiplication() {
        double[][] dataA = {{1, 2}, {3, 4}};
        double[][] dataB = {{5, 6, 7}, {8, 9, 10}}; // Not a square matrix

        Matrix A = new Matrix(2, dataA);
        assertThrows(IllegalArgumentException.class, () -> new Matrix(3, dataB)); // Should fail
    }

    /**
     * Test handling of null matrices.
     */
    @Test
    void testNullMatrixHandling() {
        Matrix A = new Matrix(2, new double[][]{{1, 2}, {3, 4}});
        assertThrows(IllegalArgumentException.class, () -> new StrassenTask(A, null, false).executeTask());
        assertThrows(IllegalArgumentException.class, () -> new StrassenTask(null, A, false).executeTask());
    }
}
