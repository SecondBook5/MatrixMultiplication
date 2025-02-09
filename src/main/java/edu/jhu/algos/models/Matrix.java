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
    private final int size;       // The size of the square matrix (n × n)
    private final double[] data; // A 1D array storing the matrix data in row-major order

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
        if (data == null) {                                   // Ensure the input matrix is not null before proceeding
            throw new NullPointerException("Matrix initialization failed: Data array cannot be null.");
        }
        try {
            MatrixValidator.validateSquareMatrix(size, size); // Validate that the matrix is square
            MatrixValidator.validateMatrix(size, size, data); // Validate that the matrix dimensions match expectations
            MatrixValidator.validatePowerOfTwoSize(size);     // Validate that the matrix size is a power of two (required for Strassen's Algorithm)

            this.size = size;                     // Store the matrix size
            this.data = new double[size * size]; // Initialize the 1D array to store matrix data
            storeInRowMajor(data);               // Store matrix elements in row-major order for efficient memory access
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
            // Copy each row from the 2D array into the 1D storage
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
            // Iterate over each row in the matrix
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
     * Returns the size of the square matrix (n × n).
     *
     * @return The size of the matrix.
     */
    public int getSize() {
        return this.size;
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
            MatrixValidator.validateMatrix(size, size, other.retrieveRowMajorAs2D()); // Validate that both matrices have the same size
            double[] result = new double[size * size];                               // Initialize array to store the sum
            unrolledElementWiseOperation(other, result, true);             // Perform element-wise addition with loop unrolling for efficiency
            return new Matrix(size, convertTo2D(result));                           // Return the sum as a new matrix
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
            MatrixValidator.validateMatrix(size, size, other.retrieveRowMajorAs2D()); // Validate that both matrices have the same size
            double[] result = new double[size * size];                               // Initialize array to store the difference
            unrolledElementWiseOperation(other, result, false);           // Perform element-wise subtraction with loop unrolling for efficiency
            return new Matrix(size, convertTo2D(result));                          // Return the difference as a new matrix
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
        if (size != other.size) { // Ensure both matrices are the same size
            throw new IllegalArgumentException("Matrix operation failed: Matrices must be the same size.");
        }
        try {
            for (int i = 0; i < size * size; i += 4) {                                                 // Process four elements at a time for performance
                result[i] = isAddition ? this.data[i] + other.data[i] : this.data[i] - other.data[i]; // Perform addition or subtraction
                if (i + 1 < size * size) result[i + 1] = isAddition ? this.data[i + 1] + other.data[i + 1] : this.data[i + 1] - other.data[i + 1];
                if (i + 2 < size * size) result[i + 2] = isAddition ? this.data[i + 2] + other.data[i + 2] : this.data[i + 2] - other.data[i + 2];
                if (i + 3 < size * size) result[i + 3] = isAddition ? this.data[i + 3] + other.data[i + 3] : this.data[i + 3] - other.data[i + 3];
            }
        } catch (Exception e) { // Catch any unexpected errors
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
        double[][] result = new double[size][size];                                      // Initialize 2D array with matrix size
        try {
            for (int i = 0; i < size; i++) {                                             // Iterate over each row
                System.arraycopy(flatData, i * size, result[i], 0, size); // Copy each row back into 2D format
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
            System.out.println(this.toString()); // Print the matrix using its string representation
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
            StringBuilder sb = new StringBuilder();                                      // Create a StringBuilder for efficient string manipulation
            sb.append("Matrix (").append(size).append("x").append(size).append("):\n"); // Append matrix dimensions
            for (int i = 0; i < size; i++) {                                            // Iterate over rows
                sb.append("| ");                                                        // Start each row with a separator
                for (int j = 0; j < size; j++) {                                       // Iterate over columns
                    sb.append(String.format("%5.2f ", data[i * size + j]));            // Format values with two decimal places
                }
                sb.append("|\n");                                                      // Close the row with a separator
            }
            return sb.toString();                                                       // Return the formatted matrix string
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
        if (this == obj) return true;                                        // If both objects are the same instance, return true
        if (!(obj instanceof Matrix)) return false;                          // If the object is not a Matrix, return false
        try {
            Matrix other = (Matrix) obj;
            return size == other.size && Arrays.equals(this.data, other.data); // Compare sizes and data arrays
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
            return Objects.hash(size, Arrays.hashCode(data)); // Generate hash based on size and matrix data
        } catch (Exception e) {
            throw new RuntimeException("Error generating hash code: " + e.getMessage(), e);
        }
    }
    /**
     * Creates an n × n zero matrix.
     *
     * @param size The size of the square matrix.
     * @return A new Matrix instance filled with zeros.
     * @throws IllegalArgumentException If the size is not a power of two or is non-positive.
     */
    public static Matrix zeroMatrix(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Matrix size must be positive.");
        }
        if (!MatrixValidator.isPowerOfTwo(size)) {
            throw new IllegalArgumentException("Matrix size must be a power of two.");
        }
        return new Matrix(size, new double[size][size]); // Initializes a zero matrix
    }
}
