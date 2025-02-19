package edu.jhu.algos.visualization;

import edu.jhu.algos.compare.PerformanceRecord;
import edu.jhu.algos.utils.DebugConfig;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.block.BlockBorder;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.ChartUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Generates a performance comparison graph of matrix multiplication algorithms.
 * <p>
 * - Compares Naive vs. Strassen multiplication
 * - Expected vs. actual multiplication counts
 * - Saves graph as a PNG file with a dynamic filename
 */
public class GraphGenerator {

    /**
     * Generates and saves a graph comparing matrix multiplication performance.
     *
     * @param records  The list of performance records.
     * @param inputFile The name of the input file (used for dynamic naming).
     * @param plotFile The file path to save the plot. If null, defaults to "<inputFile>_plot.png".
     */
    public static void generateGraph(List<PerformanceRecord> records, String inputFile, String plotFile) {
        if (records.isEmpty()) {
            System.err.println("Warning: No performance records available for plotting.");
            return;
        }

        // Default to "<inputFile>_plot.png" if no plot file is provided
        if (plotFile == null || plotFile.isEmpty()) {
            plotFile = inputFile.replaceAll("\\.txt$", "") + "_plot.png";
        }

        // Create dataset
        XYSeries naiveExpectedSeries = new XYSeries("Expected O(n³)");
        XYSeries naiveActualSeries = new XYSeries("Actual Naive Multiplications");
        XYSeries strassenExpectedSeries = new XYSeries("Expected O(n^2.8074)");
        XYSeries strassenActualSeries = new XYSeries("Actual Strassen Multiplications");

        for (PerformanceRecord record : records) {
            int n = record.getSize();
            double naiveExpected = Math.pow(n, 3);
            double strassenExpected = Math.pow(n, Math.log(7) / Math.log(2));

            naiveExpectedSeries.add(n, naiveExpected);
            naiveActualSeries.add(n, record.getNaiveMultiplications());
            strassenExpectedSeries.add(n, strassenExpected);
            strassenActualSeries.add(n, record.getStrassenMultiplications());

            DebugConfig.log("Graph Data - Size: " + n +
                    " | Naive Expected: " + naiveExpected +
                    " | Naive Actual: " + record.getNaiveMultiplications() +
                    " | Strassen Expected: " + strassenExpected +
                    " | Strassen Actual: " + record.getStrassenMultiplications());
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(naiveExpectedSeries);
        dataset.addSeries(naiveActualSeries);
        dataset.addSeries(strassenExpectedSeries);
        dataset.addSeries(strassenActualSeries);

        // Create chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Matrix Multiplication Performance",
                "Matrix Size (n)",
                "Multiplication Count",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        // Set distinct colors
        renderer.setSeriesPaint(0, Color.RED);      // Expected O(n³)
        renderer.setSeriesPaint(1, Color.BLUE);     // Actual Naive Multiplications
        renderer.setSeriesPaint(2, Color.GREEN);    // Expected O(n^2.8074)
        renderer.setSeriesPaint(3, Color.ORANGE);   // Actual Strassen Multiplications

        // Set distinct markers
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesShapesVisible(2, true);
        renderer.setSeriesShapesVisible(3, true);

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);

        // Improve legend visibility
        chart.getLegend().setFrame(BlockBorder.NONE);
        chart.getLegend().setBackgroundPaint(Color.WHITE);

        // Add annotations for clarity
        for (PerformanceRecord record : records) {
            int n = record.getSize();
            double naiveActual = record.getNaiveMultiplications();
            double strassenActual = record.getStrassenMultiplications();

            XYTextAnnotation naiveAnnotation = new XYTextAnnotation(
                    "Naive: " + (int) naiveActual, n, naiveActual);
            naiveAnnotation.setTextAnchor(TextAnchor.BOTTOM_RIGHT);
            naiveAnnotation.setPaint(Color.BLUE);
            plot.addAnnotation(naiveAnnotation);

            XYTextAnnotation strassenAnnotation = new XYTextAnnotation(
                    "Strassen: " + (int) strassenActual, n, strassenActual);
            strassenAnnotation.setTextAnchor(TextAnchor.BOTTOM_LEFT);
            strassenAnnotation.setPaint(Color.ORANGE);
            plot.addAnnotation(strassenAnnotation);
        }

        saveChartAsPNG(chart, plotFile);
    }

    /**
     * Saves the generated chart as a PNG file.
     *
     * @param chart    The chart to save.
     * @param filePath The file path where the chart should be saved.
     */
    private static void saveChartAsPNG(JFreeChart chart, String filePath) {
        File outputFile = new File(filePath);

        // Ensure the directory exists
        File outputDir = outputFile.getParentFile();
        if (outputDir != null && !outputDir.exists()) {
            if (!outputDir.mkdirs()) {
                System.err.println("Error: Unable to create output directory " + outputDir.getAbsolutePath());
                return;
            }
        }

        try {
            ChartUtils.saveChartAsPNG(outputFile, chart, 800, 600);
            DebugConfig.log("Graph saved to: " + filePath);
            System.out.println("Graph successfully saved to: " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving graph to file: " + e.getMessage());
        }
    }
}
