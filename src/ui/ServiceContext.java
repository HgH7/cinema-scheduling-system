package ui;

import model.Movie;
import model.Screen;
import service.BookingService;
import service.MovieService;
import service.ShowService;

public class ServiceContext {
    private static ServiceContext instance;

    private final MovieService movieService;
    private final ShowService showService;
    private final BookingService bookingService;

    private ServiceContext() {
        movieService = new MovieService();
        showService = new ShowService();
        bookingService = new BookingService();

        seedData();
    }

    public static synchronized ServiceContext getInstance() {
        if (instance == null) {
            instance = new ServiceContext();
        }
        return instance;
    }

    private void seedData() {
        // Seed Movies
        Movie m1 = movieService.addMovie(1, "Neon Horizon", 134, "Sci-Fi, Action");
        Movie m2 = movieService.addMovie(2, "Last Encore", 116, "Drama, Musical");
        Movie m3 = movieService.addMovie(3, "Red Velocity", 125, "Thriller, Racing");
        Movie m4 = movieService.addMovie(4, "Glow Runners", 102, "Animation, Fantasy");

        // Seed Screens
        Screen s1 = new Screen(1, "Screen 1", 4, 10);
        Screen s2 = new Screen(2, "Screen 2", 4, 10);

        // Seed Shows
        showService.addShow(1, m1, s1, "18:30");
        showService.addShow(2, m2, s2, "20:15");
        showService.addShow(3, m3, s1, "21:30");
    }

    public MovieService getMovieService() {
        return movieService;
    }

    public ShowService getShowService() {
        return showService;
    }

    public BookingService getBookingService() {
        return bookingService;
    }
}
