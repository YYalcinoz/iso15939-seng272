package gui;

import model.QualityDimension;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.List;

/**
 * Bonus — Radar (Spider) Chart
 * Draws a polygon-based radar chart for all dimension scores using Java 2D
 * Graphics.
 * Each axis represents one quality dimension; the polygon shows actual scores
 * vs max (5.0).
 */
public class RadarChartPanel extends JPanel {

    private List<QualityDimension> dimensions;
    private static final double MAX_SCORE = 5.0;

    public RadarChartPanel() {
        setBackground(UIConstants.PANEL_BG);
        setPreferredSize(new java.awt.Dimension(350, 320));
    }

    public void setDimensions(List<QualityDimension> dims) {
        this.dimensions = dims;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (dimensions == null || dimensions.isEmpty())
            return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int cx = w / 2;
        int cy = h / 2;
        Font axisNameFont = new Font("SansSerif", Font.BOLD, 11);
        Font axisScoreFont = new Font("SansSerif", Font.PLAIN, 10);

        int maxNameWidth = 0;
        g2.setFont(axisNameFont);
        FontMetrics nameFm = g2.getFontMetrics();
        for (QualityDimension d : dimensions) {
            maxNameWidth = Math.max(maxNameWidth, nameFm.stringWidth(shortName(d.getName())));
        }

        g2.setFont(axisScoreFont);
        FontMetrics scoreFm = g2.getFontMetrics();
        int scoreWidth = scoreFm.stringWidth("(5.0)");

        int horizontalLabelSpace = Math.max(maxNameWidth, scoreWidth) + 14;
        int verticalLabelSpace = nameFm.getHeight() + scoreFm.getHeight() + 16;

        int radius = Math.min(
                Math.max(40, cx - horizontalLabelSpace),
                Math.max(40, cy - verticalLabelSpace));

        int n = dimensions.size();

        // ----- Draw grid lines (reference rings) -----
        int rings = 5;
        for (int r = 1; r <= rings; r++) {
            double ringR = radius * ((double) r / rings);
            g2.setColor(new Color(210, 220, 235));
            g2.setStroke(new BasicStroke(r == rings ? 1.5f : 0.8f));
            drawPolygon(g2, cx, cy, ringR, n, null);

            // Ring label
            g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
            g2.setColor(new Color(160, 160, 160));
            g2.drawString(String.valueOf(r), cx + 3, (int) (cy - ringR + 3));
        }

        // ----- Draw axes -----
        g2.setColor(new Color(180, 195, 215));
        g2.setStroke(new BasicStroke(1.0f));
        for (int i = 0; i < n; i++) {
            double angle = getAngle(i, n);
            int x = cx + (int) (radius * Math.cos(angle));
            int y = cy + (int) (radius * Math.sin(angle));
            g2.drawLine(cx, cy, x, y);
        }

        // ----- Draw data polygon (filled) -----
        Path2D.Double dataPath = buildDataPath(cx, cy, radius, n);
        g2.setColor(new Color(44, 95, 138, 60));
        g2.fill(dataPath);
        g2.setColor(new Color(44, 95, 138, 200));
        g2.setStroke(new BasicStroke(2.0f));
        g2.draw(dataPath);

        // ----- Draw data points -----
        for (int i = 0; i < n; i++) {
            double score = dimensions.get(i).calculateScore();
            double pct = score / MAX_SCORE;
            double angle = getAngle(i, n);
            int px = cx + (int) (radius * pct * Math.cos(angle));
            int py = cy + (int) (radius * pct * Math.sin(angle));

            g2.setColor(UIConstants.PRIMARY);
            g2.fillOval(px - 5, py - 5, 10, 10);
            g2.setColor(Color.WHITE);
            g2.fillOval(px - 3, py - 3, 6, 6);
        }

        // ----- Draw axis labels -----
        g2.setFont(axisNameFont);
        for (int i = 0; i < n; i++) {
            double score = dimensions.get(i).calculateScore();
            double angle = getAngle(i, n);
            int lx = cx + (int) ((radius + 22) * Math.cos(angle));
            int ly = cy + (int) ((radius + 22) * Math.sin(angle));

            String name = shortName(dimensions.get(i).getName());
            String scoreStr = String.format("(%.1f)", score);

            FontMetrics fm = g2.getFontMetrics();
            int tw = fm.stringWidth(name);

            // Adjust label position based on quadrant
            int drawX = lx - tw / 2;
            int drawY = ly;
            if (Math.cos(angle) < -0.3)
                drawX = lx - tw;
            else if (Math.cos(angle) > 0.3)
                drawX = lx;
            if (Math.sin(angle) < -0.3)
                drawY = ly - 4;
            else if (Math.sin(angle) > 0.3)
                drawY = ly + 14;

            int scoreDrawY = drawY + 12;

            // Keep labels fully visible inside panel bounds.
            drawX = Math.max(4, Math.min(drawX, w - tw - 4));
            drawY = Math.max(nameFm.getAscent() + 2, Math.min(drawY, h - scoreFm.getHeight() - 4));
            scoreDrawY = Math.max(scoreFm.getAscent() + 2, Math.min(scoreDrawY, h - 4));

            g2.setColor(UIConstants.TEXT_DARK);
            g2.drawString(name, drawX, drawY);
            g2.setFont(axisScoreFont);
            g2.setColor(UIConstants.PRIMARY);
            g2.drawString(scoreStr, drawX, scoreDrawY);
            g2.setFont(axisNameFont);
        }
    }

    /** Draws a regular polygon outline (for grid rings). */
    private void drawPolygon(Graphics2D g2, int cx, int cy, double r, int n, Color fill) {
        Path2D.Double p = new Path2D.Double();
        for (int i = 0; i < n; i++) {
            double a = getAngle(i, n);
            double x = cx + r * Math.cos(a);
            double y = cy + r * Math.sin(a);
            if (i == 0)
                p.moveTo(x, y);
            else
                p.lineTo(x, y);
        }
        p.closePath();
        if (fill != null) {
            g2.setColor(fill);
            g2.fill(p);
        }
        g2.draw(p);
    }

    /** Builds the data polygon path. */
    private Path2D.Double buildDataPath(int cx, int cy, int radius, int n) {
        Path2D.Double p = new Path2D.Double();
        for (int i = 0; i < n; i++) {
            double score = dimensions.get(i).calculateScore();
            double pct = score / MAX_SCORE;
            double angle = getAngle(i, n);
            double x = cx + radius * pct * Math.cos(angle);
            double y = cy + radius * pct * Math.sin(angle);
            if (i == 0)
                p.moveTo(x, y);
            else
                p.lineTo(x, y);
        }
        p.closePath();
        return p;
    }

    /**
     * Returns the angle (in radians) for the i-th axis, starting from top (-90°).
     */
    private double getAngle(int i, int n) {
        return -Math.PI / 2 + (2 * Math.PI * i / n);
    }

    /** Abbreviates long dimension names for the radar labels. */
    private String shortName(String name) {
        if (name.length() <= 14)
            return name;
        // Take first word(s) up to ~14 chars
        String[] words = name.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (sb.length() + w.length() > 12)
                break;
            if (sb.length() > 0)
                sb.append(" ");
            sb.append(w);
        }
        return sb.toString().isEmpty() ? name.substring(0, 12) + "…" : sb.toString();
    }
}
