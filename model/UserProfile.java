package model;

/**
 * Holds the user profile information entered in Step 1.
 */
public class UserProfile {
    private String username;
    private String school;
    private String sessionName;

    public UserProfile(String username, String school, String sessionName) {
        this.username = username;
        this.school = school;
        this.sessionName = sessionName;
    }

    // ---------- Getters ----------
    public String getUsername()    { return username; }
    public String getSchool()      { return school; }
    public String getSessionName() { return sessionName; }
}
