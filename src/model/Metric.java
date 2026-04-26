package model;

/**
 * Represents a single measurement metric within a quality dimension.
 * Stores all metric properties and provides score calculation.
 */
public class Metric {
    private String name;
    private int coefficient;      // Weight within the dimension
    private String direction;     // "Higher" or "Lower"
    private double rangeMin;
    private double rangeMax;
    private String unit;
    private double value;         // Hard-coded collected value

    public Metric(String name, int coefficient, String direction,
                  double rangeMin, double rangeMax, String unit, double value) {
        this.name = name;
        this.coefficient = coefficient;
        this.direction = direction;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
        this.unit = unit;
        this.value = value;
    }

    /**
     * Calculates a normalized score between 1.0 and 5.0 based on the collected value.
     * Result is rounded to the nearest 0.5.
     * Formula:
     *   Higher is better: score = 1 + (value - min) / (max - min) * 4
     *   Lower  is better: score = 5 - (value - min) / (max - min) * 4
     */
    public double calculateScore() {
        double range = rangeMax - rangeMin;
        if (range == 0) return 3.0;
        double raw;
        if ("Higher".equals(direction)) {
            raw = 1.0 + (value - rangeMin) / range * 4.0;
        } else {
            raw = 5.0 - (value - rangeMin) / range * 4.0;
        }
        raw = Math.max(1.0, Math.min(5.0, raw));
        // Round to nearest 0.5
        return Math.round(raw * 2.0) / 2.0;
    }

    // ---------- Getters ----------
    public String getName()      { return name; }
    public int getCoefficient()  { return coefficient; }
    public String getDirection() { return direction; }
    public double getRangeMin()  { return rangeMin; }
    public double getRangeMax()  { return rangeMax; }
    public String getUnit()      { return unit; }
    public double getValue()     { return value; }

    /** Returns "Higher ↑" or "Lower ↓" for display purposes. */
    public String getDirectionDisplay() {
        return "Higher".equals(direction) ? "Higher ↑" : "Lower ↓";
    }

    /** Returns formatted range string, e.g. "0 – 100". */
    public String getRangeDisplay() {
        String minStr = (rangeMin == (long) rangeMin) ? String.valueOf((long) rangeMin)
                                                       : String.valueOf(rangeMin);
        String maxStr = (rangeMax == (long) rangeMax) ? String.valueOf((long) rangeMax)
                                                       : String.valueOf(rangeMax);
        return minStr + " – " + maxStr;
    }
}
