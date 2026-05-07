package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import model.Seat;
import model.Show;
import model.Booking;

public class UserSeatSelectionScreen extends JFrame {
    private final JLabel totalLabel = new JLabel();
    private final int pricePerSeat = 14;
    private final SeatToggleButton[][] seatButtons = new SeatToggleButton[4][10];
    private final Show show;

    public UserSeatSelectionScreen(Show show) {
        super("Select Your Seats");
        this.show = show;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 780);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(UITheme.BACKGROUND);
        root.setBorder(new EmptyBorder(18, 18, 18, 18));

        root.add(createHeader(), BorderLayout.NORTH);
        root.add(createSeatPanel(), BorderLayout.CENTER);
        root.add(createFooter(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Select Your Seats");
        title.setForeground(UITheme.TEXT);
        title.setFont(UITheme.FONT_TITLE);

        String subtitleText = show != null ? (show.getMovie().getTitle() + " • Grand Theater • " + show.getScreen().getName() + " • Row A-D • " + show.getShowTime()) : "Movie • Grand Theater • Screen • Row A-D";
        JLabel subtitle = new JLabel(subtitleText);
        subtitle.setForeground(UITheme.TEXT_MUTED);
        subtitle.setFont(UITheme.FONT_REGULAR);

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(title);
        top.add(Box.createRigidArea(new Dimension(0, 8)));
        top.add(subtitle);

        header.add(top, BorderLayout.WEST);
        return header;
    }

    private JPanel createSeatPanel() {
        JPanel seatArea = new JPanel(new BorderLayout());
        seatArea.setOpaque(true);
        seatArea.setBackground(UITheme.SURFACE);
        seatArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(24, 24, 24, 24)
        ));

        JPanel screenBar = new JPanel();
        screenBar.setOpaque(false);
        screenBar.setLayout(new BoxLayout(screenBar, BoxLayout.Y_AXIS));
        JPanel screenTop = new JPanel();
        screenTop.setOpaque(true);
        screenTop.setBackground(UITheme.PRIMARY);
        screenTop.setPreferredSize(new Dimension(0, 8));
        screenTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        screenBar.add(screenTop);
        JLabel screenLabel = new JLabel("THE SCREEN");
        screenLabel.setForeground(UITheme.PRIMARY);
        screenLabel.setFont(UITheme.FONT_REGULAR);
        screenLabel.setBorder(new EmptyBorder(12, 0, 0, 0));
        screenBar.add(screenLabel);

        seatArea.add(screenBar, BorderLayout.NORTH);
        seatArea.add(createSeatsGrid(), BorderLayout.CENTER);
        seatArea.add(createLegendPanel(), BorderLayout.SOUTH);
        return seatArea;
    }

    private JPanel createSeatsGrid() {
        JPanel grid = new JPanel();
        grid.setOpaque(false);
        grid.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.NONE;

        char[] rows = {'A', 'B', 'C', 'D'};
        for (int row = 0; row < rows.length; row++) {
            gbc.gridy = row;
            gbc.gridx = 0;
            JLabel rowLabel = new JLabel(String.valueOf(rows[row]));
            rowLabel.setForeground(UITheme.TEXT_MUTED);
            rowLabel.setFont(UITheme.FONT_REGULAR);
            grid.add(rowLabel, gbc);

            for (int col = 0; col < 10; col++) {
                gbc.gridx = col + 1;
                if (col == 2 || col == 6) {
                    JPanel aisle = new JPanel();
                    aisle.setOpaque(false);
                    aisle.setPreferredSize(new Dimension(32, 0));
                    grid.add(aisle, gbc);
                    continue;
                }
                SeatToggleButton seatButton = new SeatToggleButton(rows[row] + String.valueOf(col + 1));
                seatButtons[row][col] = seatButton;
                
                Seat actualSeat = show != null ? show.getSeat(String.valueOf(rows[row]), col + 1) : null;
                if (actualSeat != null && !actualSeat.isAvailable()) {
                    seatButton.setBooked(true);
                }
                
                seatButton.addActionListener(e -> updateTotal());
                grid.add(seatButton, gbc);
            }
        }
        return grid;
    }

    private JPanel createLegendPanel() {
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 12));
        legend.setOpaque(false);
        legend.add(createLegendChip(UITheme.AVAILABLE, "Available"));
        legend.add(createLegendChip(UITheme.BOOKED, "Booked"));
        legend.add(createLegendChip(UITheme.SELECTED_SEAT, "Selected"));
        return legend;
    }

    private JPanel createLegendChip(Color color, String label) {
        JPanel chip = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        chip.setOpaque(false);
        JPanel swatch = new JPanel();
        swatch.setOpaque(true);
        swatch.setBackground(color);
        swatch.setPreferredSize(new Dimension(16, 16));
        swatch.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        JLabel text = new JLabel(label);
        text.setForeground(UITheme.TEXT);
        text.setFont(UITheme.FONT_REGULAR);
        chip.add(swatch);
        chip.add(text);
        return chip;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout(16, 0));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(16, 0, 0, 0));

        totalLabel.setForeground(UITheme.TEXT);
        totalLabel.setFont(UITheme.FONT_SEMIBOLD);
        totalLabel.setText("Total Price: $0.00");

        JButton confirm = new JButton("Confirm Booking");
        UIStyles.stylePrimaryButton(confirm);
        confirm.addActionListener(e -> {
            if (show == null) {
                JOptionPane.showMessageDialog(this, "No show available for this movie.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            List<Seat> selectedSeats = new ArrayList<>();
            for (int row = 0; row < seatButtons.length; row++) {
                for (int col = 0; col < seatButtons[row].length; col++) {
                    SeatToggleButton btn = seatButtons[row][col];
                    if (btn != null && btn.isSelected() && !btn.booked) {
                        String r = String.valueOf((char)('A' + row));
                        Seat actualSeat = show.getSeat(r, col + 1);
                        if (actualSeat != null) {
                            selectedSeats.add(actualSeat);
                        }
                    }
                }
            }
            if (selectedSeats.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select at least one seat.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int newId = ApplicationServices.getInstance().getBookingService().getAllBookings().size() + 1;
            Booking b = ApplicationServices.getInstance().getBookingService().createBooking(newId, show, selectedSeats, "Moviegoer");
            if (b != null) {
                showReceiptAndSave(b);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Some selected seats are no longer available.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        footer.add(totalLabel, BorderLayout.WEST);
        footer.add(confirm, BorderLayout.EAST);
        return footer;
    }

    private void showReceiptAndSave(Booking b) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("==============================\n");
        receipt.append("      CINERESERVE RECEIPT     \n");
        receipt.append("==============================\n");
        receipt.append("Booking ID: #CR-").append(String.format("%04d", b.getId())).append("\n");
        receipt.append("Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        receipt.append("Movie: ").append(b.getShow().getMovie().getTitle()).append("\n");
        receipt.append("Screen: ").append(b.getShow().getScreen().getName()).append("\n");
        receipt.append("Showtime: ").append(b.getShow().getShowTime()).append("\n");
        receipt.append("------------------------------\n");
        receipt.append("Seats: ");
        for (int i = 0; i < b.getSeats().size(); i++) {
            Seat s = b.getSeats().get(i);
            receipt.append(s.getRow()).append(s.getNumber());
            if (i < b.getSeats().size() - 1) receipt.append(", ");
        }
        receipt.append("\n");
        double total = b.getSeats().size() * pricePerSeat;
        receipt.append(String.format("Total Price: $%.2f\n", total));
        receipt.append("==============================\n");

        try (PrintWriter out = new PrintWriter(new FileWriter("receipts.txt", true))) {
            out.println(receipt.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        JTextArea textArea = new JTextArea(receipt.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setEditable(false);
        textArea.setBackground(UITheme.SURFACE);
        textArea.setForeground(UITheme.TEXT);
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Booking Receipt", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateTotal() {
        int selectedCount = 0;
        for (int row = 0; row < seatButtons.length; row++) {
            for (int col = 0; col < seatButtons[row].length; col++) {
                SeatToggleButton button = seatButtons[row][col];
                if (button != null && button.isSelected()) {
                    selectedCount++;
                }
            }
        }
        totalLabel.setText(String.format("Total Price: $%.2f", selectedCount * (double) pricePerSeat));
    }

    private static class SeatToggleButton extends JToggleButton {
        private boolean booked;

        SeatToggleButton(String label) {
            super(label);
            setOpaque(false);
            setContentAreaFilled(false);
            setForeground(UITheme.TEXT);
            setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
            setFont(UITheme.FONT_REGULAR);
            setPreferredSize(new Dimension(56, 38));
            setFocusPainted(false);
            addActionListener(e -> repaint());
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (booked) {
                g.setColor(UITheme.BOOKED);
            } else if (isSelected()) {
                g.setColor(UITheme.SELECTED_SEAT);
            } else {
                g.setColor(UITheme.AVAILABLE);
            }
            g.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }

        void setBooked(boolean booked) {
            this.booked = booked;
            setEnabled(!booked);
            repaint();
        }
    }
}

