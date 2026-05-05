package model;

import java.util.List;

public class Booking {
    private int id;
    private String bookingId;
    private Show show;
    private List<Seat> seats;
    private String userName;
    private double totalPrice;

    public Booking(int id, Show show, List<Seat> seats, String userName) {
        this.id = id;
        this.bookingId = "NU" + System.currentTimeMillis();
        this.show = show;
        this.seats = seats;
        this.userName = userName;
        this.totalPrice = seats.size() * show.getPricePerSeat();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean confirmBooking() {
        for (Seat seat : seats) {
            if (!seat.book()) {
                return false;
            }
        }
        return true;
    }
}
