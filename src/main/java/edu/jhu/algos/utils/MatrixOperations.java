package edu.jhu.algos.utils;

import edu.jhu.algos.models.Matrix;

/**
 * Utility class for optimized matrix operations.
 * - Provides methods for matrix addition, subtraction, splitting, and merging.
 * - Uses `Matrix.java` methods to ensure efficiency and avoid redundancy.
 * - Fully supports method chaining.
 * - Includes **robust error handling** to catch and report failures.
 * - Allows debugging mode to toggle verbose logs.
 */
public final class MatrixOperations {
    private static boolean debugMode = false; // Toggle debugging mode.

    /**
     * Enables or disables debug mode for detailed logging.
     * @param debug True to enable debugging, false to disable.
     */
    public static void setDebugMode(boolean debug) {
        debugMode = debug;
    }

    /**
     * Adds two matrices element-wise.
     * - Uses the `add()` method from `Matrix.java` for optimized computation.
     * - Supports method chaining.
     *
     * @param A First matrix.
     * @param B Second matrix.
     * @return Sum matrix (A + B).
     * @throws IllegalArgumentException If matrices have mismatched dimensions or are null.
     */
    public static Matrix add(Matrix A, Matrix B) {
        try {
            if (A == null || B == null) {
                throw new IllegalArgumentException("[ERROR] Cannot add null matrices.");
            }
            if (debugMode) {
                System.out.println("[DEBUG] Performing matrix addition: " + A.getSize() + "x" + A.getSize());
            }
            return A.add(B);
        } catch (IllegalArgumentException e) {
            throw e; // Keep as IllegalArgumentException (for test compatibility)
        } catch (Exception e) {
            throw new RuntimeException("[ERROR] Matrix addition failed: " + e.getMessage(), e);
        }
    }

    /**
     * Subtracts one matrix from another element-wise.
     * - Uses the `subtract()` method from `Matrix.java` for optimized computation.
     * - Supports method chaining.
     *
     * @param A First matrix.
     * @param B Second matrix.
     * @return Difference matrix (A - B).
     * @throws IllegalArgumentException If matrices have mismatched dimensions or are null.
     */
    public static Matrix subtract(Matrix A, Matrix B) {
        try {
            if (A == null || B == null) {
                throw new IllegalArgumentException("[ERROR] Cannot subtract null matrices.");
            }
            if (debugMode) {
                System.out.println("[DEBUG] Performing matrix subtraction: " + A.getSize() + "x" + A.getSize());
            }
            return A.subtract(B);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("[ERROR] Matrix subtraction failed: " + e.getMessage(), e);
        }
    }

    /**
     * Extracts a submatrix from the source matrix.
     * - Used in divide-and-conquer algorithms like Strassen’s.
     *
     * @param source The original matrix.
     * @param rowOffset Row starting index.
     * @param colOffset Column starting index.
     * @param newSize Size of the submatrix.
     * @return Extracted submatrix.
     * @throws IllegalArgumentException If the requested submatrix is out of bounds or if source is null.
     */
    public static Matrix split(Matrix source, int rowOffset, int colOffset, int newSize) {
        try {
            if (source == null) {
                throw new IllegalArgumentException("[ERROR] Cannot split a null matrix.");
            }

            if (debugMode) {
                System.out.println("[DEBUG] Splitting matrix at [" + rowOffset + "," + colOffset + "] with size " + newSize);
            }

            double[][] sourceData = source.retrieveRowMajorAs2D();
            double[][] subMatrix = new double[newSize][newSize];

            for (int i = 0; i < newSize; i++) {
                System.arraycopy(sourceData[i + rowOffset], colOffset, subMatrix[i], 0, newSize);
            }

            return new Matrix(newSize, subMatrix);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("[ERROR] Matrix split failed: Requested submatrix is out of bounds.", e);
        } catch (Exception e) {
            throw new RuntimeException("[ERROR] Unexpected error during matrix split: " + e.getMessage(), e);
        }
    }

    /**
     * Merges a submatrix into a larger matrix at a given offset.
     * - Used for combining submatrices in Strassen’s Algorithm.
     *
     * @param target Target matrix to merge into.
     * @param source Submatrix to merge.
     * @param rowOffset Row index to start merging.
     * @param colOffset Column index to start merging.
     * @throws IllegalArgumentException If the submatrix exceeds the bounds of the target matrix or if either matrix is null.
     */
    public static void merge(Matrix target, Matrix source, int rowOffset, int colOffset) {
        try {
            if (target == null || source == null) {
                throw new IllegalArgumentException("[ERROR] Cannot merge null matrices.");
            }

            if (debugMode) {
                System.out.println("[DEBUG] Merging submatrix into target at [" + rowOffset + "," + colOffset + "]");
            }

            double[][] targetData = target.retrieveRowMajorAs2D();
            double[][] sourceData = source.retrieveRowMajorAs2D();

            for (int i = 0; i < source.getSize(); i++) {
                System.arraycopy(sourceData[i], 0, targetData[i + rowOffset], colOffset, source.getSize());
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("[ERROR] Matrix merge failed: Submatrix exceeds target matrix bounds.", e);
        } catch (Exception e) {
            throw new RuntimeException("[ERROR] Unexpected error during matrix merge: " + e.getMessage(), e);
        }
    }

    /**
     * Debugging method to print matrix contents.
     * @param matrix Matrix to print.
     * @param label Label for clarity in debugging output.
     */
    public static void debugPrint(Matrix matrix, String label) {
        try {
            if (matrix == null) {
                System.out.println("[DEBUG] " + label + " is NULL.");
                return;
            }
            System.out.println("\n=== " + label + " ===");
            System.out.println(matrix);
        } catch (Exception e) {
            throw new RuntimeException("[ERROR] Failed to print matrix: " + e.getMessage(), e);
        }
    }
}
