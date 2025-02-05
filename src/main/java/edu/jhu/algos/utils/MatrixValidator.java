package edu.jhu.algos.utils;

/**
 * Provides utility methods to validate matrix properties.
 *
 * This class ensures that matrices conform to expected constraints such as:
 * - Proper dimensions (`m × n` size matches given data).
 * - Squareness (`n × n` for Strassen’s Algorithm).
 * - Power-of-two sizes (`2^k × 2^k` for Strassen’s Algorithm compatibility).
 */
public class MatrixValidator {

    /**
     * Validates that the provided matrix has correct dimensions.
     *
     * **Checks:**
     * 1. The matrix data is **not null**.
     * 2. The matrix has exactly `rows` rows.
     * 3. Each row has exactly `cols` columns.
     *
     * **Edge Cases Handled:**
     * - Throws an exception if `data` is null.
     * - Throws an exception if row count is incorrect.
     * - Throws an exception if any row has incorrect column size.
     *
     * @param rows Expected number of rows.
     * @param cols Expected number of columns.
     * @param data The matrix data to validate.
     * @throws IllegalArgumentException If the matrix is null, improperly sized, or inconsistent.
     */
    public static void validateMatrix(int rows, int cols, double[][] data) {
        // Step 1: Ensure data is not null
        if (data == null) {
            throw new IllegalArgumentException("Matrix data cannot be null.");
        }

        // Step 2: Ensure the matrix has the correct number of rows
        if (data.length != rows) {
            throw new IllegalArgumentException(
                    "Expected " + rows + " rows but found " + data.length
            );
        }

        // Step 3: Ensure each row has the correct number of columns
        for (int i = 0; i < rows; i++) {
            if (data[i] == null || data[i].length != cols) {
                throw new IllegalArgumentException(
                        "Row " + i + " must have exactly " + cols + " columns."
                );
            }
        }
    }

    /**
     * Checks if a matrix is square (`n × n`).
     *
     * **Why?**
     * Some matrix operations, such as Strassen’s Algorithm, require square matrices.
     *
     * **Edge Cases Handled:**
     * - If the matrix is not square, an exception is thrown.
     *
     * @param rows Number of rows.
     * @param cols Number of columns.
     * @throws IllegalArgumentException If the matrix is not square.
     */
    public static void validateSquareMatrix(int rows, int cols) {
        // A square matrix must have the same number of rows and columns
        if (rows != cols) {
            throw new IllegalArgumentException(
                    "Matrix must be square (`n × n`), but got " + rows + " × " + cols
            );
        }
    }

    /**
     * Checks if a square matrix has a power-of-two size (`2^k × 2^k`).
     *
     * **Why?**
     * - Strassen’s Algorithm only works efficiently when `n` is a power of two.
     * - If `n` is not a power of two, we may need to **pad the matrix**.
     *
     * **Edge Cases Handled:**
     * - If the size is not a power of two, an exception is thrown.
     *
     * @param size The size of the square matrix (must be `n` in `n × n`).
     * @throws IllegalArgumentException If the size is not a power of two.
     */
    public static void validatePowerOfTwoSize(int size) {
        // Use the isPowerOfTwo() method to check if the size is a power of two
        if (!isPowerOfTwo(size)) {
            throw new IllegalArgumentException(
                    "Matrix size must be a power of two, but got " + size
            );
        }
    }

    /**
     * Determines whether a given number is a power of two.
     *
     * **How It Works:**
     * - A power of two in binary has exactly **one bit set** (e.g., 1, 2, 4, 8 → `0001`, `0010`, `0100`, `1000`).
     * - Subtracting `1` from a power of two **flips all lower bits to `1`**.
     * - The **bitwise AND (`n & (n - 1)`)** removes the lowest set bit.
     * - If `n & (n - 1) == 0`, then `n` is a power of two.
     *
     * **Edge Cases Handled:**
     * - Returns `false` for `n ≤ 0` (negative numbers and zero are not powers of two).
     *
     * @param n The number to check.
     * @return True if `n` is a power of two, otherwise false.
     */
    public static boolean isPowerOfTwo(int n) {
        // Ensure n is greater than zero (negative numbers and zero are not powers of two)
        if (n <= 0) {
            return false;
        }

        // Check if n is a power of two using bitwise AND trick
        return (n & (n - 1)) == 0;
    }
}
