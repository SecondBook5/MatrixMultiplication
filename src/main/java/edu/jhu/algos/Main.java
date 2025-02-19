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
 *   - `java -jar MatrixMultiplication.jar input.txt` → Uses defaults (`output/input_output.txt`, `output/input_plot.png`).
 *   - `java -jar MatrixMultiplication.jar input.txt --output my_results.txt` → Custom output file.
 *   - `java -jar MatrixMultiplication.jar input.txt --plot` → Runs with default plot filename.
 *   - `java -jar MatrixMultiplication.jar input.txt --plot-output my_graph.png` → Custom plot file.
 *   - `java -jar MatrixMultiplication.jar input.txt --debug --plot` → Runs everything with debug and plot.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java -jar MatrixMultiplication.jar <input.txt> [--output <file>] [--plot] [--plot-output <file>] [--debug]");
            System.exit(1);
        }

        String inputFile = "input/" + args[0];  // Read from input/ directory
        String outputFile = null;
        String plotFile = null;
        boolean enableDebug = false;
        boolean generatePlot = false;

        // Process optional flags
        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case "--debug":
                    enableDebug = true;
                    break;
                case "--plot":
                    generatePlot = true;
                    break;
                case "--output":
                    if (i + 1 < args.length) {
                        outputFile = "output/" + args[++i];
                    } else {
                        System.err.println("Error: --output requires a filename.");
                        System.exit(1);
                    }
                    break;
                case "--plot-output":
                    if (i + 1 < args.length) {
                        plotFile = "output/" + args[++i];
                    } else {
                        System.err.println("Error: --plot-output requires a filename.");
                        System.exit(1);
                    }
                    break;
                default:
                    System.err.println("Unknown argument: " + args[i]);
                    System.exit(1);
            }
        }

        // Ensure input file exists
        File file = new File(inputFile);
        if (!file.exists() || !file.isFile()) {
            System.err.println("Error: Input file not found: " + inputFile);
            System.exit(1);
        }

        // Generate default output filenames if not provided
        String baseName = args[0].replaceAll("\\.txt$", "");
        if (outputFile == null) outputFile = "output/" + baseName + "_output.txt";
        if (plotFile == null) plotFile = "output/" + baseName + "_plot.png";

        // Ensure output directory exists
        File outputDir = new File("output/");
        if (!outputDir.exists()) {
            if (!outputDir.mkdirs()) {
                System.err.println("Error: Unable to create output directory.");
                System.exit(1);
            }
        }

        // Enable debugging if specified
        if (enableDebug) {
            DebugConfig.enableDebug();
            DebugConfig.log("Debug mode enabled.");
        }

        // Run the comparison driver
        DebugConfig.log("Running ComparisonDriver with input: " + inputFile);
        ComparisonResult result = ComparisonDriver.runComparison(inputFile, outputFile);

        // Generate performance plot if requested
        if (generatePlot) {
            DebugConfig.log("Generating performance plot...");
            GraphGenerator.generateGraph(result.records, inputFile, plotFile);
            System.out.println("Performance plot saved to: " + plotFile);
        }

        System.out.println("Execution complete. Results saved to: " + outputFile);
    }
}
