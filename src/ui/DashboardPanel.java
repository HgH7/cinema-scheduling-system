package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Dashboard panel with analytics summary and metrics
 * Phase 3: Bonus feature for sales analytics visualization
 */
public class DashboardPanel extends JPanel {
    
    public DashboardPanel() {
        setOpaque(true);
        setBackground(UIConstants.BACKGROUND);
        setLayout(new GridLayout(2, 1, 16, 16));
        setBorder(new EmptyBorder(16, 16, 16, 16));

        add(createMoviePopularityPanel());
        add(createMetricsPanel());

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                repaint();
            }
        });
    }

    /**
     * Creates movie popularity summary
     */
    private JPanel createMoviePopularityPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        panel.setBackground(UIConstants.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel title = new JLabel("Movie Popularity Ranking");
        title.setForeground(UIConstants.TEXT);
        title.setFont(UIConstants.FONT_SEMIBOLD);
        panel.add(title, BorderLayout.NORTH);

        JTextArea stats = new JTextArea();
        stats.setOpaque(false);
        stats.setForeground(UIConstants.TEXT);
        stats.setFont(UIConstants.FONT_REGULAR);
        stats.setEditable(false);
        stats.setText(generateMovieStats());

        JScrollPane scroll = new JScrollPane(stats);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates key metrics display
     */
    private JPanel createMetricsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 12, 12));
        panel.setOpaque(true);
        panel.setBackground(UIConstants.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER),
                new EmptyBorder(12, 12, 12, 12)
        ));

        panel.add(createMetricCard("Total Revenue", getTotalRevenue(), UIConstants.PRIMARY));
        panel.add(createMetricCard("Total Bookings", getTotalBookings(), new Color(0x2f, 0x8f, 0x4d)));
        panel.add(createMetricCard("Avg Price/Seat", getAveragePrice(), new Color(0xf7, 0xc0, 0x25)));
        panel.add(createMetricCard("Top Movie", getTopMovie(), UIConstants.PRIMARY_LIGHT));

        return panel;
    }

    /**
     * Creates individual metric card
     */
    private JPanel createMetricCard(String label, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setOpaque(false);

        JLabel labelText = new JLabel(label);
        labelText.setForeground(UIConstants.TEXT_MUTED);
        labelText.setFont(UIConstants.FONT_REGULAR);

        JLabel valueText = new JLabel(value);
        valueText.setForeground(color);
        valueText.setFont(UIConstants.FONT_BOLD);

        card.add(labelText, BorderLayout.NORTH);
        card.add(valueText, BorderLayout.CENTER);

        return card;
    }


    /**
     * Generates movie statistics text
     */
    private String generateMovieStats() {
        Map<String, Integer> bookings = getMovieBookingCounts();
        StringBuilder stats = new StringBuilder();

        bookings.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .forEach(entry -> {
                    stats.append("• ").append(entry.getKey()).append(": ")
                            .append(entry.getValue()).append(" bookings\n");
                });

        return stats.toString().isEmpty() ? "No booking data available" : stats.toString();
    }

    // Data retrieval methods

    private Map<String, Double> getMovieRevenue() {
        Map<String, Double> movieRevenue = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("receipts.txt"))) {
            String line;
            String currentMovie = "";
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Movie: ")) {
                    currentMovie = line.substring(7);
                } else if (line.startsWith("Total Price: $")) {
                    double price = Double.parseDouble(line.substring(14));
                    movieRevenue.put(currentMovie, movieRevenue.getOrDefault(currentMovie, 0.0) + price);
                }
            }
        } catch (IOException e) {
            // File doesn't exist yet
        }
        return movieRevenue;
    }

    private Map<String, Integer> getMovieBookingCounts() {
        Map<String, Integer> bookingCounts = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("receipts.txt"))) {
            String line;
            String currentMovie = "";
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Movie: ")) {
                    currentMovie = line.substring(7);
                } else if (line.startsWith("Seats: ")) {
                    String seats = line.substring(7);
                    int seatCount = seats.split(", ").length;
                    bookingCounts.put(currentMovie, bookingCounts.getOrDefault(currentMovie, 0) + 1);
                }
            }
        } catch (IOException e) {
            // File doesn't exist yet
        }
        return bookingCounts;
    }

    private String getTotalRevenue() {
        double total = getMovieRevenue().values().stream().mapToDouble(Double::doubleValue).sum();
        return String.format("$%.2f", total);
    }

    private String getTotalBookings() {
        long total = getMovieBookingCounts().values().stream().mapToLong(Integer::longValue).sum();
        return String.valueOf(total);
    }

    private String getAveragePrice() {
        Map<String, Double> revenue = getMovieRevenue();
        Map<String, Integer> bookings = getMovieBookingCounts();
        
        if (bookings.isEmpty()) return "$0.00";
        
        double totalRevenue = revenue.values().stream().mapToDouble(Double::doubleValue).sum();
        long totalBookings = bookings.values().stream().mapToLong(Integer::longValue).sum();
        
        return String.format("$%.2f", totalRevenue / totalBookings);
    }

    private String getTopMovie() {
        return getMovieRevenue().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
    }
}
