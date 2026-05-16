package service;

import model.Booking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReceiptService {

    private final PersistenceService persistenceService;

    public ReceiptService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

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

    public void generateAndSaveReceipt(Booking booking) {
        String receipt = generateReceipt(booking);
        persistenceService.saveReceipt(receipt);
    }

    public ReceiptData parseReceipt(String receipt) {
        try {
            String[] lines = receipt.split("\n");
            if (lines.length < 10) return null;

            String bookingIdLine = lines[3];
            String dateLine = lines[4];
            String movieLine = lines[5];
            String screenLine = lines[6];
            String showtimeLine = lines[7];
            String seatsLine = lines[9];
            String totalLine = lines[10];

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

    public List<ReceiptData> getAllReceiptData() {
        List<String> receiptStrings = persistenceService.loadAllReceipts();
        List<ReceiptData> receipts = new ArrayList<>();

        for (int i = 0; i < receiptStrings.size(); ) {
            if (receiptStrings.get(i).startsWith("==============================")
                    && i + 2 < receiptStrings.size()
                    && receiptStrings.get(i + 1).contains("CINERESERVE")
                    && receiptStrings.get(i + 2).startsWith("==============================")) {
                int start = i;
                int end = i + 3;
                while (end < receiptStrings.size() && !receiptStrings.get(end).startsWith("==============================")) {
                    end++;
                }
                if (end >= receiptStrings.size()) {
                    end = receiptStrings.size() - 1;
                }
                List<String> block = receiptStrings.subList(start, end + 1);
                ReceiptData data = parseReceipt(String.join("\n", block));
                if (data != null) {
                    receipts.add(data);
                }
                i = end + 1;
            } else {
                i++;
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

    public boolean removeReceipt(String bookingId) {
        List<String> lines = persistenceService.loadAllReceipts();
        List<String> output = new ArrayList<>();
        boolean removed = false;

        for (int i = 0; i < lines.size(); ) {
            if (lines.get(i).startsWith("==============================")
                    && i + 2 < lines.size()
                    && lines.get(i + 1).contains("CINERESERVE")
                    && lines.get(i + 2).startsWith("==============================")) {
                int start = i;
                int end = i + 3;
                while (end < lines.size() && !lines.get(end).startsWith("==============================")) {
                    end++;
                }
                if (end >= lines.size()) {
                    end = lines.size() - 1;
                }
                List<String> block = lines.subList(start, end + 1);
                ReceiptData data = parseReceipt(String.join("\n", block));
                if (data != null && data.bookingId.equals(bookingId)) {
                    removed = true;
                } else {
                    output.addAll(block);
                }
                i = end + 1;
            } else {
                output.add(lines.get(i));
                i++;
            }
        }

        if (!removed) {
            return false;
        }

        persistenceService.saveAllReceipts(output);
        return true;
    }

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
