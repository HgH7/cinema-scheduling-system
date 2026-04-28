package model;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Show {
    private int showId;
    private Movie movie;
    private Screen screen;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Seat[][]seats;
    private double pricePerSeat;
    
    public Show(int showId, Movie movie, Screen screen, 
            LocalDateTime startTime, LocalDateTime endTime, double pricePerSeat) {
    
    this.showId = showId;
    this.movie = movie;
    this.screen = screen;
    this.startTime = startTime;
    this.endTime = endTime;
    this.pricePerSeat = pricePerSeat;
    this.seats = new Seat[screen.getRows()][screen.getCols()];
     for (int i = 0; i < screen.getRows(); i++) {
        for (int j = 0; j < screen.getCols(); j++) {
            seats[i][j] = new Seat(i, j);
            }
        }
    }
    public int getAvailableSeatsCount(){
        int counter = 0;
      for (int i = 0; i < screen.getRows(); i++) {
        for (int j = 0; j < screen.getCols(); j++) {
            if(seats[i][j].getStatus() == SeatStatus.AVAILABLE){
                counter++;
            }
        }
        }
        return counter;
    }
    public Seat getSeat(int row,int col){
        if (row < 0 || row >= screen.getRows() ||
        col < 0 || col >= screen.getCols()) {
        throw new IllegalArgumentException("Invalid seat position");
    }
       return seats[row][col];
    };
    ///nnn
}
