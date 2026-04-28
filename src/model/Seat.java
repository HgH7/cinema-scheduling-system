package model;

public class Seat {
    private int row;
    private int col;
    private SeatStatus status;
    public SeatStatus getStatus(){return status;}
    public void setStatus(SeatStatus s){
        this.status = s;
    };
    public Seat(int row, int col){
        this.row = row;
        this.col = col;
        this.status = SeatStatus.AVAILABLE;
    }


}
