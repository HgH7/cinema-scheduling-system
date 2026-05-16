package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import model.Booking;
import service.CinemaServiceManager;
import service.ReceiptService;

public class AdminBookingManagementPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JButton cancelBookingButton;
    private JTextField seatRowField;
    private JTextField seatNumberField;
    private JComboBox<String> showComboBox;

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
                updateShowComboBox();
            }
        });
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Cancel Booking");
        title.setForeground(UITheme.TEXT);
        title.setFont(UITheme.FONT_TITLE);

        header.add(title, BorderLayout.WEST);
        return header;
    }

    private JPanel createContent() {
        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setOpaque(false);
        content.add(createCancelBookingPanel(), BorderLayout.NORTH);
        content.add(createTablePanel(), BorderLayout.CENTER);
        return content;
    }

    private JPanel createCancelBookingPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 12, 12));
        panel.setOpaque(true);
        panel.setBackground(UITheme.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(16, 16, 16, 16)
        ));

        seatRowField = new JTextField();
        UIStyles.styleTextField(seatRowField);
        seatRowField.setToolTipText("Enter seat row (e.g., A, B, C)");

        seatNumberField = new JTextField();
        UIStyles.styleTextField(seatNumberField);
        seatNumberField.setToolTipText("Enter seat number (e.g., 1, 2, 3)");

        showComboBox = new JComboBox<>();
        UIStyles.styleComboBox(showComboBox);

        panel.add(labeledField("Seat Row", seatRowField));
        panel.add(labeledField("Seat Number", seatNumberField));
        panel.add(labeledField("Show", showComboBox));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setOpaque(false);
        cancelBookingButton = new JButton("Cancel Booking");
        UIStyles.stylePrimaryButton(cancelBookingButton);
        cancelBookingButton.addActionListener(e -> cancelBookingByInput());
        buttonPanel.add(cancelBookingButton);

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setOpaque(false);
        containerPanel.add(panel, BorderLayout.CENTER);
        containerPanel.add(buttonPanel, BorderLayout.SOUTH);

        return containerPanel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        panel.setBackground(UITheme.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(16, 16, 16, 16)
        ));

        String[] columns = {"Booking ID", "Movie Title", "User Name", "Selected Seats", "Date & Time", "Status"};
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

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    public void refreshTable() {
        if (tableModel == null) return;
        tableModel.setRowCount(0);

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
                        "Confirmed"
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
                    "Confirmed"
            });
        }
    }

    private void updateShowComboBox() {
        showComboBox.removeAllItems();
        java.util.List<model.Show> allShows = CinemaServiceManager.getInstance().getShowService().getAllShows();
        for (model.Show show : allShows) {
            showComboBox.addItem(show.getMovie().getTitle() + " - " + show.getShowTime() + " (Show ID: " + show.getId() + ")");
        }
    }

    private void cancelBookingByInput() {
        String seatRow = seatRowField.getText().trim().toUpperCase();
        String seatNumberStr = seatNumberField.getText().trim();
        int selectedShowIndex = showComboBox.getSelectedIndex();

        if (seatRow.isEmpty() || seatNumberStr.isEmpty() || selectedShowIndex < 0) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int seatNumber;
        try {
            seatNumber = Integer.parseInt(seatNumberStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Seat number must be a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        java.util.List<model.Show> allShows = CinemaServiceManager.getInstance().getShowService().getAllShows();
        model.Show selectedShow = allShows.get(selectedShowIndex);

        Booking bookingToCancel = findBookingByShowAndSeat(selectedShow, seatRow, seatNumber);
        if (bookingToCancel == null) {
            JOptionPane.showMessageDialog(this, "No booking found for seat " + seatRow + seatNumber + " in the selected show.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String message = "Booking ID: #" + bookingToCancel.getBookingId() + "\n"
                + "Movie: " + bookingToCancel.getShow().getMovie().getTitle() + "\n"
                + "Showtime: " + bookingToCancel.getShow().getShowTime() + "\n"
                + "Seats: " + bookingToCancel.getSeats().stream().map(seat -> seat.getRow() + seat.getNumber()).reduce((a, b) -> a + ", " + b).orElse("") + "\n\n"
                + "Do you want to cancel this booking?";
        int option = JOptionPane.showConfirmDialog(this, message, "Confirm Cancel Booking", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            ReceiptService receiptService = CinemaServiceManager.getInstance().getReceiptService();
            boolean removed = receiptService.removeReceipt(bookingToCancel.getBookingId());
            boolean bookingCanceled = CinemaServiceManager.getInstance().getBookingService().cancelBooking(bookingToCancel.getId());

            if (removed || bookingCanceled) {
                JOptionPane.showMessageDialog(this, "Booking cancelled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
                seatRowField.setText("");
                seatNumberField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Unable to cancel booking.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Booking findBookingByShowAndSeat(model.Show show, String seatRow, int seatNumber) {
        java.util.List<Booking> allBookings = CinemaServiceManager.getInstance().getBookingService().getAllBookings();
        for (Booking booking : allBookings) {
            if (booking.getShow().getId() == show.getId()) {
                for (model.Seat seat : booking.getSeats()) {
                    if (seat.getRow().equalsIgnoreCase(seatRow) && seat.getNumber() == seatNumber) {
                        return booking;
                    }
                }
            }
        }
        return null;
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
