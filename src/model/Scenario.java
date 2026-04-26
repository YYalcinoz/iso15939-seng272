package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a measurement scenario (e.g. "Scenario C — Team Alpha").
 * Contains a set of dimensions that belong to this scenario.
 */
public class Scenario {
    private String name;
    private String mode;         // "Health" or "Education"
    private String qualityType;  // "Product" or "Process"
    private List<QualityDimension> dimensions;

    public Scenario(String name, String mode, String qualityType) {
        this.name = name;
        this.mode = mode;
        this.qualityType = qualityType;
        this.dimensions = new ArrayList<QualityDimension>();
    }

    /** Adds a quality dimension to this scenario. */
    public void addDimension(QualityDimension dimension) {
        dimensions.add(dimension);
    }

    // ---------- Getters ----------
    public String getName()               { return name; }
    public String getMode()               { return mode; }
    public String getQualityType()        { return qualityType; }
    public List<QualityDimension> getDimensions() { return dimensions; }

    @Override
    public String toString() { return name; }
}
