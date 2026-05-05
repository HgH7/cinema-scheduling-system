# 🎯 Cinema Scheduling System - Technical Deep Dive

*Interview Preparation Guide: Every line of code explained with functionality and design decisions*

---

## 📂 Project Overview

This Java Swing application implements a cinema management system with MVC architecture. The system handles movie scheduling, seat booking, and administrative operations with file-based persistence.

**Architecture**: Model-View-Controller (MVC) with Service Layer
**UI Framework**: Java Swing with custom dark theme
**Data Persistence**: File-based (movies.txt, receipts.txt)
**Key Features**: Seat booking, conflict detection, receipt generation

---

## 🔍 Code Analysis by Component

### 1. Main.java - Application Entry Point

```java
public class Main {
    public static void main(String[] args) {
        System.out.println("Cinema Scheduling System");
        MainMenuUI.startApplication();
    }
}
```

**Purpose**: Entry point for the Java application
- Prints startup message to console
- Delegates to MainMenuUI for GUI initialization
- **Design Decision**: Simple entry point keeps main() clean, delegates to UI layer

**Interview Q&A**:
- Why separate main() from UI? Separation of concerns, testability
- What does startApplication() do? Initializes Swing EDT and creates main menu

---

### 2. Model Layer - Data Structures

#### Movie.java
```java
public class Movie {
    private int id;
    private String title;
    private int duration;  // in minutes
    private String genre;
    private String imagePath;

    public Movie(int id, String title, int duration, String genre, String imagePath) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.genre = genre;
        this.imagePath = imagePath;
    }

    // Getters and setters...
}
```

**Purpose**: Represents a movie entity
- **id**: Unique identifier (auto-generated)
- **title**: Movie name
- **duration**: Length in minutes (used for scheduling conflicts)
- **genre**: Category for filtering/searching
- **imagePath**: Poster image location

**Design Decisions**:
- Immutable constructor - all required fields
- No setters for id (immutable identifier)
- String imagePath allows both file paths and URLs

**Interview Q&A**:
- Why not use LocalDate for release date? Scope limited to current scheduling
- How does duration affect scheduling? Used in conflict detection logic

#### Show.java
```java
public class Show {
    private int id;
    private Movie movie;
    private Screen screen;
    private String showTime;  // HH:mm format
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double pricePerSeat;

    public Show(int id, Movie movie, Screen screen, String showTime) {
        this.id = id;
        this.movie = movie;
        this.screen = screen;
        this.showTime = showTime;
        this.pricePerSeat = 10.0;
    }

    // Multiple constructors for flexibility
    public Show(int id, Movie movie, Screen screen,
            LocalDateTime startTime, LocalDateTime endTime, double pricePerSeat) {
        // Full initialization
    }
}
```

**Purpose**: Represents a scheduled movie showing
- **id**: Unique show identifier
- **movie**: Reference to Movie object
- **screen**: Which auditorium (1-5)
- **showTime**: Display time (HH:mm)
- **startTime/endTime**: Precise scheduling (LocalDateTime)
- **pricePerSeat**: Cost per ticket

**Design Decisions**:
- Two constructors: Simple (time string) and full (LocalDateTime)
- pricePerSeat defaults to 10.0 but overridable
- Composition over inheritance (has-a Movie, has-a Screen)

**Interview Q&A**:
- Why two constructors? Flexibility for different use cases
- How does showTime differ from startTime? showTime is display format, startTime is precise

#### Booking.java
```java
public class Booking {
    private int id;
    private String bookingId;  // "NU" + timestamp
    private Show show;
    private List<Seat> seats;
    private String userName;
    private double totalPrice;

    public Booking(int id, Show show, List<Seat> seats, String userName) {
        this.id = id;
        this.bookingId = "NU" + System.currentTimeMillis();
        this.show = show;
        this.seats = seats;
        this.userName = userName;
        this.totalPrice = seats.size() * show.getPricePerSeat();
    }

    public boolean confirmBooking() {
        for (Seat seat : seats) {
            if (!seat.book()) {
                return false;  // Atomic operation
            }
        }
        return true;
    }
}
```

**Purpose**: Represents a ticket booking transaction
- **id**: Database-style identifier
- **bookingId**: User-friendly ID with timestamp
- **show**: Which showing
- **seats**: List of selected seats
- **userName**: Customer identifier
- **totalPrice**: Calculated total cost

**Design Decisions**:
- confirmBooking() is atomic - either all seats book or none
- bookingId uses timestamp for uniqueness
- totalPrice calculated in constructor (derived data)

**Interview Q&A**:
- Why atomic booking? Prevents partial bookings in concurrent scenarios
- Why bookingId format? "NU" prefix + timestamp ensures uniqueness

#### Seat.java
```java
public class Seat {
    private String row;  // "A", "B", "C", "D"
    private int number;  // 1-10
    private SeatStatus status;

    public Seat(String row, int number) {
        this.row = row;
        this.number = number;
        this.status = SeatStatus.AVAILABLE;
    }

    public boolean isAvailable() {
        return status == SeatStatus.AVAILABLE;
    }

    public boolean book() {
        if (isAvailable()) {
            status = SeatStatus.BOOKED;
            return true;
        }
        return false;
    }
}
```

**Purpose**: Represents individual seat in auditorium
- **row**: Letter designation (A-D)
- **number**: Seat number (1-10)
- **status**: Current availability state

**Design Decisions**:
- Status enum prevents invalid states
- book() method encapsulates state transition logic
- Immutable row/number (seats don't move)

**Interview Q&A**:
- Why not boolean isBooked? Enum allows future states (RESERVED, MAINTENANCE)
- Why book() returns boolean? Indicates success/failure

#### SeatStatus.java
```java
public enum SeatStatus {
    AVAILABLE,
    BOOKED
}
```

**Purpose**: Type-safe seat states
**Design Decision**: Enum prevents magic strings, extensible

#### Screen.java
```java
public class Screen {
    private int id;
    private String name;  // "Screen 1", "Screen 2", etc.
    private int rows;
    private int cols;
    private List<Seat> seats;

    public Screen(int id, String name, int rows, int seatsPerRow) {
        this.id = id;
        this.name = name;
        this.rows = rows;
        this.cols = seatsPerRow;
        this.seats = new ArrayList<>();
        for (int r = 1; r <= rows; r++) {
            char rowChar = (char) ('A' + r - 1);
            for (int s = 1; s <= seatsPerRow; s++) {
                seats.add(new Seat(String.valueOf(rowChar), s));
            }
        }
    }

    public Seat getSeat(String row, int number) {
        for (Seat seat : seats) {
            if (seat.getRow().equals(row) && seat.getNumber() == number) {
                return seat;
            }
        }
        return null;
    }
}
```

**Purpose**: Represents physical auditorium
- **id/name**: Unique identifiers
- **rows/cols**: Layout dimensions
- **seats**: Collection of Seat objects

**Design Decisions**:
- Constructor auto-generates seat grid
- getSeat() method for coordinate-based access
- Row letters generated from ASCII ('A' + offset)

**Interview Q&A**:
- Why auto-generate seats? Consistent layout, no manual setup
- Why getSeat(String, int)? User-friendly coordinate system

---

### 3. Service Layer - Business Logic

#### MovieService.java
```java
public class MovieService {
    private List<Movie> movies = new ArrayList<>();

    public Movie addMovie(int id, String title, int duration, String genre, String imagePath) {
        Movie movie = new Movie(id, title, duration, genre, imagePath);
        movies.add(movie);
        return movie;
    }

    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies);  // Defensive copy
    }

    public Movie findMovieById(int id) {
        for (Movie m : movies) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }
}
```

**Purpose**: Manages movie data operations
**Design Decisions**:
- In-memory storage (could be database)
- Defensive copying prevents external modification
- Simple CRUD operations

**Interview Q&A**:
- Why ArrayList? Ordered, allows duplicates (though IDs prevent)
- Why defensive copy? Prevents service layer data corruption

#### ShowService.java
```java
public class ShowService {
    private List<Show> shows = new ArrayList<>();

    public Show addShow(int id, Movie movie, Screen screen, String showTime) {
        // Check for conflicts
        for (Show s : shows) {
            if (s.getScreen().getId() == screen.getId()
                && s.getShowTime().equals(showTime)) {
                return null;  // Conflict detected
            }
        }

        Show show = new Show(id, movie, screen, showTime);
        shows.add(show);
        return show;
    }
}
```

**Purpose**: Manages show scheduling with conflict detection
**Design Decisions**:
- Conflict detection prevents double-booking
- Returns null on conflict (caller handles)
- Simple equality check for showTime

**Interview Q&A**:
- Why not more sophisticated conflict detection? Scope limitation
- Why return null instead of exception? Simple error handling

#### BookingService.java
```java
public class BookingService {
    private List<Booking> bookings = new ArrayList<>();

    public Booking createBooking(int id, Show show, List<Seat> seats, String userName) {
        // Pre-check availability
        for (Seat seat : seats) {
            if (!seat.isAvailable()) {
                return null;
            }
        }

        // Book all seats
        for (Seat seat : seats) {
            seat.book();
        }

        Booking booking = new Booking(id, show, seats, userName);
        bookings.add(booking);
        return booking;
    }
}
```

**Purpose**: Handles booking creation with seat reservation
**Design Decisions**:
- Two-phase commit: check then book
- Atomic operation (all or nothing)
- Seat status updated immediately

**Interview Q&A**:
- Why two loops? Separation of validation and execution
- Why not rollback on failure? Simple implementation, seats auto-reset

---

### 4. UI Layer - User Interface

#### ServiceContext.java (Singleton)
```java
public class ServiceContext {
    private static ServiceContext instance;

    private final MovieService movieService;
    private final ShowService showService;
    private final BookingService bookingService;

    private ServiceContext() {
        movieService = new MovieService();
        showService = new ShowService();
        bookingService = new BookingService();
        seedData();
    }

    public static synchronized ServiceContext getInstance() {
        if (instance == null) {
            instance = new ServiceContext();
        }
        return instance;
    }
}
```

**Purpose**: Singleton service locator/container
**Design Decisions**:
- Thread-safe singleton (synchronized)
- Lazy initialization
- Seeds initial data

**Interview Q&A**:
- Why singleton? Single point of access, shared state
- Why synchronized? Thread safety in multi-threaded Swing

#### UIConstants.java
```java
public class UIConstants {
    public static final Color BACKGROUND = new Color(0x20, 0x0e, 0x0c);
    public static final Color SURFACE = new Color(0x1a, 0x1a, 0x1a);
    // ... more colors and fonts
}
```

**Purpose**: Centralized UI theming constants
**Design Decisions**:
- Hex color codes for consistency
- Font constants prevent magic strings
- Easy theme changes

#### MainMenuUI.java
```java
public class MainMenuUI {
    public static void startApplication() {
        SwingUtilities.invokeLater(() -> new MainMenuUI().createAndShow());
    }

    private void createAndShow() {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        JFrame frame = new JFrame("Cinema Scheduling System");
        // ... setup frame and content
    }
}
```

**Purpose**: Application entry point UI
**Design Decisions**:
- invokeLater() ensures EDT usage
- Cross-platform look and feel
- Modal dialog for mode selection

**Interview Q&A**:
- Why invokeLater()? Swing thread safety
- Why try-catch on L&F? Graceful degradation

#### SeatSelectionUI.java (Key Component)
```java
public class SeatSelectionUI extends JFrame {
    private final SeatToggleButton[][] seatButtons = new SeatToggleButton[4][10];
    private final Show show;

    // Constructor creates 4x10 grid
    public SeatSelectionUI(Show show) {
        // Initialize grid
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 10; col++) {
                SeatToggleButton seatButton = new SeatToggleButton(/*...*/);
                seatButtons[row][col] = seatButton;

                // Check if seat is booked
                Seat actualSeat = show.getSeat(/*...*/);
                if (actualSeat != null && !actualSeat.isAvailable()) {
                    seatButton.setBooked(true);
                }
            }
        }
    }
}
```

**Purpose**: Interactive seat selection interface
**Design Decisions**:
- 2D array mirrors physical layout
- Real-time seat status from Show object
- Toggle buttons for selection

**Interview Q&A**:
- Why 2D array? Direct mapping to grid coordinates
- Why check actualSeat? Ensures persistence across sessions

---

## 🔄 Key Workflows

### 1. Movie Addition Workflow
1. Admin enters movie details in AdminMoviesPanel
2. Calls MovieService.addMovie()
3. Movie added to in-memory list
4. ServiceContext.saveMovies() writes to movies.txt

### 2. Seat Booking Workflow
1. User selects movie in MovieBrowsingPanel
2. Clicks movie → opens SeatSelectionUI
3. User selects seats → BookingService.createBooking()
4. Seats marked as booked
5. Receipt generated and saved

### 3. Show Scheduling Workflow
1. Admin selects movie and time in AdminShowsPanel
2. ShowService.addShow() checks conflicts
3. If no conflict → Show created
4. Show added to shows list

---

## 🐛 Bug Fixes Applied

### Issue 1: Booked seats not persisting
**Problem**: New Seat objects created instead of using Show seats
**Fix**: Use show.getSeat(row, col) to get actual seat references

### Issue 2: Bulk cancellation
**Problem**: contains() matched substrings
**Fix**: Exact equality check with "Booking ID: " + bookingId

---

## 📊 Data Persistence

### movies.txt Format
```
1	The Dark Knight	152	Action, Crime, Drama	img/dark_knight.jpg
2	Inception	148	Sci-Fi, Action, Thriller	img/inception.jpg
```

### receipts.txt Format
```
==============================
      CINERESERVE RECEIPT
==============================
Booking ID: #CR-0001
Date: 2024-01-15 14:30:00
Movie: The Dark Knight
Screen: Screen 1
Showtime: 18:30
------------------------------
Seats: A1, A2
Total Price: $28.00
==============================
```

---

## 🧪 Testing Scenarios

1. **Seat Booking**: Book seats → restart app → verify seats still booked
2. **Conflict Detection**: Try scheduling overlapping shows → should fail
3. **Cancellation**: Cancel specific booking → only that booking removed
4. **UI Responsiveness**: All buttons functional, no exceptions

---

## 💡 Interview Preparation Questions

### Architecture & Design
1. **Why MVC?** Separation of concerns, testability, maintainability
2. **Why Singleton for ServiceContext?** Single point of access, shared state
3. **Why file-based persistence?** Simple, no database dependency
4. **Why Swing over JavaFX?** Mature, stable, good for desktop apps

### Data Structures
1. **Why List<Movie> not Map?** Ordered display, simple iteration
2. **Why enum for SeatStatus?** Type safety, extensibility
3. **Why ArrayList over LinkedList?** Fast random access for UI

### Concurrency
1. **Thread safety?** Swing EDT handles UI, services are single-threaded
2. **Race conditions?** Minimal - single user system

### Error Handling
1. **Why return null instead of exceptions?** Simple, caller handles
2. **Validation?** UI level validation, service level checks

### Performance
1. **Scalability?** In-memory storage limits size
2. **Optimization opportunities?** Database integration, caching

---

## 🔮 Future Improvements

1. **Database Integration**: Replace file storage with JDBC
2. **User Authentication**: Login system with roles
3. **Payment Processing**: Integration with payment gateways
4. **REST API**: Web service for mobile apps
5. **Advanced Scheduling**: Recurring shows, calendar view
6. **Reporting**: Detailed analytics and charts

---

*This technical documentation covers every major component, design decision, and potential interview question for the Cinema Scheduling System project.*