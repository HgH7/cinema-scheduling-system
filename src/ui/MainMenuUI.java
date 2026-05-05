package ui;

import javax.swing.*;
import java.awt.*;

public class MainMenuUI {
    public static void startApplication() {
        SwingUtilities.invokeLater(() -> new MainMenuUI().createAndShow());
    }

    private void createAndShow() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        JFrame frame = new JFrame("Cinema Scheduling System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(960, 680);
        frame.setLocationRelativeTo(null);
        frame.setBackground(UIConstants.BACKGROUND);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIConstants.BACKGROUND);
        root.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel content = new JPanel();
        content.setOpaque(true);
        content.setBackground(UIConstants.SURFACE);
        content.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Cinema Booking System");
        title.setForeground(UIConstants.TEXT);
        title.setFont(UIConstants.FONT_LARGE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Choose a mode to continue");
        subtitle.setForeground(UIConstants.TEXT_MUTED);
        subtitle.setFont(UIConstants.FONT_REGULAR);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(BorderFactory.createEmptyBorder(12, 0, 24, 0));

        JPanel buttons = new JPanel(new GridLayout(1, 2, 24, 0));
        buttons.setOpaque(false);
        buttons.setMaximumSize(new Dimension(820, 240));

        JButton userButton = buildModeButton("User Side", "Browse movies, choose showtimes, and select seats.");
        JButton adminButton = buildModeButton("Admin Side", "Manage movies, schedule shows, and monitor bookings.");

        userButton.addActionListener(e -> {
            frame.dispose();
            new UserUI();
        });
        adminButton.addActionListener(e -> {
            frame.dispose();
            new AdminUI();
        });

        buttons.add(userButton);
        buttons.add(adminButton);

        content.add(title);
        content.add(subtitle);
        content.add(buttons);

        root.add(content, BorderLayout.CENTER);
        frame.setContentPane(root);
        frame.setVisible(true);
    }

    private JButton buildModeButton(String title, String description) {
        JButton button = new JButton();
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setBackground(UIConstants.SURFACE_ALT);
        button.setForeground(UIConstants.TEXT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setLayout(new BorderLayout(10, 10));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel label = new JLabel(title);
        label.setForeground(UIConstants.PRIMARY);
        label.setFont(UIConstants.FONT_BOLD);

        JLabel descriptionLabel = new JLabel("<html><body style='color:#ffdad5; font-family:Sans-Serif; font-size:12px; line-height:1.4;'>" + description + "</body></html>");
        descriptionLabel.setFont(UIConstants.FONT_REGULAR);

        button.add(label, BorderLayout.NORTH);
        button.add(descriptionLabel, BorderLayout.CENTER);
        return button;
    }
}

