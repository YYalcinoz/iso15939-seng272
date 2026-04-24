package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a quality dimension (e.g. Usability, Reliability).
 * Contains a list of metrics and a coefficient (weight) for the overall score.
 */
public class QualityDimension {
    private String name;
    private int coefficient;          // Weight within the overall scenario
    private List<Metric> metrics;

    public QualityDimension(String name, int coefficient) {
        this.name = name;
        this.coefficient = coefficient;
        this.metrics = new ArrayList<>();
    }

    /** Adds a metric to this dimension. */
    public void addMetric(Metric metric) {
        metrics.add(metric);
    }

    /**
     * Calculates the weighted average score of all metrics in this dimension.
     * dimensionScore = Σ(metricScore × metricCoefficient) / Σ(metricCoefficient)
     */
    public double calculateScore() {
        double weightedSum = 0;
        double totalCoeff = 0;
        for (Metric m : metrics) {
            weightedSum += m.calculateScore() * m.getCoefficient();
            totalCoeff += m.getCoefficient();
        }
        return (totalCoeff > 0) ? (weightedSum / totalCoeff) : 0.0;
    }

    // ---------- Getters ----------
    public String getName()         { return name; }
    public int getCoefficient()     { return coefficient; }
    public List<Metric> getMetrics() { return metrics; }
}
