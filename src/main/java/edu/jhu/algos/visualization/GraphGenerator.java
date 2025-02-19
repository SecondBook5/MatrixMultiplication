package edu.jhu.algos.visualization;

import edu.jhu.algos.compare.PerformanceRecord;
import edu.jhu.algos.utils.DebugConfig;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.ChartUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Generates and displays performance comparison graphs of matrix multiplication algorithms
 * with annotations for clarity.
 */
public class GraphGenerator {

    public static void generateGraph(List<PerformanceRecord> records) {
        XYSeries naiveExpectedSeries = new XYSeries("Expected O(n³)");
        XYSeries naiveActualSeries = new XYSeries("Actual Naive Multiplications");
        XYSeries strassenExpectedSeries = new XYSeries("Expected O(n^2.8074)");
        XYSeries strassenActualSeries = new XYSeries("Actual Strassen Multiplications");

        XYSeriesCollection dataset = new XYSeriesCollection();

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

        dataset.addSeries(naiveExpectedSeries);
        dataset.addSeries(naiveActualSeries);
        dataset.addSeries(strassenExpectedSeries);
        dataset.addSeries(strassenActualSeries);

        // Create the chart
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

        // Set distinct colors for each series
        renderer.setSeriesPaint(0, Color.RED);      // Expected O(n³)
        renderer.setSeriesPaint(1, Color.BLUE);     // Actual Naive Multiplications
        renderer.setSeriesPaint(2, Color.GREEN);    // Expected O(n^2.8074)
        renderer.setSeriesPaint(3, Color.ORANGE);   // Actual Strassen Multiplications

        // Set distinct markers
        renderer.setSeriesShapesVisible(0, true); // Expected O(n³) - Squares
        renderer.setSeriesShapesVisible(1, true); // Actual Naive - Circles
        renderer.setSeriesShapesVisible(2, true); // Expected O(n^2.8074) - Triangles
        renderer.setSeriesShapesVisible(3, true); // Actual Strassen - Diamonds

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

        // Display chart in a GUI window
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Performance Graph");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(new ChartPanel(chart));
            frame.pack();
            frame.setVisible(true);

            new Timer(5000, e -> frame.dispose()).start(); // Auto-close in 5 seconds
        });

        // Save chart as PNG
        saveChartAsPNG(chart, "output/matrix_performance.png");
    }

    private static void saveChartAsPNG(JFreeChart chart, String filePath) {
        File outputFile = new File(filePath);
        try {
            ChartUtils.saveChartAsPNG(outputFile, chart, 800, 600);
            DebugConfig.log("Annotated Graph saved to: " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving graph to file: " + e.getMessage());
        }
    }
}
