package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import model.Movie;

public class AdminMovieManagementPanel extends JPanel {
    private final JTextField titleField = new JTextField();
    private final JTextField durationField = new JTextField();
    private final JComboBox<String> genreBox = new JComboBox<>(new String[]{"Action", "Drama", "Sci-Fi", "Horror", "Comedy", "Animation, Fantasy", "Thriller, Racing", "Sci-Fi, Action", "Drama, Musical"});
    private final JTextField imagePathField = new JTextField();
    private DefaultTableModel tableModel;

    public AdminMovieManagementPanel() {
        setOpaque(true);
        setBackground(UITheme.BACKGROUND);
        setLayout(new BorderLayout(16, 16));
        setBorder(new EmptyBorder(24, 24, 24, 24));

        add(createHeader(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Movie Management");
        title.setForeground(UITheme.TEXT);
        title.setFont(UITheme.FONT_TITLE);

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
        form.setBackground(UITheme.SURFACE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(20, 20, 20, 20)
        ));
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        JLabel sectionTitle = new JLabel("Add New Movie");
        sectionTitle.setForeground(UITheme.TEXT);
        sectionTitle.setFont(UITheme.FONT_SEMIBOLD);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(sectionTitle);
        form.add(Box.createRigidArea(new Dimension(0, 16)));

        form.add(createLabeledField("Movie Title", titleField));
        form.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel row = new JPanel(new GridLayout(1, 2, 12, 0));
        row.setOpaque(false);
        row.add(createLabeledField("Duration (min)", durationField));
        row.add(createLabeledField("Genre", genreBox));
        form.add(row);
        form.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel imagePathPanel = new JPanel(new BorderLayout(8, 0));
        imagePathPanel.setOpaque(false);
        UIStyles.styleTextField(imagePathField);
        imagePathPanel.add(imagePathField, BorderLayout.CENTER);
        
        JButton browseButton = new JButton("Browse...");
        browseButton.setBackground(UITheme.SURFACE_ALT);
        browseButton.setForeground(UITheme.TEXT);
        browseButton.setFocusPainted(false);
        browseButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                imagePathField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        imagePathPanel.add(browseButton, BorderLayout.EAST);
        
        form.add(UIStyles.createLabeledComponent("Image Path or URL", imagePathPanel));
        form.add(Box.createRigidArea(new Dimension(0, 18)));

        JButton addButton = new JButton("Add to Library");
        UIStyles.stylePrimaryButton(addButton);
        addButton.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        addButton.addActionListener(e -> {
            String title = titleField.getText();
            String durationText = durationField.getText();
            String genre = (String) genreBox.getSelectedItem();
            String imagePath = imagePathField.getText();
            if (!title.isEmpty() && !durationText.isEmpty()) {
                try {
                    int duration = Integer.parseInt(durationText);
                    int newId = ApplicationServices.getInstance().getMovieService().getAllMovies().size() + 1;
                    ApplicationServices.getInstance().getMovieService().addMovie(newId, title, duration, genre, imagePath);
                    ApplicationServices.getInstance().saveMovies();
                    refreshTable();
                    titleField.setText("");
                    durationField.setText("");
                    imagePathField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Duration must be a number", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        form.add(addButton);

        return form;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        panel.setBackground(UITheme.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel title = new JLabel("Current Listings");
        title.setForeground(UITheme.TEXT);
        title.setFont(UITheme.FONT_SEMIBOLD);
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"Movie", "Duration", "Genre", "Actions"};
        tableModel = new DefaultTableModel(new Object[0][0], columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        refreshTable();

        JTable table = new JTable(tableModel);
        table.setRowHeight(40);
        table.getTableHeader().setReorderingAllowed(false);
        UIStyles.styleDarkTable(table);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (row >= 0 && col == 3) {
                    String title = (String) tableModel.getValueAt(row, 0);
                    Movie toRemove = null;
                    for (Movie m : ApplicationServices.getInstance().getMovieService().getAllMovies()) {
                        if (m.getTitle().equals(title)) {
                            toRemove = m;
                            break;
                        }
                    }
                    if (toRemove != null) {
                        ApplicationServices.getInstance().getMovieService().removeMovie(toRemove.getId());
                        ApplicationServices.getInstance().saveMovies();
                        refreshTable();
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Movie> movies = ApplicationServices.getInstance().getMovieService().getAllMovies();
        for (Movie m : movies) {
            tableModel.addRow(new Object[]{m.getTitle(), m.getDuration() + " min", m.getGenre(), "Delete"});
        }
    }

    private JPanel createLabeledField(String labelText, JComponent field) {
        if (field instanceof JTextField) {
            UIStyles.styleTextField((JTextField) field);
        }
        if (field instanceof JComboBox) {
            UIStyles.styleComboBox((JComboBox<?>) field);
        }
        return UIStyles.createLabeledComponent(labelText, field);
    }
}
