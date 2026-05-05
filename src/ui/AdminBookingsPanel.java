package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import model.Booking;
import model.Seat;

public class AdminBookingsPanel extends JPanel {
    private DefaultTableModel tableModel;

    public AdminBookingsPanel() {
        setOpaque(true);
        setBackground(UIConstants.BACKGROUND);
        setLayout(new BorderLayout(16, 16));
        setBorder(new EmptyBorder(24, 24, 24, 24));

        add(createHeader(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshTable();
            }
        });
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Booking Overview");
        title.setForeground(UIConstants.TEXT);
        title.setFont(UIConstants.FONT_TITLE);

        header.add(title, BorderLayout.WEST);
        return header;
    }

    private JPanel createContent() {
        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setOpaque(false);
        content.add(createFilterPanel(), BorderLayout.NORTH);
        content.add(createTablePanel(), BorderLayout.CENTER);
        return content;
    }

    private JPanel createFilterPanel() {
        JPanel filters = new JPanel(new GridLayout(1, 3, 12, 12));
        filters.setOpaque(true);
        filters.setBackground(UIConstants.SURFACE);
        filters.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER),
                new EmptyBorder(16, 16, 16, 16)
        ));

        filters.add(labeledField("Search Bookings", new JTextField()));
        filters.add(labeledField("Date Range", new JComboBox<>(new String[]{"Last 7 Days", "Last 30 Days", "Custom Range"})));
        filters.add(labeledField("Status", new JComboBox<>(new String[]{"All Statuses", "Confirmed", "Cancelled"})));

        return filters;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        panel.setBackground(UIConstants.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER),
                new EmptyBorder(16, 16, 16, 16)
        ));

        String[] columns = {"Booking ID", "Movie Title", "User Name", "Selected Seats", "Date & Time", "Status", "Actions"};
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
        table.setRowHeight(36);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setOpaque(true);
        table.getTableHeader().setBackground(UIConstants.SURFACE_ALT);
        table.getTableHeader().setForeground(UIConstants.TEXT_MUTED);
        table.getTableHeader().setFont(UIConstants.FONT_REGULAR);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (row >= 0 && col == 6) {
                    String id = (String) tableModel.getValueAt(row, 0);
                    cancelBooking(id);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void refreshTable() {
        if (tableModel == null) return;
        tableModel.setRowCount(0);

        try (BufferedReader reader = new BufferedReader(new FileReader("receipts.txt"))) {
            String line;
            String id = "";
            String movie = "";
            String user = "Moviegoer";
            String seats = "";
            String time = "";
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Booking ID: ")) {
                    id = line.substring(12);
                } else if (line.startsWith("Movie: ")) {
                    movie = line.substring(7);
                } else if (line.startsWith("Date: ")) {
                    time = line.substring(6);
                } else if (line.startsWith("Seats: ")) {
                    seats = line.substring(7);
                } else if (line.startsWith("Total Price: ")) {
                    tableModel.addRow(new Object[]{id, movie, user, seats, time, "Confirmed", "Cancel"});
                    id = ""; movie = ""; seats = ""; time = "";
                }
            }
        } catch (IOException e) {
            // File might not exist yet, ignore
        }
    }

    private void cancelBooking(String bookingId) {
        java.io.File file = new java.io.File("receipts.txt");
        if (!file.exists()) return;

        try {
            java.util.List<String> lines = java.nio.file.Files.readAllLines(file.toPath());
            java.util.List<String> outputLines = new java.util.ArrayList<>();
            java.util.List<String> currentReceipt = new java.util.ArrayList<>();
            boolean deleteThisReceipt = false;

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                currentReceipt.add(line);
                
                if (line.startsWith("Booking ID: ") && line.equals("Booking ID: " + bookingId)) {
                    deleteThisReceipt = true;
                }
                
                if (line.trim().isEmpty() || i == lines.size() - 1) {
                    if (!deleteThisReceipt) {
                        outputLines.addAll(currentReceipt);
                    }
                    currentReceipt.clear();
                    deleteThisReceipt = false;
                }
            }
            java.nio.file.Files.write(file.toPath(), outputLines);
            refreshTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JPanel labeledField(String labelText, JComponent component) {
        JPanel wrapper = new JPanel(new BorderLayout(0, 6));
        wrapper.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setForeground(UIConstants.TEXT_MUTED);
        label.setFont(UIConstants.FONT_REGULAR);
        wrapper.add(label, BorderLayout.NORTH);

        component.setBackground(UIConstants.SURFACE_ALT);
        component.setForeground(UIConstants.TEXT);
        if (component instanceof JTextField) {
            ((JTextField) component).setBorder(BorderFactory.createLineBorder(UIConstants.BORDER));
        }
        if (component instanceof JComboBox) {
            ((JComboBox<?>) component).setBorder(BorderFactory.createLineBorder(UIConstants.BORDER));
        }

        wrapper.add(component, BorderLayout.CENTER);
        return wrapper;
    }
}
