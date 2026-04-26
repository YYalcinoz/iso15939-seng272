package gui;

import model.AppState;
import model.QualityDimension;
import model.Scenario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

/**
 * Step 5 — Analyse
 * Displays:
 * 5a. Dimension-based weighted average scores (JProgressBar)
 * 5b. Radar chart (Bonus — drawn with Graphics2D)
 * 5c. Gap analysis (lowest scoring dimension)
 */
public class Step5AnalysePanel extends JPanel {

    private final MainFrame frame;
    private final JPanel dimScorePanel = new JPanel();
    private final RadarChartPanel radarChart = new RadarChartPanel();
    private final JPanel gapPanel = new JPanel();

    public Step5AnalysePanel(MainFrame frame) {
        this.frame = frame;
        buildUI();
    }

    public void refresh() {
        Scenario sc = AppState.getInstance().getSelectedScenario();
        if (sc == null) {
            dimScorePanel.removeAll();
            gapPanel.removeAll();
            JLabel msg = new JLabel("No scenario selected. Please complete Step 2.");
            msg.setFont(UIConstants.BODY_FONT);
            msg.setForeground(UIConstants.DANGER);
            msg.setAlignmentX(Component.LEFT_ALIGNMENT);
            dimScorePanel.add(msg);
            radarChart.setDimensions(java.util.Collections.emptyList());
            revalidate();
            repaint();
            return;
        }

        List<QualityDimension> dims = sc.getDimensions();
        buildDimensionScores(dims);
        radarChart.setDimensions(dims);
        buildGapAnalysis(dims);

        revalidate();
        repaint();
    }

    // -------------------------------------------------------
    // UI Construction
    // -------------------------------------------------------
    private void buildUI() {
        setBackground(UIConstants.LIGHT_BG);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel();
        header.setBackground(UIConstants.LIGHT_BG);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(20, 40, 8, 40));

        JLabel title = new JLabel("Step 5: Analyze Results");
        title.setFont(UIConstants.TITLE_FONT);
        title.setForeground(UIConstants.PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(title);
        header.add(Box.createVerticalStrut(4));

        JLabel sub = new JLabel("Weighted dimension scores, radar chart, and gap analysis.");
        sub.setFont(UIConstants.BODY_FONT);
        sub.setForeground(UIConstants.TEXT_SECONDARY);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(sub);
        add(header, BorderLayout.NORTH);

        // Main content split: left = scores+gap, right = radar
        JPanel content = new JPanel(new BorderLayout(20, 0));
        content.setBackground(UIConstants.LIGHT_BG);
        content.setBorder(new EmptyBorder(0, 30, 10, 30));

        JPanel leftCol = new JPanel();
        leftCol.setLayout(new BoxLayout(leftCol, BoxLayout.Y_AXIS));
        leftCol.setBackground(UIConstants.LIGHT_BG);

        // 5a Dimension scores
        dimScorePanel.setLayout(new BoxLayout(dimScorePanel, BoxLayout.Y_AXIS));
        dimScorePanel.setBackground(UIConstants.PANEL_BG);
        dimScorePanel.setBorder(BorderFactory.createCompoundBorder(
                titledBorder("Dimension Scores"),
                new EmptyBorder(10, 16, 12, 16)));
        dimScorePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftCol.add(dimScorePanel);
        leftCol.add(Box.createVerticalStrut(14));

        // 5c Gap analysis
        gapPanel.setLayout(new BoxLayout(gapPanel, BoxLayout.Y_AXIS));
        gapPanel.setBackground(UIConstants.PANEL_BG);
        gapPanel.setBorder(BorderFactory.createCompoundBorder(
                titledBorder("Gap Analysis"),
                new EmptyBorder(10, 16, 14, 16)));
        gapPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftCol.add(gapPanel);

        content.add(leftCol, BorderLayout.CENTER);

        // 5b Radar chart
        JPanel radarWrapper = new JPanel(new BorderLayout());
        radarWrapper.setBackground(UIConstants.PANEL_BG);
        radarWrapper.setBorder(BorderFactory.createCompoundBorder(
                titledBorder("Radar Chart"),
                new EmptyBorder(8, 8, 8, 8)));
        radarWrapper.setPreferredSize(new java.awt.Dimension(360, 0));
        radarWrapper.add(radarChart, BorderLayout.CENTER);
        content.add(radarWrapper, BorderLayout.EAST);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        // Nav bar
        add(buildNavBar(), BorderLayout.SOUTH);
    }

    // -------------------------------------------------------
    // 5a Dimension Scores
    // -------------------------------------------------------
    private void buildDimensionScores(List<QualityDimension> dims) {
        dimScorePanel.removeAll();

        for (QualityDimension dim : dims) {
            double score = dim.calculateScore();
            dimScorePanel.add(buildScoreRow(dim.getName(), dim.getCoefficient(), score));
            dimScorePanel.add(Box.createVerticalStrut(10));
        }
    }

    private JPanel buildScoreRow(String name, int coeff, double score) {
        JPanel row = new JPanel(new BorderLayout(10, 2));
        row.setBackground(UIConstants.PANEL_BG);
        row.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 50));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Top line: name + coeff + score value
        JPanel topLine = new JPanel(new BorderLayout());
        topLine.setBackground(UIConstants.PANEL_BG);

        JLabel nameLabel = new JLabel(name + "  (Coeff: " + coeff + ")");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        nameLabel.setForeground(UIConstants.TEXT_DARK);

        JLabel scoreLabel = new JLabel(String.format("%.2f / 5.00", score));
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        scoreLabel.setForeground(scoreColor(score));

        topLine.add(nameLabel, BorderLayout.WEST);
        topLine.add(scoreLabel, BorderLayout.EAST);

        // Progress bar
        JProgressBar bar = new JProgressBar(0, 500);
        bar.setValue((int) (score / 5.0 * 500));
        bar.setStringPainted(false);
        bar.setBackground(new Color(220, 230, 240));
        bar.setForeground(scoreColor(score));
        bar.setPreferredSize(new java.awt.Dimension(0, 14));
        bar.setBorder(null);

        row.add(topLine, BorderLayout.NORTH);
        row.add(bar, BorderLayout.CENTER);
        return row;
    }

    // -------------------------------------------------------
    // 5c Gap Analysis
    // -------------------------------------------------------
    private void buildGapAnalysis(List<QualityDimension> dims) {
        gapPanel.removeAll();

        // Find lowest-scoring dimension
        QualityDimension lowest = dims.get(0);
        for (QualityDimension d : dims) {
            if (d.calculateScore() < lowest.calculateScore())
                lowest = d;
        }

        double score = lowest.calculateScore();
        double gap = 5.0 - score;
        String level = qualityLevel(score);
        Color color = scoreColor(score);

        addGapLine("Weakest Dimension:", lowest.getName(), UIConstants.TEXT_DARK, Font.BOLD);
        addGapLine("Score:", String.format("%.2f / 5.00", score), color, Font.BOLD);
        addGapLine("Gap to Maximum:", String.format("%.2f  (5.00 - %.2f)", gap, score), UIConstants.TEXT_DARK,
                Font.PLAIN);
        addGapLine("Quality Level:", level, levelColor(level), Font.BOLD);
        gapPanel.add(Box.createVerticalStrut(10));

        JLabel warning = new JLabel(
                "<html><i>This dimension has the lowest score and requires the most improvement.</i></html>");
        warning.setFont(UIConstants.BODY_FONT);
        warning.setForeground(UIConstants.WARNING);
        warning.setAlignmentX(Component.LEFT_ALIGNMENT);
        gapPanel.add(warning);
    }

    private void addGapLine(String label, String value, Color valueColor, int style) {
        JPanel line = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        line.setBackground(UIConstants.PANEL_BG);
        line.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lbl.setForeground(UIConstants.TEXT_SECONDARY);
        lbl.setPreferredSize(new java.awt.Dimension(160, 22));

        JLabel val = new JLabel(value);
        val.setFont(new Font("SansSerif", style, 13));
        val.setForeground(valueColor);

        line.add(lbl);
        line.add(val);
        gapPanel.add(line);
    }

    // -------------------------------------------------------
    // Helpers
    // -------------------------------------------------------
    private Color scoreColor(double score) {
        return UIConstants.getScoreColor(score);
    }

    private String qualityLevel(double score) {
        if (score >= 4.5)
            return "Excellent";
        if (score >= 3.5)
            return "Good";
        if (score >= 2.5)
            return "Needs Improvement";
        return "Poor";
    }

    private Color levelColor(String level) {
        switch (level) {
            case "Excellent":
                return UIConstants.SUCCESS;
            case "Good":
                return new Color(70, 150, 70);
            case "Needs Improvement":
                return UIConstants.WARNING;
            default:
                return UIConstants.DANGER;
        }
    }

    private TitledBorder titledBorder(String title) {
        TitledBorder b = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER_COLOR), "  " + title + "  ");
        b.setTitleFont(new Font("SansSerif", Font.BOLD, 13));
        b.setTitleColor(UIConstants.PRIMARY);
        return b;
    }

    private JPanel buildNavBar() {
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        nav.setBackground(UIConstants.LIGHT_BG);
        nav.setBorder(new EmptyBorder(0, 30, 10, 30));
        nav.add(makeButton("\u2190  Back to Collect", new Color(150, 150, 150), e -> frame.goToStep(4)));
        nav.add(makeButton("\u21BA  Restart Session", new Color(100, 120, 140), e -> frame.resetSession()));
        return nav;
    }

    private JButton makeButton(String text, Color bg, java.awt.event.ActionListener al) {
        JButton btn = new JButton(text);
        btn.setFont(UIConstants.BUTTON_FONT);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new java.awt.Dimension(210, UIConstants.BUTTON_HEIGHT));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(al);
        return btn;
    }
}
