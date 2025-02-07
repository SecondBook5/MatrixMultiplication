package edu.jhu.algos.utils; // Define the package where this class belongs

import edu.jhu.algos.models.Matrix; // Import the Matrix class to represent matrices
import java.io.*; // Import Java I/O classes for reading and writing files
import java.util.ArrayList; // Import ArrayList to dynamically store matrix pairs
import java.util.List; // Import List interface for handling multiple matrix pairs

/**
 * Handles reading and writing matrices from structured text files.
 * - Supports primary structured format (size header, two matrices).
 * - Detects and processes alternative formats (CSV, JSON-like, flat file).
 */
public class MatrixFileHandler {

    /**
     * Reads multiple matrix pairs from a structured file.
     * Supports primary structured format and detects alternate formats.
     *
     * **Supported Formats:**
     * - Primary Format: Matrix size, followed by `A` and `B` matrices.
     * - CSV Format: Comma-separated values instead of space-separated.
     * - JSON-like: Detects `[[...]]` notation and parses JSON-style input.
     * - Flat File: No explicit size header, infers size dynamically.
     *
     * @param filePath The file path of the matrix input file.
     * @return A list of matrix pairs [(A1, B1), (A2, B2), ...]
     * @throws IOException If there is an error reading the file.
     * @throws IllegalArgumentException If file format is invalid.
     */
    public static List<Matrix[]> readMatricesFromFile(String filePath) throws IOException {
        List<Matrix[]> matrixPairs = new ArrayList<>(); // Create a list to store pairs of matrices

        // Try-with-resources ensures the BufferedReader is automatically closed after reading
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line; // Variable to store each line read from the file

            // Process each line in the file
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Remove any leading or trailing spaces
                if (line.isEmpty()) continue; // Skip blank lines to avoid unnecessary processing

                // Detect format: If it starts with '[', assume JSON-like format
                if (line.startsWith("[")) {
                    double[][] matrixA = parseJsonMatrix(line); // Parse JSON-like matrix A
                    double[][] matrixB = parseJsonMatrix(reader.readLine().trim()); // Parse JSON-like matrix B
                    int size = matrixA.length; // Infer matrix size from row count
                    matrixPairs.add(new Matrix[]{new Matrix(size, size, matrixA), new Matrix(size, size, matrixB)});
                    continue; // Skip the rest of the loop for this iteration
                }

                // Detect and parse flat file format (no explicit size header)
                if (!line.matches("\\d+")) { // If first line is not a number, assume flat file
                    double[][] matrixA = parseFlatMatrix(reader, line); // Parse matrix A
                    double[][] matrixB = parseFlatMatrix(reader, null); // Parse matrix B
                    int size = matrixA.length; // Infer matrix size from row count
                    matrixPairs.add(new Matrix[]{new Matrix(size, size, matrixA), new Matrix(size, size, matrixB)});
                    continue; // Skip the rest of the loop for this iteration
                }

                // Default: Primary structured format (with explicit size header)
                int size = parseMatrixSize(line); // Parse matrix size from the first line
                double[][] matrixA = readMatrix(reader, size); // Read the first matrix (A)
                double[][] matrixB = readMatrix(reader, size); // Read the second matrix (B)
                matrixPairs.add(new Matrix[]{new Matrix(size, size, matrixA), new Matrix(size, size, matrixB)});
            }
        }
        return matrixPairs; // Return the list containing all matrix pairs
    }

    /**
     * Parses a JSON-like matrix format `[[1,2,3],[4,5,6],[7,8,9]]`.
     *
     * @param jsonLine A single-line JSON-like matrix.
     * @return A 2D double array representing the matrix.
     * @throws IllegalArgumentException If JSON format is invalid.
     */
    private static double[][] parseJsonMatrix(String jsonLine) {
        jsonLine = jsonLine.trim();

        // Ensure it starts and ends with brackets (valid JSON-like format)
        if (!jsonLine.startsWith("[[") || !jsonLine.endsWith("]]")) {
            throw new IllegalArgumentException("Invalid JSON matrix format: " + jsonLine);
        }

        // Remove outer brackets [[...]] to extract matrix data
        jsonLine = jsonLine.substring(2, jsonLine.length() - 2);

        // Split into rows (handling cases with and without spaces)
        String[] rows = jsonLine.split("\\],\\[");

        int size = rows.length; // Matrix size inferred from row count
        double[][] matrix = new double[size][size];

        for (int i = 0; i < size; i++) {
            String[] values = rows[i].split(","); // Split row values by comma

            if (values.length != size) { // Ensure correct number of columns
                throw new IllegalArgumentException("Malformed JSON matrix row: " + rows[i]);
            }

            for (int j = 0; j < size; j++) {
                try {
                    matrix[i][j] = Double.parseDouble(values[j].trim()); // Convert to double
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number at row " + i + ", column " + j + ": " + values[j]);
                }
            }
        }
        return matrix;
    }

    /**
     * Parses a flat matrix format (no explicit size header).
     * Auto-detects matrix size based on row count.
     *
     * @param reader BufferedReader to read from.
     * @param firstLine First line if already read (or null to read from file).
     * @return A 2D double array representing the matrix.
     * @throws IOException If file reading fails.
     */
    private static double[][] parseFlatMatrix(BufferedReader reader, String firstLine) throws IOException {
        List<String> lines = new ArrayList<>(); // List to temporarily store matrix rows
        if (firstLine != null) lines.add(firstLine.trim()); // Add first line if provided

        String line;
        while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
            lines.add(line.trim()); // Read and trim each line
        }

        int size = lines.size(); // Determine the matrix size dynamically
        double[][] matrix = new double[size][size]; // Create a square matrix

        for (int i = 0; i < size; i++) {
            String[] values = lines.get(i).split("\\s+|,"); // Support both space and comma separation
            if (values.length != size) throw new IllegalArgumentException("Malformed matrix row: " + lines.get(i));

            for (int j = 0; j < size; j++) {
                matrix[i][j] = Double.parseDouble(values[j].trim()); // Convert each value to double
            }
        }
        return matrix;
    }

    /**
     * Parses the matrix size from a line.
     *
     * @param line The line containing the matrix size.
     * @return The parsed size as an integer.
     * @throws IllegalArgumentException If the size is invalid.
     */
    private static int parseMatrixSize(String line) {
        try {
            int size = Integer.parseInt(line); // Convert string to integer
            if (size <= 0) throw new NumberFormatException(); // Ensure size is positive
            return size;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid matrix size: " + line);
        }
    }

    /**
     * Reads a matrix of given size from the file.
     *
     * @param reader The BufferedReader to read from.
     * @param size The matrix size.
     * @return The matrix as a 2D double array.
     * @throws IOException If there is an error reading the file.
     */
    private static double[][] readMatrix(BufferedReader reader, int size) throws IOException {
        double[][] matrix = new double[size][size]; // Create a square matrix

        for (int i = 0; i < size; i++) {
            String line = reader.readLine();
            if (line == null || line.trim().isEmpty()) throw new IllegalArgumentException("Incomplete matrix data at row " + i);

            String[] values = line.trim().split("\\s+|,"); // Support space and comma separation
            if (values.length != size) throw new IllegalArgumentException("Expected " + size + " values in row " + i + ", found " + values.length);

            for (int j = 0; j < size; j++) {
                matrix[i][j] = Double.parseDouble(values[j]); // Convert to double
            }
        }
        return matrix;
    }
    /**
     * Writes a matrix to a file in a human-readable format.
     *
     * @param matrix The matrix to write.
     * @param filePath The file path to save the matrix.
     * @throws IOException If there is an error writing the file.
     */
    public static void writeMatrixToFile(Matrix matrix, String filePath) throws IOException {
        // Try-with-resources ensures BufferedWriter is closed automatically
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(matrix.toString()); // Convert matrix to string format and write to file
        }
    }
}
