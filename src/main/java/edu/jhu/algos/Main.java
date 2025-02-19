package edu.jhu.algos;

import edu.jhu.algos.compare.ComparisonDriver;
import edu.jhu.algos.compare.ComparisonDriver.ComparisonResult;
import edu.jhu.algos.utils.DebugConfig;
import edu.jhu.algos.visualization.GraphGenerator;

import java.io.File;

/**
 * Main entry point for running matrix multiplication performance analysis.
 * <p>
 * Usage:
 *   - `java -jar MatrixMultiplication.jar input.txt` → Runs the driver and saves results.
 *   - `java -jar MatrixMultiplication.jar input.txt --debug` → Runs with debug logs enabled.
 *   - `java -jar MatrixMultiplication.jar input.txt --plot` → Runs and generates a performance plot.
 *   - `java -jar MatrixMultiplication.jar input.txt --debug --plot` → Runs everything.
 */
public class Main {

    public static void main(String[] args) {
        // Ensure at least one argument (input file) is provided
        if (args.length < 1) {
            System.err.println("Usage: java -jar MatrixMultiplication.jar <input.txt> [--debug] [--plot]");
            System.exit(1);
        }

        String inputFile = args[0]; // First argument is the input file path
        boolean enableDebug = false;
        boolean generatePlot = false;

        // Process optional flags
        for (int i = 1; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("--debug")) {
                enableDebug = true;
            } else if (args[i].equalsIgnoreCase("--plot")) {
                generatePlot = true;
            }
        }

        // Enable debugging if specified
        if (enableDebug) {
            DebugConfig.enableDebug();
            DebugConfig.log("Debug mode enabled.");
        }

        // Check if the file exists before proceeding
        File file = new File(inputFile);
        if (!file.exists() || !file.isFile()) {
            System.err.println("Error: Input file not found: " + inputFile);
            System.exit(1);
        }

        // Run the comparison driver
        DebugConfig.log("Running ComparisonDriver with input: " + inputFile);
        ComparisonResult result = ComparisonDriver.runComparison(inputFile);

        // Generate performance plot if requested
        if (generatePlot) {
            DebugConfig.log("Generating performance plot...");
            GraphGenerator.generateGraph(result.records);
            System.out.println("Performance plot saved to output/matrix_performance.png");
        }

        System.out.println("Execution complete. Results saved to output/matrix_comparison.txt");
    }
}
