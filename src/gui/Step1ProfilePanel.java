package gui;

import model.AppState;
import model.UserProfile;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Step 1 — Profile
 * Collects username, school, and session name.
 * Validates all fields before allowing navigation to Step 2.
 */
public class Step1ProfilePanel extends JPanel {

    private final MainFrame frame;
    private final JTextField usernameField = new JTextField();
    private final JTextField schoolField = new JTextField();
    private final JTextField sessionNameField = new JTextField();

    public Step1ProfilePanel(MainFrame frame) {
        this.frame = frame;
        buildUI();
    }

    private void buildUI() {
        setBackground(UIConstants.LIGHT_BG);
        setLayout(new BorderLayout());

        JPanel card = new JPanel();
        card.setBackground(UIConstants.PANEL_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
                new EmptyBorder(35, 45, 35, 45)));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // Title
        JLabel title = new JLabel("Step 1: User Profile");
        title.setFont(UIConstants.TITLE_FONT);
        title.setForeground(UIConstants.PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(6));

        JLabel sub = new JLabel("Please fill in your information to begin this measurement run.");
        sub.setFont(UIConstants.BODY_FONT);
        sub.setForeground(UIConstants.TEXT_SECONDARY);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(sub);
        card.add(Box.createVerticalStrut(28));

        card.add(buildRow("Username", usernameField, "Enter your full name or student ID"));
        card.add(Box.createVerticalStrut(14));
        card.add(buildRow("School", schoolField, "e.g. Faculty of Computer Science"));
        card.add(Box.createVerticalStrut(14));
        card.add(buildRow("Session Name", sessionNameField, "e.g. Binary Search vs Linear Search (10k records)"));
        card.add(Box.createVerticalStrut(28));

        JButton nextBtn = makeButton("Next: Define Quality  \u2192", UIConstants.PRIMARY);
        nextBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        nextBtn.addActionListener(e -> handleNext());
        card.add(nextBtn);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(UIConstants.LIGHT_BG);
        wrapper.setBorder(new EmptyBorder(50, 80, 50, 80));
        wrapper.add(card);
        add(wrapper, BorderLayout.CENTER);
    }

    private JPanel buildRow(String label, JTextField field, String hint) {
        JPanel row = new JPanel();
        row.setBackground(UIConstants.PANEL_BG);
        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(UIConstants.TEXT_DARK);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.add(lbl);
        row.add(Box.createVerticalStrut(4));

        field.setFont(UIConstants.BODY_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER_COLOR),
                new EmptyBorder(5, 9, 5, 9)));
        field.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 36));
        field.setPreferredSize(new java.awt.Dimension(380, 36));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.add(field);
        row.add(Box.createVerticalStrut(3));

        JLabel hintLbl = new JLabel(hint);
        hintLbl.setFont(UIConstants.SMALL_FONT);
        hintLbl.setForeground(UIConstants.TEXT_SECONDARY);
        hintLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.add(hintLbl);
        return row;
    }

    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(UIConstants.BUTTON_FONT);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new java.awt.Dimension(260, UIConstants.BUTTON_HEIGHT));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void handleNext() {
        String u = usernameField.getText().trim();
        String s = schoolField.getText().trim();
        String n = sessionNameField.getText().trim();

        if (u.isEmpty()) {
            warn("Please enter your username to continue.");
            usernameField.requestFocus();
            return;
        }
        if (s.isEmpty()) {
            warn("Please enter your school name to continue.");
            schoolField.requestFocus();
            return;
        }
        if (n.isEmpty()) {
            warn("Please enter a session name before proceeding.");
            sessionNameField.requestFocus();
            return;
        }

        AppState.getInstance().setProfile(new UserProfile(u, s, n));
        frame.goToStep(2);
    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Missing Information", JOptionPane.WARNING_MESSAGE);
    }
}
