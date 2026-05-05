package exception;

/**
 * Thrown when booking operation fails
 */
public class BookingException extends CinemaException {
    public BookingException(String message) {
        super(message);
    }

    public BookingException(String message, Throwable cause) {
        super(message, cause);
    }
}
