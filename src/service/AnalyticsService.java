package service;

import java.util.*;

/**
 * Service for analytics and reporting operations
 */
public class AnalyticsService {

    private final ReceiptService receiptService;

    public AnalyticsService(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    /**
     * Gets sales metrics summary
     * @return Sales metrics
     */
    public SalesMetrics getSalesMetrics() {
        List<ReceiptService.ReceiptData> receipts = receiptService.getAllReceiptData();

        double totalRevenue = 0;
        int totalTickets = 0;
        int totalBookings = receipts.size();

        for (ReceiptService.ReceiptData receipt : receipts) {
            totalRevenue += receipt.total;
            totalTickets += receipt.seats.size();
        }

        double avgPricePerBooking = totalBookings > 0 ? totalRevenue / totalBookings : 0;

        return new SalesMetrics(totalRevenue, totalTickets, totalBookings, avgPricePerBooking);
    }

    /**
     * Gets sales data grouped by movie
     * @return Map of movie title to sales data
     */
    public Map<String, MovieSalesData> getSalesByMovie() {
        Map<String, MovieSalesData> salesByMovie = new HashMap<>();
        List<ReceiptService.ReceiptData> receipts = receiptService.getAllReceiptData();

        for (ReceiptService.ReceiptData receipt : receipts) {
            MovieSalesData data = salesByMovie.computeIfAbsent(receipt.movie, k -> new MovieSalesData());
            data.bookingCount++;
            data.revenue += receipt.total;
            data.ticketCount += receipt.seats.size();
        }

        return salesByMovie;
    }

    /**
     * Gets sales data grouped by date
     * @return Map of date to sales data
     */
    public Map<String, DailySalesData> getSalesByDate() {
        Map<String, DailySalesData> salesByDate = new HashMap<>();
        List<ReceiptService.ReceiptData> receipts = receiptService.getAllReceiptData();

        for (ReceiptService.ReceiptData receipt : receipts) {
            // Extract date part (yyyy-MM-dd)
            String date = receipt.date.split(" ")[0];
            DailySalesData data = salesByDate.computeIfAbsent(date, k -> new DailySalesData());
            data.bookingCount++;
            data.revenue += receipt.total;
            data.ticketCount += receipt.seats.size();
        }

        return salesByDate;
    }

    /**
     * Gets movie popularity ranking
     * @return Map of movie to popularity score
     */
    public Map<String, Integer> getMoviePopularity() {
        Map<String, Integer> popularity = new HashMap<>();
        List<ReceiptService.ReceiptData> receipts = receiptService.getAllReceiptData();

        for (ReceiptService.ReceiptData receipt : receipts) {
            popularity.put(receipt.movie, popularity.getOrDefault(receipt.movie, 0) + receipt.seats.size());
        }

        return popularity;
    }

    /**
     * Gets the top movie by ticket sales
     * @return Movie title or null if no data
     */
    public String getTopMovie() {
        Map<String, Integer> popularity = getMoviePopularity();
        return popularity.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public SalesMetrics computeMetrics(List<ReceiptService.ReceiptData> receipts) {
        double totalRevenue = 0;
        int totalTickets = 0;
        int totalBookings = receipts.size();

        for (ReceiptService.ReceiptData receipt : receipts) {
            totalRevenue += receipt.total;
            totalTickets += receipt.seats.size();
        }
        double avgPricePerBooking = totalBookings > 0 ? totalRevenue / totalBookings : 0;
        return new SalesMetrics(totalRevenue, totalTickets, totalBookings, avgPricePerBooking);
    }

    public Map<String, MovieSalesData> computeSalesByMovie(List<ReceiptService.ReceiptData> receipts) {
        Map<String, MovieSalesData> salesByMovie = new HashMap<>();
        for (ReceiptService.ReceiptData receipt : receipts) {
            MovieSalesData data = salesByMovie.computeIfAbsent(receipt.movie, k -> new MovieSalesData());
            data.bookingCount++;
            data.revenue += receipt.total;
            data.ticketCount += receipt.seats.size();
        }
        return salesByMovie;
    }

    public Map<String, DailySalesData> computeSalesByDate(List<ReceiptService.ReceiptData> receipts) {
        Map<String, DailySalesData> salesByDate = new HashMap<>();
        for (ReceiptService.ReceiptData receipt : receipts) {
            String date = receipt.date.split(" ")[0];
            DailySalesData data = salesByDate.computeIfAbsent(date, k -> new DailySalesData());
            data.bookingCount++;
            data.revenue += receipt.total;
            data.ticketCount += receipt.seats.size();
        }
        return salesByDate;
    }

    public Map<String, Integer> computeMoviePopularity(List<ReceiptService.ReceiptData> receipts) {
        Map<String, Integer> popularity = new HashMap<>();
        for (ReceiptService.ReceiptData receipt : receipts) {
            popularity.put(receipt.movie, popularity.getOrDefault(receipt.movie, 0) + receipt.seats.size());
        }
        return popularity;
    }

    public String computeTopMovie(List<ReceiptService.ReceiptData> receipts) {
        Map<String, Integer> popularity = computeMoviePopularity(receipts);
        return popularity.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Data class for sales metrics
     */
    public static class SalesMetrics {
        public final double totalRevenue;
        public final int totalTickets;
        public final int totalBookings;
        public final double avgPricePerBooking;

        public SalesMetrics(double totalRevenue, int totalTickets, int totalBookings, double avgPricePerBooking) {
            this.totalRevenue = totalRevenue;
            this.totalTickets = totalTickets;
            this.totalBookings = totalBookings;
            this.avgPricePerBooking = avgPricePerBooking;
        }
    }

    /**
     * Data class for movie sales data
     */
    public static class MovieSalesData {
        public int bookingCount = 0;
        public int ticketCount = 0;
        public double revenue = 0;
    }

    /**
     * Data class for daily sales data
     */
    public static class DailySalesData {
        public int bookingCount = 0;
        public int ticketCount = 0;
        public double revenue = 0;
    }
}