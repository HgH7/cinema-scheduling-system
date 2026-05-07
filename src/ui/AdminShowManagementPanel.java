package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import service.CinemaServiceManager;
import model.Movie;
import model.Screen;
import model.Show;
import util.InputValidator;

public class AdminShowManagementPanel extends JPanel {
    private JComboBox<String> movieBox;
    private JComboBox<String> screenBox;
    private JTextField timeField;
    private DefaultTableModel tableModel;

    public AdminShowManagementPanel() {
        setOpaque(true);
        setBackground(UITheme.BACKGROUND);
        setLayout(new BorderLayout(16, 16));
        setBorder(new EmptyBorder(24, 24, 24, 24));

        add(createHeader(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshMovieBox();
                refreshTable();
            }
        });
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Show Management");
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

        JLabel title = new JLabel("Create New Show");
        title.setForeground(UITheme.TEXT);
        title.setFont(UITheme.FONT_SEMIBOLD);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(title);
        form.add(Box.createRigidArea(new Dimension(0, 16)));
        movieBox = new JComboBox<>();
        UIStyles.styleComboBox(movieBox);
        refreshMovieBox();
        form.add(UIStyles.createLabeledComponent("Select Movie", movieBox));
        form.add(Box.createRigidArea(new Dimension(0, 12)));

        screenBox = new JComboBox<>(new String[]{"Screen 1", "Screen 2", "Screen 3", "Screen 4", "Screen 5"});
        UIStyles.styleComboBox(screenBox);
        form.add(UIStyles.createLabeledComponent("Auditorium Screen", screenBox));
        form.add(Box.createRigidArea(new Dimension(0, 12)));

        timeField = new JTextField("18:30");
        UIStyles.styleTextField(timeField);
        form.add(UIStyles.createLabeledComponent("Start Time", timeField));
        form.add(Box.createRigidArea(new Dimension(0, 18)));

        JButton scheduleButton = new JButton("Schedule Show");
        UIStyles.stylePrimaryButton(scheduleButton);
        scheduleButton.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        scheduleButton.addActionListener(e -> {
            String selectedMovieTitle = (String) movieBox.getSelectedItem();
            String time = timeField.getText();
            if (selectedMovieTitle == null || selectedMovieTitle.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a movie.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                InputValidator.validateShowTime(time);
            } catch (InputValidator.ValidationException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Time", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Movie movie = null;
            for (Movie m : CinemaServiceManager.getInstance().getMovieService().getAllMovies()) {
                if (m.getTitle().equals(selectedMovieTitle)) {
                    movie = m;
                    break;
                }
            }

            if (movie == null) {
                JOptionPane.showMessageDialog(this, "Selected movie could not be found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String screenName = (String) screenBox.getSelectedItem();
            int screenNum = Integer.parseInt(screenName.replace("Screen ", ""));
            Screen screen = new Screen(screenNum, screenName, 4, 10);

            int newId = CinemaServiceManager.getInstance().getShowService().getAllShows().size() + 1;
            Show newShow = CinemaServiceManager.getInstance().getShowService().addShow(newId, movie, screen, time);
            if (newShow != null) {
                refreshTable();
                timeField.setText("18:30");
            } else {
                JOptionPane.showMessageDialog(this, "Time conflict on " + screenName + " with an existing show.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        form.add(scheduleButton);

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

        JLabel title = new JLabel("Existing Shows");
        title.setForeground(UITheme.TEXT);
        title.setFont(UITheme.FONT_SEMIBOLD);
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"Movie", "Screen", "Time", "Status"};
        tableModel = new DefaultTableModel(new Object[0][0], columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        refreshTable();
        JTable table = new JTable(tableModel);
        table.setRowHeight(40);
        UIStyles.styleDarkTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void refreshMovieBox() {
        movieBox.removeAllItems();
        for (Movie m : CinemaServiceManager.getInstance().getMovieService().getAllMovies()) {
            movieBox.addItem(m.getTitle());
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Show s : CinemaServiceManager.getInstance().getShowService().getAllShows()) {
            tableModel.addRow(new Object[]{s.getMovie().getTitle(), s.getScreen().getName(), s.getShowTime(), "Active"});
        }
    }
}
