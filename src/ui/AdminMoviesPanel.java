package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminMoviesPanel extends JPanel {
    public AdminMoviesPanel() {
        setOpaque(true);
        setBackground(UIConstants.BACKGROUND);
        setLayout(new BorderLayout(16, 16));
        setBorder(new EmptyBorder(24, 24, 24, 24));

        add(createHeader(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Movie Management");
        title.setForeground(UIConstants.TEXT);
        title.setFont(UIConstants.FONT_TITLE);

        header.add(title, BorderLayout.WEST);
        return header;
    }

    private JPanel createContent() {
        JPanel content = new JPanel(new GridLayout(1, 2, 18, 0));
        content.setOpaque(false);

        content.add(createFormPanel());
        content.add(createTablePanel());
        return content;
    }

    private JPanel createFormPanel() {
        JPanel form = new JPanel();
        form.setOpaque(true);
        form.setBackground(UIConstants.SURFACE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER),
                new EmptyBorder(20, 20, 20, 20)
        ));
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        JLabel sectionTitle = new JLabel("Add New Movie");
        sectionTitle.setForeground(UIConstants.TEXT);
        sectionTitle.setFont(UIConstants.FONT_SEMIBOLD);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(sectionTitle);
        form.add(Box.createRigidArea(new Dimension(0, 16)));

        form.add(createLabeledField("Movie Title", new JTextField()));
        form.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel row = new JPanel(new GridLayout(1, 2, 12, 0));
        row.setOpaque(false);
        row.add(createLabeledField("Duration (min)", new JTextField()));
        JComboBox<String> genre = new JComboBox<>(new String[]{"Action", "Drama", "Sci-Fi", "Horror", "Comedy"});
        row.add(createLabeledField("Genre", genre));
        form.add(row);
        form.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel posterPanel = new JPanel(new BorderLayout());
        posterPanel.setOpaque(true);
        posterPanel.setBackground(UIConstants.SURFACE_ALT);
        posterPanel.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER));
        posterPanel.setPreferredSize(new Dimension(0, 120));
        posterPanel.add(new JLabel("Upload Cover Art", SwingConstants.CENTER), BorderLayout.CENTER);
        form.add(createLabeledComponent("Poster Preview", posterPanel));
        form.add(Box.createRigidArea(new Dimension(0, 18)));

        JButton addButton = new JButton("Add to Library");
        addButton.setBackground(UIConstants.PRIMARY);
        addButton.setForeground(Color.WHITE);
        addButton.setFont(UIConstants.FONT_SEMIBOLD);
        addButton.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        addButton.setFocusPainted(false);
        form.add(addButton);

        return form;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        panel.setBackground(UIConstants.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel title = new JLabel("Current Listings");
        title.setForeground(UIConstants.TEXT);
        title.setFont(UIConstants.FONT_SEMIBOLD);
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"Movie", "Duration", "Genre", "Actions"};
        Object[][] rows = {
                {"The Midnight Protocol", "142 min", "Sci-Fi", "Delete"},
                {"Dune: Part Two", "166 min", "Adventure", "Delete"},
                {"Velocity X", "118 min", "Action", "Delete"},
                {"Echoes of Silence", "95 min", "Drama", "Delete"}
        };
        DefaultTableModel model = new DefaultTableModel(rows, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setBackground(UIConstants.SURFACE);
        table.setForeground(UIConstants.TEXT);
        table.setFont(UIConstants.FONT_REGULAR);
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setOpaque(true);
        table.getTableHeader().setBackground(UIConstants.SURFACE_ALT);
        table.getTableHeader().setForeground(UIConstants.TEXT_MUTED);
        table.getTableHeader().setFont(UIConstants.FONT_REGULAR);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLabeledField(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setForeground(UIConstants.TEXT_MUTED);
        label.setFont(UIConstants.FONT_REGULAR);
        field.setFont(UIConstants.FONT_REGULAR);
        field.setBackground(UIConstants.SURFACE_ALT);
        field.setForeground(UIConstants.TEXT);
        if (field instanceof JTextField) {
            ((JTextField) field).setBorder(BorderFactory.createLineBorder(UIConstants.BORDER));
        }
        if (field instanceof JComboBox) {
            ((JComboBox<?>) field).setBackground(UIConstants.SURFACE_ALT);
            ((JComboBox<?>) field).setForeground(UIConstants.TEXT);
        }

        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLabeledComponent(String labelText, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setForeground(UIConstants.TEXT_MUTED);
        label.setFont(UIConstants.FONT_REGULAR);
        panel.add(label, BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }
}
