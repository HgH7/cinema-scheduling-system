package model;

public class Seat {
    private String row;
    private int number;
    private SeatStatus status;

    public Seat(String row, int number) {
        this.row = row;
        this.number = number;
        this.status = SeatStatus.AVAILABLE;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public boolean isAvailable() {
        return status == SeatStatus.AVAILABLE;
    }

    public boolean book() {
        if (isAvailable()) {
            status = SeatStatus.BOOKED;
            return true;
        }
        return false;
    }
}
