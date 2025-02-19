package edu.jhu.algos.visualization;

import edu.jhu.algos.compare.PerformanceRecord;
import edu.jhu.algos.utils.DebugConfig;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.ChartUtils; // Import for saving PNG

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Generates and displays performance comparison graphs of matrix multiplication algorithms.
 * - Plots expected vs. actual multiplication counts for Naive and Strassen algorithms.
 * - Saves the chart as a PNG file for report analysis.
 */
public class GraphGenerator {

    /**
     * Generates a plot comparing theoretical vs. observed multiplication counts.
     *
     * @param records A list of PerformanceRecord objects containing timing and multiplication data.
     */
    public static void generateGraph(List<PerformanceRecord> records) {
        XYSeries naiveExpectedSeries = new XYSeries("Expected O(n³)");
        XYSeries naiveActualSeries = new XYSeries("Actual Naive Multiplications");
        XYSeries strassenExpectedSeries = new XYSeries("Expected O(n^2.8074)");
        XYSeries strassenActualSeries = new XYSeries("Actual Strassen Multiplications");

        // Populate data points from records
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

        // Create dataset
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

        // Customize chart
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        // Customize line styles (expected = dashed, actual = solid)
        renderer.setSeriesPaint(0, Color.RED);    // Expected O(n³)
        renderer.setSeriesPaint(1, Color.BLUE);   // Actual Naive
        renderer.setSeriesPaint(2, Color.GREEN);  // Expected O(n^2.8074)
        renderer.setSeriesPaint(3, Color.ORANGE); // Actual Strassen

        plot.setRenderer(renderer);

        // Display chart in a GUI window
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Performance Graph");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new ChartPanel(chart));
            frame.pack();
            frame.setVisible(true);
        });

        // **Save chart as PNG**
        saveChartAsPNG(chart, "output/matrix_performance.png");
    }

    /**
     * Saves the chart as a PNG file.
     *
     * @param chart The JFreeChart object.
     * @param filePath The output file path.
     */
    private static void saveChartAsPNG(JFreeChart chart, String filePath) {
        File outputFile = new File(filePath);
        try {
            ChartUtils.saveChartAsPNG(outputFile, chart, 800, 600);
            DebugConfig.log("Graph saved to: " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving graph to file: " + e.getMessage());
        }
    }
}
