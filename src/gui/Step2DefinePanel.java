package gui;

import model.AppState;
import model.Scenario;
import model.ScenarioRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

/**
 * Step 2 — Define Quality Dimensions
 * The user selects:
 * 2a. Quality Type (Product / Process) — mutual exclusion via ButtonGroup
 * 2b. Mode (Custom / Health / Education) — mutual exclusion via ButtonGroup
 * 2c. Scenario (depends on mode) — mutual exclusion via ButtonGroup
 */
public class Step2DefinePanel extends JPanel {

    private final MainFrame frame;

    // 2a — Quality Type
    private final ButtonGroup qualityGroup = new ButtonGroup();
    private final JRadioButton productBtn = new JRadioButton("Product Quality");
    private final JRadioButton processBtn = new JRadioButton("Process Quality");

    // 2b — Mode
    private final ButtonGroup modeGroup = new ButtonGroup();
    private final JRadioButton customBtn = new JRadioButton("Custom");
    private final JRadioButton healthBtn = new JRadioButton("Health");
    private final JRadioButton educationBtn = new JRadioButton("Education");

    // 2c — Scenario (dynamic)
    private final ButtonGroup scenarioGroup = new ButtonGroup();
    private final JPanel scenarioPanel = new JPanel();

    private Scenario selectedScenario = null;

    public Step2DefinePanel(MainFrame frame) {
        this.frame = frame;
        buildUI();
    }

    /** Called every time this panel is shown. */
    public void refresh() {
        AppState state = AppState.getInstance();

        if ("Process".equals(state.getQualityType())) {
            processBtn.setSelected(true);
        } else {
            productBtn.setSelected(true);
        }

        if ("Custom".equals(state.getMode())) {
            customBtn.setSelected(true);
        } else if ("Education".equals(state.getMode())) {
            educationBtn.setSelected(true);
        } else {
            healthBtn.setSelected(true);
        }

        updateScenarioList();

        Scenario savedScenario = state.getSelectedScenario();
        if (savedScenario != null) {
            java.util.Enumeration<AbstractButton> elements = scenarioGroup.getElements();
            while (elements.hasMoreElements()) {
                AbstractButton b = elements.nextElement();
                Object candidate = b.getClientProperty("scenario");
                if (candidate instanceof Scenario) {
                    Scenario sc = (Scenario) candidate;
                    if (savedScenario.getName().equals(sc.getName())
                            && savedScenario.getMode().equals(sc.getMode())) {
                        b.setSelected(true);
                        selectedScenario = sc;
                        break;
                    }
                }
            }
        }
    }

    // -------------------------------------------------------
    // UI Construction
    // -------------------------------------------------------
    private void buildUI() {
        setBackground(UIConstants.LIGHT_BG);
        setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setBackground(UIConstants.LIGHT_BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(30, 50, 20, 50));

        // Title
        JLabel title = new JLabel("Step 2: Define Quality Dimensions");
        title.setFont(UIConstants.TITLE_FONT);
        title.setForeground(UIConstants.PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(6));

        JLabel sub = new JLabel("Select quality type, mode, and the measurement scenario for this session.");
        sub.setFont(UIConstants.BODY_FONT);
        sub.setForeground(UIConstants.TEXT_SECONDARY);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(sub);
        content.add(Box.createVerticalStrut(24));

        // 2a Quality Type
        content.add(buildQualityTypeSection());
        content.add(Box.createVerticalStrut(16));

        // 2b Mode
        content.add(buildModeSection());
        content.add(Box.createVerticalStrut(16));

        // 2c Scenario
        content.add(buildScenarioSection());
        content.add(Box.createVerticalStrut(24));

        // Nav buttons
        content.add(buildNavBar());

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildQualityTypeSection() {
        JPanel panel = createSection("2a — Quality Type");

        // Configure radio buttons
        qualityGroup.add(productBtn);
        qualityGroup.add(processBtn);
        productBtn.setSelected(true); // Default

        styleRadio(productBtn);
        styleRadio(processBtn);

        JLabel desc1 = descLabel("Software product characteristics: performance, security, usability, reliability");
        JLabel desc2 = descLabel("Development process characteristics: sprint efficiency, code quality, collaboration");

        panel.add(productBtn);
        panel.add(desc1);
        panel.add(Box.createVerticalStrut(6));
        panel.add(processBtn);
        panel.add(desc2);

        return panel;
    }

    private JPanel buildModeSection() {
        JPanel panel = createSection("2b — Mode Selection");

        modeGroup.add(customBtn);
        modeGroup.add(healthBtn);
        modeGroup.add(educationBtn);
        healthBtn.setSelected(true); // Default

        styleRadio(customBtn);
        styleRadio(healthBtn);
        styleRadio(educationBtn);

        // Update scenarios when mode changes
        customBtn.addActionListener(e -> updateScenarioList());
        healthBtn.addActionListener(e -> updateScenarioList());
        educationBtn.addActionListener(e -> updateScenarioList());

        JLabel cDesc = descLabel("User-defined dimensions/metrics baseline scenario (bonus mode)");
        JLabel hDesc = descLabel("Health management system scenarios (ready-made dataset)");
        JLabel eDesc = descLabel("Education LMS system scenarios (ready-made dataset)");

        panel.add(customBtn);
        panel.add(cDesc);
        panel.add(Box.createVerticalStrut(6));
        panel.add(healthBtn);
        panel.add(hDesc);
        panel.add(Box.createVerticalStrut(6));
        panel.add(educationBtn);
        panel.add(eDesc);

        return panel;
    }

    private JPanel buildScenarioSection() {
        JPanel outer = createSection("2c — Scenario Selection");

        scenarioPanel.setOpaque(false);
        scenarioPanel.setLayout(new BoxLayout(scenarioPanel, BoxLayout.Y_AXIS));
        outer.add(scenarioPanel);

        updateScenarioList();
        return outer;
    }

    /** Rebuilds the scenario radio buttons based on the current mode selection. */
    private void updateScenarioList() {
        // Clear old buttons from group
        scenarioGroup.clearSelection();
        // Remove all elements from the Enumeration
        java.util.Enumeration<AbstractButton> elements = scenarioGroup.getElements();
        while (elements.hasMoreElements()) {
            scenarioGroup.remove(elements.nextElement());
        }

        scenarioPanel.removeAll();
        selectedScenario = null;

        String mode = customBtn.isSelected() ? "Custom" : (healthBtn.isSelected() ? "Health" : "Education");
        List<Scenario> list = ScenarioRepository.getInstance().getScenariosByMode(mode);

        for (int i = 0; i < list.size(); i++) {
            Scenario sc = list.get(i);
            JRadioButton rb = new JRadioButton(sc.getName());
            styleRadio(rb);
            rb.putClientProperty("scenario", sc);

            if (i == 0) {
                rb.setSelected(true);
                selectedScenario = sc;
            }
            rb.addActionListener(e -> {
                selectedScenario = (Scenario) rb.getClientProperty("scenario");
            });

            scenarioGroup.add(rb);
            scenarioPanel.add(rb);
            scenarioPanel.add(Box.createVerticalStrut(4));
        }

        scenarioPanel.revalidate();
        scenarioPanel.repaint();
    }

    private JPanel buildNavBar() {
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        nav.setOpaque(false);
        nav.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton backBtn = makeButton("\u2190  Back", UIConstants.TEXT_SECONDARY);
        backBtn.setBackground(new Color(180, 180, 180));
        backBtn.addActionListener(e -> frame.goToStep(1));

        JButton nextBtn = makeButton("Next: Plan Measurement  \u2192", UIConstants.PRIMARY);
        nextBtn.addActionListener(e -> handleNext());

        nav.add(backBtn);
        nav.add(nextBtn);
        return nav;
    }

    // -------------------------------------------------------
    // Logic
    // -------------------------------------------------------
    private void handleNext() {
        if (selectedScenario == null) {
            JOptionPane.showMessageDialog(this, "Please select a scenario before proceeding.",
                    "No Scenario Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        AppState state = AppState.getInstance();
        state.setQualityType(productBtn.isSelected() ? "Product" : "Process");
        state.setMode(customBtn.isSelected() ? "Custom" : (healthBtn.isSelected() ? "Health" : "Education"));
        state.setSelectedScenario(selectedScenario);

        frame.goToStep(3);
    }

    // -------------------------------------------------------
    // Helpers
    // -------------------------------------------------------
    private JPanel createSection(String title) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(UIConstants.PANEL_BG);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
                "  " + title + "  ");
        border.setTitleFont(new Font("SansSerif", Font.BOLD, 13));
        border.setTitleColor(UIConstants.PRIMARY);

        p.setBorder(BorderFactory.createCompoundBorder(border, new EmptyBorder(8, 14, 12, 14)));
        return p;
    }

    private void styleRadio(JRadioButton rb) {
        rb.setFont(UIConstants.BODY_FONT);
        rb.setBackground(UIConstants.PANEL_BG);
        rb.setForeground(UIConstants.TEXT_DARK);
        rb.setAlignmentX(Component.LEFT_ALIGNMENT);
        rb.setFocusPainted(false);
    }

    private JLabel descLabel(String text) {
        JLabel l = new JLabel("      " + text);
        l.setFont(UIConstants.SMALL_FONT);
        l.setForeground(UIConstants.TEXT_SECONDARY);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(UIConstants.BUTTON_FONT);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new java.awt.Dimension(230, UIConstants.BUTTON_HEIGHT));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
