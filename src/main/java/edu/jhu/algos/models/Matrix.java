package edu.jhu.algos.models;

import edu.jhu.algos.utils.MatrixValidator;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a square matrix (n × n) with fundamental operations.
 * Uses row-major storage for cache efficiency.
 * Implements defensive programming with centralized error handling.
 */
public final class Matrix {
    private final int size; // Size of the square matrix (n × n)
    private final double[] data; // 1D array for row-major storage

    /**
     * Constructs an n × n matrix and validates input.
     * Stores matrix in row-major order for performance benefits.
     *
     * @param size The size of the square matrix (must be a power of two).
     * @param data A 2D array representing the matrix elements.
     * @throws IllegalArgumentException If the matrix is not valid.
     */
    public Matrix(int size, double[][] data) {
        try {
            MatrixValidator.validateSquareMatrix(size, size);
            MatrixValidator.validateMatrix(size, size, data);
            MatrixValidator.validatePowerOfTwoSize(size);

            this.size = size;
            this.data = new double[size * size]; // 1D storage for row-major order

            // Store elements row by row
            for (int i = 0; i < size; i++) {
                System.arraycopy(data[i], 0, this.data, i * size, size);
            }
        } catch (Exception e) {
            throw new RuntimeException("Matrix initialization failed: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves the 2D matrix representation from the row-major stored data.
     *
     * @return A 2D array representation of the matrix.
     */
    public double[][] retrieveAs2D() {
        double[][] result = new double[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(this.data, i * size, result[i], 0, size);
        }
        return result;
    }

    /**
     * Adds two matrices element-wise using loop-unrolling for performance.
     * Supports method chaining.
     *
     * @param other The matrix to add.
     * @return A new matrix containing the sum.
     */
    public Matrix add(Matrix other) {
        MatrixValidator.validateMatrix(size, size, other.retrieveAs2D());
        double[] result = new double[size * size];
        elementWiseOperation(other, result, true);
        return new Matrix(size, convertTo2D(result)); // Supports method chaining.
    }

    /**
     * Subtracts another matrix element-wise using loop-unrolling for performance.
     * Supports method chaining.
     *
     * @param other The matrix to subtract.
     * @return A new matrix containing the difference.
     */
    public Matrix subtract(Matrix other) {
        MatrixValidator.validateMatrix(size, size, other.retrieveAs2D());
        double[] result = new double[size * size];
        elementWiseOperation(other, result, false);
        return new Matrix(size, convertTo2D(result)); // Supports method chaining.
    }

    /**
     * Performs element-wise operations (addition or subtraction) using loop-unrolling.
     *
     * **Loop-unrolling explanation:**
     * - Instead of iterating one element at a time, we process **four elements per loop**.
     * - Reduces loop overhead and improves CPU cache efficiency.
     *
     * @param other The matrix to operate on.
     * @param result The array storing computed values.
     * @param isAddition Determines whether to add or subtract elements.
     */
    private void elementWiseOperation(Matrix other, double[] result, boolean isAddition) {
        try {
            for (int i = 0; i < size * size; i += 4) {
                result[i] = isAddition ? this.data[i] + other.data[i] : this.data[i] - other.data[i];
                if (i + 1 < size * size) result[i + 1] = isAddition ? this.data[i + 1] + other.data[i + 1] : this.data[i + 1] - other.data[i + 1];
                if (i + 2 < size * size) result[i + 2] = isAddition ? this.data[i + 2] + other.data[i + 2] : this.data[i + 2] - other.data[i + 2];
                if (i + 3 < size * size) result[i + 3] = isAddition ? this.data[i + 3] + other.data[i + 3] : this.data[i + 3] - other.data[i + 3];
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during matrix operation: " + e.getMessage(), e);
        }
    }

    /**
     * Converts a 1D row-major stored matrix back to a 2D array.
     *
     * @param flatData The 1D row-major stored matrix.
     * @return A 2D representation of the matrix.
     */
    private double[][] convertTo2D(double[] flatData) {
        double[][] result = new double[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(flatData, i * size, result[i], 0, size);
        }
        return result;
    }

    /**
     * Prints the matrix for debugging.
     */
    public void debugPrint() {
        System.out.println(this.toString());
    }

    /**
     * Returns a formatted string representation of the matrix.
     *
     * @return A string representation of the matrix.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Matrix (" + size + "x" + size + "):\n");
        for (int i = 0; i < size; i++) {
            sb.append("| ");
            for (int j = 0; j < size; j++) {
                sb.append(String.format("%6.2f ", data[i * size + j]));
            }
            sb.append("|\n");
        }
        return sb.toString();
    }

    /**
     * Computes the hash code for the matrix.
     *
     * @return The hash code of the matrix.
     */
    @Override
    public int hashCode() {
        return Objects.hash(size, Arrays.hashCode(data));
    }

    /**
     * Checks if this matrix is equal to another matrix.
     * Two matrices are equal if they have the same size and identical data.
     *
     * @param obj The object to compare.
     * @return True if matrices are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Matrix)) return false;
        Matrix other = (Matrix) obj;
        return size == other.size && Arrays.equals(this.data, other.data);
    }
}
