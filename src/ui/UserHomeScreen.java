package ui;

import javax.swing.*;
import java.awt.*;

public class UserHomeScreen extends JFrame {
    public UserHomeScreen() {
        super("CineReserve");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 860);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BACKGROUND);

        UserMovieCatalogPanel browsingPanel = new UserMovieCatalogPanel();
        root.add(createTopBar(browsingPanel), BorderLayout.NORTH);
        root.add(browsingPanel, BorderLayout.CENTER);

        setContentPane(root);
        setVisible(true);
    }

    private JPanel createTopBar(UserMovieCatalogPanel browsingPanel) {
        JPanel bar = new JPanel(new BorderLayout(16, 0));
        bar.setBackground(new Color(0x0f0f0f));
        bar.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        JLabel logo = new JLabel("CineReserve");
        logo.setForeground(UITheme.PRIMARY);
        logo.setFont(UITheme.FONT_BOLD);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setOpaque(false);
        JTextField search = new JTextField();
        search.setBackground(UITheme.SURFACE_ALT);
        search.setForeground(UITheme.TEXT);
        search.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        search.setPreferredSize(new Dimension(360, 34));
        search.setText("Search movies, actors, genres...");
        search.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (search.getText().equals("Search movies, actors, genres...")) {
                    search.setText("");
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (search.getText().isEmpty()) {
                    search.setText("Search movies, actors, genres...");
                    browsingPanel.filterMovies("");
                }
            }
        });
        search.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { doSearch(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { doSearch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { doSearch(); }
            private void doSearch() {
                String text = search.getText();
                if (!text.equals("Search movies, actors, genres...")) {
                    browsingPanel.filterMovies(text);
                }
            }
        });
        searchPanel.add(search, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        JButton switchMode = new JButton("Switch Mode");
        switchMode.setOpaque(true);
        UIStyles.stylePrimaryButton(switchMode);
        switchMode.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        switchMode.addActionListener(e -> {
            dispose();
            new AdminDashboard();
        });

        actions.add(switchMode);

        bar.add(logo, BorderLayout.WEST);
        bar.add(searchPanel, BorderLayout.CENTER);
        bar.add(actions, BorderLayout.EAST);
        return bar;
    }


}

