package service;

import model.Show;
import model.Movie;
import model.Screen;

import java.util.ArrayList;
import java.util.List;

public class ShowService {
    private List<Show> shows = new ArrayList<>();

    public Show addShow(int id, Movie movie, Screen screen, String showTime) {

        for (Show s : shows) {
            if (s.getScreen().getId() == screen.getId()
                && s.getShowTime().equals(showTime)) {
                return null;
            }
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

    public boolean removeShow(int id) {
        for (Show s : shows) {
            if (s.getId() == id) {
                shows.remove(s);
                return true;
            }
        }
        return false;
    }
}