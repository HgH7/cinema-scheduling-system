# Cinema Scheduling System Service Guide

## 1. Class Workflow: How the model classes work together

### Core model classes
- `Movie`
  - Represents a film.
  - Stores `id`, `title`, `duration`, and `genre`.
- `Screen`
  - Represents a theater screen.
  - Stores `id`, `name`, `rows`, `cols`, and a list of `Seat` objects.
  - Generates seats for the screen layout.
- `Seat`
  - Represents one seat in a screen.
  - Stores `row`, `number`, and `status` (`AVAILABLE` or `BOOKED`).
  - Can be booked via `book()`.
- `Show`
  - Represents a movie presentation at a specific time on a screen.
  - Stores `id`, `movie`, `screen`, `showTime`, optional `startTime`, `endTime`, and `pricePerSeat`.
  - Provides seat availability checks and returns the current `Seat` from the screen.
- `Booking`
  - Represents a user reservation.
  - Stores `id`, `bookingId`, `show`, `seats`, `userName`, and `totalPrice`.
  - Confirms booking by marking each selected seat as booked.

### Workflow between classes
1. **Movie and Screen creation**
   - Admin adds `Movie` objects and `Screen` objects.
2. **Show scheduling**
   - Admin creates a `Show` by assigning a `Movie` to a `Screen` and giving it a time.
   - A `Show` uses the `Screen` seat layout to determine available seats.
3. **Seat selection**
   - User picks seats from a `Show` by calling its `getSeat(row, col)` or by checking available seats on the screen.
4. **Booking**
   - User creates a `Booking` for a `Show` with selected `Seat` objects.
   - `Booking.confirmBooking()` books the seats by changing their status.
5. **Booking lifecycle**
   - After confirmation, seats become `BOOKED` and should prevent other users from selecting them.

## 2. What the service layer should do

The services should provide business logic and manage collections of the models:
- `MovieService`
- `ShowService`
- `BookingService`

### Responsibilities for each service

#### MovieService
- Store and manage the list of movies.
- Add new movies.
- Remove movies.
- Find movies by ID.
- List all movies.
- Validate movie data on creation.

#### ShowService
- Store and manage scheduled shows.
- Add a new show.
- Remove a show.
- Find a show by ID.
- List all shows.
- List shows by movie or by screen.
- Validate schedule conflicts: prevent two shows at the same screen and overlapping times.
- Check seat availability for a show.

#### BookingService
- Store and manage bookings.
- Create a booking.
- Cancel a booking.
- Find booking by ID or booking number.
- List bookings for a user.
- Validate seat availability before booking.
- Update show seat status when booking is confirmed.

## 3. Recommended service classes and methods

### MovieService class
```java
package service;

import model.Movie;
import java.util.ArrayList;
import java.util.List;

public class MovieService {
    private final List<Movie> movies = new ArrayList<>();

    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies);
    }

    public Movie addMovie(int id, String title, int duration, String genre) {
        Movie movie = new Movie(id, title, duration, genre);
        movies.add(movie);
        return movie;
    }

    public Movie findMovieById(int id) {
        return movies.stream()
            .filter(m -> m.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public boolean removeMovie(int id) {
        return movies.removeIf(m -> m.getId() == id);
    }
}
```

### ShowService class
```java
package service;

import model.Show;
import model.Movie;
import model.Screen;
import java.util.ArrayList;
import java.util.List;

public class ShowService {
    private final List<Show> shows = new ArrayList<>();

    public List<Show> getAllShows() {
        return new ArrayList<>(shows);
    }

    public Show addShow(int id, Movie movie, Screen screen, String showTime) {
        Show show = new Show(id, movie, screen, showTime);
        shows.add(show);
        return show;
    }

    public Show findShowById(int id) {
        return shows.stream()
            .filter(s -> s.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public boolean removeShow(int id) {
        return shows.removeIf(s -> s.getId() == id);
    }

    public List<Show> findShowsByMovie(int movieId) {
        List<Show> result = new ArrayList<>();
        for (Show show : shows) {
            if (show.getMovie().getId() == movieId) {
                result.add(show);
            }
        }
        return result;
    }

    public boolean isScreenAvailable(Screen screen, String showTime) {
        for (Show show : shows) {
            if (show.getScreen().getId() == screen.getId()
                && show.getShowTime().equals(showTime)) {
                return false;
            }
        }
        return true;
    }
}
```

### BookingService class
```java
package service;

import model.Booking;
import model.Show;
import model.Seat;
import java.util.ArrayList;
import java.util.List;

public class BookingService {
    private final List<Booking> bookings = new ArrayList<>();

    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }

    public Booking createBooking(int id, Show show, List<Seat> seats, String userName) {
        Booking booking = new Booking(id, show, seats, userName);
        if (booking.confirmBooking()) {
            bookings.add(booking);
            return booking;
        }
        return null;
    }

    public Booking findBookingById(int id) {
        return bookings.stream()
            .filter(b -> b.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public List<Booking> findBookingsByUser(String userName) {
        List<Booking> result = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getUserName().equalsIgnoreCase(userName)) {
                result.add(booking);
            }
        }
        return result;
    }

    public boolean cancelBooking(int bookingId) {
        return bookings.removeIf(b -> b.getId() == bookingId);
    }
}
```

## 4. What to build next in services

### Minimum viable service functionality
- `MovieService`
  - add movie
  - remove movie
  - get all movies
  - find by id
- `ShowService`
  - add show
  - remove show
  - get all shows
  - find by id
  - check if screen is free at a time
  - list shows by movie or screen
- `BookingService`
  - create booking
  - confirm booking
  - list bookings
  - find bookings by user or id
  - cancel booking

### Optional improvements
- Track booking totals and revenue per show.
- Prevent booking if a seat is already booked.
- Add show overlap validation using `startTime` and `endTime`.
- Add seat selection helper methods, like `getAvailableSeats()` on `Show` or `Screen`.
- Add a `User` model for customer profiles.

## 5. Implementation plan

### Step 1: Define service fields
- Each service should keep an internal `List<T>`.
- Use `new ArrayList<>()` and expose copies for safety.

### Step 2: Implement CRUD operations
- Create add/remove/find methods for movies, shows, and bookings.
- Use simple loops or Java streams.
- Keep the service layer isolated from the UI.

### Step 3: Implement business rules
- In `ShowService`, validate screen availability before adding a show.
- In `BookingService`, validate seat status before confirming.
- In `Booking.confirmBooking()`, if any seat booking fails, return `false`.

### Step 4: Connect services to UI
- Use `MainMenuUI` to instantiate and hold `MovieService`, `ShowService`, and `BookingService`.
- Provide commands such as `createMovie()`, `scheduleShow()`, `selectSeats()`, and `confirmBooking()`.

### Step 5: Test manually
- Add a few movies and screens.
- Schedule a show.
- Book seats for that show.
- Confirm a booking and verify seat status changes.
- Try booking the same seat twice.

## 6. Suggested class responsibilities

### UI layer
- Present menus and collect user input.
- Call service methods to perform actions.
- Display results and errors.

### Service layer
- Hold lists of domain objects.
- Enforce rules, validation, and business flow.
- Keep the UI simple and free of domain logic.

### Model layer
- Represent data and base behavior.
- The `Booking` class should know how to commit seat status changes.
- The `Show` class should know how to check available seats.

## 7. Notes for your own implementation
- Keep each service small and focused.
- Don’t duplicate model logic inside UI.
- Prefer meaningful method names like `findShowsByMovie` and `confirmBooking`.
- Start with the simplest version, then add validation and conflict checks.

---

This file gives you a concrete service design and a step-by-step plan. Use it as your implementation blueprint and adapt the method names to fit your app’s UI flow.