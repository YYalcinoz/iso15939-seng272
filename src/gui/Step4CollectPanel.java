package gui;

import model.AppState;
import model.QualityDimension;
import model.Metric;
import model.Scenario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * Step 4 — Collect Data
 * Shows hard-coded raw data values for each metric.
 * Calculates and displays score (1–5) for each metric.
 */
public class Step4CollectPanel extends JPanel {

    private final MainFrame frame;
    private final JPanel tableContainer = new JPanel();
    private final JLabel scenarioLabel = new JLabel();

    public Step4CollectPanel(MainFrame frame) {
        this.frame = frame;
        buildUI();
    }

    public void refresh() {
        Scenario sc = AppState.getInstance().getSelectedScenario();
        if (sc == null) {
            scenarioLabel.setText("No scenario selected. Please complete Step 2.");
            tableContainer.removeAll();
            tableContainer.revalidate();
            tableContainer.repaint();
            return;
        }

        scenarioLabel.setText("Scenario: " + sc.getName());
        tableContainer.removeAll();
        tableContainer.setLayout(new BoxLayout(tableContainer, BoxLayout.Y_AXIS));
        tableContainer.setBackground(UIConstants.LIGHT_BG);

        for (QualityDimension dim : sc.getDimensions()) {
            tableContainer.add(buildDimensionBlock(dim));
            tableContainer.add(Box.createVerticalStrut(14));
        }

        tableContainer.revalidate();
        tableContainer.repaint();
    }

    // -------------------------------------------------------
    // UI
    // -------------------------------------------------------
    private void buildUI() {
        setBackground(UIConstants.LIGHT_BG);
        setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setBackground(UIConstants.LIGHT_BG);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(24, 40, 10, 40));

        JLabel title = new JLabel("Step 4: Collect Data");
        title.setFont(UIConstants.TITLE_FONT);
        title.setForeground(UIConstants.PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(title);
        header.add(Box.createVerticalStrut(4));

        JLabel sub = new JLabel(
                "Raw metric values and calculated scores (1–5 scale). Scores are rounded to nearest 0.5.");
        sub.setFont(UIConstants.BODY_FONT);
        sub.setForeground(UIConstants.TEXT_SECONDARY);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(sub);
        header.add(Box.createVerticalStrut(6));

        scenarioLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        scenarioLabel.setForeground(UIConstants.PRIMARY);
        scenarioLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(scenarioLabel);

        add(header, BorderLayout.NORTH);

        tableContainer.setBackground(UIConstants.LIGHT_BG);
        tableContainer.setBorder(new EmptyBorder(0, 40, 10, 40));

        JScrollPane scroll = new JScrollPane(tableContainer);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        add(buildNavBar(), BorderLayout.SOUTH);
    }

    private JPanel buildDimensionBlock(QualityDimension dim) {
        JPanel block = new JPanel(new BorderLayout());
        block.setBackground(UIConstants.PANEL_BG);
        block.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR));
        block.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Dimension header
        JPanel dimHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        dimHeader.setBackground(UIConstants.TABLE_HEADER);
        JLabel dimLabel = new JLabel(dim.getName()
                + "  (Coefficient: " + dim.getCoefficient() + ")"
                + "  |  Avg Score: " + String.format("%.2f", dim.calculateScore()));
        dimLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        dimLabel.setForeground(Color.WHITE);
        dimHeader.add(dimLabel);
        block.add(dimHeader, BorderLayout.NORTH);

        // Table columns: Metric | Direction | Range | Value | Score | Coeff/Unit
        String[] cols = { "Metric", "Direction", "Range", "Value", "Score (1–5)", "Coeff / Unit" };
        List<Metric> metrics = dim.getMetrics();
        Object[][] data = new Object[metrics.size()][6];

        for (int i = 0; i < metrics.size(); i++) {
            Metric m = metrics.get(i);
            double score = m.calculateScore();
            data[i][0] = m.getName();
            data[i][1] = m.getDirectionDisplay();
            data[i][2] = m.getRangeDisplay();
            data[i][3] = formatValue(m.getValue());
            data[i][4] = formatScore(score);
            data[i][5] = m.getCoefficient() + " / " + m.getUnit();
        }

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(model);
        styleTable(table);
        block.add(table, BorderLayout.CENTER);

        return block;
    }

    /** Format a double value — strip unnecessary decimal zeros */
    private String formatValue(double v) {
        if (v == Math.floor(v))
            return String.valueOf((int) v);
        return String.valueOf(v);
    }

    /** Format a score with color hint embedded as text */
    private String formatScore(double score) {
        return String.format("%.1f", score);
    }

    private void styleTable(JTable table) {
        table.setFont(UIConstants.BODY_FONT);
        table.setRowHeight(28);
        table.setShowGrid(false);
        table.setIntercellSpacing(new java.awt.Dimension(0, 0));
        table.setSelectionBackground(UIConstants.PRIMARY_LIGHT);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
        header.setBackground(new Color(230, 238, 248));
        header.setForeground(UIConstants.PRIMARY_DARK);
        header.setPreferredSize(new java.awt.Dimension(0, 28));
        header.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, UIConstants.BORDER_COLOR));

        // Score column gets color-coding
        int scoreCol = 4;
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                Color bg = sel ? UIConstants.PRIMARY_LIGHT
                        : (row % 2 == 0 ? UIConstants.PANEL_BG : UIConstants.TABLE_ALT);
                setBackground(bg);
                setBorder(new EmptyBorder(0, 10, 0, 10));
                setForeground(UIConstants.TEXT_DARK);

                if (col == scoreCol && val != null) {
                    try {
                        double sc = Double.parseDouble(val.toString());
                        setForeground(UIConstants.getScoreColor(sc));
                        setFont(new Font("SansSerif", Font.BOLD, 13));
                    } catch (NumberFormatException ignored) {
                    }
                }
                return this;
            }
        });

        int[] widths = { 180, 110, 100, 70, 90, 100 };
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    private JPanel buildNavBar() {
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        nav.setBackground(UIConstants.LIGHT_BG);
        nav.setBorder(new EmptyBorder(0, 30, 10, 30));
        nav.add(makeButton("\u2190  Back", new Color(150, 150, 150), e -> frame.goToStep(3)));
        nav.add(makeButton("Next: Analyse  \u2192", UIConstants.PRIMARY, e -> frame.goToStep(5)));
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
