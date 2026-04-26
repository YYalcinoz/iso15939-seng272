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
 * Step 3 — Plan Measurement
 * Displays all dimensions and metrics of the selected scenario in a read-only
 * table.
 * Groups metrics under their dimension header.
 */
public class Step3PlanPanel extends JPanel {

    private final MainFrame frame;
    private final JPanel tableContainer = new JPanel();
    private final JLabel scenarioLabel = new JLabel();

    public Step3PlanPanel(MainFrame frame) {
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

        scenarioLabel.setText("Scenario: " + sc.getName()
                + "   |   Mode: " + sc.getMode()
                + "   |   Quality: " + AppState.getInstance().getQualityType());

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
    // UI Construction
    // -------------------------------------------------------
    private void buildUI() {
        setBackground(UIConstants.LIGHT_BG);
        setLayout(new BorderLayout());

        // --- Header ---
        JPanel header = new JPanel();
        header.setBackground(UIConstants.LIGHT_BG);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(24, 40, 10, 40));

        JLabel title = new JLabel("Step 3: Plan Measurement");
        title.setFont(UIConstants.TITLE_FONT);
        title.setForeground(UIConstants.PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(title);
        header.add(Box.createVerticalStrut(4));

        JLabel sub = new JLabel("Quality dimensions and metrics defined for the selected scenario. (Read-Only)");
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

        // --- Scrollable table area ---
        tableContainer.setBackground(UIConstants.LIGHT_BG);
        tableContainer.setBorder(new EmptyBorder(0, 40, 10, 40));

        JScrollPane scroll = new JScrollPane(tableContainer);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(UIConstants.LIGHT_BG);
        add(scroll, BorderLayout.CENTER);

        // --- Navigation ---
        add(buildNavBar(), BorderLayout.SOUTH);
    }

    private JPanel buildDimensionBlock(QualityDimension dim) {
        JPanel block = new JPanel(new BorderLayout());
        block.setBackground(UIConstants.PANEL_BG);
        block.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR));
        block.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Dimension header bar
        JPanel dimHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        dimHeader.setBackground(UIConstants.TABLE_HEADER);
        JLabel dimLabel = new JLabel(dim.getName() + "   (Coefficient: " + dim.getCoefficient() + ")");
        dimLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        dimLabel.setForeground(Color.WHITE);
        dimHeader.add(dimLabel);
        block.add(dimHeader, BorderLayout.NORTH);

        // Table
        String[] cols = { "Metric", "Coefficient", "Direction", "Range", "Unit" };
        List<Metric> metrics = dim.getMetrics();
        Object[][] data = new Object[metrics.size()][5];
        for (int i = 0; i < metrics.size(); i++) {
            Metric m = metrics.get(i);
            data[i][0] = m.getName();
            data[i][1] = m.getCoefficient();
            data[i][2] = m.getDirectionDisplay();
            data[i][3] = m.getRangeDisplay();
            data[i][4] = m.getUnit();
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

        // Alternating row colours
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setBackground(sel ? UIConstants.PRIMARY_LIGHT
                        : (row % 2 == 0 ? UIConstants.PANEL_BG : UIConstants.TABLE_ALT));
                setBorder(new EmptyBorder(0, 10, 0, 10));
                setForeground(UIConstants.TEXT_DARK);
                return this;
            }
        });

        // Column widths
        int[] widths = { 200, 90, 120, 100, 80 };
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    private JPanel buildNavBar() {
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        nav.setBackground(UIConstants.LIGHT_BG);
        nav.setBorder(new EmptyBorder(0, 30, 10, 30));

        nav.add(makeButton("\u2190  Back", new Color(150, 150, 150), e -> frame.goToStep(2)));
        nav.add(makeButton("Next: Collect Data  \u2192", UIConstants.PRIMARY, e -> frame.goToStep(4)));
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
        btn.setPreferredSize(new java.awt.Dimension(220, UIConstants.BUTTON_HEIGHT));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(al);
        return btn;
    }
}
