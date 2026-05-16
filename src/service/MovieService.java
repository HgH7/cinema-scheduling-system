package service;

import model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieService {

    private final PersistenceService persistenceService;
    private List<Movie> movies;

    public MovieService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
        this.movies = new ArrayList<>();
    }

    public void loadMovies() {
        this.movies = persistenceService.loadMovies();
    }

    public void saveMovies() {
        persistenceService.saveMovies(movies);
    }

    public Movie addMovie(int id, String title, int duration, String genre, String imagePath) {
        Movie movie = new Movie(id, title, duration, genre, imagePath);
        movies.add(movie);
        return movie;
    }

    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies);
    }

    public Movie findMovieById(int id) {
        for (Movie m : movies) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    public boolean removeMovie(int id) {
        for (Movie m : movies) {
            if (m.getId() == id) {
                movies.remove(m);
                return true;
            }
        }
        return false;
    }
}
