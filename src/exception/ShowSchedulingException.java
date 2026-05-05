package exception;

/**
 * Thrown when show scheduling fails (conflicts, invalid data, etc.)
 */
public class ShowSchedulingException extends CinemaException {
    public ShowSchedulingException(String message) {
        super(message);
    }

    public ShowSchedulingException(String message, Throwable cause) {
        super(message, cause);
    }
}
