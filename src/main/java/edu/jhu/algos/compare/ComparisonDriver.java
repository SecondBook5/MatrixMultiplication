package edu.jhu.algos.compare;

import edu.jhu.algos.models.Matrix;
import edu.jhu.algos.io.MatrixFileHandler;
import edu.jhu.algos.algorithms.MatrixMultiplier;
import edu.jhu.algos.algorithms.NaiveMultiplication;
import edu.jhu.algos.algorithms.StrassenMultiplication;
import edu.jhu.algos.utils.MatrixUtils;
import edu.jhu.algos.utils.DebugConfig;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Runs Naive and Strassen matrix multiplication on multiple (A, B) matrix pairs.
 * <p>
 * Generates:
 * - A detailed output log (step-by-step process).
 * - A performance comparison table (ASCII format).
 * - A full `.txt` log file for saving the entire output.
 * </p>
 */
public class ComparisonDriver {

    private static final String OUTPUT_TXT_FILE = "output/matrix_comparison.txt"; // TXT output file path

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
        StringBuilder fullOutput = new StringBuilder();  // Stores formatted output for printing & saving
        List<PerformanceRecord> records = new ArrayList<>();  // Stores performance metrics

        try {
            MatrixFileHandler fileHandler = new MatrixFileHandler();
            List<Matrix[]> pairs = fileHandler.readMatrixPairs(inputFile); // Read (A, B) pairs

            if (pairs.isEmpty()) {
                fullOutput.append("Warning: No valid matrix pairs found in file: ").append(inputFile).append("\n");
                return new ComparisonResult(fullOutput.toString(), records);
            }

            for (int i = 0; i < pairs.size(); i++) {
                Matrix A = pairs.get(i)[0];
                Matrix B = pairs.get(i)[1];
                int n = A.getSize();

                fullOutput.append("====================================================\n")
                        .append("Matrix Pair #").append(i + 1).append("\n")
                        .append("Matrix A (size ").append(n).append("):\n")
                        .append(MatrixUtils.toString(A)).append("\n")
                        .append("Matrix B (size ").append(n).append("):\n")
                        .append(MatrixUtils.toString(B)).append("\n");

                // Run Naive Multiplication
                MultiplicationResult naiveResult = runMultiplication(new NaiveMultiplication(), A, B, "Naive");
                fullOutput.append(naiveResult.output);
                DebugConfig.log("Retrieved Naive Multiplications = " + naiveResult.multiplications);

                // Run Strassen Multiplication
                MultiplicationResult strassenResult = runMultiplication(new StrassenMultiplication(), A, B, "Strassen");
                fullOutput.append(strassenResult.output);
                DebugConfig.log("Retrieved Strassen Multiplications = " + strassenResult.multiplications);

                // Compare outputs for correctness
                boolean same = MatrixUtils.compareMatrices(naiveResult.result, strassenResult.result);
                fullOutput.append("Naive vs. Strassen same? ").append(same).append("\n")
                        .append("====================================================\n\n");

                // Compute Big-O constants using CurveFitter
                double naiveConstant = CurveFitter.fitConstant(records, 3.0, true);
                double strassenConstant = CurveFitter.fitConstant(records, Math.log(7) / Math.log(2), false);

                // Store performance data
                records.add(new PerformanceRecord(n, naiveResult.timeMs, naiveResult.multiplications,
                        strassenResult.timeMs, strassenResult.multiplications, naiveConstant, strassenConstant));
            }

            // PRINT ALL RESULTS FIRST
            System.out.println(fullOutput.toString());

            // THEN APPEND PERFORMANCE TABLE & SAVE OUTPUTS
            fullOutput.append("\n=== Performance Comparison Table ===\n")
                    .append(ComparisonTableGenerator.toAsciiTable(records));

            System.out.println(fullOutput.toString());

            saveToFile(OUTPUT_TXT_FILE, fullOutput.toString());

            System.out.println("Full comparison output saved to: " + OUTPUT_TXT_FILE);

        } catch (IOException e) {
            fullOutput.append("I/O Error while processing file '").append(inputFile).append("': ").append(e.getMessage()).append("\n");
        }

        return new ComparisonResult(fullOutput.toString(), records);
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
        DebugConfig.log("Running " + methodName + " multiplication...");

        Matrix result = multiplier.multiply(A, B);  // Perform multiplication
        long timeMs = multiplier.getElapsedTimeMs();  // Get execution time
        long multiplications = multiplier.getMultiplicationCount();  // Get multiplication count

        DebugConfig.log(methodName + " Multiplication Done");
        DebugConfig.log("Time taken: " + timeMs + " ms");
        DebugConfig.log("Multiplications counted: " + multiplications);

        // Generate formatted output
        StringBuilder output = new StringBuilder();
        output.append(methodName).append(" Multiplication Result:\n")
                .append(MatrixUtils.toString(result)).append("\n")
                .append(methodName).append(" Time (ms): ").append(timeMs).append("\n")
                .append(methodName).append(" Multiplications: ").append(multiplications).append("\n\n");

        return new MultiplicationResult(result, timeMs, multiplications, output.toString());
    }

    /**
     * Saves a given string to a file.
     * @param filePath The file path where the content should be saved.
     * @param content The string content to write to the file.
     */
    private static void saveToFile(String filePath, String content) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("Error saving file " + filePath + ": " + e.getMessage());
        }
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
