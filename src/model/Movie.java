package model;

public class Movie {
    private int id;
    private String title;
    private int duration;
    private String genre;
    private String imagePath;

    public Movie(int id, String title, int duration, String genre, String imagePath) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.genre = genre;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
