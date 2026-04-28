package model;
import java.util.List;
import java.util.ArrayList;
public class Booking {
    private String bookingId;
    private Show show;
    private List<Seat> seatList;
    private double totalPrice;
    public double getTotalPrice(){return totalPrice;}

}
