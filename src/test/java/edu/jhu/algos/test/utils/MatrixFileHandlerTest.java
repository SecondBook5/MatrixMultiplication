package edu.jhu.algos.test.utils;

import edu.jhu.algos.models.Matrix;
import edu.jhu.algos.utils.MatrixFileHandler;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MatrixFileHandler class.
 * - Tests reading matrices from various file formats.
 * - Ensures error handling for invalid input files.
 * - Verifies that matrices are correctly written to output files.
 */
public class MatrixFileHandlerTest {

    private static final String TEST_FILE = "test_matrices.txt"; // Temporary test file

    /**
     * Tests reading matrices from a correctly formatted structured file.
     * Validates that matrix pairs are read correctly.
     */
    @Test
    void testReadMatricesFromFile() throws IOException {
        String content = """
                2
                1 2
                3 4
                5 6
                7 8

                3
                1 2 3
                4 5 6
                7 8 9
                10 11 12
                13 14 15
                16 17 18
                """;
        writeTestFile(content);

        List<Matrix[]> matrices = MatrixFileHandler.readMatricesFromFile(TEST_FILE);

        assertEquals(2, matrices.size()); // Expecting 2 matrix pairs
        assertEquals(2, matrices.get(0)[0].getRows()); // First matrix size should be 2x2
        assertEquals(3, matrices.get(1)[0].getRows()); // Second matrix size should be 3x3
    }

    /**
     * Tests reading a CSV-formatted matrix file.
     * Ensures that comma-separated values are correctly parsed.
     */
    @Test
    void testReadCSVMatrixFile() throws IOException {
        String content = """
                2
                1,2
                3,4
                5,6
                7,8
                """;
        writeTestFile(content);

        List<Matrix[]> matrices = MatrixFileHandler.readMatricesFromFile(TEST_FILE);

        assertEquals(1, matrices.size()); // Expecting 1 matrix pair
        assertEquals(2, matrices.get(0)[0].getRows()); // Matrix size should be 2x2
        assertEquals(5.0, matrices.get(0)[1].getData(true)[0][0]); // Verify first element of matrix B
    }

    /**
     * Tests reading a JSON-like matrix file.
     * Ensures JSON-style `[ [1,2], [3,4] ]` input is correctly parsed.
     */
    @Test
    void testReadJsonMatrixFile() throws IOException {
        String content = """
                [[1,2],[3,4]]
                [[5,6],[7,8]]
                """;
        writeTestFile(content);

        List<Matrix[]> matrices = MatrixFileHandler.readMatricesFromFile(TEST_FILE);

        assertEquals(1, matrices.size()); // Expecting 1 matrix pair
        assertEquals(1.0, matrices.get(0)[0].getData(true)[0][0]); // Verify first element of matrix A
        assertEquals(6.0, matrices.get(0)[1].getData(true)[0][1]); // Verify first row, second column of matrix B
    }

    /**
     * Tests reading a flat file format (without explicit matrix size).
     * The matrix size should be inferred dynamically.
     */
    @Test
    void testReadFlatFileMatrix() throws IOException {
        String content = """
                1 2
                3 4

                5 6
                7 8
                """;
        writeTestFile(content);

        List<Matrix[]> matrices = MatrixFileHandler.readMatricesFromFile(TEST_FILE);

        assertEquals(1, matrices.size()); // Expecting 1 matrix pair
        assertEquals(2, matrices.get(0)[0].getRows()); // Matrix size should be 2x2
        assertEquals(7.0, matrices.get(0)[1].getData(true)[1][0]); // Verify second row, first column of matrix B
    }

    /**
     * Tests error handling for an invalid matrix format.
     * Ensures that the parser throws an `IllegalArgumentException` for a malformed file.
     */
    @Test
    void testInvalidMatrixFormat() throws IOException {
        String content = """
                3
                1 2
                4 5 6
                """;
        writeTestFile(content);

        assertThrows(IllegalArgumentException.class, () -> MatrixFileHandler.readMatricesFromFile(TEST_FILE));
    }

    /**
     * Tests error handling for an invalid numeric entry.
     * Ensures that a matrix with non-numeric values is correctly rejected.
     */
    @Test
    void testInvalidNumericEntry() throws IOException {
        String content = """
                2
                1 2
                X 4
                5 6
                7 8
                """;
        writeTestFile(content);

        assertThrows(IllegalArgumentException.class, () -> MatrixFileHandler.readMatricesFromFile(TEST_FILE));
    }

    /**
     * Tests writing a matrix to a file.
     * Ensures that the output file correctly stores the matrix.
     */
    @Test
    void testWriteMatrixToFile() throws IOException {
        Matrix matrix = new Matrix(2, 2, new double[][]{{1, 2}, {3, 4}});
        MatrixFileHandler.writeMatrixToFile(matrix, TEST_FILE);

        BufferedReader reader = new BufferedReader(new FileReader(TEST_FILE));
        assertNotNull(reader.readLine()); // Ensure the file is not empty
        reader.close();
    }

    /**
     * Writes a test file with the provided content.
     *
     * @param content The string content to write to the file.
     * @throws IOException If writing fails.
     */
    private void writeTestFile(String content) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE));
        writer.write(content);
        writer.close();
    }
}
