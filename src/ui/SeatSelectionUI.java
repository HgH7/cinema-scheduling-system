package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SeatSelectionUI extends JFrame {
    private final JLabel totalLabel = new JLabel();
    private final int pricePerSeat = 14;
    private final SeatToggleButton[][] seatButtons = new SeatToggleButton[4][10];

    public SeatSelectionUI(String movieTitle) {
        super("Select Your Seats");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 780);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(UIConstants.BACKGROUND);
        root.setBorder(new EmptyBorder(18, 18, 18, 18));

        root.add(createHeader(movieTitle), BorderLayout.NORTH);
        root.add(createSeatPanel(), BorderLayout.CENTER);
        root.add(createFooter(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel createHeader(String movieTitle) {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Select Your Seats");
        title.setForeground(UIConstants.TEXT);
        title.setFont(UIConstants.FONT_TITLE);

        JLabel subtitle = new JLabel(movieTitle + " • Grand Theater • Screen 4 • Row A-M");
        subtitle.setForeground(UIConstants.TEXT_MUTED);
        subtitle.setFont(UIConstants.FONT_REGULAR);

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
        seatArea.setBackground(UIConstants.SURFACE);
        seatArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER),
                new EmptyBorder(24, 24, 24, 24)
        ));

        JPanel screenBar = new JPanel();
        screenBar.setOpaque(false);
        screenBar.setLayout(new BoxLayout(screenBar, BoxLayout.Y_AXIS));
        JPanel screenTop = new JPanel();
        screenTop.setOpaque(true);
        screenTop.setBackground(UIConstants.PRIMARY);
        screenTop.setPreferredSize(new Dimension(0, 8));
        screenTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        screenBar.add(screenTop);
        JLabel screenLabel = new JLabel("THE SCREEN");
        screenLabel.setForeground(UIConstants.PRIMARY);
        screenLabel.setFont(UIConstants.FONT_REGULAR);
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
            rowLabel.setForeground(UIConstants.TEXT_MUTED);
            rowLabel.setFont(UIConstants.FONT_REGULAR);
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
                if ((row == 0 && col >= 4 && col <= 7) || (row == 3 && col >= 8)) {
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
        legend.add(createLegendChip(UIConstants.AVAILABLE, "Available"));
        legend.add(createLegendChip(UIConstants.BOOKED, "Booked"));
        legend.add(createLegendChip(UIConstants.SELECTED_SEAT, "Selected"));
        return legend;
    }

    private JPanel createLegendChip(Color color, String label) {
        JPanel chip = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        chip.setOpaque(false);
        JPanel swatch = new JPanel();
        swatch.setOpaque(true);
        swatch.setBackground(color);
        swatch.setPreferredSize(new Dimension(16, 16));
        swatch.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER));
        JLabel text = new JLabel(label);
        text.setForeground(UIConstants.TEXT);
        text.setFont(UIConstants.FONT_REGULAR);
        chip.add(swatch);
        chip.add(text);
        return chip;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout(16, 0));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(16, 0, 0, 0));

        totalLabel.setForeground(UIConstants.TEXT);
        totalLabel.setFont(UIConstants.FONT_SEMIBOLD);
        totalLabel.setText("Total Price: $0.00");

        JButton confirm = new JButton("Confirm Booking");
        confirm.setBackground(UIConstants.PRIMARY);
        confirm.setForeground(Color.WHITE);
        confirm.setFont(UIConstants.FONT_SEMIBOLD);
        confirm.setFocusPainted(false);
        confirm.addActionListener(e -> JOptionPane.showMessageDialog(this, "Booking confirmed", "Booking", JOptionPane.INFORMATION_MESSAGE));

        footer.add(totalLabel, BorderLayout.WEST);
        footer.add(confirm, BorderLayout.EAST);
        return footer;
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
            setOpaque(true);
            setForeground(UIConstants.TEXT);
            setBackground(UIConstants.AVAILABLE);
            setBorder(BorderFactory.createLineBorder(UIConstants.BORDER));
            setFont(UIConstants.FONT_REGULAR);
            setPreferredSize(new Dimension(56, 38));
            setFocusPainted(false);
            addActionListener(e -> refreshState());
        }

        void setBooked(boolean booked) {
            this.booked = booked;
            setEnabled(!booked);
            refreshState();
        }

        void refreshState() {
            if (booked) {
                setBackground(UIConstants.BOOKED);
            } else if (isSelected()) {
                setBackground(UIConstants.SELECTED_SEAT);
            } else {
                setBackground(UIConstants.AVAILABLE);
            }
        }
    }
}

