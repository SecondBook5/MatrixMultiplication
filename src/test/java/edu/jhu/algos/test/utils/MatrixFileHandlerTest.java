package edu.jhu.algos.test.utils;

import edu.jhu.algos.models.Matrix;
import edu.jhu.algos.utils.MatrixFileHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MatrixFileHandler.
 * Ensures proper reading and writing of matrix files, along with error handling.
 */
public class MatrixFileHandlerTest {

    private static final String TEST_INPUT_FILE = "input/test_matrix.txt";
    private static final String TEST_OUTPUT_FILE = "output/test_matrix_output.txt";

    /**
     * Sets up test directories before running tests.
     * Ensures input and output folders exist.
     */
    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectories(Path.of("input"));
        Files.createDirectories(Path.of("output"));
    }

    /**
     * Cleans up test files after running tests.
     */
    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(TEST_INPUT_FILE));
        Files.deleteIfExists(Path.of(TEST_OUTPUT_FILE));
    }

    /**
     * Tests successful reading of a valid matrix file.
     * Expected: Properly parsed matrix from file.
     */
    @Test
    void testReadValidMatrixFile() throws IOException {
        String matrixContent = """
                2
                1.0 2.0
                3.0 4.0
                """;
        Files.writeString(Path.of(TEST_INPUT_FILE), matrixContent);

        List<Matrix> matrices = MatrixFileHandler.readMatrices("test_matrix.txt");
        assertEquals(1, matrices.size(), "Expected 1 matrix in file.");
        assertEquals(2, matrices.get(0).getSize(), "Matrix size should be 2x2.");
    }

    /**
     * Tests handling of a missing file.
     * Expected: IOException with proper message.
     */
    @Test
    void testReadMissingFile() {
        IOException exception = assertThrows(IOException.class, () -> {
            MatrixFileHandler.readMatrices("non_existent_file.txt");
        });
        assertTrue(exception.getMessage().contains("Input file not found"), "Expected file not found error.");
    }

    /**
     * Tests handling of an empty file.
     * Expected: IOException with proper message.
     */
    @Test
    void testReadEmptyFile() throws IOException {
        Files.writeString(Path.of(TEST_INPUT_FILE), "");

        IOException exception = assertThrows(IOException.class, () -> {
            MatrixFileHandler.readMatrices("test_matrix.txt");
        });
        assertTrue(exception.getMessage().contains("does not contain any valid matrices"), "Expected empty file error.");
    }

    /**
     * Tests handling of an invalid matrix (wrong row size).
     * Expected: IOException due to mismatch between declared and actual row size.
     */
    @Test
    void testReadInvalidMatrixRowSize() throws IOException {
        String matrixContent = """
                2
                1.0 2.0 3.0
                4.0 5.0
                """;
        Files.writeString(Path.of(TEST_INPUT_FILE), matrixContent);

        IOException exception = assertThrows(IOException.class, () -> {
            MatrixFileHandler.readMatrices("test_matrix.txt");
        });
        assertTrue(exception.getMessage().contains("Row length mismatch"), "Expected row size mismatch error.");
    }

    /**
     * Tests handling of a non-numeric matrix value.
     * Expected: IOException due to invalid numeric value.
     */
    @Test
    void testReadInvalidNumericValue() throws IOException {
        String matrixContent = """
                2
                1.0 2.0
                3.0 X
                """;
        Files.writeString(Path.of(TEST_INPUT_FILE), matrixContent);

        IOException exception = assertThrows(IOException.class, () -> {
            MatrixFileHandler.readMatrices("test_matrix.txt");
        });
        assertTrue(exception.getMessage().contains("Invalid numeric value"), "Expected non-numeric value error.");
    }

    /**
     * Tests handling of an invalid file extension (should only accept `.txt`).
     * Expected: IOException due to unsupported file type.
     */
    @Test
    void testInvalidFileExtension() {
        IOException exception = assertThrows(IOException.class, () -> {
            MatrixFileHandler.readMatrices("test_matrix.csv");
        });
        assertTrue(exception.getMessage().contains("Invalid file format"), "Expected invalid file format error.");
    }

    /**
     * Tests successful writing of a matrix to an output file.
     * Expected: Properly formatted matrix written to file.
     */
    @Test
    void testWriteMatrixFile() throws IOException {
        double[][] data = {{1.1, 2.2}, {3.3, 4.4}};
        Matrix matrix = new Matrix(2, data);

        MatrixFileHandler.writeMatrix(matrix, "test_matrix_output.txt");

        assertTrue(Files.exists(Path.of(TEST_OUTPUT_FILE)), "Expected output file to be created.");

        List<String> lines = Files.readAllLines(Path.of(TEST_OUTPUT_FILE));
        assertEquals(3, lines.size(), "Expected 3 lines in the output file (size + 2 rows).");
        assertEquals("2", lines.get(0), "First line should be matrix size.");
    }

    /**
     * Tests handling of writing to an invalid output file format.
     * Expected: IOException due to unsupported file type.
     */
    @Test
    void testWriteInvalidFileExtension() {
        double[][] data = {{1, 2}, {3, 4}};
        Matrix matrix = new Matrix(2, data);

        IOException exception = assertThrows(IOException.class, () -> {
            MatrixFileHandler.writeMatrix(matrix, "test_output.csv");
        });
        assertTrue(exception.getMessage().contains("Invalid file format"), "Expected invalid file format error.");
    }

    /**
     * Tests that the output directory is created if it doesn't exist.
     * Expected: The directory should be automatically created.
     */
    @Test
    void testEnsureOutputDirectoryExists() throws IOException {
        Files.deleteIfExists(Path.of("output"));
        MatrixFileHandler.writeMatrix(new Matrix(2, new double[][]{{1, 2}, {3, 4}}), "test_matrix_output.txt");
        assertTrue(Files.exists(Path.of("output")), "Expected output directory to be created.");
    }
}
