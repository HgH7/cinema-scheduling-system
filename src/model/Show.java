package model;

import java.time.LocalDateTime;

public class Show {
    private int id;
    private Movie movie;
    private Screen screen;
    private String showTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double pricePerSeat;

    public Show(int id, Movie movie, Screen screen, String showTime) {
        this.id = id;
        this.movie = movie;
        this.screen = screen;
        this.showTime = showTime;
        this.pricePerSeat = 10.0;
    }

    public Show(int id, Movie movie, Screen screen, 
            LocalDateTime startTime, LocalDateTime endTime, double pricePerSeat) {
        this.id = id;
        this.movie = movie;
        this.screen = screen;
        this.startTime = startTime;
        this.endTime = endTime;
        this.pricePerSeat = pricePerSeat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public double getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(double pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    public boolean checkAvailability() {
        for (Seat seat : screen.getSeats()) {
            if (seat.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public int getAvailableSeatsCount() {
        int counter = 0;
        for (Seat seat : screen.getSeats()) {
            if (seat.isAvailable()) {
                counter++;
            }
        }
        return counter;
    }

    public Seat getSeat(String row, int col) {
        return screen.getSeat(row, col);
    }
}
