package edu.jhu.algos.test.utils;

import edu.jhu.algos.models.Matrix;
import edu.jhu.algos.utils.MatrixOperations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MatrixOperations.
 * - Validates correct matrix operations.
 * - Ensures error handling is robust.
 */
public class MatrixOperationsTest {
    private Matrix A, B, C;

    @BeforeEach
    void setUp() {
        double[][] dataA = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };

        double[][] dataB = {
                {16, 15, 14, 13},
                {12, 11, 10, 9},
                {8, 7, 6, 5},
                {4, 3, 2, 1}
        };

        double[][] dataC = {  // Different size (invalid case)
                {1, 2},
                {3, 4}
        };

        A = new Matrix(4, dataA);
        B = new Matrix(4, dataB);
        C = new Matrix(2, dataC); // Used for mismatched size tests
    }

    @Test
    void testAddition() {
        Matrix result = MatrixOperations.add(A, B);
        double[][] expectedData = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                expectedData[i][j] = A.retrieveRowMajorAs2D()[i][j] + B.retrieveRowMajorAs2D()[i][j];
            }
        }
        assertArrayEquals(expectedData, result.retrieveRowMajorAs2D());
    }

    @Test
    void testSubtraction() {
        Matrix result = MatrixOperations.subtract(A, B);
        double[][] expectedData = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                expectedData[i][j] = A.retrieveRowMajorAs2D()[i][j] - B.retrieveRowMajorAs2D()[i][j];
            }
        }
        assertArrayEquals(expectedData, result.retrieveRowMajorAs2D());
    }

    @Test
    void testAdditionWithNullMatrix() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                MatrixOperations.add(A, null)
        );
        assertTrue(exception.getMessage().contains("Cannot add null matrices."));
    }

    @Test
    void testSubtractionWithNullMatrix() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                MatrixOperations.subtract(null, B)
        );
        assertTrue(exception.getMessage().contains("Cannot subtract null matrices."));
    }

    @Test
    void testAdditionWithMismatchedSize() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                MatrixOperations.add(A, C)
        );
        assertTrue(exception.getMessage().contains("Matrices must have the same dimensions."));
    }

    @Test
    void testValidSplit() {
        Matrix subMatrix = MatrixOperations.split(A, 0, 0, 2);
        double[][] expectedData = {
                {1, 2},
                {5, 6}
        };
        assertArrayEquals(expectedData, subMatrix.retrieveRowMajorAs2D());
    }

    @Test
    void testSplitOutOfBounds() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                MatrixOperations.split(A, 3, 3, 2)
        );
        assertTrue(exception.getMessage().contains("Requested submatrix is out of bounds."));
    }

    @Test
    void testValidMerge() {
        Matrix target = MatrixOperations.add(A, B);
        Matrix subMatrix = new Matrix(2, new double[][]{
                {100, 101},
                {102, 103}
        });

        MatrixOperations.merge(target, subMatrix, 1, 1);
        double[][] expectedData = target.retrieveRowMajorAs2D();
        expectedData[1][1] = 100;
        expectedData[1][2] = 101;
        expectedData[2][1] = 102;
        expectedData[2][2] = 103;

        assertArrayEquals(expectedData, target.retrieveRowMajorAs2D());
    }

    @Test
    void testMergeOutOfBounds() {
        Matrix subMatrix = new Matrix(4, new double[][]{
                {100, 101, 102, 103},
                {104, 105, 106, 107},
                {108, 109, 110, 111},
                {112, 113, 114, 115}
        });

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                MatrixOperations.merge(A, subMatrix, 2, 2)
        );

        assertTrue(exception.getMessage().contains("Submatrix exceeds target matrix bounds."));
    }

    @Test
    void testMergeNullSubmatrix() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                MatrixOperations.merge(A, null, 1, 1)
        );
        assertTrue(exception.getMessage().contains("Cannot merge null matrices."));
    }

    @Test
    void testDebugModeToggle() {
        MatrixOperations.setDebugMode(true);
        assertDoesNotThrow(() -> MatrixOperations.debugPrint(A, "Test Debug Mode Enabled"));

        MatrixOperations.setDebugMode(false);
        assertDoesNotThrow(() -> MatrixOperations.debugPrint(A, "Test Debug Mode Disabled"));
    }

    @Test
    void testGracefulExceptionHandling() {
        Matrix invalidMatrix = new Matrix(4, new double[][]{
                {1, 2, 3, 4},
                {5, 6},
                {7, 8, 9},
                {10, 11, 12, 13}
        });

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                MatrixOperations.add(A, invalidMatrix)
        );

        assertTrue(exception.getMessage().contains("Row 1 must have exactly 4 columns."));
    }
}
