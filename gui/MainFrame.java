package gui;

import model.AppState;

import javax.swing.*;
import java.awt.*;

/**
 * Main application window. Uses CardLayout to switch between wizard steps.
 * Owns the StepIndicatorPanel (top) and all 5 step panels.
 */
public class MainFrame extends JFrame {

    // CardLayout keys
    public static final String STEP1 = "STEP1";
    public static final String STEP2 = "STEP2";
    public static final String STEP3 = "STEP3";
    public static final String STEP4 = "STEP4";
    public static final String STEP5 = "STEP5";

    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final StepIndicatorPanel stepIndicator;

    private final Step1ProfilePanel step1;
    private final Step2DefinePanel step2;
    private final Step3PlanPanel step3;
    private final Step4CollectPanel step4;
    private final Step5AnalysePanel step5;

    public MainFrame() {
        setTitle("ISO 15939 Measurement Process Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 680);
        setMinimumSize(new java.awt.Dimension(800, 580));
        setLocationRelativeTo(null);

        // Step indicator at top
        stepIndicator = new StepIndicatorPanel();

        // Card panel in center
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(UIConstants.LIGHT_BG);

        step1 = new Step1ProfilePanel(this);
        step2 = new Step2DefinePanel(this);
        step3 = new Step3PlanPanel(this);
        step4 = new Step4CollectPanel(this);
        step5 = new Step5AnalysePanel(this);

        cardPanel.add(step1, STEP1);
        cardPanel.add(step2, STEP2);
        cardPanel.add(step3, STEP3);
        cardPanel.add(step4, STEP4);
        cardPanel.add(step5, STEP5);

        setLayout(new BorderLayout());
        add(stepIndicator, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        // Start at step 1
        goToStep(1);
    }

    /**
     * Navigates to the given step (1–5), updating the step indicator
     * and refreshing panel data.
     */
    public void goToStep(int step) {
        stepIndicator.setCurrentStep(step);
        switch (step) {
            case 1:
                cardLayout.show(cardPanel, STEP1);
                break;
            case 2:
                cardLayout.show(cardPanel, STEP2);
                step2.refresh();
                break;
            case 3:
                cardLayout.show(cardPanel, STEP3);
                step3.refresh();
                break;
            case 4:
                cardLayout.show(cardPanel, STEP4);
                step4.refresh();
                break;
            case 5:
                cardLayout.show(cardPanel, STEP5);
                step5.refresh();
                break;
        }
    }

    /** Resets singleton state and returns the wizard to step 1. */
    public void resetSession() {
        AppState.getInstance().clear();
        goToStep(1);
    }
}
