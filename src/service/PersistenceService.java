package service;

import model.Movie;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for handling data persistence operations
 */
public class PersistenceService {

    private static final String MOVIES_FILE = "movies.txt";
    private static final String RECEIPTS_FILE = "receipts.txt";

    /**
     * Loads movies from the movies.txt file
     * @return List of loaded movies
     */
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

    /**
     * Saves movies to the movies.txt file
     * @param movies List of movies to save
     */
    public void saveMovies(List<Movie> movies) {
        try (PrintWriter out = new PrintWriter(new FileWriter(MOVIES_FILE))) {
            for (Movie movie : movies) {
                out.println(movie.getId() + "|" + movie.getTitle() + "|" + movie.getDuration() + "|" + movie.getGenre() + "|" + movie.getImagePath());
            }
        } catch (IOException e) {
            System.err.println("Error saving movies: " + e.getMessage());
        }
    }

    /**
     * Saves a receipt to the receipts.txt file
     * @param receipt The receipt content to append
     */
    public void saveReceipt(String receipt) {
        try (PrintWriter out = new PrintWriter(new FileWriter(RECEIPTS_FILE, true))) {
            out.println(receipt);
        } catch (IOException e) {
            System.err.println("Error saving receipt: " + e.getMessage());
        }
    }

    /**
     * Saves a batch of receipt lines to receipts.txt
     * @param receiptLines Lines to write into the receipts file
     */
    public void saveAllReceipts(List<String> receiptLines) {
        try (PrintWriter out = new PrintWriter(new FileWriter(RECEIPTS_FILE))) {
            for (String line : receiptLines) {
                out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error saving receipts: " + e.getMessage());
        }
    }

    /**
     * Loads all receipts from the receipts.txt file
     * @return List of receipt lines
     */
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