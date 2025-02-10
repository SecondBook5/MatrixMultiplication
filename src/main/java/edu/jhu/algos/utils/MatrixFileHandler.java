package edu.jhu.algos.utils;

import edu.jhu.algos.models.Matrix;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles reading and writing matrices from/to files.
 * - Reads structured input files from the `input/` directory.
 * - Writes computed matrices to the `output/` directory.
 * - Ensures directories exist before reading/writing.
 * - Provides detailed error messages for debugging.
 */
public class MatrixFileHandler {

    private static final String INPUT_DIR = "input";   // Default input directory at project root
    private static final String OUTPUT_DIR = "output"; // Default output directory at project root

    /**
     * Reads multiple matrices from a structured input file.
     * @param filename The file name to read from (inside `input/`).
     * @return A list of matrices read from the file.
     * @throws IOException If an error occurs while reading the file.
     */
    public static List<Matrix> readMatrices(String filename) throws IOException {
        List<Matrix> matrices = new ArrayList<>(); // Stores the matrices found in the file
        Path filePath = Path.of(INPUT_DIR, filename); // Full path to the input file

        if (!filename.endsWith(".txt")) {
            throw new IOException("Invalid file format: " + filename + ". Only .txt files are supported.");
        }

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("Input file not found: " + filePath.toAbsolutePath());
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) { // BufferedReader for efficient reading
            String line;
            int matrixNumber = 1; // Track which matrix we're reading
            while ((line = reader.readLine()) != null) { // Read each line until EOF
                line = line.trim();
                if (line.isEmpty()) continue; // Skip blank lines

                int size = getSize(filename, line, matrixNumber);

                double[][] data = new double[size][size]; // Initialize matrix storage

                for (int i = 0; i < size; i++) {
                    line = reader.readLine(); // Read next row of the matrix
                    if (line == null) throw new IOException("Unexpected end of file while reading matrix #" + matrixNumber + " in " + filename);

                    String[] values = line.trim().split("\\s+"); // Split values by whitespace
                    if (values.length != size) {
                        throw new IOException("Row length mismatch in matrix #" + matrixNumber + " in " + filename + ": expected " + size + " values, found " + values.length);
                    }

                    for (int j = 0; j < size; j++) {
                        try {
                            data[i][j] = Double.parseDouble(values[j]); // Parse each value into a double
                        } catch (NumberFormatException e) {
                            throw new IOException("Invalid numeric value in matrix #" + matrixNumber + " at row " + i + ", column " + j + " in " + filename);
                        }
                    }
                }
                matrices.add(new Matrix(size, data)); // Add parsed matrix to list
                matrixNumber++; // Move to next matrix
            }
        }

        if (matrices.isEmpty()) {
            throw new IOException("File " + filename + " does not contain any valid matrices.");
        }

        return matrices;
    }

    /**
     * Extracts matrix size from the input file.
     * @param filename The filename being processed.
     * @param line The line containing the matrix size.
     * @param matrixNumber The current matrix number being processed.
     * @return The extracted matrix size.
     * @throws IOException If the size is invalid.
     */
    private static int getSize(String filename, String line, int matrixNumber) throws IOException {
        int size;
        try {
            size = Integer.parseInt(line); // Read the matrix size from the first line
            if (size <= 0) {
                throw new IOException("Invalid matrix size in file: " + filename + ". Matrix #" + matrixNumber + " has non-positive size.");
            }
        } catch (NumberFormatException e) {
            throw new IOException("Invalid matrix size format in file: " + filename + ". Matrix #" + matrixNumber);
        }
        return size;
    }

    /**
     * Writes a matrix to a structured output file.
     * @param matrix The matrix to write.
     * @param filename The output file name (inside `output/`).
     * @throws IOException If an error occurs while writing.
     */
    public static void writeMatrix(Matrix matrix, String filename) throws IOException {
        ensureDirectoryExists(OUTPUT_DIR); // Ensure output directory exists
        Path filePath = Path.of(OUTPUT_DIR, filename); // Full path to the output file

        if (!filename.endsWith(".txt")) {
            throw new IOException("Invalid file format: " + filename + ". Only .txt files are supported.");
        }

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(matrix.getSize() + "\n"); // Write matrix size
            double[][] data = matrix.retrieveRowMajorAs2D(); // Retrieve matrix data

            for (double[] row : data) {
                for (double value : row) {
                    writer.write(String.format("%8.4f ", value)); // Format output with 4 decimal places
                }
                writer.newLine(); // Move to next row
            }
        } catch (IOException e) {
            throw new IOException("Error writing matrix to file: " + filePath.toAbsolutePath(), e);
        }
    }

    /**
     * Ensures that the output directory exists before writing files.
     * @param directoryPath The directory path to check.
     * @throws IOException If the directory cannot be created.
     */
    private static void ensureDirectoryExists(String directoryPath) throws IOException {
        Path path = Path.of(directoryPath);
        if (!Files.exists(path)) { // Check if directory exists
            Files.createDirectories(path); // Create directory if it doesn't exist
        }
    }
}
