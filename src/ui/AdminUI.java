package ui;

import javax.swing.*;
import java.awt.*;

public class AdminUI extends JFrame {
    private final CardLayout contentLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(contentLayout);

    public AdminUI() {
        super("Cinema Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        add(createSidebar(), BorderLayout.WEST);
        add(createTopBar(), BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        contentPanel.add(new AdminMoviesPanel(), "movies");
        contentPanel.add(new AdminShowsPanel(), "shows");
        contentPanel.add(new AdminBookingsPanel(), "bookings");
        contentLayout.show(contentPanel, "movies");

        setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel side = new JPanel();
        side.setBackground(UIConstants.SURFACE);
        side.setPreferredSize(new Dimension(260, 0));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel brand = new JLabel("Cinema Admin");
        brand.setForeground(UIConstants.PRIMARY);
        brand.setFont(UIConstants.FONT_BOLD);
        side.add(brand);
        side.add(Box.createRigidArea(new Dimension(0, 6)));
        JLabel subtitle = new JLabel("Premium Experience");
        subtitle.setForeground(UIConstants.TEXT_MUTED);
        subtitle.setFont(UIConstants.FONT_REGULAR);
        side.add(subtitle);
        side.add(Box.createRigidArea(new Dimension(0, 24)));

        side.add(createSidebarButton("Movies", "movies", true));
        side.add(createSidebarButton("Showtimes", "shows", false));
        side.add(createSidebarButton("Theaters", "theaters", false));
        side.add(createSidebarButton("Bookings", "bookings", false));
        side.add(createSidebarButton("Settings", "settings", false));
        side.add(Box.createVerticalGlue());

        JPanel profile = new JPanel(new BorderLayout(12, 0));
        profile.setOpaque(false);
        profile.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.BORDER));
        profile.setPreferredSize(new Dimension(220, 88));
        JLabel avatar = new JLabel("👤");
        avatar.setOpaque(true);
        avatar.setBackground(UIConstants.SURFACE_ALT);
        avatar.setForeground(UIConstants.TEXT);
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(48, 48));
        avatar.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER));

        JPanel details = new JPanel();
        details.setOpaque(false);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        JLabel name = new JLabel("Admin User");
        name.setForeground(UIConstants.TEXT);
        name.setFont(UIConstants.FONT_SEMIBOLD);
        JLabel role = new JLabel("Super Admin");
        role.setForeground(UIConstants.TEXT_MUTED);
        role.setFont(UIConstants.FONT_REGULAR);
        details.add(name);
        details.add(role);

        profile.add(avatar, BorderLayout.WEST);
        profile.add(details, BorderLayout.CENTER);
        side.add(profile);

        return side;
    }

    private JButton createSidebarButton(String label, String cardName, boolean active) {
        JButton button = new JButton(label);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setBackground(active ? UIConstants.SURFACE_ALT : UIConstants.SURFACE);
        button.setForeground(active ? UIConstants.TEXT : UIConstants.TEXT_MUTED);
        button.setFont(UIConstants.FONT_REGULAR);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> {
            contentLayout.show(contentPanel, cardName);
        });
        return button;
    }

    private JPanel createTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(0x0f0f0f));
        top.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel pageTitle = new JLabel("Cinema Admin Dashboard");
        pageTitle.setForeground(UIConstants.TEXT);
        pageTitle.setFont(UIConstants.FONT_SEMIBOLD);

        JButton logout = new JButton("Log out");
        logout.setBackground(UIConstants.PRIMARY);
        logout.setForeground(Color.WHITE);
        logout.setFont(UIConstants.FONT_SEMIBOLD);
        logout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logout.addActionListener(e -> {
            dispose();
            MainMenuUI.startApplication();
        });

        top.add(pageTitle, BorderLayout.WEST);
        top.add(logout, BorderLayout.EAST);
        return top;
    }
}

