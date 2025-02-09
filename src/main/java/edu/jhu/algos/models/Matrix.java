package edu.jhu.algos.models;

import edu.jhu.algos.utils.MatrixValidator;
import java.util.Arrays;
import java.util.Objects;

public final class Matrix {
    private final int size;
    private final double[] data;

    public Matrix(int size, double[][] data) {
        if (data == null) {
            throw new NullPointerException("Matrix initialization failed: Data array cannot be null.");
        }
        try {
            MatrixValidator.validateSquareMatrix(size, size);
            MatrixValidator.validateMatrix(size, size, data);
            MatrixValidator.validatePowerOfTwoSize(size);

            this.size = size;
            this.data = new double[size * size];
            storeInRowMajor(data);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Matrix initialization failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error initializing matrix: " + e.getMessage(), e);
        }
    }

    private void storeInRowMajor(double[][] data) {
        for (int i = 0; i < size; i++) {
            System.arraycopy(data[i], 0, this.data, i * size, size);
        }
    }

    public double[][] retrieveRowMajorAs2D() {
        double[][] result = new double[size][size];
        try {
            for (int i = 0; i < size; i++) {
                System.arraycopy(this.data, i * size, result[i], 0, size);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving 2D matrix representation: " + e.getMessage(), e);
        }
        return result;
    }

    public Matrix add(Matrix other) {
        if (other == null) {
            throw new NullPointerException("Matrix addition failed: Other matrix cannot be null.");
        }
        try {
            MatrixValidator.validateMatrix(size, size, other.retrieveRowMajorAs2D());
            double[] result = new double[size * size];
            unrolledElementWiseOperation(other, result, true);
            return new Matrix(size, convertTo2D(result));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Matrix addition failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during matrix addition: " + e.getMessage(), e);
        }
    }

    public Matrix subtract(Matrix other) {
        if (other == null) {
            throw new NullPointerException("Matrix subtraction failed: Other matrix cannot be null.");
        }
        try {
            MatrixValidator.validateMatrix(size, size, other.retrieveRowMajorAs2D());
            double[] result = new double[size * size];
            unrolledElementWiseOperation(other, result, false);
            return new Matrix(size, convertTo2D(result));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Matrix subtraction failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during matrix subtraction: " + e.getMessage(), e);
        }
    }

    private void unrolledElementWiseOperation(Matrix other, double[] result, boolean isAddition) {
        if (other == null) {
            throw new NullPointerException("Matrix operation failed: Other matrix cannot be null.");
        }
        if (size != other.size) {
            throw new IllegalArgumentException("Matrix operation failed: Matrices must be the same size.");
        }
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

    private double[][] convertTo2D(double[] flatData) {
        double[][] result = new double[size][size];
        try {
            for (int i = 0; i < size; i++) {
                System.arraycopy(flatData, i * size, result[i], 0, size);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error converting 1D matrix to 2D: " + e.getMessage(), e);
        }
        return result;
    }

    public void debugPrint() {
        try {
            System.out.println(this.toString());
        } catch (Exception e) {
            throw new RuntimeException("Error while printing matrix: " + e.getMessage(), e);
        }
    }

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

    @Override
    public int hashCode() {
        try {
            return Objects.hash(size, Arrays.hashCode(data));
        } catch (Exception e) {
            throw new RuntimeException("Error generating hash code: " + e.getMessage(), e);
        }
    }
}
