package service;

import model.Movie;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class PersistenceService {

    private static final String DATA_DIR = "data";
    private static final String MOVIES_FILE = DATA_DIR + "/movies.txt";
    private static final String SHOWS_FILE = DATA_DIR + "/shows.txt";
    private static final String RECEIPTS_FILE = DATA_DIR + "/receipts.txt";

    private void ensureDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public List<Movie> loadMovies() {
        List<Movie> movies = new ArrayList<>();
        File file = new File(MOVIES_FILE);
        if (file.exists()) {
            try {
                List<String> lines = Files.readAllLines(file.toPath());
                for (String line : lines) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split("\\|");
                    if (parts.length >= 4) {
                        int id = Integer.parseInt(parts[0]);
                        String title = parts[1];
                        int duration = Integer.parseInt(parts[2]);
                        String genre = parts[3];
                        String imagePath = parts.length > 4 ? parts[4] : "";
                        movies.add(new Movie(id, title, duration, genre, imagePath));
                    }
                }
            } catch (IOException | NumberFormatException e) {
                System.err.println("Error loading movies: " + e.getMessage());
            }
        }
        return movies;
    }

    public void saveMovies(List<Movie> movies) {
        ensureDataDirectory();
        try (PrintWriter out = new PrintWriter(new FileWriter(MOVIES_FILE))) {
            for (Movie movie : movies) {
                out.println(movie.getId() + "|" + movie.getTitle() + "|" + movie.getDuration() + "|" + movie.getGenre() + "|" + movie.getImagePath());
            }
        } catch (IOException e) {
            System.err.println("Error saving movies: " + e.getMessage());
        }
    }

    public void saveShows(List<model.Show> shows) {
        ensureDataDirectory();
        try (PrintWriter out = new PrintWriter(new FileWriter(SHOWS_FILE))) {
            for (model.Show show : shows) {
                out.println(show.getId() + "|" + show.getMovie().getId() + "|" + show.getScreen().getId() + "|" + show.getShowTime());
            }
        } catch (IOException e) {
            System.err.println("Error saving shows: " + e.getMessage());
        }
    }

    public List<String> loadShows() {
        List<String> shows = new ArrayList<>();
        File file = new File(SHOWS_FILE);
        if (file.exists()) {
            try {
                shows = Files.readAllLines(file.toPath());
            } catch (IOException e) {
                System.err.println("Error loading shows: " + e.getMessage());
            }
        }
        return shows;
    }

    public void saveReceipt(String receipt) {
        ensureDataDirectory();
        try (PrintWriter out = new PrintWriter(new FileWriter(RECEIPTS_FILE, true))) {
            out.println(receipt);
        } catch (IOException e) {
            System.err.println("Error saving receipt: " + e.getMessage());
        }
    }

    public void saveAllReceipts(List<String> receiptLines) {
        ensureDataDirectory();
        try (PrintWriter out = new PrintWriter(new FileWriter(RECEIPTS_FILE))) {
            for (String line : receiptLines) {
                out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error saving receipts: " + e.getMessage());
        }
    }

    public List<String> loadAllReceipts() {
        List<String> receipts = new ArrayList<>();
        File file = new File(RECEIPTS_FILE);
        if (file.exists()) {
            try {
                receipts = Files.readAllLines(file.toPath());
            } catch (IOException e) {
                System.err.println("Error loading receipts: " + e.getMessage());
            }
        }
        return receipts;
    }
}
