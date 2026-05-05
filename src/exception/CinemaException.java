package exception;

/**
 * Base exception class for Cinema Scheduling System
 * All custom exceptions should extend this class
 */
public class CinemaException extends Exception {
    public CinemaException(String message) {
        super(message);
    }

    public CinemaException(String message, Throwable cause) {
        super(message, cause);
    }
}
