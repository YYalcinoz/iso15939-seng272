package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Displays the wizard step indicator at the top of the screen.
 * Shows step numbers, names, and status (active / completed / pending).
 */
public class StepIndicatorPanel extends JPanel {
    private int currentStep = 1;
    private static final int TOTAL_STEPS = 5;

    public StepIndicatorPanel() {
        setPreferredSize(new Dimension(0, 70));
        setBackground(UIConstants.PRIMARY_DARK);
    }

    public void setCurrentStep(int step) {
        this.currentStep = step;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int cx_base = w / 2;
        int stepW = Math.min(160, (w - 40) / TOTAL_STEPS);
        int totalW = stepW * TOTAL_STEPS;
        int startX = cx_base - totalW / 2 + stepW / 2;

        int circleY = h / 2 - 4;
        int circleR = 14;
        int labelY = circleY + circleR + 14;

        for (int i = 1; i <= TOTAL_STEPS; i++) {
            int cx = startX + (i - 1) * stepW;

            // Connector line
            if (i > 1) {
                int prevCx = startX + (i - 2) * stepW;
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
                g2.setColor(i - 1 < currentStep ? UIConstants.STEP_DONE : new Color(90, 115, 140));
                int connectorGap = 2;
                g2.drawLine(prevCx + circleR + connectorGap, circleY, cx - circleR - connectorGap, circleY);
            }

            // Circle fill
            Color circleColor;
            if (i < currentStep)
                circleColor = UIConstants.STEP_DONE;
            else if (i == currentStep)
                circleColor = UIConstants.PRIMARY_LIGHT;
            else
                circleColor = new Color(75, 100, 125);

            g2.setColor(circleColor);
            g2.fillOval(cx - circleR, circleY - circleR, circleR * 2, circleR * 2);

            // White border for active
            if (i == currentStep) {
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(cx - circleR, circleY - circleR, circleR * 2, circleR * 2);
            }

            // Number or check
            if (i < currentStep) {
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("\u2713", cx - fm.stringWidth("\u2713") / 2, circleY + 5);
            } else {
                String num = String.valueOf(i);
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                g2.setColor(i == currentStep ? UIConstants.PRIMARY_DARK : new Color(160, 185, 205));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(num, cx - fm.stringWidth(num) / 2, circleY + 4);
            }

            // Step label
            String name = UIConstants.STEP_NAMES[i - 1];
            g2.setFont(i == currentStep
                    ? new Font("SansSerif", Font.BOLD, 11)
                    : new Font("SansSerif", Font.PLAIN, 10));
            g2.setColor(i == currentStep ? Color.WHITE
                    : (i < currentStep ? new Color(170, 215, 170) : new Color(125, 155, 180)));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(name, cx - fm.stringWidth(name) / 2, labelY);
        }
    }
}
