package edu.jhu.algos.models;

import edu.jhu.algos.utils.MatrixValidator;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a general m × n matrix with fundamental operations.
 * Uses `MatrixValidator` for input validation and includes performance optimizations.
 *
 * - Supports both square (n × n) and non-square (m × n) matrices.
 * - Implements loop unrolling for improved performance in addition and subtraction.
 * - Ensures immutability with controlled deep copies.
 * - Implements defensive programming with robust error handling.
 */
public final class Matrix {
    // Number of rows in the matrix
    private final int rows;

    // Number of columns in the matrix
    private final int cols;

    // The 2D array representing matrix data (marked final for immutability)
    private final double[][] data;

    /**
     * Constructs an m × n matrix and validates its input.
     * Uses `MatrixValidator` to check for valid dimensions.
     *
     * @param rows Number of rows (m).
     * @param cols Number of columns (n).
     * @param data A 2D array representing the matrix elements.
     * @throws IllegalArgumentException If dimensions or data are invalid.
     */
    public Matrix(int rows, int cols, double[][] data) {
        try {
            // Validate input dimensions and ensure non-null data
            MatrixValidator.validateMatrix(rows, cols, data);
        } catch (Exception e) {
            throw new IllegalArgumentException("Matrix construction failed: " + e.getMessage(), e);
        }

        // Assign number of rows and columns
        this.rows = rows;
        this.cols = cols;

        // Store a deep copy of the matrix to enforce immutability
        this.data = deepCopy(data);
    }

    /**
     * Returns true if the matrix is square (n × n).
     *
     * @return True if square, false otherwise.
     */
    public boolean isSquare() {
        return rows == cols;
    }

    /**
     * Returns true if the matrix is both square and a power of two in size.
     * This is required for Strassen’s Algorithm.
     *
     * @return True if the matrix size is a power of two, false otherwise.
     */
    public boolean isPowerOfTwoSquare() {
        return isSquare() && MatrixValidator.isPowerOfTwo(rows);
    }

    /**
     * Creates a deep copy of the matrix data to prevent external modification.
     *
     * @param source The original matrix data.
     * @return A new independent 2D array copy.
     */
    private double[][] deepCopy(double[][] source) {
        try {
            double[][] copy = new double[rows][cols];

            // Use System.arraycopy() for optimized copying of each row
            for (int i = 0; i < rows; i++) {
                System.arraycopy(source[i], 0, copy[i], 0, cols);
            }
            return copy;
        } catch (Exception e) {
            throw new RuntimeException("Deep copy of matrix data failed: " + e.getMessage(), e);
        }
    }

    /**
     * Returns the number of rows in the matrix.
     *
     * @return Number of rows.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the number of columns in the matrix.
     *
     * @return Number of columns.
     */
    public int getCols() {
        return cols;
    }

    /**
     * Returns a reference to the matrix data (for read-only access).
     *
     * @return The matrix data (immutable reference).
     */
    public double[][] getData() {
        return data;
    }

    /**
     * Returns a deep copy of the matrix data for safe external modification.
     *
     * @return A new deep copy of the matrix data.
     */
    public double[][] getDataCopy() {
        return deepCopy(data);
    }

    /**
     * Adds two matrices element-wise with optimized loop unrolling.
     *
     * **Loop Unrolling Optimization:**
     * - Instead of iterating element-by-element (`j++`), we process **four elements at a time (`j += 4`)**.
     * - This reduces **loop overhead** and **improves CPU cache efficiency**.
     * - If the number of columns is not a multiple of 4, the remaining elements are handled separately.
     *
     * @param other The matrix to add.
     * @return A new matrix containing the sum of both matrices.
     * @throws IllegalArgumentException If matrices have different sizes.
     */
    public Matrix add(Matrix other) {
        try {
            // Validate that both matrices have the same dimensions
            MatrixValidator.validateMatrix(rows, cols, other.getData());

            // Initialize the result matrix
            double[][] result = new double[rows][cols];

            // Perform element-wise addition with loop unrolling (4-way)
            for (int i = 0; i < rows; i++) {
                int j = 0;

                // **Process four elements at a time (main loop for efficiency)**
                for (; j <= cols - 4; j += 4) {
                    result[i][j] = this.data[i][j] + other.data[i][j];
                    result[i][j + 1] = this.data[i][j + 1] + other.data[i][j + 1];
                    result[i][j + 2] = this.data[i][j + 2] + other.data[i][j + 2];
                    result[i][j + 3] = this.data[i][j + 3] + other.data[i][j + 3];
                }

                // **Handle remaining elements (if `cols` is not a multiple of 4)**
                for (; j < cols; j++) {
                    result[i][j] = this.data[i][j] + other.data[i][j];
                }
            }

            // Return the new matrix containing the summed values
            return new Matrix(rows, cols, result);

        } catch (IllegalArgumentException e) {
            throw e; // Preserve the original exception type (expected behavior)
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during matrix addition: " + e.getMessage(), e);
        }
    }

    /**
     * Subtracts another matrix element-wise with optimized loop unrolling.
     *
     * **Loop Unrolling Optimization:**
     * - Similar to `add()`, we process four elements at a time to improve performance.
     * - If `cols` is not a multiple of 4, remaining elements are handled separately.
     *
     * @param other The matrix to subtract.
     * @return A new matrix containing the difference.
     * @throws IllegalArgumentException If matrices have different sizes.
     */
    public Matrix subtract(Matrix other) {
        try {
            // Validate that both matrices have the same dimensions
            MatrixValidator.validateMatrix(rows, cols, other.getData());

            // Initialize the result matrix
            double[][] result = new double[rows][cols];

            // Perform element-wise subtraction with loop unrolling (4-way)
            for (int i = 0; i < rows; i++) {
                int j = 0;

                // **Process four elements at a time (main loop for efficiency)**
                for (; j <= cols - 4; j += 4) {
                    result[i][j] = this.data[i][j] - other.data[i][j];
                    result[i][j + 1] = this.data[i][j + 1] - other.data[i][j + 1];
                    result[i][j + 2] = this.data[i][j + 2] - other.data[i][j + 2];
                    result[i][j + 3] = this.data[i][j + 3] - other.data[i][j + 3];
                }

                // **Handle remaining elements (if `cols` is not a multiple of 4)**
                for (; j < cols; j++) {
                    result[i][j] = this.data[i][j] - other.data[i][j];
                }
            }

            // Return the new matrix containing the difference values
            return new Matrix(rows, cols, result);

        } catch (IllegalArgumentException e) {
            throw e; // Preserve the original exception type (expected behavior)
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during matrix subtraction: " + e.getMessage(), e);
        }
    }

    /**
     * Provides a formatted string representation of the matrix for easy debugging.
     *
     * **Format Example:**
     * ```
     * |  1.00  2.00 |
     * |  3.00  4.00 |
     * ```
     *
     * @return A formatted string representation of the matrix.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (double[] row : data) {
            sb.append("| "); // Start each row with "| "

            for (int j = 0; j < row.length; j++) {
                sb.append(String.format("%5.2f", row[j])); // Fixed width (5 spaces, 2 decimal places)
                if (j < row.length - 1) {
                    sb.append(" "); // Add space only between numbers
                }
            }

            sb.append(" |\n"); // Close row with " |" and newline
        }

        return sb.toString().trim(); // Trim trailing spaces or newlines
    }


    /**
     * Ensures proper matrix equality comparison by manually checking rows.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Matrix)) return false;
        Matrix other = (Matrix) obj;
        return rows == other.rows && cols == other.cols && Arrays.deepEquals(this.data, other.data);
    }

    /**
     * Generates a hash code for the matrix using row-wise hashing.
     */
    @Override
    public int hashCode() {
        return Objects.hash(rows, cols, Arrays.deepHashCode(data));
    }
}
