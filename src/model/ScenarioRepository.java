package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository that holds all hard-coded scenario data.
 * Implements Singleton pattern. All scenario data is defined here.
 *
 * Modes : Custom (Starter), Health (Scenario A, B), Education (Scenario C, D)
 * Each scenario contains dimensions and metrics per ISO 15939 standard.
 */
public class ScenarioRepository {
    private static final ScenarioRepository INSTANCE = new ScenarioRepository();

    // Maps mode name -> list of scenarios
    private final Map<String, List<Scenario>> scenarioMap = new HashMap<>();

    private ScenarioRepository() {
        initCustomScenarios();
        initEducationScenarios();
        initHealthScenarios();
    }

    public static ScenarioRepository getInstance() {
        return INSTANCE;
    }

    // =========================================================
    // CUSTOM MODE
    // =========================================================
    private void initCustomScenarios() {
        List<Scenario> customScenarios = new ArrayList<>();

        Scenario starter = new Scenario("Custom Starter Scenario", "Custom", "Product");

        QualityDimension dim1 = new QualityDimension("Usability", 25);
        dim1.addMetric(new Metric("Task Success Rate", 50, "Higher", 0, 100, "%", 78));
        dim1.addMetric(new Metric("Time on Task", 50, "Lower", 0, 30, "min", 11));
        starter.addDimension(dim1);

        QualityDimension dim2 = new QualityDimension("Performance Efficiency", 25);
        dim2.addMetric(new Metric("Response Time", 50, "Lower", 0, 5, "sec", 1.4));
        dim2.addMetric(new Metric("Throughput", 50, "Higher", 0, 1000, "req/s", 620));
        starter.addDimension(dim2);

        QualityDimension dim3 = new QualityDimension("Reliability", 25);
        dim3.addMetric(new Metric("Uptime", 50, "Higher", 95, 100, "%", 98.9));
        dim3.addMetric(new Metric("MTTR", 50, "Lower", 0, 120, "min", 25));
        starter.addDimension(dim3);

        QualityDimension dim4 = new QualityDimension("Maintainability", 25);
        dim4.addMetric(new Metric("Code Coverage", 50, "Higher", 0, 100, "%", 72));
        dim4.addMetric(new Metric("Technical Debt Ratio", 50, "Lower", 0, 100, "%", 24));
        starter.addDimension(dim4);

        customScenarios.add(starter);

        // --- Custom Advanced Scenario ---
        Scenario advanced = new Scenario("Custom Advanced Scenario", "Custom", "Process");

        QualityDimension sprintEff = new QualityDimension("Sprint Efficiency", 30);
        sprintEff.addMetric(new Metric("Velocity", 50, "Higher", 0, 100, "points", 74));
        sprintEff.addMetric(new Metric("Sprint Goal Met Rate", 50, "Higher", 0, 100, "%", 83));
        advanced.addDimension(sprintEff);

        QualityDimension codeQuality = new QualityDimension("Code Quality", 35);
        codeQuality.addMetric(new Metric("Code Coverage", 50, "Higher", 0, 100, "%", 68));
        codeQuality.addMetric(new Metric("Bug Density", 50, "Lower", 0, 50, "bugs/KLOC", 6));
        advanced.addDimension(codeQuality);

        QualityDimension teamCollab = new QualityDimension("Team Collaboration", 35);
        teamCollab.addMetric(new Metric("PR Review Time", 50, "Lower", 0, 48, "hours", 12));
        teamCollab.addMetric(new Metric("Meeting Efficiency", 50, "Higher", 0, 100, "%", 78));
        advanced.addDimension(teamCollab);

        customScenarios.add(advanced);
        scenarioMap.put("Custom", customScenarios);
    }

    // =========================================================
    // EDUCATION MODE
    // =========================================================
    private void initEducationScenarios() {
        List<Scenario> eduScenarios = new ArrayList<>();

        // --- Scenario C: Team Alpha ---
        Scenario scenC = new Scenario("Scenario C — Team Alpha", "Education", "Product");

        QualityDimension usabilityC = new QualityDimension("Usability", 25);
        usabilityC.addMetric(new Metric("SUS Score", 50, "Higher", 0, 100, "points", 95));
        usabilityC.addMetric(new Metric("Onboarding Time", 50, "Lower", 0, 60, "min", 3));
        scenC.addDimension(usabilityC);

        QualityDimension perfC = new QualityDimension("Performance Efficiency", 20);
        perfC.addMetric(new Metric("Video Start Time", 50, "Lower", 0, 15, "sec", 2));
        perfC.addMetric(new Metric("Concurrent Exams", 50, "Higher", 0, 600, "users", 540));
        scenC.addDimension(perfC);

        QualityDimension accessC = new QualityDimension("Accessibility", 20);
        accessC.addMetric(new Metric("WCAG Compliance", 50, "Higher", 0, 100, "%", 88));
        accessC.addMetric(new Metric("Screen Reader Score", 50, "Higher", 0, 100, "%", 76));
        scenC.addDimension(accessC);

        QualityDimension relC = new QualityDimension("Reliability", 20);
        relC.addMetric(new Metric("Uptime", 50, "Higher", 95, 100, "%", 99.5));
        relC.addMetric(new Metric("MTTR", 50, "Lower", 0, 120, "min", 15));
        scenC.addDimension(relC);

        QualityDimension funcC = new QualityDimension("Functional Suitability", 15);
        funcC.addMetric(new Metric("Feature Completion", 50, "Higher", 0, 100, "%", 92));
        funcC.addMetric(new Metric("Assignment Submit Rate", 50, "Higher", 0, 100, "%", 85));
        scenC.addDimension(funcC);

        eduScenarios.add(scenC);

        // --- Scenario D: Team Beta ---
        Scenario scenD = new Scenario("Scenario D — Team Beta", "Education", "Product");

        QualityDimension usabilityD = new QualityDimension("Usability", 25);
        usabilityD.addMetric(new Metric("Interface Satisfaction", 50, "Higher", 0, 100, "points", 72));
        usabilityD.addMetric(new Metric("Navigation Time", 50, "Lower", 0, 120, "sec", 45));
        scenD.addDimension(usabilityD);

        QualityDimension perfD = new QualityDimension("Performance Efficiency", 25);
        perfD.addMetric(new Metric("Page Load Time", 50, "Lower", 0, 10, "sec", 3));
        perfD.addMetric(new Metric("Concurrent Users", 50, "Higher", 0, 500, "users", 350));
        scenD.addDimension(perfD);

        QualityDimension maintD = new QualityDimension("Maintainability", 25);
        maintD.addMetric(new Metric("Code Coverage", 50, "Higher", 0, 100, "%", 65));
        maintD.addMetric(new Metric("Technical Debt Ratio", 50, "Lower", 0, 100, "%", 20));
        scenD.addDimension(maintD);

        QualityDimension relD = new QualityDimension("Reliability", 25);
        relD.addMetric(new Metric("Uptime", 50, "Higher", 95, 100, "%", 97.5));
        relD.addMetric(new Metric("Defect Density", 50, "Lower", 0, 50, "defects/KLOC", 8));
        scenD.addDimension(relD);

        eduScenarios.add(scenD);
        scenarioMap.put("Education", eduScenarios);
    }

    // =========================================================
    // HEALTH MODE
    // =========================================================
    private void initHealthScenarios() {
        List<Scenario> healthScenarios = new ArrayList<>();

        // --- Scenario A: Hospital System ---
        Scenario scenA = new Scenario("Scenario A — Hospital System", "Health", "Product");

        QualityDimension secA = new QualityDimension("Security", 30);
        secA.addMetric(new Metric("Authentication Score", 50, "Higher", 0, 100, "points", 88));
        secA.addMetric(new Metric("Data Breach Incidents", 50, "Lower", 0, 10, "incidents", 0));
        scenA.addDimension(secA);

        QualityDimension relA = new QualityDimension("Reliability", 25);
        relA.addMetric(new Metric("System Uptime", 50, "Higher", 95, 100, "%", 99.8));
        relA.addMetric(new Metric("MTTR", 50, "Lower", 0, 60, "min", 10));
        scenA.addDimension(relA);

        QualityDimension usabilityA = new QualityDimension("Usability", 25);
        usabilityA.addMetric(new Metric("SUS Score", 50, "Higher", 0, 100, "points", 78));
        usabilityA.addMetric(new Metric("Task Completion Rate", 50, "Higher", 0, 100, "%", 91));
        scenA.addDimension(usabilityA);

        QualityDimension perfA = new QualityDimension("Performance Efficiency", 20);
        perfA.addMetric(new Metric("Response Time", 50, "Lower", 0, 5, "sec", 0.8));
        perfA.addMetric(new Metric("Throughput", 50, "Higher", 0, 1000, "req/s", 750));
        scenA.addDimension(perfA);

        healthScenarios.add(scenA);

        // --- Scenario B: Pharmacy System ---
        Scenario scenB = new Scenario("Scenario B — Pharmacy System", "Health", "Product");

        QualityDimension accB = new QualityDimension("Accuracy", 35);
        accB.addMetric(new Metric("Prescription Accuracy", 50, "Higher", 0, 100, "%", 99));
        accB.addMetric(new Metric("Error Rate", 50, "Lower", 0, 100, "%", 1));
        scenB.addDimension(accB);

        QualityDimension usabilityB = new QualityDimension("Usability", 30);
        usabilityB.addMetric(new Metric("SUS Score", 50, "Higher", 0, 100, "points", 82));
        usabilityB.addMetric(new Metric("Training Time", 50, "Lower", 0, 120, "min", 30));
        scenB.addDimension(usabilityB);

        QualityDimension perfB = new QualityDimension("Performance Efficiency", 20);
        perfB.addMetric(new Metric("Search Response", 50, "Lower", 0, 3, "sec", 0.5));
        perfB.addMetric(new Metric("Concurrent Users", 50, "Higher", 0, 200, "users", 160));
        scenB.addDimension(perfB);

        QualityDimension relB = new QualityDimension("Reliability", 15);
        relB.addMetric(new Metric("Uptime", 50, "Higher", 95, 100, "%", 99.2));
        relB.addMetric(new Metric("Backup Success Rate", 50, "Higher", 0, 100, "%", 100));
        scenB.addDimension(relB);

        healthScenarios.add(scenB);
        scenarioMap.put("Health", healthScenarios);
    }

    // =========================================================
    // Public API
    // =========================================================

    /** Returns all scenarios for the given mode. */
    public List<Scenario> getScenariosByMode(String mode) {
        List<Scenario> list = scenarioMap.get(mode);
        return (list != null) ? list : new ArrayList<>();
    }

    /** Returns all available modes. */
    public List<String> getModes() {
        return new ArrayList<>(scenarioMap.keySet());
    }
}
