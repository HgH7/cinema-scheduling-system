package service;

import model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for booking operations
 */
public class BookingService {

    private final ReceiptService receiptService;
    private List<Booking> bookings;

    public BookingService(ReceiptService receiptService) {
        this.receiptService = receiptService;
        this.bookings = new ArrayList<>();
    }

    public Booking createBooking(int id, Show show, List<Seat> seats, String userName) {
        for (Seat seat : seats) {
            if (!seat.isAvailable()) {
                return null;
            }
        }

        for (Seat seat : seats) {
            seat.book();
        }

        Booking booking = new Booking(id, show, seats, userName);
        bookings.add(booking);

        // Generate and save receipt
        receiptService.generateAndSaveReceipt(booking);
        CinemaServiceManager.getInstance().notifyDataChangeListeners();

        return booking;
    }

    public void loadBookingsFromReceipts(ShowService showService) {
        bookings.clear();
        for (ReceiptService.ReceiptData receipt : receiptService.getAllReceiptData()) {
            Show matchingShow = findMatchingShow(showService, receipt);
            if (matchingShow == null) {
                continue;
            }

            List<Seat> seats = new ArrayList<>();
            for (String seatLabel : receipt.seats) {
                if (seatLabel.length() < 2) {
                    continue;
                }
                String row = seatLabel.substring(0, 1);
                int number;
                try {
                    number = Integer.parseInt(seatLabel.substring(1));
                } catch (NumberFormatException e) {
                    continue;
                }
                Seat seat = matchingShow.getSeat(row, number);
                if (seat != null && seat.isAvailable()) {
                    seat.book();
                    seats.add(seat);
                }
            }
            if (!seats.isEmpty()) {
                try {
                    int receiptId = Integer.parseInt(receipt.bookingId);
                    Booking booking = new Booking(receiptId, matchingShow, seats, "Moviegoer");
                    bookings.add(booking);
                } catch (NumberFormatException ignored) {
                    // Ignore malformed receipt IDs and continue loading remaining bookings.
                }
            }
        }
    }

    private Show findMatchingShow(ShowService showService, ReceiptService.ReceiptData receipt) {
        for (Show show : showService.getAllShows()) {
            if (show.getMovie() != null
                    && show.getScreen() != null
                    && show.getMovie().getTitle().equals(receipt.movie)
                    && show.getScreen().getName().equals(receipt.screen)
                    && show.getShowTime().equals(receipt.showtime)) {
                return show;
            }
        }
        return null;
    }

    public int getNextBookingId() {
        int maxId = 0;
        for (Booking booking : bookings) {
            maxId = Math.max(maxId, booking.getId());
        }
        for (ReceiptService.ReceiptData receipt : receiptService.getAllReceiptData()) {
            try {
                maxId = Math.max(maxId, Integer.parseInt(receipt.bookingId));
            } catch (NumberFormatException ignored) {
            }
        }
        return maxId + 1;
    }

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }

    public Booking findBookingById(int id) {
        for (Booking b : bookings) {
            if (b.getId() == id) return b;
        }
        return null;
    }

    public boolean cancelBooking(int bookingId) {
        Booking booking = findBookingById(bookingId);
        if (booking != null) {
            for (Seat seat : booking.getSeats()) {
                seat.setStatus(SeatStatus.AVAILABLE);
            }
            bookings.remove(booking);
            CinemaServiceManager.getInstance().notifyDataChangeListeners();
            return true;
        }

        ReceiptService.ReceiptData receipt = receiptService.getReceiptById(String.valueOf(bookingId));
        if (receipt != null) {
            boolean canceled = cancelBookingFromReceipt(receipt);
            if (canceled) {
                CinemaServiceManager.getInstance().notifyDataChangeListeners();
            }
            return canceled;
        }

        return false;
    }

    private boolean cancelBookingFromReceipt(ReceiptService.ReceiptData receipt) {
        for (Show show : CinemaServiceManager.getInstance().getShowService().getAllShows()) {
            if (show.getMovie() != null
                    && show.getScreen() != null
                    && show.getMovie().getTitle().equals(receipt.movie)
                    && show.getScreen().getName().equals(receipt.screen)
                    && show.getShowTime().equals(receipt.showtime)) {
                for (String seatLabel : receipt.seats) {
                    if (seatLabel.length() < 2) {
                        continue;
                    }
                    String row = seatLabel.substring(0, 1);
                    int number;
                    try {
                        number = Integer.parseInt(seatLabel.substring(1));
                    } catch (NumberFormatException e) {
                        continue;
                    }
                    Seat seat = show.getSeat(row, number);
                    if (seat != null) {
                        seat.setStatus(SeatStatus.AVAILABLE);
                    }
                }
                return true;
            }
        }
        return false;
    }
}
