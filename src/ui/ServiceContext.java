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
        loadMovies();

        // Seed Screens
        Screen s1 = new Screen(1, "Screen 1", 4, 10);
        Screen s2 = new Screen(2, "Screen 2", 4, 10);
        Screen s3 = new Screen(3, "Screen 3", 4, 10);
        Screen s4 = new Screen(4, "Screen 4", 4, 10);
        Screen s5 = new Screen(5, "Screen 5", 4, 10);

        // Seed Shows
        showService.addShow(1, movieService.findMovieById(1), s1, "18:30");
        showService.addShow(2, movieService.findMovieById(2), s2, "20:15");
        showService.addShow(3, movieService.findMovieById(3), s1, "21:30");
    }

    public void loadMovies() {
        java.io.File file = new java.io.File("movies.txt");
        if (file.exists()) {
            try {
                java.util.List<String> lines = java.nio.file.Files.readAllLines(file.toPath());
                for (String line : lines) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split("\\|");
                    if (parts.length >= 4) {
                        int id = Integer.parseInt(parts[0]);
                        String title = parts[1];
                        int duration = Integer.parseInt(parts[2]);
                        String genre = parts[3];
                        String imagePath = parts.length > 4 ? parts[4] : "";
                        movieService.addMovie(id, title, duration, genre, imagePath);
                    }
                }
            } catch (java.io.IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        }
        
        if (movieService.getAllMovies().isEmpty()) {
            movieService.addMovie(1, "The Midnight Protocol", 142, "Sci-Fi", "");
            movieService.addMovie(2, "Dune: Part Two", 166, "Adventure", "");
            movieService.addMovie(3, "Velocity X", 118, "Action", "");
            movieService.addMovie(4, "Glow Runners", 102, "Animation, Fantasy", "");
            saveMovies();
        }
    }

    public void saveMovies() {
        try (java.io.PrintWriter out = new java.io.PrintWriter(new java.io.FileWriter("movies.txt"))) {
            for (Movie m : movieService.getAllMovies()) {
                out.println(m.getId() + "|" + m.getTitle() + "|" + m.getDuration() + "|" + m.getGenre() + "|" + m.getImagePath());
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
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
