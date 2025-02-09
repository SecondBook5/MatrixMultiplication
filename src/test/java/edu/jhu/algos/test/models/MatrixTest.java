package edu.jhu.algos.test.models;

import edu.jhu.algos.models.Matrix;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MatrixTest {

    @Test
    void testMatrixCreation() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        Matrix matrix = new Matrix(2, data);
        assertEquals(2, matrix.retrieveRowMajorAs2D().length);
        assertEquals(2, matrix.retrieveRowMajorAs2D()[0].length);
    }

    @Test
    void testMatrixInitializationErrors() {
        assertThrows(NullPointerException.class, () -> new Matrix(2, null));
        assertThrows(IllegalArgumentException.class, () -> new Matrix(3, new double[3][3]));
        assertThrows(IllegalArgumentException.class, () -> new Matrix(4, new double[4][3]));
    }

    @Test
    void testRetrieveRowMajorAs2D() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        Matrix matrix = new Matrix(2, data);
        assertArrayEquals(data, matrix.retrieveRowMajorAs2D());
    }

    @Test
    void testMatrixAddition() {
        double[][] dataA = {{1.0, 2.0}, {3.0, 4.0}};
        double[][] dataB = {{5.0, 6.0}, {7.0, 8.0}};
        double[][] expectedSum = {{6.0, 8.0}, {10.0, 12.0}};
        Matrix A = new Matrix(2, dataA);
        Matrix B = new Matrix(2, dataB);
        assertArrayEquals(expectedSum, A.add(B).retrieveRowMajorAs2D());
    }

    @Test
    void testMatrixSubtraction() {
        double[][] dataA = {{5.0, 6.0}, {7.0, 8.0}};
        double[][] dataB = {{1.0, 2.0}, {3.0, 4.0}};
        double[][] expectedDiff = {{4.0, 4.0}, {4.0, 4.0}};
        Matrix A = new Matrix(2, dataA);
        Matrix B = new Matrix(2, dataB);
        assertArrayEquals(expectedDiff, A.subtract(B).retrieveRowMajorAs2D());
    }

    @Test
    void testMatrixEquality() {
        double[][] dataA = {{1.0, 2.0}, {3.0, 4.0}};
        double[][] dataB = {{1.0, 2.0}, {3.0, 4.0}};
        double[][] dataC = {{5.0, 6.0}, {7.0, 8.0}};
        Matrix A = new Matrix(2, dataA);
        Matrix B = new Matrix(2, dataB);
        Matrix C = new Matrix(2, dataC);
        assertEquals(A, B);
        assertNotEquals(A, C);
    }

    @Test
    void testMethodChaining() {
        double[][] dataA = {{1.0, 2.0}, {3.0, 4.0}};
        double[][] dataB = {{2.0, 2.0}, {2.0, 2.0}};
        double[][] dataC = {{1.0, 1.0}, {1.0, 1.0}};
        double[][] expectedResult = {{2.0, 3.0}, {4.0, 5.0}};
        Matrix A = new Matrix(2, dataA);
        Matrix B = new Matrix(2, dataB);
        Matrix C = new Matrix(2, dataC);
        assertArrayEquals(expectedResult, A.add(B).subtract(C).retrieveRowMajorAs2D());
    }

    @Test
    void testZeroMatrixFactoryMethod() {
        Matrix zeroMatrix = Matrix.zeroMatrix(2);
        double[][] expected = {{0.0, 0.0}, {0.0, 0.0}};
        assertArrayEquals(expected, zeroMatrix.retrieveRowMajorAs2D());
    }

    @Test
    void testZeroMatrixInvalidSize() {
        assertThrows(IllegalArgumentException.class, () -> Matrix.zeroMatrix(-1));
        assertThrows(IllegalArgumentException.class, () -> Matrix.zeroMatrix(3));
    }

    @Test
    void testMatrixDataIntegrity() {
        double[][] originalData = {{1.0, 2.0}, {3.0, 4.0}};
        double[][] additionData = {{5.0, 6.0}, {7.0, 8.0}};
        Matrix A = new Matrix(2, originalData);
        Matrix B = new Matrix(2, additionData);
        A.add(B);
        A.subtract(B);
        assertArrayEquals(originalData, A.retrieveRowMajorAs2D());
        assertArrayEquals(additionData, B.retrieveRowMajorAs2D());
    }

    @Test
    void testLargeMatrixOperations() {
        int size = 1024;
        double[][] dataA = new double[size][size];
        double[][] dataB = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                dataA[i][j] = 1.0;
                dataB[i][j] = 2.0;
            }
        }
        Matrix A = new Matrix(size, dataA);
        Matrix B = new Matrix(size, dataB);
        Matrix sum = A.add(B);
        assertEquals(3.0, sum.retrieveRowMajorAs2D()[0][0]);
        assertEquals(3.0, sum.retrieveRowMajorAs2D()[size - 1][size - 1]);
    }

    @Test
    void testIdentityMatrixOperations() {
        double[][] dataA = {{1.0, 2.0}, {3.0, 4.0}};
        double[][] identity = {{1.0, 0.0}, {0.0, 1.0}};
        double[][] expectedSum = {{2.0, 2.0}, {3.0, 5.0}};
        double[][] expectedDiff = {{0.0, 2.0}, {3.0, 3.0}};
        Matrix A = new Matrix(2, dataA);
        Matrix I = new Matrix(2, identity);
        assertArrayEquals(expectedSum, A.add(I).retrieveRowMajorAs2D());
        assertArrayEquals(expectedDiff, A.subtract(I).retrieveRowMajorAs2D());
    }


    /**
     * Tests that two equal matrices have the same hash code.
     */
    @Test
    void testHashCodeConsistency() {
        double[][] dataA = {{1.0, 2.0}, {3.0, 4.0}};
        double[][] dataB = {{1.0, 2.0}, {3.0, 4.0}};
        Matrix A = new Matrix(2, dataA);
        Matrix B = new Matrix(2, dataB);

        assertEquals(A.hashCode(), B.hashCode());
    }

    /**
     * Tests that the string representation of a matrix is formatted correctly.
     */
    @Test
    void testToString() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        Matrix matrix = new Matrix(2, data);

        String expectedOutput =
                """
                        Matrix (2x2):
                        |  1.00  2.00 |
                        |  3.00  4.00 |
                        """;

        assertEquals(expectedOutput.trim(), matrix.toString().trim());
    }

    /**
     * Tests that creating a non-power-of-two matrix throws an exception.
     */
    @Test
    void testNonPowerOfTwoMatrixRejection() {
        assertThrows(IllegalArgumentException.class, () -> new Matrix(3, new double[3][3]));
        assertThrows(IllegalArgumentException.class, () -> new Matrix(5, new double[5][5]));
    }

    /**
     * Tests that getSize() returns the correct size of the matrix.
     */
    @Test
    void testGetSize() {
        Matrix matrix = new Matrix(4, new double[4][4]);
        assertEquals(4, matrix.getSize());
    }
}

