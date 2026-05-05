package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AdminSalesPanel extends JPanel {
    private DefaultTableModel movieTableModel;
    private DefaultTableModel dayTableModel;
    private JLabel totalRevenueLabel;
    private JLabel totalTicketsLabel;

    public AdminSalesPanel() {
        setOpaque(true);
        setBackground(UIConstants.BACKGROUND);
        setLayout(new BorderLayout(16, 16));
        setBorder(new EmptyBorder(24, 24, 24, 24));

        add(createHeader(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshData();
            }
        });
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Sales Analytics");
        title.setForeground(UIConstants.TEXT);
        title.setFont(UIConstants.FONT_TITLE);

        header.add(title, BorderLayout.WEST);
        return header;
    }

    private JPanel createContent() {
        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setOpaque(false);
        content.add(createSummaryPanel(), BorderLayout.NORTH);
        content.add(createTablesPanel(), BorderLayout.CENTER);
        return content;
    }

    private JPanel createSummaryPanel() {
        JPanel summary = new JPanel(new GridLayout(1, 2, 16, 0));
        summary.setOpaque(true);
        summary.setBackground(UIConstants.SURFACE);
        summary.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER),
                new EmptyBorder(24, 24, 24, 24)
        ));

        summary.add(createStatCard("Total Revenue", "$0.00", UIConstants.PRIMARY));
        summary.add(createStatCard("Total Tickets Sold", "0", UIConstants.BORDER));

        return summary;
    }

    private JPanel createStatCard(String label, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setOpaque(false);

        JLabel labelText = new JLabel(label);
        labelText.setForeground(UIConstants.TEXT_MUTED);
        labelText.setFont(UIConstants.FONT_REGULAR);

        if (label.equals("Total Revenue")) {
            totalRevenueLabel = new JLabel(value);
        } else {
            totalTicketsLabel = new JLabel(value);
        }

        JLabel valueText = (label.equals("Total Revenue")) ? totalRevenueLabel : totalTicketsLabel;
        valueText.setForeground(color);
        valueText.setFont(UIConstants.FONT_LARGE);

        card.add(labelText, BorderLayout.NORTH);
        card.add(valueText, BorderLayout.CENTER);

        return card;
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
        panel.setBackground(UIConstants.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER),
                new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel title = new JLabel("Sales by Movie");
        title.setForeground(UIConstants.TEXT);
        title.setFont(UIConstants.FONT_SEMIBOLD);
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"Movie Title", "Tickets Sold", "Revenue"};
        movieTableModel = new DefaultTableModel(new Object[0][0], columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(movieTableModel);
        table.setBackground(UIConstants.SURFACE);
        table.setForeground(UIConstants.TEXT);
        table.setFont(UIConstants.FONT_REGULAR);
        table.setRowHeight(32);
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

    private JPanel createDaySalesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        panel.setBackground(UIConstants.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER),
                new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel title = new JLabel("Sales by Date");
        title.setForeground(UIConstants.TEXT);
        title.setFont(UIConstants.FONT_SEMIBOLD);
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"Date", "Tickets Sold", "Revenue"};
        dayTableModel = new DefaultTableModel(new Object[0][0], columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(dayTableModel);
        table.setBackground(UIConstants.SURFACE);
        table.setForeground(UIConstants.TEXT);
        table.setFont(UIConstants.FONT_REGULAR);
        table.setRowHeight(32);
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

    private void refreshData() {
        movieTableModel.setRowCount(0);
        dayTableModel.setRowCount(0);

        Map<String, SalesData> movieSales = new HashMap<>();
        Map<String, SalesData> daySales = new HashMap<>();
        double totalRevenue = 0;
        int totalTickets = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("receipts.txt"))) {
            String line;
            String currentMovie = "";
            String currentDate = "";
            int currentTickets = 0;
            double currentRevenue = 0;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Movie: ")) {
                    currentMovie = line.substring(7);
                } else if (line.startsWith("Date: ")) {
                    currentDate = line.substring(6);
                    // Extract just the date part (YYYY-MM-DD)
                    if (currentDate.length() > 10) {
                        currentDate = currentDate.substring(0, 10);
                    }
                } else if (line.startsWith("Seats: ")) {
                    String seats = line.substring(7);
                    currentTickets = seats.split(", ").length;
                } else if (line.startsWith("Total Price: ")) {
                    String priceStr = line.substring(13).replace("$", "");
                    try {
                        currentRevenue = Double.parseDouble(priceStr);
                    } catch (NumberFormatException e) {
                        currentRevenue = 0;
                    }

                    // Update movie sales
                    movieSales.putIfAbsent(currentMovie, new SalesData());
                    SalesData movieData = movieSales.get(currentMovie);
                    movieData.tickets += currentTickets;
                    movieData.revenue += currentRevenue;

                    // Update day sales
                    daySales.putIfAbsent(currentDate, new SalesData());
                    SalesData dayData = daySales.get(currentDate);
                    dayData.tickets += currentTickets;
                    dayData.revenue += currentRevenue;

                    // Update totals
                    totalTickets += currentTickets;
                    totalRevenue += currentRevenue;

                    // Reset current booking data
                    currentMovie = "";
                    currentDate = "";
                    currentTickets = 0;
                    currentRevenue = 0;
                }
            }
        } catch (IOException e) {
            // File might not exist yet, ignore
        }

        // Add movie sales to table (sorted by revenue)
        movieSales.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue().revenue, a.getValue().revenue))
                .forEach(entry -> movieTableModel.addRow(new Object[]{
                        entry.getKey(),
                        entry.getValue().tickets,
                        String.format("$%.2f", entry.getValue().revenue)
                }));

        // Add day sales to table (sorted by date)
        daySales.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> dayTableModel.addRow(new Object[]{
                        entry.getKey(),
                        entry.getValue().tickets,
                        String.format("$%.2f", entry.getValue().revenue)
                }));

        // Update summary
        totalRevenueLabel.setText(String.format("$%.2f", totalRevenue));
        totalTicketsLabel.setText(String.valueOf(totalTickets));
    }

    private static class SalesData {
        int tickets = 0;
        double revenue = 0;
    }
}
