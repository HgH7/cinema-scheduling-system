package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import model.Booking;
import service.CinemaServiceManager;
import service.ReceiptService;

public class AdminBookingManagementPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JButton cancelSelectedButton;
    private String selectedBookingId;

    public AdminBookingManagementPanel() {
        setOpaque(true);
        setBackground(UITheme.BACKGROUND);
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
        title.setForeground(UITheme.TEXT);
        title.setFont(UITheme.FONT_TITLE);

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
        filters.setBackground(UITheme.SURFACE);
        filters.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
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
        panel.setBackground(UITheme.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
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
        table.setRowHeight(36);
        UIStyles.styleDarkTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        configureTableSelection(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(createActionBar(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createActionBar() {
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        actions.setOpaque(false);

        cancelSelectedButton = new JButton("Cancel Selected Booking");
        UIStyles.stylePrimaryButton(cancelSelectedButton);
        cancelSelectedButton.setEnabled(false);
        cancelSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedBookingId != null) {
                    promptCancelBooking(selectedBookingId);
                }
            }
        });

        actions.add(cancelSelectedButton);
        return actions;
    }

    private void configureTableSelection(JTable table) {
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        selectedBookingId = (String) tableModel.getValueAt(row, 0);
                        cancelSelectedButton.setEnabled(true);
                    } else {
                        selectedBookingId = null;
                        cancelSelectedButton.setEnabled(false);
                    }
                }
            }
        });
    }

    public void refreshTable() {
        if (tableModel == null) return;
        tableModel.setRowCount(0);
        selectedBookingId = null;
        if (cancelSelectedButton != null) {
            cancelSelectedButton.setEnabled(false);
        }

        ReceiptService receiptService = CinemaServiceManager.getInstance().getReceiptService();
        java.util.List<ReceiptService.ReceiptData> receipts = receiptService.getAllReceiptData();
        if (receipts.isEmpty()) {
            for (Booking booking : CinemaServiceManager.getInstance().getBookingService().getAllBookings()) {
                tableModel.addRow(new Object[]{
                        booking.getId(),
                        booking.getShow().getMovie().getTitle(),
                        booking.getUserName(),
                        booking.getSeats().stream().map(seat -> seat.getRow() + seat.getNumber()).reduce((a, b) -> a + ", " + b).orElse(""),
                        booking.getShow().getShowTime(),
                        "Confirmed",
                        "Cancel"
                });
            }
            return;
        }

        for (ReceiptService.ReceiptData receipt : receipts) {
            tableModel.addRow(new Object[]{
                    receipt.bookingId,
                    receipt.movie,
                    "Moviegoer",
                    receipt.seats.toString().replace("[", "").replace("]", "").replace(", ", ", "),
                    receipt.date + " " + receipt.showtime,
                    "Confirmed",
                    "Cancel"
            });
        }
    }

    private void promptCancelBooking(String bookingId) {
        ReceiptService receiptService = CinemaServiceManager.getInstance().getReceiptService();
        ReceiptService.ReceiptData receipt = findReceiptById(bookingId);
        String seatText = receipt != null ? receipt.seats.toString().replace("[", "").replace("]", "") : "Unknown";
        String message = "Booking ID: #CR-" + bookingId + "\n"
                + "Movie: " + (receipt != null ? receipt.movie : "Unknown") + "\n"
                + "Showtime: " + (receipt != null ? receipt.showtime : "Unknown") + "\n"
                + "Seats: " + seatText + "\n\n"
                + "Do you want to cancel this booking?";
        int option = JOptionPane.showConfirmDialog(this, message, "Confirm Cancel Booking", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            cancelBooking(bookingId);
        }
    }

    private ReceiptService.ReceiptData findReceiptById(String bookingId) {
        for (ReceiptService.ReceiptData receipt : CinemaServiceManager.getInstance().getReceiptService().getAllReceiptData()) {
            if (receipt.bookingId.equals(bookingId)) {
                return receipt;
            }
        }
        return null;
    }

    private void cancelBooking(String bookingId) {
        ReceiptService receiptService = CinemaServiceManager.getInstance().getReceiptService();
        boolean removed = receiptService.removeReceipt(bookingId);
        boolean bookingCanceled = false;
        try {
            int idValue = Integer.parseInt(bookingId);
            bookingCanceled = CinemaServiceManager.getInstance().getBookingService().cancelBooking(idValue);
        } catch (NumberFormatException ignored) {
            // If the booking id does not parse, we still remove the receipt data.
        }
        if (removed || bookingCanceled) {
            refreshTable();
            selectedBookingId = null;
            cancelSelectedButton.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "Unable to cancel booking. Receipt not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel labeledField(String labelText, JComponent component) {
        if (component instanceof JTextField) {
            UIStyles.styleTextField((JTextField) component);
        }
        if (component instanceof JComboBox) {
            UIStyles.styleComboBox((JComboBox<?>) component);
        }
        return UIStyles.createLabeledComponent(labelText, component);
    }
}
