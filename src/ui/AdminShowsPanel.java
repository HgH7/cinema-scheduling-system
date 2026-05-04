package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import model.Movie;
import model.Screen;
import model.Show;

public class AdminShowsPanel extends JPanel {
    private JComboBox<String> movieBox;
    private JRadioButton screen1;
    private JRadioButton screen2;
    private JFormattedTextField timeField;
    private DefaultTableModel tableModel;

    public AdminShowsPanel() {
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

        JLabel title = new JLabel("Show Management");
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

        JLabel title = new JLabel("Create New Show");
        title.setForeground(UIConstants.TEXT);
        title.setFont(UIConstants.FONT_SEMIBOLD);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(title);
        form.add(Box.createRigidArea(new Dimension(0, 16)));
        movieBox = new JComboBox<>();
        refreshMovieBox();
        form.add(createLabeledComponent("Select Movie", movieBox));
        form.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel screens = new JPanel(new GridLayout(1, 2, 10, 0));
        screens.setOpaque(false);
        screen1 = new JRadioButton("Screen 1", true);
        screen2 = new JRadioButton("Screen 2");
        ButtonGroup group = new ButtonGroup();
        group.add(screen1);
        group.add(screen2);
        for (JRadioButton radio : new JRadioButton[]{screen1, screen2}) {
            radio.setOpaque(true);
            radio.setBackground(UIConstants.SURFACE_ALT);
            radio.setForeground(UIConstants.TEXT);
            radio.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER));
        }
        screens.add(screen1);
        screens.add(screen2);
        form.add(createLabeledComponent("Auditorium Screen", screens));
        form.add(Box.createRigidArea(new Dimension(0, 12)));

        timeField = new JFormattedTextField(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
        timeField.setBackground(UIConstants.SURFACE_ALT);
        timeField.setForeground(UIConstants.TEXT);
        timeField.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER));
        form.add(createLabeledComponent("Start Time", timeField));
        form.add(Box.createRigidArea(new Dimension(0, 18)));

        JButton scheduleButton = new JButton("Schedule Show");
        scheduleButton.setBackground(UIConstants.PRIMARY);
        scheduleButton.setForeground(Color.WHITE);
        scheduleButton.setFont(UIConstants.FONT_SEMIBOLD);
        scheduleButton.setFocusPainted(false);
        scheduleButton.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        scheduleButton.addActionListener(e -> {
            String selectedMovieTitle = (String) movieBox.getSelectedItem();
            String time = timeField.getText();
            if (selectedMovieTitle != null && time != null && !time.isEmpty()) {
                Movie movie = null;
                for (Movie m : ServiceContext.getInstance().getMovieService().getAllMovies()) {
                    if (m.getTitle().equals(selectedMovieTitle)) {
                        movie = m;
                        break;
                    }
                }
                if (movie != null) {
                    Screen screen = screen1.isSelected() ? new Screen(1, "Screen 1", 4, 10) : new Screen(2, "Screen 2", 4, 10);
                    int newId = ServiceContext.getInstance().getShowService().getAllShows().size() + 1;
                    Show newShow = ServiceContext.getInstance().getShowService().addShow(newId, movie, screen, time);
                    if (newShow == null) {
                        JOptionPane.showMessageDialog(this, "Time conflict on this screen.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        refreshTable();
                        timeField.setText("");
                    }
                }
            }
        });
        form.add(scheduleButton);

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

        JLabel title = new JLabel("Existing Shows");
        title.setForeground(UIConstants.TEXT);
        title.setFont(UIConstants.FONT_SEMIBOLD);
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
        table.setBackground(UIConstants.SURFACE);
        table.setForeground(UIConstants.TEXT);
        table.setFont(UIConstants.FONT_REGULAR);
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setOpaque(true);
        table.getTableHeader().setBackground(UIConstants.SURFACE_ALT);
        table.getTableHeader().setForeground(UIConstants.TEXT_MUTED);
        table.getTableHeader().setFont(UIConstants.FONT_REGULAR);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void refreshMovieBox() {
        movieBox.removeAllItems();
        for (Movie m : ServiceContext.getInstance().getMovieService().getAllMovies()) {
            movieBox.addItem(m.getTitle());
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Show s : ServiceContext.getInstance().getShowService().getAllShows()) {
            tableModel.addRow(new Object[]{s.getMovie().getTitle(), s.getScreen().getName(), s.getShowTime(), "Active"});
        }
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
