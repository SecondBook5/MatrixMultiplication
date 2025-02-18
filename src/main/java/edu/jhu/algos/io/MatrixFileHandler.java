package edu.jhu.algos.io;

import edu.jhu.algos.models.Matrix;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles reading multiple matrix pairs from a file in row-major order.
 *
 * Expected format:
 * (1) A line with size n
 * (2) n lines for matrix A
 * (3) n lines for matrix B
 * (4) a blank line (optional)
 * Then repeats for more pairs.
 *
 * The method readMatrixPairs(...) returns a List of Matrix[] pairs,
 * where each Matrix[] has exactly two elements: [ A, B ].
 */
public class MatrixFileHandler {

    /**
     * Reads multiple (A, B) matrix pairs from a file in row-major order.
     * Each pair is stored in a Matrix[] of length 2: [A, B].
     *
     * @param filePath The path to the input file.
     * @return A list of Matrix[] pairs. For each pair, pair[0] is A, pair[1] is B.
     * @throws IOException If an I/O error occurs during reading.
     */
    public List<Matrix[]> readMatrixPairs(String filePath) throws IOException {
        List<Matrix[]> pairs = new ArrayList<>(); // List to store all (A, B) matrix pairs

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Try-with-resources ensures the file is properly closed

            String line;  // Variable to read each line
            while ((line = reader.readLine()) != null) { // Read file line by line
                if (line.trim().isEmpty()) { // Skip blank lines (optional in input format)
                    continue;
                }

                // Parse matrix size from the first non-blank line
                int n;
                try {
                    n = Integer.parseInt(line.trim()); // Convert string to integer
                    if (n <= 0) { // Ensure matrix size is valid
                        throw new IOException("Matrix size must be a positive integer.");
                    }
                } catch (NumberFormatException e) {
                    throw new IOException("Invalid matrix size in file: '" + line + "'. Expected a positive integer.");
                }

                // Read matrices A and B from the file
                int[][] dataA = readMatrix(reader, n, "A"); // Reads matrix A
                int[][] dataB = readMatrix(reader, n, "B"); // Reads matrix B

                // Create Matrix objects for A and B
                Matrix A = new Matrix(dataA);
                Matrix B = new Matrix(dataB);

                // Store the pair [A, B] in the list
                pairs.add(new Matrix[]{ A, B });

                // Debug log to confirm successful reading
                System.out.println("Successfully read matrix pair #" + pairs.size());
            }
        }

        return pairs; // Return all matrix pairs
    }

    /**
     * Reads a single matrix of size n x n from the given BufferedReader.
     *
     * @param reader The BufferedReader for reading the file.
     * @param n The size of the matrix (number of rows and columns).
     * @param matrixName The name of the matrix (for error messages).
     * @return A 2D integer array representing the matrix.
     * @throws IOException If an error occurs during reading.
     */
    private int[][] readMatrix(BufferedReader reader, int n, String matrixName) throws IOException {
        int[][] matrix = new int[n][n]; // Create an empty n x n matrix

        for (int i = 0; i < n; i++) { // Loop over rows
            String rowLine = reader.readLine(); // Read next row from file

            if (rowLine == null) { // Handle unexpected end of file
                throw new IOException("Unexpected end of file while reading matrix " + matrixName + ".");
            }

            String[] tokens = rowLine.trim().split("\\s+"); // Split row into tokens (numbers)
            if (tokens.length != n) { // Ensure correct number of columns
                throw new IOException("Incorrect number of columns in row " + (i + 1) + " of matrix " + matrixName + ".");
            }

            try {
                for (int j = 0; j < n; j++) { // Loop over columns
                    matrix[i][j] = Integer.parseInt(tokens[j]); // Convert to integer
                }
            } catch (NumberFormatException e) { // Handle non-integer values
                throw new IOException("Invalid number in matrix " + matrixName + " at row " + (i + 1) + ": '" + rowLine + "'");
            }
        }

        return matrix; // Return the parsed matrix
    }
}
