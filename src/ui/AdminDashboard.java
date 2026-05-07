package ui;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {
    private final CardLayout contentLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(contentLayout);

    public AdminDashboard() {
        super("Cinema Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        add(createSidebar(), BorderLayout.WEST);
        add(createTopBar(), BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        contentPanel.add(new AdminMovieManagementPanel(), "movies");
        contentPanel.add(new AdminShowManagementPanel(), "shows");
        contentPanel.add(new AdminBookingManagementPanel(), "bookings");
        contentPanel.add(new AdminAnalyticsDashboardPanel(), "analytics");
        contentLayout.show(contentPanel, "movies");

        setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel side = new JPanel();
        side.setBackground(UITheme.SURFACE);
        side.setPreferredSize(new Dimension(260, 0));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel brand = new JLabel("Cinema Admin");
        brand.setForeground(UITheme.PRIMARY);
        brand.setFont(UITheme.FONT_BOLD);
        side.add(brand);
        side.add(Box.createRigidArea(new Dimension(0, 6)));
        JLabel subtitle = new JLabel("Premium Experience");
        subtitle.setForeground(UITheme.TEXT_MUTED);
        subtitle.setFont(UITheme.FONT_REGULAR);
        side.add(subtitle);
        side.add(Box.createRigidArea(new Dimension(0, 24)));

        side.add(createSidebarButton("Movies", "movies", true));
        side.add(createSidebarButton("Showtimes", "shows", false));
        side.add(createSidebarButton("Bookings", "bookings", false));
        side.add(createSidebarButton("Analytics Dashboard", "analytics", false));
        side.add(Box.createVerticalGlue());

        return side;
    }

    private JButton createSidebarButton(String label, String cardName, boolean active) {
        JButton button = new JButton(label);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setBackground(active ? UITheme.SURFACE_ALT : UITheme.SURFACE);
        button.setForeground(active ? UITheme.TEXT : UITheme.TEXT_MUTED);
        button.setFont(UITheme.FONT_REGULAR);
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
        pageTitle.setForeground(UITheme.TEXT);
        pageTitle.setFont(UITheme.FONT_SEMIBOLD);

        JButton logout = new JButton("Log out");
        UIStyles.stylePrimaryButton(logout);
        logout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logout.addActionListener(e -> {
            dispose();
            AppMainMenu.startApplication();
        });

        top.add(pageTitle, BorderLayout.WEST);
        top.add(logout, BorderLayout.EAST);
        return top;
    }
}

