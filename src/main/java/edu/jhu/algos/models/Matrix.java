package edu.jhu.algos.models;

import edu.jhu.algos.utils.MatrixValidator;

/**
 * Represents a square matrix of size 2^n x 2^n.
 * This class focuses solely on storing matrix data (and validating it).
 * Operations (add, subtract, multiply, etc.) and utilities (print, fillRandom) live elsewhere.
 */
public class Matrix {
    private final int size;     // The dimension of this matrix (must be a power of 2).
    private final int[][] data; // 2D array holding the matrix values in row-major order by default.

    /**
     * Constructs an empty matrix of the given size (2^n).
     * @param size The matrix size (must be a power of 2).
     * @throws IllegalArgumentException if 'size' is not a power of 2.
     */
    public Matrix(int size) {
        // Validate size is a power of 2
        if (!MatrixValidator.isPowerOfTwo(size)) {
            // Throw an error if invalid
            throw new IllegalArgumentException("Matrix size must be a power of 2.");
        }
        this.size = size;             // Record the dimension
        this.data = new int[size][size]; // Initialize the storage with zeros
    }

    /**
     * Constructs a matrix from an existing 2D array.
     * The input array must be square and its dimension must be a power of 2.
     * @param inputData The 2D array to store in this Matrix.
     * @throws IllegalArgumentException if 'inputData' is invalid (null, not square, or size not power of 2).
     */
    public Matrix(int[][] inputData) {
        // Validate the 2D array
        if (!MatrixValidator.isValidMatrix(inputData)) {
            throw new IllegalArgumentException("Invalid matrix: must be square and dimension must be power of 2.");
        }
        this.size = inputData.length; // The dimension is the length of the array
        this.data = inputData;        // Store the reference (or you could deep-copy if you prefer)
    }

    /**
     * Retrieves the dimension (size) of the matrix.
     * @return The size (2^n) of this matrix.
     */
    public int getSize() {
        return size; // Return stored size
    }

    /**
     * Returns the underlying 2D array reference.
     * @return A 2D array of integers representing this matrix.
     */
    public int[][] getData() {
        // Returning data directly is minimal overhead, but means external code can change it.
        // If you need safety, do a deep copy. For SRP, that can be a utility method in MatrixUtils.
        return data;
    }

    /**
     * Sets the value at (row, col).
     * @param row Row index (0-based).
     * @param col Column index (0-based).
     * @param value The integer to place at (row, col).
     * @throws IndexOutOfBoundsException if (row, col) is out of bounds.
     */
    public void set(int row, int col, int value) {
        // Check index validity
        if (!MatrixValidator.isValidIndex(row, col, size)) {
            throw new IndexOutOfBoundsException(
                    "Invalid indices (" + row + ", " + col + ") in a " + size + "x" + size + " matrix."
            );
        }
        data[row][col] = value; // Store value
    }

    /**
     * Retrieves the integer at (row, col).
     * @param row Row index (0-based).
     * @param col Column index (0-based).
     * @return The value stored at (row, col).
     * @throws IndexOutOfBoundsException if (row, col) is out of bounds.
     */
    public int get(int row, int col) {
        // Check index validity
        if (!MatrixValidator.isValidIndex(row, col, size)) {
            throw new IndexOutOfBoundsException(
                    "Invalid indices (" + row + ", " + col + ") in a " + size + "x" + size + " matrix."
            );
        }
        return data[row][col]; // Return the stored value
    }
}
