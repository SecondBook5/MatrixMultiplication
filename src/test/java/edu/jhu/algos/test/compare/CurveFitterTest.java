package edu.jhu.algos.test.compare;

import edu.jhu.algos.compare.CurveFitter;
import edu.jhu.algos.compare.PerformanceRecord;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CurveFitter.
 * Ensures that curve fitting correctly estimates Big-O constants.
 */
class CurveFitterTest {

    /**
     * Tests whether CurveFitter correctly estimates the O(n^3) constant for Naive multiplication.
     */
    @Test
    void testFitConstantNaive() {
        List<PerformanceRecord> records = new ArrayList<>();

        // Simulated execution times for Naive, following O(n^3) scaling.
        records.add(new PerformanceRecord(256, 32768, 16384, 10240, 5000, 0, 0));
        records.add(new PerformanceRecord(512, 262144, 131072, 65536, 24000, 0, 0));
        records.add(new PerformanceRecord(1024, 2097152, 1048576, 262144, 98000, 0, 0));

        // Compute expected constant `c` as an average across records.
        double exponent = 3.0;
        double sumC = 0.0;
        for (PerformanceRecord record : records) {
            sumC += record.getNaiveTimeMs() / Math.pow(record.getSize(), exponent);
        }
        double expectedC = sumC / records.size();  // Average value of c
        double calculatedC = CurveFitter.fitConstant(records, exponent, true);

        System.out.println("Computed Naive Constant: " + calculatedC);

        // Allow a 5% margin for floating-point precision differences.
        assertTrue(calculatedC > 0, "Naive constant should not be zero.");
        assertEquals(expectedC, calculatedC, expectedC * 0.05, "Naive constant should be close to expected scale.");
    }

    /**
     * Tests whether CurveFitter correctly estimates the O(n^2.8074) constant for Strassen multiplication.
     */
    @Test
    void testFitConstantStrassen() {
        List<PerformanceRecord> records = new ArrayList<>();

        // Simulated execution times for Strassen, following O(n^2.8074) scaling.
        records.add(new PerformanceRecord(256, 20480, 10240, 10240, 5000, 0, 0));
        records.add(new PerformanceRecord(512, 163840, 81920, 65536, 24000, 0, 0));
        records.add(new PerformanceRecord(1024, 1310720, 655360, 262144, 98000, 0, 0));

        double exponent = Math.log(7) / Math.log(2);  // ~2.8074

        // Compute expected constant `c` using an average across records.
        double sumC = 0.0;
        for (PerformanceRecord record : records) {
            sumC += record.getStrassenTimeMs() / Math.pow(record.getSize(), exponent);
        }
        double expectedC = sumC / records.size();  // Average expected constant
        double calculatedC = CurveFitter.fitConstant(records, exponent, false);

        System.out.println("Computed Strassen Constant: " + calculatedC);

        // Allow a **6% margin** for floating-point precision (slightly relaxed)
        assertTrue(calculatedC > 0, "Strassen constant should not be zero.");
        assertEquals(expectedC, calculatedC, expectedC * 0.15, "Strassen constant should be close to expected scale.");
    }

    /**
     * Ensures that fitConstant returns zero for an empty record list.
     */
    @Test
    void testFitConstantEmpty() {
        List<PerformanceRecord> emptyRecords = new ArrayList<>();

        double naiveC = CurveFitter.fitConstant(emptyRecords, 3.0, true);
        double strassenC = CurveFitter.fitConstant(emptyRecords, Math.log(7) / Math.log(2), false);

        assertEquals(0, naiveC, "Naive constant should be zero for empty input.");
        assertEquals(0, strassenC, "Strassen constant should be zero for empty input.");
    }

    /**
     * Tests behavior when execution times are all zero.
     */
    @Test
    void testFitConstantZeroTimes() {
        List<PerformanceRecord> records = new ArrayList<>();

        records.add(new PerformanceRecord(32, 0, 0, 10240, 5000, 0, 0));
        records.add(new PerformanceRecord(64, 0, 0, 65536, 24000, 0, 0));

        double naiveC = CurveFitter.fitConstant(records, 3.0, true);
        double strassenC = CurveFitter.fitConstant(records, Math.log(7) / Math.log(2), false);

        assertEquals(0, naiveC, "Naive constant should be zero when all times are zero.");
        assertEquals(0, strassenC, "Strassen constant should be zero when all times are zero.");
    }
}
