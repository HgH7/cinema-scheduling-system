package service;

import model.Booking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for handling receipt operations
 */
public class ReceiptService {

    private final PersistenceService persistenceService;

    public ReceiptService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    /**
     * Generates a receipt string for a booking
     * @param booking The booking to generate receipt for
     * @return Formatted receipt string
     */
    public String generateReceipt(Booking booking) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("==============================\n");
        receipt.append("      CINERESERVE RECEIPT     \n");
        receipt.append("==============================\n");
        receipt.append("Booking ID: #CR-").append(String.format("%04d", booking.getId())).append("\n");
        receipt.append("Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        receipt.append("Movie: ").append(booking.getShow().getMovie().getTitle()).append("\n");
        receipt.append("Screen: ").append(booking.getShow().getScreen().getName()).append("\n");
        receipt.append("Showtime: ").append(booking.getShow().getShowTime()).append("\n");
        receipt.append("------------------------------\n");
        receipt.append("Seats: ");
        for (int i = 0; i < booking.getSeats().size(); i++) {
            receipt.append(booking.getSeats().get(i).getRow()).append(booking.getSeats().get(i).getNumber());
            if (i < booking.getSeats().size() - 1) receipt.append(", ");
        }
        receipt.append("\n");
        receipt.append(String.format("Total Price: $%.2f\n", booking.getTotalPrice()));
        receipt.append("==============================\n");
        return receipt.toString();
    }

    /**
     * Generates and saves a receipt for a booking
     * @param booking The booking to process
     */
    public void generateAndSaveReceipt(Booking booking) {
        String receipt = generateReceipt(booking);
        persistenceService.saveReceipt(receipt);
    }

    /**
     * Parses a receipt string to extract booking information
     * @param receipt The receipt string
     * @return Parsed receipt data or null if invalid
     */
    public ReceiptData parseReceipt(String receipt) {
        try {
            String[] lines = receipt.split("\n");
            if (lines.length < 10) return null;

            String bookingIdLine = lines[3]; // "Booking ID: #CR-0001"
            String dateLine = lines[4]; // "Date: 2023-12-01 14:30:00"
            String movieLine = lines[5]; // "Movie: Movie Title"
            String screenLine = lines[6]; // "Screen: Screen 1"
            String showtimeLine = lines[7]; // "Showtime: 18:30"
            String seatsLine = lines[9]; // "Seats: A1, A2"
            String totalLine = lines[10]; // "Total Price: $28.00"

            String bookingId = bookingIdLine.substring(bookingIdLine.indexOf("#CR-") + 4);
            String date = dateLine.substring(dateLine.indexOf(": ") + 2);
            String movie = movieLine.substring(movieLine.indexOf(": ") + 2);
            String screen = screenLine.substring(screenLine.indexOf(": ") + 2);
            String showtime = showtimeLine.substring(showtimeLine.indexOf(": ") + 2);
            String seatsStr = seatsLine.substring(seatsLine.indexOf(": ") + 2);
            String totalStr = totalLine.substring(totalLine.indexOf("$") + 1);

            double total = Double.parseDouble(totalStr);
            List<String> seats = new ArrayList<>();
            if (!seatsStr.trim().isEmpty()) {
                String[] seatParts = seatsStr.split(", ");
                for (String seat : seatParts) {
                    seats.add(seat.trim());
                }
            }

            return new ReceiptData(bookingId, date, movie, screen, showtime, seats, total);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets all receipt data
     * @return List of parsed receipt data
     */
    public List<ReceiptData> getAllReceiptData() {
        List<String> receiptStrings = persistenceService.loadAllReceipts();
        List<ReceiptData> receipts = new ArrayList<>();
        List<String> currentReceipt = new ArrayList<>();

        for (String line : receiptStrings) {
            if (line.startsWith("==============================")) {
                if (!currentReceipt.isEmpty()) {
                    ReceiptData data = parseReceipt(String.join("\n", currentReceipt));
                    if (data != null) {
                        receipts.add(data);
                    }
                    currentReceipt.clear();
                }
            }
            currentReceipt.add(line);
        }

        if (!currentReceipt.isEmpty()) {
            ReceiptData data = parseReceipt(String.join("\n", currentReceipt));
            if (data != null) {
                receipts.add(data);
            }
        }

        return receipts;
    }

    public ReceiptData getReceiptById(String bookingId) {
        for (ReceiptData receipt : getAllReceiptData()) {
            if (receipt.bookingId.equals(bookingId)) {
                return receipt;
            }
        }
        return null;
    }

    /**
     * Removes a receipt from the persistence file by booking id
     * @param bookingId Booking id to remove (without prefix)
     * @return true if a receipt was removed
     */
    public boolean removeReceipt(String bookingId) {
        List<String> lines = persistenceService.loadAllReceipts();
        List<String> output = new ArrayList<>();
        List<String> block = new ArrayList<>();
        boolean dropBlock = false;

        for (String line : lines) {
            if (line.startsWith("==============================")) {
                if (!block.isEmpty()) {
                    if (!dropBlock) {
                        output.addAll(block);
                    }
                    block.clear();
                    dropBlock = false;
                }
            }

            block.add(line);
            if (line.startsWith("Booking ID: #CR-")) {
                String id = line.substring(line.indexOf("#CR-") + 4);
                if (id.equals(bookingId)) {
                    dropBlock = true;
                }
            }
        }

        if (!block.isEmpty() && !dropBlock) {
            output.addAll(block);
        }

        if (output.size() == lines.size()) {
            return false;
        }

        persistenceService.saveAllReceipts(output);
        return true;
    }

    /**
     * Data class for parsed receipt information
     */
    public static class ReceiptData {
        public final String bookingId;
        public final String date;
        public final String movie;
        public final String screen;
        public final String showtime;
        public final List<String> seats;
        public final double total;

        public ReceiptData(String bookingId, String date, String movie, String screen, String showtime, List<String> seats, double total) {
            this.bookingId = bookingId;
            this.date = date;
            this.movie = movie;
            this.screen = screen;
            this.showtime = showtime;
            this.seats = seats;
            this.total = total;
        }
    }
}