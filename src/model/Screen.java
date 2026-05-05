package model;

import java.util.ArrayList;
import java.util.List;

public class Screen {
    private int id;
    private String name;
    private int rows;
    private int cols;
    private List<Seat> seats;

    public Screen(int id, String name, int rows, int seatsPerRow) {
        this.id = id;
        this.name = name;
        this.rows = rows;
        this.cols = seatsPerRow;
        this.seats = new ArrayList<>();
        for (int r = 1; r <= rows; r++) {
            char rowChar = (char) ('A' + r - 1);
            for (int s = 1; s <= seatsPerRow; s++) {
                seats.add(new Seat(String.valueOf(rowChar), s));
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public Seat getSeat(String row, int number) {
        for (Seat seat : seats) {
            if (seat.getRow().equals(row) && seat.getNumber() == number) {
                return seat;
            }
        }
        return null;
    }
}
