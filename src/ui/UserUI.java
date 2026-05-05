package ui;

import javax.swing.*;
import java.awt.*;

public class UserUI extends JFrame {
    public UserUI() {
        super("CineReserve");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 860);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIConstants.BACKGROUND);

        root.add(createTopBar(), BorderLayout.NORTH);
        root.add(createSideBar(), BorderLayout.WEST);
        root.add(new MovieBrowsingPanel(), BorderLayout.CENTER);

        setContentPane(root);
        setVisible(true);
    }

    private JPanel createTopBar() {
        JPanel bar = new JPanel(new BorderLayout(16, 0));
        bar.setBackground(new Color(0x0f0f0f));
        bar.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        JLabel logo = new JLabel("CineReserve");
        logo.setForeground(UIConstants.PRIMARY);
        logo.setFont(UIConstants.FONT_BOLD);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setOpaque(false);
        JTextField search = new JTextField();
        search.setBackground(UIConstants.SURFACE_ALT);
        search.setForeground(UIConstants.TEXT);
        search.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        search.setPreferredSize(new Dimension(360, 34));
        search.setText("Search movies, actors, genres...");
        searchPanel.add(search, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        JButton notifications = new JButton("🔔");
        notifications.setOpaque(true);
        notifications.setBackground(UIConstants.SURFACE_ALT);
        notifications.setForeground(UIConstants.TEXT);
        notifications.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        JButton account = new JButton("👤");
        account.setOpaque(true);
        account.setBackground(UIConstants.SURFACE_ALT);
        account.setForeground(UIConstants.TEXT);
        account.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        JButton login = new JButton("Login");
        login.setOpaque(true);
        login.setBackground(UIConstants.SURFACE_ALT);
        login.setForeground(UIConstants.TEXT);
        login.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER));

        actions.add(notifications);
        actions.add(account);
        actions.add(login);

        bar.add(logo, BorderLayout.WEST);
        bar.add(searchPanel, BorderLayout.CENTER);
        bar.add(actions, BorderLayout.EAST);
        return bar;
    }

    private JPanel createSideBar() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(UIConstants.SURFACE);
        side.setBorder(BorderFactory.createEmptyBorder(24, 16, 24, 16));
        side.setPreferredSize(new Dimension(220, 0));

        JLabel title = new JLabel("Cinema Admin");
        title.setForeground(UIConstants.PRIMARY);
        title.setFont(UIConstants.FONT_BOLD);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(title);
        side.add(Box.createRigidArea(new Dimension(0, 8)));
        JLabel subtitle = new JLabel("Premium Experience");
        subtitle.setForeground(UIConstants.TEXT_MUTED);
        subtitle.setFont(UIConstants.FONT_REGULAR);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(subtitle);
        side.add(Box.createRigidArea(new Dimension(0, 24)));

        side.add(createSideButton("Movies", true));
        side.add(createSideButton("Showtimes", false));
        side.add(createSideButton("Theaters", false));
        side.add(createSideButton("Bookings", false));
        side.add(createSideButton("Settings", false));

        side.add(Box.createVerticalGlue());
        side.add(createProfilePanel());

        return side;
    }

    private JPanel createProfilePanel() {
        JPanel profile = new JPanel(new BorderLayout(12, 0));
        profile.setOpaque(false);
        profile.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.BORDER));
        profile.setPreferredSize(new Dimension(180, 80));
        JLabel avatar = new JLabel("👤");
        avatar.setOpaque(true);
        avatar.setBackground(UIConstants.SURFACE_ALT);
        avatar.setForeground(UIConstants.TEXT);
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(48, 48));
        avatar.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER));

        JPanel labels = new JPanel();
        labels.setOpaque(false);
        labels.setLayout(new BoxLayout(labels, BoxLayout.Y_AXIS));
        JLabel name = new JLabel("Alex Rivera");
        name.setForeground(UIConstants.TEXT);
        name.setFont(UIConstants.FONT_SEMIBOLD);
        JLabel role = new JLabel("Gold Member");
        role.setForeground(UIConstants.TEXT_MUTED);
        role.setFont(UIConstants.FONT_REGULAR);
        labels.add(name);
        labels.add(role);

        profile.add(avatar, BorderLayout.WEST);
        profile.add(labels, BorderLayout.CENTER);
        return profile;
    }

    private JButton createSideButton(String title, boolean active) {
        JButton button = new JButton(title);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setBackground(active ? UIConstants.SURFACE_ALT : UIConstants.SURFACE);
        button.setForeground(active ? UIConstants.TEXT : UIConstants.TEXT_MUTED);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }
}

