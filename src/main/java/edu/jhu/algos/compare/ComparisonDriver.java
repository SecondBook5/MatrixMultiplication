package edu.jhu.algos.compare;

import edu.jhu.algos.models.Matrix;
import edu.jhu.algos.io.MatrixFileHandler;
import edu.jhu.algos.algorithms.MatrixMultiplier;
import edu.jhu.algos.algorithms.NaiveMultiplication;
import edu.jhu.algos.algorithms.StrassenMultiplication;
import edu.jhu.algos.utils.MatrixUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Runs Naive and Strassen matrix multiplication on multiple (A, B) matrix pairs.
 * <p>
 * Returns:
 * - A detailed output log (step-by-step process).
 * - A list of performance records (size, time, multiplications).
 * </p>
 * This class does not print to console or write to files directly.
 */
public class ComparisonDriver {

    /**
     * Reads matrix pairs from the input file, applies Naive and Strassen multiplication,
     * and returns:
     * - A formatted step-by-step output log,
     * - A list of performance records for comparison.
     *
     * @param inputFile Path to the input file containing matrix pairs.
     * @return ComparisonResult object containing output logs and performance records.
     */
    public static ComparisonResult runComparison(String inputFile) {
        StringBuilder details = new StringBuilder();  // Stores formatted output
        List<PerformanceRecord> records = new ArrayList<>();  // Stores performance metrics

        try {
            MatrixFileHandler fileHandler = new MatrixFileHandler();
            List<Matrix[]> pairs = fileHandler.readMatrixPairs(inputFile); // Read (A, B) pairs

            for (int i = 0; i < pairs.size(); i++) {
                Matrix A = pairs.get(i)[0];
                Matrix B = pairs.get(i)[1];
                int n = A.getSize();

                details.append("====================================================\n")
                        .append("Matrix Pair #").append(i + 1).append("\n")
                        .append("Matrix A (size ").append(n).append("):\n")
                        .append(MatrixUtils.toString(A)).append("\n")
                        .append("Matrix B (size ").append(n).append("):\n")
                        .append(MatrixUtils.toString(B)).append("\n");

                // Run Naive Multiplication
                MultiplicationResult naiveResult = runMultiplication(new NaiveMultiplication(), A, B, "Naive");
                details.append(naiveResult.output);

                // Run Strassen Multiplication
                MultiplicationResult strassenResult = runMultiplication(new StrassenMultiplication(), A, B, "Strassen");
                details.append(strassenResult.output);

                // Compare outputs for correctness
                boolean same = MatrixUtils.compareMatrices(naiveResult.result, strassenResult.result);
                details.append("Naive vs. Strassen same? ").append(same).append("\n")
                        .append("====================================================\n\n");

                // Store performance data
                records.add(new PerformanceRecord(n, naiveResult.timeMs, strassenResult.timeMs, naiveResult.multiplications, strassenResult.multiplications));
            }

        } catch (IOException e) {
            details.append("I/O Error: ").append(e.getMessage()).append("\n");
        }

        return new ComparisonResult(details.toString(), records);
    }

    /**
     * Runs a specific matrix multiplication algorithm on (A, B) and returns the result.
     *
     * @param multiplier The algorithm to use (Naive or Strassen).
     * @param A The first matrix.
     * @param B The second matrix.
     * @param methodName The name of the algorithm (for logging).
     * @return MultiplicationResult object containing the result matrix, time, and multiplications.
     */
    private static MultiplicationResult runMultiplication(MatrixMultiplier multiplier, Matrix A, Matrix B, String methodName) {
        Matrix result = multiplier.multiply(A, B);  // Perform multiplication
        long timeMs = multiplier.getElapsedTimeMs();  // Get execution time
        long multiplications = multiplier.getMultiplicationCount();  // Get multiplication count

        // Generate formatted output
        StringBuilder output = new StringBuilder();
        output.append(methodName).append(" Multiplication Result:\n")
                .append(MatrixUtils.toString(result)).append("\n")
                .append(methodName).append(" Time (ms): ").append(timeMs).append("\n")
                .append(methodName).append(" Multiplications: ").append(multiplications).append("\n\n");

        return new MultiplicationResult(result, timeMs, multiplications, output.toString());
    }

    /**
     * Stores the results of a single multiplication method.
     */
    private static class MultiplicationResult {
        public final Matrix result;
        public final long timeMs;
        public final long multiplications;
        public final String output;

        public MultiplicationResult(Matrix result, long timeMs, long multiplications, String output) {
            this.result = result;
            this.timeMs = timeMs;
            this.multiplications = multiplications;
            this.output = output;
        }
    }

    /**
     * Encapsulates the results of `runComparison`:
     * - The full step-by-step output string.
     * - A list of performance records for all matrix pairs.
     */
    public static class ComparisonResult {
        public final String detailedOutput;
        public final List<PerformanceRecord> records;

        public ComparisonResult(String detailedOutput, List<PerformanceRecord> records) {
            this.detailedOutput = detailedOutput;
            this.records = records;
        }
    }
}
