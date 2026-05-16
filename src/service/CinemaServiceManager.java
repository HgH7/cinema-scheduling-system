package service;

import model.Screen;

public class CinemaServiceManager {
    private static CinemaServiceManager instance;

    private final PersistenceService persistenceService;
    private final MovieService movieService;
    private final ShowService showService;
    private final BookingService bookingService;
    private final ReceiptService receiptService;
    private final AnalyticsService analyticsService;
    private final java.util.List<Runnable> dataChangeListeners = new java.util.ArrayList<>();
    private final java.util.List<Screen> screens = new java.util.ArrayList<>();

    private CinemaServiceManager() {
        persistenceService = new PersistenceService();
        receiptService = new ReceiptService(persistenceService);
        analyticsService = new AnalyticsService(receiptService);
        movieService = new MovieService(persistenceService);
        showService = new ShowService(persistenceService);
        bookingService = new BookingService(receiptService);

        seedData();
    }

    public void addDataChangeListener(Runnable listener) {
        if (listener != null && !dataChangeListeners.contains(listener)) {
            dataChangeListeners.add(listener);
        }
    }

    public void removeDataChangeListener(Runnable listener) {
        dataChangeListeners.remove(listener);
    }

    public void notifyDataChangeListeners() {
        for (Runnable listener : new java.util.ArrayList<>(dataChangeListeners)) {
            try {
                listener.run();
            } catch (Exception ignored) {
            }
        }
    }

    public static synchronized CinemaServiceManager getInstance() {
        if (instance == null) {
            instance = new CinemaServiceManager();
        }
        return instance;
    }

    private void seedData() {
        movieService.loadMovies();

        if (movieService.getAllMovies().isEmpty()) {
            movieService.addMovie(1, "The Matrix", 136, "Sci-Fi", "matrix.jpg");
            movieService.addMovie(2, "Inception", 148, "Sci-Fi", "inception.jpg");
            movieService.addMovie(3, "Interstellar", 169, "Sci-Fi", "interstellar.jpg");
            movieService.saveMovies();
        }

        screens.clear();
        screens.add(new Screen(1, "Screen 1", 4, 10));
        screens.add(new Screen(2, "Screen 2", 4, 10));
        screens.add(new Screen(3, "Screen 3", 4, 10));
        screens.add(new Screen(4, "Screen 4", 4, 10));
        screens.add(new Screen(5, "Screen 5", 4, 10));

        showService.loadShows(movieService, screens);

        if (showService.getAllShows().isEmpty()) {
            showService.addShow(1, movieService.findMovieById(1), screens.get(0), "18:30");
            showService.addShow(2, movieService.findMovieById(2), screens.get(1), "20:15");
            showService.addShow(3, movieService.findMovieById(3), screens.get(0), "21:30");
            showService.saveShows();
        }
    }

    public PersistenceService getPersistenceService() {
        return persistenceService;
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

    public ReceiptService getReceiptService() {
        return receiptService;
    }

    public AnalyticsService getAnalyticsService() {
        return analyticsService;
    }
}
