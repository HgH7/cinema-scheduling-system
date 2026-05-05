package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Show {
    private int id;
    private Movie movie;
    private Screen screen;
    private String showTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double pricePerSeat;
    private List<Seat> seats;

    public Show(int id, Movie movie, Screen screen, String showTime) {
        this.id = id;
        this.movie = movie;
        this.screen = screen;
        this.showTime = showTime;
        this.pricePerSeat = 10.0;
        this.seats = cloneSeats(screen);
    }

    public Show(int id, Movie movie, Screen screen,
            LocalDateTime startTime, LocalDateTime endTime, double pricePerSeat) {
        this.id = id;
        this.movie = movie;
        this.screen = screen;
        this.startTime = startTime;
        this.endTime = endTime;
        this.pricePerSeat = pricePerSeat;
        this.seats = cloneSeats(screen);
    }

    private List<Seat> cloneSeats(Screen screen) {
        List<Seat> cloned = new ArrayList<>();
        for (Seat seat : screen.getSeats()) {
            cloned.add(new Seat(seat.getRow(), seat.getNumber()));
        }
        return cloned;
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
        for (Seat seat : seats) {
            if (seat.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public int getAvailableSeatsCount() {
        int counter = 0;
        for (Seat seat : seats) {
            if (seat.isAvailable()) {
                counter++;
            }
        }
        return counter;
    }

    public Seat getSeat(String row, int col) {
        for (Seat seat : seats) {
            if (seat.getRow().equals(row) && seat.getNumber() == col) {
                return seat;
            }
        }
        return null;
    }
}
