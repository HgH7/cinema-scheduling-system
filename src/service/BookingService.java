package service;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class BookingService {
    private List<Booking> bookings = new ArrayList<>();

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

        return booking;
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
}