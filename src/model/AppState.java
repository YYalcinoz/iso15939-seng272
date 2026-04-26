package model;

/**
 * Singleton class that holds the global application state.
 * Shared across all wizard steps.
 */
public class AppState {
    private static final AppState INSTANCE = new AppState();

    private UserProfile profile;
    private String qualityType; // "Product" or "Process"
    private String mode; // "Health" or "Education"
    private Scenario selectedScenario;

    private AppState() {
    }

    public static AppState getInstance() {
        return INSTANCE;
    }

    // ---------- Getters & Setters ----------
    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile p) {
        this.profile = p;
    }

    public String getQualityType() {
        return qualityType;
    }

    public void setQualityType(String qt) {
        this.qualityType = qt;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Scenario getSelectedScenario() {
        return selectedScenario;
    }

    public void setSelectedScenario(Scenario s) {
        this.selectedScenario = s;
    }

    /** Clears all session state for a fresh restart. */
    public void clear() {
        profile = null;
        qualityType = null;
        mode = null;
        selectedScenario = null;
    }
}
