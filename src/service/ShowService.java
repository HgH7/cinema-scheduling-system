package service;

import model.Show;
import model.Movie;
import model.Screen;
import service.MovieService;
import service.PersistenceService;

import java.util.ArrayList;
import java.util.List;

public class ShowService {
    private final PersistenceService persistenceService;
    private List<Show> shows = new ArrayList<>();

    public ShowService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public Show addShow(int id, Movie movie, Screen screen, String showTime) {
        if (movie == null) {
            return null;
        }
        if (hasTimeConflict(screen, showTime, movie.getDuration())) {
            return null;
        }

        Show show = new Show(id, movie, screen, showTime);
        shows.add(show);
        persistenceService.saveShows(shows);
        return show;
    }

    public Show findShowById(int id) {
        for (Show s : shows) {
            if (s.getId() == id) return s;
        }
        return null;
    }

    public List<Show> getAllShows() {
        return new ArrayList<>(shows);
    }

    public List<Show> findShowsByMovie(Movie movie) {
        List<Show> result = new ArrayList<>();
        if (movie == null) {
            return result;
        }
        for (Show s : shows) {
            if (s.getMovie() != null && s.getMovie().getId() == movie.getId()) {
                result.add(s);
            }
        }
        return result;
    }

    public List<Show> findShowsByMovieTitle(String title) {
        List<Show> result = new ArrayList<>();
        if (title == null) {
            return result;
        }
        for (Show s : shows) {
            if (s.getMovie() != null && title.equals(s.getMovie().getTitle())) {
                result.add(s);
            }
        }
        return result;
    }

    public boolean removeShow(int id) {
        for (Show s : shows) {
            if (s.getId() == id) {
                shows.remove(s);
                persistenceService.saveShows(shows);
                return true;
            }
        }
        return false;
    }

    public void saveShows() {
        persistenceService.saveShows(shows);
    }

    public void loadShows(MovieService movieService, List<Screen> screens) {
        shows.clear();
        List<String> showLines = persistenceService.loadShows();
        for (String line : showLines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.split("\\|");
            if (parts.length < 4) {
                continue;
            }
            try {
                int id = Integer.parseInt(parts[0]);
                int movieId = Integer.parseInt(parts[1]);
                int screenId = Integer.parseInt(parts[2]);
                String showTime = parts[3];
                Movie movie = movieService.findMovieById(movieId);
                Screen screen = null;
                for (Screen s : screens) {
                    if (s.getId() == screenId) {
                        screen = s;
                        break;
                    }
                }
                if (movie != null && screen != null) {
                    shows.add(new Show(id, movie, screen, showTime));
                }
            } catch (NumberFormatException ignored) {
            }
        }
    }

    public boolean hasTimeConflict(Screen screen, String showTime, int durationMinutes) {
        int proposedStart = parseTime(showTime);
        int proposedEnd = proposedStart + durationMinutes;

        for (Show existingShow : shows) {
            if (existingShow.getScreen().getId() == screen.getId()) {
                int existingStart = parseTime(existingShow.getShowTime());
                int existingEnd = existingStart + existingShow.getMovie().getDuration();

                if (isTimeOverlap(proposedStart, proposedEnd, existingStart, existingEnd)) {
                    return true;
                }
            }
        }
        return false;
    }

    private int parseTime(String timeString) {
        String[] parts = timeString.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }

    private boolean isTimeOverlap(int start1, int end1, int start2, int end2) {
        return start1 < end2 && start2 < end1;
    }
}
