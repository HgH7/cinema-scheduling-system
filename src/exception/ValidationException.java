package exception;

/**
 * Thrown when user input validation fails
 */
public class ValidationException extends CinemaException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
