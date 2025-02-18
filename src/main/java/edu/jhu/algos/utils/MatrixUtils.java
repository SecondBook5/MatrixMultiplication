package edu.jhu.algos.utils;

import edu.jhu.algos.models.Matrix;
import edu.jhu.algos.utils.MatrixValidator;
import java.util.Random;

/**
 * Provides a set of utility functions for matrix manipulation,
 * such as deep copying, random filling, creating special matrices, and string/printing helpers.
 */
public class MatrixUtils {

    /**
     * Performs a deep copy of a 2D int array.
     * @param original The 2D array to copy.
     * @return A new 2D array with copied values.
     */
    public static int[][] deepCopy(int[][] original) {
        // Create a new 2D array of the same dimensions
        int[][] copy = new int[original.length][original.length];
        // Loop over each row
        for (int i = 0; i < original.length; i++) {
            // Copy row contents using System.arraycopy for efficiency
            System.arraycopy(original[i], 0, copy[i], 0, original[i].length);
        }
        return copy; // Return the fully copied 2D array
    }

    /**
     * Fills an existing Matrix with random integer values in the range [min, max].
     * @param matrix The Matrix to fill with random values.
     * @param min The minimum random value (inclusive).
     * @param max The maximum random value (inclusive).
     * @throws IllegalArgumentException If min > max.
     */
    public static void fillRandom(Matrix matrix, int min, int max) {
        try {
            // Ensure valid range
            if (min > max) {
                throw new IllegalArgumentException("Minimum value cannot be greater than maximum value.");
            }
            // Prepare a random number generator
            Random rand = new Random();
            // Get matrix dimension
            int n = matrix.getSize();
            // Populate every cell with a random value
            for (int i = 0; i < n; i++) {           // row index
                for (int j = 0; j < n; j++) {       // column index
                    // Generate random value between min and max
                    int randomValue = rand.nextInt((max - min) + 1) + min;
                    matrix.set(i, j, randomValue);  // Place into the matrix
                }
            }
        } catch (IllegalArgumentException e) {
            // Throw an error if min > max
            throw new IllegalArgumentException("Error in fillRandom(): " + e.getMessage());
        }
    }

    /**
     * Creates a new Matrix of given size, filled entirely with zeros.
     * @param size The dimension of the square matrix (must be power of 2).
     * @return A newly constructed zero-initialized Matrix.
     * @throws IllegalArgumentException If the size is not a power of 2.
     */
    public static Matrix createZeroMatrix(int size) {
        // Validate that 'size' is a power of 2
        if (!MatrixValidator.isPowerOfTwo(size)) {
            throw new IllegalArgumentException("Size must be a power of 2 for a valid Matrix.");
        }
        // Simply create a new Matrix of given size
        // By default, it's initialized to 0
        return new Matrix(size);
    }

    /**
     * Creates an identity matrix of the given size, i.e., 1s on main diagonal and 0s elsewhere.
     * @param size The dimension of the square matrix (must be power of 2).
     * @return A new Matrix representing the identity matrix.
     * @throws IllegalArgumentException If the size is not a power of 2.
     */
    public static Matrix createIdentityMatrix(int size) {
        if (!MatrixValidator.isPowerOfTwo(size)) {
            throw new IllegalArgumentException("Size must be a power of 2 for a valid Matrix.");
        }
        // Build a new Matrix of given size
        Matrix identity = new Matrix(size);
        // Place 1s along the diagonal
        for (int i = 0; i < size; i++) {
            identity.set(i, i, 1);
        }
        return identity;
    }

    /**
     * Prints a Matrix to the console in a readable format.
     * @param matrix The Matrix to print.
     */
    public static void printMatrix(Matrix matrix) {
        // Get the size of the matrix
        int n = matrix.getSize();
        // For each row 'i'
        for (int i = 0; i < n; i++) {
            // For each column 'j'
            for (int j = 0; j < n; j++) {
                // Print using formatting for alignment
                System.out.printf("%4d ", matrix.get(i, j));
            }
            System.out.println(); // After finishing one row, move to next line
        }
    }

    /**
     * Generates a string representation of a Matrix,
     * instead of directly printing to the console.
     * @param matrix The Matrix to convert to a string.
     * @return A multi-line String showing the matrix content row by row.
     */
    public static String toString(Matrix matrix) {
        // Use StringBuilder for efficient concatenation
        StringBuilder sb = new StringBuilder();
        int n = matrix.getSize(); // Dimension of the matrix
        // For each row
        for (int i = 0; i < n; i++) {
            // For each column
            for (int j = 0; j < n; j++) {
                sb.append(String.format("%4d ", matrix.get(i, j))); // Format each cell
            }
            sb.append("\n"); // New line after finishing one row
        }
        // Return the accumulated string
        return sb.toString();
    }

    /**
     * Compares two matrices for exact equality of every element.
     * @param A First matrix.
     * @param B Second matrix.
     * @return True if all corresponding elements match, false otherwise.
     */
    public static boolean compareMatrices(Matrix A, Matrix B) {
        // First check if they share the same size
        if (A.getSize() != B.getSize()) {
            return false; // Different dimensions => cannot be equal
        }
        // Same size => compare each element
        int n = A.getSize();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (A.get(i, j) != B.get(i, j)) {
                    return false; // Mismatch found => not equal
                }
            }
        }
        // All matched => they are equal
        return true;
    }
}
