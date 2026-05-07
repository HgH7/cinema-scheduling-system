package service;

import model.Screen;

/**
 * Service manager for all cinema services
 */
public class CinemaServiceManager {
    private static CinemaServiceManager instance;

    private final PersistenceService persistenceService;
    private final MovieService movieService;
    private final ShowService showService;
    private final BookingService bookingService;
    private final ReceiptService receiptService;
    private final AnalyticsService analyticsService;

    private CinemaServiceManager() {
        persistenceService = new PersistenceService();
        receiptService = new ReceiptService(persistenceService);
        analyticsService = new AnalyticsService(receiptService);
        movieService = new MovieService(persistenceService);
        showService = new ShowService();
        bookingService = new BookingService(receiptService);

        seedData();
    }

    public static synchronized CinemaServiceManager getInstance() {
        if (instance == null) {
            instance = new CinemaServiceManager();
        }
        return instance;
    }

    private void seedData() {
        movieService.loadMovies();

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

        bookingService.loadBookingsFromReceipts(showService);
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