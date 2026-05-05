package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminBookingsPanel extends JPanel {
    public AdminBookingsPanel() {
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
        JPanel filters = new JPanel(new GridLayout(1, 4, 12, 12));
        filters.setOpaque(true);
        filters.setBackground(UIConstants.SURFACE);
        filters.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER),
                new EmptyBorder(16, 16, 16, 16)
        ));

        filters.add(labeledField("Search Bookings", new JTextField()));
        filters.add(labeledField("Date Range", new JComboBox<>(new String[]{"Last 7 Days", "Last 30 Days", "Custom Range"})));
        filters.add(labeledField("Theater", new JComboBox<>(new String[]{"All Locations", "Grand Plaza IMAX", "Downtown 8", "Westside Cinema"})));
        filters.add(labeledField("Status", new JComboBox<>(new String[]{"All Statuses", "Confirmed", "Pending", "Cancelled"})));

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

        String[] columns = {"Booking ID", "Movie Title", "User Name", "Selected Seats", "Date & Time", "Status"};
        Object[][] rows = {
                {"#CR-9204", "Midnight Horizon", "Alex Sterling", "H-12, H-13", "Oct 24, 18:45 PM", "Confirmed"},
                {"#CR-9205", "Velocity X Rush", "Elena Rodriguez", "C-04", "Oct 24, 21:15 PM", "Pending"},
                {"#CR-9206", "The Enigma Grove", "Marcus Thorne", "A-01, A-02, A-03", "Oct 25, 14:00 PM", "Confirmed"},
                {"#CR-9207", "Echoes of Silence", "Sarah Jenkins", "L-20", "Oct 25, 18:30 PM", "Cancelled"}
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
        table.setRowHeight(36);
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
