package service;

import model.Show;
import model.Movie;
import model.Screen;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for show operations
 */
public class ShowService {
    private List<Show> shows = new ArrayList<>();

    public Show addShow(int id, Movie movie, Screen screen, String showTime) {
        if (hasTimeConflict(screen, showTime, movie.getDuration())) {
            return null;
        }

        Show show = new Show(id, movie, screen, showTime);
        shows.add(show);
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
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if there's a time conflict for scheduling a show
     * @param screen The screen to check
     * @param showTime The proposed show time (HH:mm)
     * @param durationMinutes The movie duration in minutes
     * @return true if there's a conflict, false otherwise
     */
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

    /**
     * Parses time string (HH:mm) to minutes since midnight
     * @param timeString Time in HH:mm format
     * @return Minutes since midnight
     */
    private int parseTime(String timeString) {
        String[] parts = timeString.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }

    /**
     * Checks if two time ranges overlap
     * @param start1 Start of first range
     * @param end1 End of first range
     * @param start2 Start of second range
     * @param end2 End of second range
     * @return true if they overlap
     */
    private boolean isTimeOverlap(int start1, int end1, int start2, int end2) {
        return start1 < end2 && start2 < end1;
    }
}