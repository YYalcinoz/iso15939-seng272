package gui;

import java.awt.*;

/**
 * Centralised UI constants for consistent look and feel across all panels.
 */
public class UIConstants {
    // ---------- Colors ----------
    public static final Color PRIMARY = new Color(44, 95, 138);
    public static final Color PRIMARY_DARK = new Color(28, 65, 100);
    public static final Color PRIMARY_LIGHT = new Color(220, 235, 248);
    public static final Color SUCCESS = new Color(56, 161, 82);
    public static final Color WARNING = new Color(230, 126, 34);
    public static final Color DANGER = new Color(192, 57, 43);
    public static final Color LIGHT_BG = new Color(245, 247, 250);
    public static final Color PANEL_BG = Color.WHITE;
    public static final Color TEXT_DARK = new Color(30, 30, 30);
    public static final Color TEXT_SECONDARY = new Color(110, 110, 110);
    public static final Color BORDER_COLOR = new Color(210, 215, 220);
    public static final Color TABLE_HEADER = new Color(60, 100, 140);
    public static final Color TABLE_ALT = new Color(240, 245, 250);
    public static final Color STEP_DONE = new Color(56, 161, 82);
    public static final Color STEP_ACTIVE = new Color(44, 95, 138);
    public static final Color STEP_INACTIVE = new Color(180, 180, 180);

    // ---------- Fonts ----------
    public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 20);
    public static final Font SUBTITLE_FONT = new Font("SansSerif", Font.BOLD, 14);
    public static final Font BODY_FONT = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font SMALL_FONT = new Font("SansSerif", Font.PLAIN, 11);
    public static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 13);
    public static final Font MONO_FONT = new Font("Monospaced", Font.PLAIN, 12);

    // ---------- Sizes ----------
    public static final int PADDING = 20;
    public static final int SMALL_PADDING = 10;
    public static final int BUTTON_HEIGHT = 35;

    // Step names for the indicator
    public static final String[] STEP_NAMES = { "Profile", "Define", "Plan", "Collect", "Analyse" };

    /** Shared score color thresholds used across all result views. */
    public static Color getScoreColor(double score) {
        if (score >= 4.0)
            return SUCCESS;
        if (score >= 3.0)
            return WARNING;
        return DANGER;
    }

    private UIConstants() {
    }
}
