package ui;

import javax.swing.*;
import java.awt.*;

public final class UIStyles {
    private UIStyles() {
    }

    public static void styleTextField(JTextField field) {
        field.setFont(UITheme.FONT_REGULAR);
        field.setBackground(UITheme.SURFACE_ALT);
        field.setForeground(UITheme.TEXT);
        field.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
    }

    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(UITheme.FONT_REGULAR);
        comboBox.setBackground(UITheme.SURFACE_ALT);
        comboBox.setForeground(UITheme.TEXT);
        comboBox.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
    }

    public static void stylePrimaryButton(JButton button) {
        button.setBackground(UITheme.PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFont(UITheme.FONT_SEMIBOLD);
        button.setFocusPainted(false);
    }

    public static void styleDarkTable(JTable table) {
        table.setBackground(UITheme.SURFACE);
        table.setForeground(UITheme.TEXT);
        table.setFont(UITheme.FONT_REGULAR);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setOpaque(true);
        table.getTableHeader().setBackground(UITheme.SURFACE_ALT);
        table.getTableHeader().setForeground(UITheme.TEXT_MUTED);
        table.getTableHeader().setFont(UITheme.FONT_REGULAR);
    }

    public static JPanel createLabeledComponent(String labelText, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setForeground(UITheme.TEXT_MUTED);
        label.setFont(UITheme.FONT_REGULAR);
        panel.add(label, BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }
}
