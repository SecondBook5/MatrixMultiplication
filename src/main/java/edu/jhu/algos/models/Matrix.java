package edu.jhu.algos.models;

import edu.jhu.algos.utils.MatrixValidator;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a square matrix (n × n) using row-major storage.
 * Provides optimized matrix operations such as addition and subtraction.
 * Implements defensive programming with centralized error handling.
 */
public final class Matrix {
    // The size of the square matrix (n × n)
    private final int size;
    // A 1D array storing the matrix data in row-major order
    private final double[] data;

    /**
     * Constructs an n × n matrix and validates the input.
     * Ensures the matrix follows square and power-of-two constraints.
     *
     * @param size The size of the square matrix.
     * @param data A 2D array representing the matrix elements.
     * @throws NullPointerException If the input data array is null.
     * @throws IllegalArgumentException If the matrix is not square or has invalid dimensions.
     * @throws RuntimeException If an unexpected error occurs during matrix initialization.
     */
    public Matrix(int size, double[][] data) {
        // Ensure the input matrix is not null before proceeding
        if (data == null) {
            throw new NullPointerException("Matrix initialization failed: Data array cannot be null.");
        }
        try {
            // Validate that the matrix is square
            MatrixValidator.validateSquareMatrix(size, size);
            // Validate that the matrix dimensions match expectations
            MatrixValidator.validateMatrix(size, size, data);
            // Validate that the matrix size is a power of two (required for Strassen's Algorithm)
            MatrixValidator.validatePowerOfTwoSize(size);

            this.size = size;
            this.data = new double[size * size];
            // Store matrix elements in row-major order for efficient memory access
            storeInRowMajor(data);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Matrix initialization failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error initializing matrix: " + e.getMessage(), e);
        }
    }

    /**
     * Stores a 2D matrix into a 1D array using row-major order.
     * In row-major order, elements of each row are stored sequentially.
     *
     * @param data The 2D array representing the matrix.
     */
    private void storeInRowMajor(double[][] data) {
        for (int i = 0; i < size; i++) {
            // Copy each row into the corresponding position in the 1D array
            System.arraycopy(data[i], 0, this.data, i * size, size);
        }
    }

    /**
     * Converts the row-major stored matrix back into a 2D array.
     *
     * @return A 2D array representation of the matrix.
     * @throws RuntimeException If an error occurs while retrieving the matrix.
     */
    public double[][] retrieveRowMajorAs2D() {
        double[][] result = new double[size][size];
        try {
            for (int i = 0; i < size; i++) {
                // Copy back each row from the 1D storage into the 2D array
                System.arraycopy(this.data, i * size, result[i], 0, size);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving 2D matrix representation: " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * Adds two matrices element-wise.
     * Uses loop unrolling to optimize performance.
     *
     * @param other The matrix to add.
     * @return A new matrix containing the sum.
     * @throws NullPointerException If the input matrix is null.
     * @throws IllegalArgumentException If the matrices have mismatched dimensions.
     * @throws RuntimeException If an unexpected error occurs during addition.
     */
    public Matrix add(Matrix other) {
        if (other == null) {
            throw new NullPointerException("Matrix addition failed: Other matrix cannot be null.");
        }
        try {
            // Validate that both matrices have the same size
            MatrixValidator.validateMatrix(size, size, other.retrieveRowMajorAs2D());
            double[] result = new double[size * size];
            // Perform element-wise addition with loop unrolling for efficiency
            unrolledElementWiseOperation(other, result, true);
            return new Matrix(size, convertTo2D(result));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Matrix addition failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during matrix addition: " + e.getMessage(), e);
        }
    }

    /**
     * Subtracts another matrix element-wise.
     * Uses loop unrolling to optimize performance.
     *
     * @param other The matrix to subtract.
     * @return A new matrix containing the difference.
     * @throws NullPointerException If the input matrix is null.
     * @throws IllegalArgumentException If the matrices have mismatched dimensions.
     * @throws RuntimeException If an unexpected error occurs during subtraction.
     */
    public Matrix subtract(Matrix other) {
        if (other == null) {
            throw new NullPointerException("Matrix subtraction failed: Other matrix cannot be null.");
        }
        try {
            // Validate that both matrices have the same size
            MatrixValidator.validateMatrix(size, size, other.retrieveRowMajorAs2D());
            double[] result = new double[size * size];
            // Perform element-wise subtraction with loop unrolling
            unrolledElementWiseOperation(other, result, false);
            return new Matrix(size, convertTo2D(result));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Matrix subtraction failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during matrix subtraction: " + e.getMessage(), e);
        }
    }

    /**
     * Performs element-wise addition or subtraction using loop-unrolling.
     * This improves performance by reducing loop overhead.
     *
     * @param other The matrix to operate on.
     * @param result The array where the computed values will be stored.
     * @param isAddition If true, performs addition; otherwise, performs subtraction.
     * @throws NullPointerException If the input matrix is null.
     * @throws IllegalArgumentException If the matrices are not the same size.
     * @throws RuntimeException If an unexpected error occurs during computation.
     */
    private void unrolledElementWiseOperation(Matrix other, double[] result, boolean isAddition) {
        if (other == null) {
            throw new NullPointerException("Matrix operation failed: Other matrix cannot be null.");
        }
        if (size != other.size) {
            throw new IllegalArgumentException("Matrix operation failed: Matrices must be the same size.");
        }
        try {
            // Loop through the array in steps of 4 for loop-unrolling optimization
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
     * Converts a 1D row-major stored matrix back into a 2D array.
     *
     * @param flatData The 1D array storing matrix data in row-major order.
     * @return A 2D representation of the matrix.
     * @throws RuntimeException If an error occurs while converting the data.
     */
    private double[][] convertTo2D(double[] flatData) {
        double[][] result = new double[size][size];
        try {
            for (int i = 0; i < size; i++) {
                // Copy each row from the 1D storage back into the 2D array
                System.arraycopy(flatData, i * size, result[i], 0, size);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error converting 1D matrix to 2D: " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * Prints the matrix to the console for debugging purposes.
     *
     * @throws RuntimeException If an error occurs while printing the matrix.
     */
    public void debugPrint() {
        try {
            System.out.println(this.toString());
        } catch (Exception e) {
            throw new RuntimeException("Error while printing matrix: " + e.getMessage(), e);
        }
    }

    /**
     * Returns a formatted string representation of the matrix.
     *
     * @return A string representation of the matrix.
     * @throws RuntimeException If an error occurs while generating the string.
     */
    @Override
    public String toString() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("Matrix (").append(size).append("x").append(size).append("):\n");
            for (int i = 0; i < size; i++) {
                sb.append("| ");
                for (int j = 0; j < size; j++) {
                    sb.append(String.format("%6.2f ", data[i * size + j]));
                }
                sb.append("|\n");
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generating matrix string representation: " + e.getMessage(), e);
        }
    }

    /**
     * Checks if this matrix is equal to another matrix.
     * Two matrices are equal if they have the same size and identical data.
     *
     * @param obj The object to compare.
     * @return True if matrices are equal, false otherwise.
     * @throws RuntimeException If an error occurs during comparison.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Matrix)) return false;
        try {
            Matrix other = (Matrix) obj;
            return size == other.size && Arrays.equals(this.data, other.data);
        } catch (Exception e) {
            throw new RuntimeException("Error comparing matrices: " + e.getMessage(), e);
        }
    }

    /**
     * Computes the hash code for the matrix.
     * Uses both size and matrix data to generate a unique hash.
     *
     * @return The hash code of the matrix.
     * @throws RuntimeException If an error occurs while generating the hash code.
     */
    @Override
    public int hashCode() {
        try {
            return Objects.hash(size, Arrays.hashCode(data));
        } catch (Exception e) {
            throw new RuntimeException("Error generating hash code: " + e.getMessage(), e);
        }
    }
}
