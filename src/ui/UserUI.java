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


}

