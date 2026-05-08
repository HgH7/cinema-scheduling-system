package ui;

import service.AnalyticsService;
import service.CinemaServiceManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import model.Booking;
import service.ReceiptService;

public class AdminAnalyticsDashboardPanel extends JPanel {
    private DefaultTableModel movieTableModel;
    private DefaultTableModel dayTableModel;
    private JLabel totalRevenueLabel;
    private JLabel totalTicketsLabel;
    private JLabel totalBookingsLabel;
    private JLabel avgPriceLabel;
    private JLabel topMovieLabel;
    private JTextArea popularityArea;

    public AdminAnalyticsDashboardPanel() {
        setOpaque(true);
        setBackground(UITheme.BACKGROUND);
        setLayout(new BorderLayout(16, 16));
        setBorder(new EmptyBorder(24, 24, 24, 24));

        add(createHeader(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);
        refreshData();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshData();
            }
        });

        CinemaServiceManager.getInstance().addDataChangeListener(this::refreshData);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Analytics Dashboard");
        title.setForeground(UITheme.TEXT);
        title.setFont(UITheme.FONT_TITLE);

        header.add(title, BorderLayout.WEST);
        return header;
    }

    private JPanel createContent() {
        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setOpaque(false);
        content.add(createSummaryPanel(), BorderLayout.NORTH);
        content.add(createCenterPanel(), BorderLayout.CENTER);
        return content;
    }

    private JPanel createSummaryPanel() {
        JPanel summary = new JPanel(new GridLayout(1, 5, 16, 0));
        summary.setOpaque(true);
        summary.setBackground(UITheme.SURFACE);
        summary.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(24, 24, 24, 24)
        ));

        summary.add(createStatCard("Total Revenue", "$0.00", UITheme.PRIMARY));
        summary.add(createStatCard("Total Tickets Sold", "0", UITheme.BORDER));
        summary.add(createStatCard("Total Bookings", "0", new Color(0x2f, 0x8f, 0x4d)));
        summary.add(createStatCard("Avg Price/Booking", "$0.00", new Color(0xf7, 0xc0, 0x25)));
        summary.add(createStatCard("Top Movie", "N/A", UITheme.PRIMARY_LIGHT));

        return summary;
    }

    private JPanel createStatCard(String label, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setOpaque(false);

        JLabel labelText = new JLabel(label);
        labelText.setForeground(UITheme.TEXT_MUTED);
        labelText.setFont(UITheme.FONT_REGULAR);

        JLabel valueText = new JLabel(value);
        if (label.equals("Total Revenue")) {
            totalRevenueLabel = valueText;
        } else if (label.equals("Total Tickets Sold")) {
            totalTicketsLabel = valueText;
        } else if (label.equals("Total Bookings")) {
            totalBookingsLabel = valueText;
        } else if (label.equals("Avg Price/Booking")) {
            avgPriceLabel = valueText;
        } else if (label.equals("Top Movie")) {
            topMovieLabel = valueText;
        }
        valueText.setForeground(color);
        valueText.setFont(UITheme.FONT_LARGE);

        card.add(labelText, BorderLayout.NORTH);
        card.add(valueText, BorderLayout.CENTER);

        return card;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);

        panel.add(createPopularityPanel(), BorderLayout.NORTH);
        panel.add(createTablesPanel(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPopularityPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        panel.setBackground(UITheme.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel title = new JLabel("Movie Popularity Ranking");
        title.setForeground(UITheme.TEXT);
        title.setFont(UITheme.FONT_SEMIBOLD);
        panel.add(title, BorderLayout.NORTH);

        popularityArea = new JTextArea("No booking data available");
        popularityArea.setOpaque(false);
        popularityArea.setForeground(UITheme.TEXT);
        popularityArea.setFont(UITheme.FONT_REGULAR);
        popularityArea.setEditable(false);

        JScrollPane scroll = new JScrollPane(popularityArea);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTablesPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 16, 0));
        panel.setOpaque(false);
        panel.add(createMovieSalesPanel());
        panel.add(createDaySalesPanel());

        return panel;
    }

    private JPanel createMovieSalesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        panel.setBackground(UITheme.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel title = new JLabel("Sales by Movie");
        title.setForeground(UITheme.TEXT);
        title.setFont(UITheme.FONT_SEMIBOLD);
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"Movie Title", "Tickets Sold", "Revenue"};
        movieTableModel = new DefaultTableModel(new Object[0][0], columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(movieTableModel);
        table.setRowHeight(32);
        UIStyles.styleDarkTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDaySalesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        panel.setBackground(UITheme.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel title = new JLabel("Sales by Date");
        title.setForeground(UITheme.TEXT);
        title.setFont(UITheme.FONT_SEMIBOLD);
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"Date", "Tickets Sold", "Revenue"};
        dayTableModel = new DefaultTableModel(new Object[0][0], columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(dayTableModel);
        table.setRowHeight(32);
        UIStyles.styleDarkTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    public void refreshData() {
        List<ReceiptService.ReceiptData> receiptData = getReceiptData();
        AnalyticsService analytics = CinemaServiceManager.getInstance().getAnalyticsService();

        // Clear tables
        movieTableModel.setRowCount(0);
        dayTableModel.setRowCount(0);

        // Use receipt data directly when available
        AnalyticsService.SalesMetrics metrics = analytics.computeMetrics(receiptData);
        Map<String, AnalyticsService.MovieSalesData> movieSales = analytics.computeSalesByMovie(receiptData);
        Map<String, AnalyticsService.DailySalesData> daySales = analytics.computeSalesByDate(receiptData);
        Map<String, Integer> popularity = analytics.computeMoviePopularity(receiptData);
        String topMovie = analytics.computeTopMovie(receiptData);

        // Populate movie sales table
        movieSales.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue().revenue, a.getValue().revenue))
                .forEach(entry -> movieTableModel.addRow(new Object[]{
                        entry.getKey(),
                        entry.getValue().ticketCount,
                        String.format("$%.2f", entry.getValue().revenue)
                }));

        // Populate day sales table
        daySales.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> dayTableModel.addRow(new Object[]{
                        entry.getKey(),
                        entry.getValue().ticketCount,
                        String.format("$%.2f", entry.getValue().revenue)
                }));

        // Update summary labels
        totalRevenueLabel.setText(String.format("$%.2f", metrics.totalRevenue));
        totalTicketsLabel.setText(String.valueOf(metrics.totalTickets));
        totalBookingsLabel.setText(String.valueOf(metrics.totalBookings));
        avgPriceLabel.setText(String.format("$%.2f", metrics.avgPricePerBooking));
        topMovieLabel.setText(topMovie != null ? topMovie : "N/A");

        // Update popularity text
        popularityArea.setText(getPopularityText(popularity));
        revalidate();
        repaint();
    }

    private String getPopularityText(Map<String, Integer> movieBookingCounts) {
        if (movieBookingCounts.isEmpty()) {
            return "No booking data available";
        }
        StringBuilder stats = new StringBuilder();
        movieBookingCounts.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .forEach(entry -> stats.append("• ")
                        .append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue())
                        .append(" bookings\n"));
        return stats.toString();
    }

    private List<ReceiptService.ReceiptData> getReceiptData() {
        ReceiptService receiptService = CinemaServiceManager.getInstance().getReceiptService();
        List<ReceiptService.ReceiptData> receipts = receiptService.getAllReceiptData();
        if (!receipts.isEmpty()) {
            return receipts;
        }

        List<Booking> bookings = CinemaServiceManager.getInstance().getBookingService().getAllBookings();
        List<ReceiptService.ReceiptData> fallback = new java.util.ArrayList<>();
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        for (Booking booking : bookings) {
            List<String> seatLabels = new java.util.ArrayList<>();
            for (model.Seat seat : booking.getSeats()) {
                seatLabels.add(seat.getRow() + seat.getNumber());
            }
            String bookingDate = booking.getShow().getStartTime() != null
                    ? booking.getShow().getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    : currentDateTime;
            fallback.add(new ReceiptService.ReceiptData(
                    String.valueOf(booking.getId()),
                    bookingDate,
                    booking.getShow().getMovie().getTitle(),
                    booking.getShow().getScreen().getName(),
                    booking.getShow().getShowTime(),
                    seatLabels,
                    booking.getTotalPrice()));
        }
        return fallback;
    }
}
