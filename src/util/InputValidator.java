package util;

import exception.ValidationException;

/**
 * Utility class for validating user inputs
 * All validation methods throw ValidationException with descriptive messages
 */
public class InputValidator {

    /**
     * Validates movie title
     * @param title The title to validate
     * @throws ValidationException if title is invalid
     */
    public static void validateMovieTitle(String title) throws ValidationException {
        if (title == null || title.trim().isEmpty()) {
            throw new ValidationException("Error: Movie title cannot be empty");
        }
        if (title.length() < 2) {
            throw new ValidationException("Error: Movie title must be at least 2 characters");
        }
        if (title.length() > 100) {
            throw new ValidationException("Error: Movie title cannot exceed 100 characters");
        }
        if (!title.matches("^[a-zA-Z0-9\\s:,'&-]+$")) {
            throw new ValidationException("Error: Movie title contains invalid characters");
        }
    }

    /**
     * Validates movie duration
     * @param durationStr The duration string to validate
     * @throws ValidationException if duration is invalid
     */
    public static void validateDuration(String durationStr) throws ValidationException {
        if (durationStr == null || durationStr.trim().isEmpty()) {
            throw new ValidationException("Error: Duration cannot be empty");
        }
        try {
            int duration = Integer.parseInt(durationStr.trim());
            if (duration < 30) {
                throw new ValidationException("Error: Duration must be at least 30 minutes");
            }
            if (duration > 300) {
                throw new ValidationException("Error: Duration cannot exceed 5 hours (300 minutes)");
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("Error: Duration must be a valid number (in minutes)");
        }
    }

    /**
     * Validates show time format
     * @param time The time string to validate
     * @throws ValidationException if time is invalid
     */
    public static void validateShowTime(String time) throws ValidationException {
        if (time == null || time.trim().isEmpty()) {
            throw new ValidationException("Error: Show time cannot be empty");
        }
        if (!time.matches("\\d{2}:\\d{2}")) {
            throw new ValidationException("Error: Time must be in HH:mm format (e.g., 18:30)");
        }
        try {
            String[] parts = time.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);

            if (hours < 0 || hours > 23) {
                throw new ValidationException("Error: Hours must be between 00 and 23");
            }
            if (minutes < 0 || minutes > 59) {
                throw new ValidationException("Error: Minutes must be between 00 and 59");
            }
        } catch (Exception e) {
            throw new ValidationException("Error: Invalid time format. Use HH:mm (e.g., 18:30)");
        }
    }

    /**
     * Validates username
     * @param username The username to validate
     * @throws ValidationException if username is invalid
     */
    public static void validateUsername(String username) throws ValidationException {
        if (username == null || username.trim().isEmpty()) {
            throw new ValidationException("Error: Username cannot be empty");
        }
        if (username.length() < 2) {
            throw new ValidationException("Error: Username must be at least 2 characters");
        }
        if (username.length() > 50) {
            throw new ValidationException("Error: Username cannot exceed 50 characters");
        }
        if (!username.matches("^[a-zA-Z0-9_\\s-]+$")) {
            throw new ValidationException("Error: Username contains invalid characters");
        }
    }

    /**
     * Validates price per seat
     * @param priceStr The price string to validate
     * @throws ValidationException if price is invalid
     */
    public static void validatePrice(String priceStr) throws ValidationException {
        if (priceStr == null || priceStr.trim().isEmpty()) {
            throw new ValidationException("Error: Price cannot be empty");
        }
        try {
            double price = Double.parseDouble(priceStr.trim());
            if (price <= 0) {
                throw new ValidationException("Error: Price must be greater than 0");
            }
            if (price > 10000) {
                throw new ValidationException("Error: Price exceeds maximum limit of $10,000");
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("Error: Price must be a valid number (e.g., 14.50)");
        }
    }

    /**
     * Validates genre
     * @param genre The genre to validate
     * @throws ValidationException if genre is invalid
     */
    public static void validateGenre(String genre) throws ValidationException {
        if (genre == null || genre.trim().isEmpty()) {
            throw new ValidationException("Error: Genre cannot be empty");
        }
        if (genre.length() > 100) {
            throw new ValidationException("Error: Genre cannot exceed 100 characters");
        }
    }

    /**
     * Validates image path
     * @param imagePath The image path to validate
     * @throws ValidationException if path is invalid
     */
    public static void validateImagePath(String imagePath) throws ValidationException {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            throw new ValidationException("Error: Image path cannot be empty");
        }
        if (!imagePath.matches("(?i).*\\.(jpg|jpeg|png|gif|bmp)$")) {
            throw new ValidationException("Error: Image must be JPG, PNG, GIF, or BMP format");
        }
    }

    /**
     * Validates that at least one seat is selected
     * @param seatCount The number of seats selected
     * @throws ValidationException if no seats selected
     */
    public static void validateSeatsSelected(int seatCount) throws ValidationException {
        if (seatCount == 0) {
            throw new ValidationException("Error: Please select at least one seat");
        }
        if (seatCount > 20) {
            throw new ValidationException("Error: Maximum 20 seats can be booked at once");
        }
    }

    /**
     * Validates movie/show selection
     * @param selection The selected item
     * @throws ValidationException if nothing selected
     */
    public static void validateSelection(Object selection) throws ValidationException {
        if (selection == null) {
            throw new ValidationException("Error: Please select a valid option");
        }
    }
}
