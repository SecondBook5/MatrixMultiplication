package edu.jhu.algos.models;

import edu.jhu.algos.utils.MatrixValidator;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a general m × n matrix with fundamental operations.
 * Uses `MatrixValidator` for input validation and includes performance optimizations.
 */
public final class Matrix {
    // Number of rows in the matrix
    private final int rows;

    // Number of columns in the matrix
    private final int cols;

    // The 2D array representing matrix data
    private final double[][] data;

    /**
     * Constructs an m × n matrix and validates its input.
     * Uses `MatrixValidator` to check for valid dimensions.
     *
     * @param rows Number of rows (m).
     * @param cols Number of columns (n).
     * @param data A 2D array representing the matrix elements.
     */
    public Matrix(int rows, int cols, double[][] data) {
        // Validate input dimensions using `MatrixValidator`
        MatrixValidator.validateMatrix(rows, cols, data);

        // Assign rows and columns
        this.rows = rows;
        this.cols = cols;

        // Store a deep copy of the matrix data to ensure immutability
        this.data = deepCopy(data);
    }

    /**
     * Returns true if the matrix is square (n × n).
     * A square matrix has the same number of rows and columns.
     */
    public boolean isSquare() {
        return rows == cols;
    }

    /**
     * Returns true if the matrix is both square and a power of two in size.
     * This is required for Strassen’s Algorithm.
     */
    public boolean isPowerOfTwoSquare() {
        return isSquare() && MatrixValidator.isPowerOfTwo(rows);
    }

    /**
     * Creates a deep copy of the matrix data to prevent external modification.
     * A deep copy ensures that modifying the original data array does not affect this matrix.
     *
     * @param source The original matrix data.
     * @return A new independent 2D array copy.
     */
    private double[][] deepCopy(double[][] source) {
        double[][] copy = new double[rows][cols];

        // Use System.arraycopy() for optimized copying of each row
        for (int i = 0; i < rows; i++) {
            System.arraycopy(source[i], 0, copy[i], 0, cols);
        }
        return copy;
    }

    /**
     * Returns the number of rows in the matrix.
     *
     * @return Number of rows (m).
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the number of columns in the matrix.
     *
     * @return Number of columns (n).
     */
    public int getCols() {
        return cols;
    }

    /**
     * Returns a deep copy of the matrix data if requested, or a direct reference otherwise.
     * This improves performance when a deep copy is not needed.
     *
     * @param deepCopy If true, returns a deep copy; otherwise, returns direct reference.
     * @return A copy of the matrix data if deepCopy is true; otherwise, direct reference.
     */
    public double[][] getData(boolean deepCopy) {
        return deepCopy ? deepCopy(data) : data;
    }

    /**
     * Adds two matrices element-wise using loop unrolling for optimization.
     * Loop unrolling allows processing two elements at a time for faster execution.
     *
     * @param other The matrix to add.
     * @return A new matrix containing the sum of both matrices.
     * @throws IllegalArgumentException If matrices have different sizes.
     */
    public Matrix add(Matrix other) {
        // Validate that both matrices have the same dimensions
        MatrixValidator.validateMatrix(rows, cols, other.getData(false));

        // Initialize result matrix
        double[][] result = new double[rows][cols];

        // Perform element-wise addition using loop unrolling
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j += 2) { // Process two elements at a time
                result[i][j] = this.data[i][j] + other.data[i][j];
                if (j + 1 < cols) {
                    result[i][j + 1] = this.data[i][j + 1] + other.data[i][j + 1];
                }
            }
        }
        return new Matrix(rows, cols, result);
    }

    /**
     * Subtracts another matrix element-wise using loop unrolling for optimization.
     * Loop unrolling improves CPU efficiency by reducing loop overhead.
     *
     * @param other The matrix to subtract.
     * @return A new matrix containing the difference.
     * @throws IllegalArgumentException If matrices have different sizes.
     */
    public Matrix subtract(Matrix other) {
        // Validate that both matrices have the same dimensions
        MatrixValidator.validateMatrix(rows, cols, other.getData(false));

        // Initialize result matrix
        double[][] result = new double[rows][cols];

        // Perform element-wise subtraction using loop unrolling
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j += 2) { // Process two elements at a time
                result[i][j] = this.data[i][j] - other.data[i][j];
                if (j + 1 < cols) {
                    result[i][j + 1] = this.data[i][j + 1] - other.data[i][j + 1];
                }
            }
        }
        return new Matrix(rows, cols, result);
    }

    /**
     * Provides a formatted string representation of the matrix for easy debugging.
     * Ensures uniform spacing and removes extra trailing spaces.
     *
     * @return A formatted string representation of the matrix.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (double[] row : data) {
            sb.append("| ");
            for (int j = 0; j < row.length; j++) {
                // Ensure consistent spacing without trailing spaces
                sb.append(String.format("%5.2f", row[j])); // Reduced width to prevent extra padding
                if (j < row.length - 1) {
                    sb.append(" "); // Add space only between numbers
                }
            }
            sb.append(" |\n"); // Keep vertical alignment consistent
        }
        return sb.toString().trim(); // Trim any trailing spaces or newlines
    }


    /**
     * Checks if this matrix is equal to another matrix.
     * Two matrices are considered equal if they have the same dimensions and identical data.
     *
     * @param obj The object to compare against.
     * @return True if both matrices have identical data, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // If the objects are the same reference, they are equal
        if (!(obj instanceof Matrix)) return false; // If not the same type, return false
        Matrix matrix = (Matrix) obj;
        return rows == matrix.rows && cols == matrix.cols && Arrays.deepEquals(this.data, matrix.data);
    }

    /**
     * Generates a unique hash code for the matrix.
     * Uses a combination of row/column count and data hash to ensure uniqueness.
     *
     * @return The hash code for the matrix.
     */
    @Override
    public int hashCode() {
        return Objects.hash(rows, cols, Arrays.deepHashCode(data));
    }
}
