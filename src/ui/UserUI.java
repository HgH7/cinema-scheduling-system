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

        MovieBrowsingPanel browsingPanel = new MovieBrowsingPanel();
        root.add(createTopBar(browsingPanel), BorderLayout.NORTH);
        root.add(browsingPanel, BorderLayout.CENTER);

        setContentPane(root);
        setVisible(true);
    }

    private JPanel createTopBar(MovieBrowsingPanel browsingPanel) {
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
        switchMode.setBackground(UIConstants.PRIMARY);
        switchMode.setForeground(Color.WHITE);
        switchMode.setFont(UIConstants.FONT_SEMIBOLD);
        switchMode.setFocusPainted(false);
        switchMode.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        switchMode.addActionListener(e -> {
            dispose();
            new AdminUI();
        });

        actions.add(switchMode);

        bar.add(logo, BorderLayout.WEST);
        bar.add(searchPanel, BorderLayout.CENTER);
        bar.add(actions, BorderLayout.EAST);
        return bar;
    }


}

