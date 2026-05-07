package ui;

import javax.swing.*;
import java.awt.*;

public class AppMainMenu {
    public static void startApplication() {
        SwingUtilities.invokeLater(() -> new AppMainMenu().createAndShow());
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
        frame.setBackground(UITheme.BACKGROUND);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BACKGROUND);
        root.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel content = new JPanel();
        content.setOpaque(true);
        content.setBackground(UITheme.SURFACE);
        content.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Cinema Booking System");
        title.setForeground(UITheme.TEXT);
        title.setFont(UITheme.FONT_LARGE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Choose a mode to continue");
        subtitle.setForeground(UITheme.TEXT_MUTED);
        subtitle.setFont(UITheme.FONT_REGULAR);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(BorderFactory.createEmptyBorder(12, 0, 24, 0));

        JPanel buttons = new JPanel(new GridLayout(1, 2, 24, 0));
        buttons.setOpaque(false);
        buttons.setMaximumSize(new Dimension(820, 240));

        JButton userButton = buildModeButton("User Side", "Browse movies, choose showtimes, and select seats.");
        JButton adminButton = buildModeButton("Admin Side", "Manage movies, schedule shows, and monitor bookings.");

        userButton.addActionListener(e -> {
            frame.dispose();
            new UserHomeScreen();
        });
        adminButton.addActionListener(e -> {
            frame.dispose();
            new AdminDashboard();
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
        button.setBackground(UITheme.SURFACE_ALT);
        button.setForeground(UITheme.TEXT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setLayout(new BorderLayout(10, 10));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel label = new JLabel(title);
        label.setForeground(UITheme.PRIMARY);
        label.setFont(UITheme.FONT_BOLD);

        JLabel descriptionLabel = new JLabel("<html><body style='color:#ffdad5; font-family:Sans-Serif; font-size:12px; line-height:1.4;'>" + description + "</body></html>");
        descriptionLabel.setFont(UITheme.FONT_REGULAR);

        button.add(label, BorderLayout.NORTH);
        button.add(descriptionLabel, BorderLayout.CENTER);
        return button;
    }
}

