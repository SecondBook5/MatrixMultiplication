package edu.jhu.algos.test.io;

import edu.jhu.algos.io.MatrixFileHandler;
import edu.jhu.algos.models.Matrix;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MatrixFileHandler.
 * Ensures correct reading of matrix files and error handling.
 */
public class MatrixFileHandlerTest {

    /**
     * Tests if MatrixFileHandler correctly reads a properly formatted input file.
     */
    @Test
    void testValidMatrixFile() throws IOException {
        // Sample valid input
        String input = """
            2
            1 2
            3 4
            5 6
            7 8
            
            4
            1 2 3 4
            5 6 7 8
            9 10 11 12
            13 14 15 16
            17 18 19 20
            21 22 23 24
            25 26 27 28
            29 30 31 32
            """;

        // Create temporary file
        Path tempFile = Files.createTempFile("matrix_test", ".txt");
        Files.writeString(tempFile, input);

        // Read file using MatrixFileHandler
        MatrixFileHandler handler = new MatrixFileHandler();
        List<Matrix[]> pairs = handler.readMatrixPairs(tempFile.toString());

        // Ensure 2 matrix pairs are read
        assertEquals(2, pairs.size(), "Incorrect number of matrix pairs read.");

        // Verify first 2x2 matrix multiplication input
        Matrix A1 = pairs.get(0)[0];
        Matrix B1 = pairs.get(0)[1];
        assertArrayEquals(new int[][]{{1, 2}, {3, 4}}, A1.getData(), "Matrix A1 is incorrect.");
        assertArrayEquals(new int[][]{{5, 6}, {7, 8}}, B1.getData(), "Matrix B1 is incorrect.");

        // Verify second 4x4 matrix multiplication input
        Matrix A2 = pairs.get(1)[0];
        Matrix B2 = pairs.get(1)[1];
        assertArrayEquals(new int[][]{
                        {1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}}, A2.getData(),
                "Matrix A2 is incorrect.");
        assertArrayEquals(new int[][]{
                        {17, 18, 19, 20}, {21, 22, 23, 24}, {25, 26, 27, 28}, {29, 30, 31, 32}}, B2.getData(),
                "Matrix B2 is incorrect.");
    }

    /**
     * Tests if the handler properly handles missing matrix data.
     */
    @Test
    void testMissingMatrixData() throws IOException {
        // Incomplete matrix (missing last row of matrix B)
        String input = """
            3
            1 2 3
            4 5 6
            7 8 9
            10 11 12
            13 14 15
            """;

        Path tempFile = Files.createTempFile("matrix_test", ".txt");
        Files.writeString(tempFile, input);

        MatrixFileHandler handler = new MatrixFileHandler();

        // Expect an exception due to missing rows in matrix B
        Exception exception = assertThrows(IOException.class, () -> handler.readMatrixPairs(tempFile.toString()));
        assertTrue(exception.getMessage().contains("Unexpected end of file"), "Did not catch missing matrix data.");
    }

    /**
     * Tests if the handler detects an invalid matrix size.
     */
    @Test
    void testInvalidMatrixSize() throws IOException {
        String input = """
            -2
            1 2
            3 4
            5 6
            7 8
            """;

        Path tempFile = Files.createTempFile("matrix_test", ".txt");
        Files.writeString(tempFile, input);

        MatrixFileHandler handler = new MatrixFileHandler();

        // Expect an exception due to invalid size (-2)
        Exception exception = assertThrows(IOException.class, () -> handler.readMatrixPairs(tempFile.toString()));
        assertTrue(exception.getMessage().contains("Matrix size must be a positive integer"),
                "Did not correctly handle invalid matrix size.");
    }

    /**
     * Tests if the handler detects non-numeric data in matrix entries.
     */
    @Test
    void testNonNumericData() throws IOException {
        String input = """
            2
            1 2
            3 x
            5 6
            7 8
            """;

        Path tempFile = Files.createTempFile("matrix_test", ".txt");
        Files.writeString(tempFile, input);

        MatrixFileHandler handler = new MatrixFileHandler();

        // Expect an exception due to "x" being non-numeric
        Exception exception = assertThrows(IOException.class, () -> handler.readMatrixPairs(tempFile.toString()));
        assertTrue(exception.getMessage().contains("Invalid number"),
                "Did not correctly handle non-numeric data.");
    }

    /**
     * Tests if the handler detects mismatched row sizes.
     */
    @Test
    void testIncorrectRowSize() throws IOException {
        String input = """
            3
            1 2
            4 5 6
            7 8 9
            10 11 12
            13 14 15
            16 17 18
            """;

        Path tempFile = Files.createTempFile("matrix_test", ".txt");
        Files.writeString(tempFile, input);

        MatrixFileHandler handler = new MatrixFileHandler();

        // Expect an exception due to incorrect number of columns in first row
        Exception exception = assertThrows(IOException.class, () -> handler.readMatrixPairs(tempFile.toString()));
        assertTrue(exception.getMessage().contains("Incorrect number of columns"),
                "Did not correctly handle incorrect row size.");
    }

    /**
     * Tests if the handler correctly handles a file with only blank lines.
     */
    @Test
    void testBlankFile() throws IOException {
        String input = "\n\n\n";  // Only blank lines

        Path tempFile = Files.createTempFile("matrix_test", ".txt");
        Files.writeString(tempFile, input);

        MatrixFileHandler handler = new MatrixFileHandler();
        List<Matrix[]> pairs = handler.readMatrixPairs(tempFile.toString());

        // Expect zero pairs to be read
        assertEquals(0, pairs.size(), "Blank file should result in zero matrix pairs.");
    }
}
