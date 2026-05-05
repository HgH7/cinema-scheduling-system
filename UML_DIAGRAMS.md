# 📐 Cinema Scheduling System - UML & Sequence Diagrams

*Complete diagram specifications for generating visual architecture diagrams*

---

## 🎯 Class Diagram (UML)

### PlantUML Code for Class Diagram

```plantuml
@startuml Cinema System Class Diagram

package "Model Layer" {
    class Movie {
        - id: int
        - title: String
        - duration: int
        - genre: String
        - imagePath: String
        + Movie(id, title, duration, genre, imagePath)
        + getId(): int
        + getTitle(): String
        + getDuration(): int
        + getGenre(): String
        + getImagePath(): String
    }

    class Show {
        - id: int
        - movie: Movie
        - screen: Screen
        - showTime: String
        - startTime: LocalDateTime
        - endTime: LocalDateTime
        - pricePerSeat: double
        + Show(id, movie, screen, showTime)
        + getId(): int
        + getMovie(): Movie
        + getScreen(): Screen
        + getShowTime(): String
        + getPricePerSeat(): double
        + getSeat(row, col): Seat
    }

    class Booking {
        - id: int
        - bookingId: String
        - show: Show
        - seats: List<Seat>
        - userName: String
        - totalPrice: double
        + Booking(id, show, seats, userName)
        + getId(): int
        + getBookingId(): String
        + getShow(): Show
        + getSeats(): List<Seat>
        + confirmBooking(): boolean
    }

    class Seat {
        - row: String
        - number: int
        - status: SeatStatus
        + Seat(row, number)
        + isAvailable(): boolean
        + book(): boolean
        + getRow(): String
        + getNumber(): int
    }

    enum SeatStatus {
        AVAILABLE
        BOOKED
    }

    class Screen {
        - id: int
        - name: String
        - rows: int
        - cols: int
        - seats: List<Seat>
        + Screen(id, name, rows, seatsPerRow)
        + getSeat(row, number): Seat
        + getSeats(): List<Seat>
    }
}

package "Service Layer" {
    class MovieService {
        - movies: List<Movie>
        + addMovie(id, title, duration, genre, imagePath): Movie
        + getAllMovies(): List<Movie>
        + findMovieById(id): Movie
        + removeMovie(id): boolean
    }

    class ShowService {
        - shows: List<Show>
        + addShow(id, movie, screen, showTime): Show
        + getAllShows(): List<Show>
        + findShowById(id): Show
        + removeShow(id): boolean
    }

    class BookingService {
        - bookings: List<Booking>
        + createBooking(id, show, seats, userName): Booking
        + getAllBookings(): List<Booking>
        + findBookingById(id): Booking
    }
}

package "UI Layer" {
    class ServiceContext {
        - instance: ServiceContext
        - movieService: MovieService
        - showService: ShowService
        - bookingService: BookingService
        + getInstance(): ServiceContext
        + getMovieService(): MovieService
        + getShowService(): ShowService
        + getBookingService(): BookingService
    }

    class MainMenuUI {
        + {static} startApplication()
        - createAndShow()
    }

    class UserUI {
        - browsingPanel: MovieBrowsingPanel
        + UserUI()
    }

    class AdminUI {
        - contentLayout: CardLayout
        - contentPanel: JPanel
        + AdminUI()
    }

    class MovieBrowsingPanel {
        - cards: JPanel
        + filterMovies(query: String)
    }

    class SeatSelectionUI {
        - seatButtons: SeatToggleButton[][]
        - show: Show
        + SeatSelectionUI(show)
    }

    class AdminMoviesPanel {
        + AdminMoviesPanel()
    }

    class AdminShowsPanel {
        + AdminShowsPanel()
    }

    class AdminBookingsPanel {
        + AdminBookingsPanel()
    }

    class AdminSalesPanel {
        + AdminSalesPanel()
    }
}

' Relationships
Movie ||--o{ Show : has
Screen ||--o{ Show : hosts
Screen *-- Seat : contains
Show ||--o{ Booking : has
Booking *-- Seat : reserves

MovieService ..> Movie : manages
ShowService ..> Show : manages
BookingService ..> Booking : manages

ServiceContext --> MovieService : singleton
ServiceContext --> ShowService : singleton
ServiceContext --> BookingService : singleton

MainMenuUI --> UserUI : launches
MainMenuUI --> AdminUI : launches

UserUI --> MovieBrowsingPanel : contains
MovieBrowsingPanel --> SeatSelectionUI : opens

AdminUI --> AdminMoviesPanel : contains
AdminUI --> AdminShowsPanel : contains
AdminUI --> AdminBookingsPanel : contains
AdminUI --> AdminSalesPanel : contains

SeatSelectionUI --> BookingService : uses
AdminMoviesPanel --> MovieService : uses
AdminShowsPanel --> ShowService : uses
AdminBookingsPanel --> BookingService : uses

@enduml
```

---

## 🔄 Sequence Diagrams

### 1. Movie Addition Sequence

```plantuml
@startuml Movie Addition Sequence

actor Admin
participant AdminMoviesPanel
participant MovieService
participant Movie
participant ServiceContext

Admin -> AdminMoviesPanel: Enter movie details
Admin -> AdminMoviesPanel: Click "Add to Library"

AdminMoviesPanel -> MovieService: addMovie(id, title, duration, genre, imagePath)
MovieService -> Movie: new Movie(...)
MovieService --> AdminMoviesPanel: Movie object

AdminMoviesPanel -> ServiceContext: saveMovies()
ServiceContext -> MovieService: getAllMovies()
MovieService --> ServiceContext: List<Movie>
ServiceContext -> ServiceContext: Write to movies.txt

AdminMoviesPanel -> AdminMoviesPanel: refreshTable()
AdminMoviesPanel --> Admin: Success message

@enduml
```

### 2. Seat Booking Sequence

```plantuml
@startuml Seat Booking Sequence

actor User
participant MovieBrowsingPanel
participant SeatSelectionUI
participant BookingService
participant Show
participant Seat

User -> MovieBrowsingPanel: Browse movies
User -> MovieBrowsingPanel: Click movie card

MovieBrowsingPanel -> SeatSelectionUI: new SeatSelectionUI(show)

SeatSelectionUI -> Show: getSeat(row, col)
Show -> Screen: getSeat(row, number)
Screen --> Show: Seat object
Show --> SeatSelectionUI: Seat status

SeatSelectionUI --> User: Display seat map

User -> SeatSelectionUI: Select seats
User -> SeatSelectionUI: Click "Confirm Booking"

SeatSelectionUI -> BookingService: createBooking(id, show, selectedSeats, userName)
BookingService -> Seat: isAvailable() [for each seat]
Seat --> BookingService: true/false

alt All seats available
    BookingService -> Seat: book() [for each seat]
    Seat -> Seat: status = BOOKED
    BookingService -> Booking: new Booking(...)
    BookingService --> SeatSelectionUI: Booking object
    SeatSelectionUI --> User: Show receipt
else Some seats unavailable
    BookingService --> SeatSelectionUI: null
    SeatSelectionUI --> User: Error message
end

@enduml
```

### 3. Show Scheduling with Conflict Detection

```plantuml
@startuml Show Scheduling Sequence

actor Admin
participant AdminShowsPanel
participant ShowService
participant Show
participant Screen

Admin -> AdminShowsPanel: Select movie, screen, time
Admin -> AdminShowsPanel: Click "Schedule Show"

AdminShowsPanel -> ShowService: addShow(id, movie, screen, showTime)

loop For each existing show
    ShowService -> Show: getScreen()
    Show -> Screen: getId()
    Screen --> Show: screenId
    ShowService -> Show: getShowTime()
    Show --> ShowService: existingTime

    alt Conflict detected
        ShowService --> AdminShowsPanel: null (conflict)
        AdminShowsPanel --> Admin: Error: Time conflict
        break
    end
end

ShowService -> Show: new Show(id, movie, screen, showTime)
ShowService --> AdminShowsPanel: Show object
AdminShowsPanel --> Admin: Success

@enduml
```

### 4. Booking Cancellation Sequence

```plantuml
@startuml Booking Cancellation Sequence

actor Admin
participant AdminBookingsPanel
participant receipts.txt

Admin -> AdminBookingsPanel: View bookings table
Admin -> AdminBookingsPanel: Click "Cancel" on booking

AdminBookingsPanel -> AdminBookingsPanel: cancelBooking(bookingId)

AdminBookingsPanel -> receipts.txt: Read all lines
receipts.txt --> AdminBookingsPanel: List<String>

AdminBookingsPanel -> AdminBookingsPanel: Find receipt with exact bookingId match
AdminBookingsPanel -> AdminBookingsPanel: Remove matching receipt block

AdminBookingsPanel -> receipts.txt: Write filtered lines
AdminBookingsPanel -> AdminBookingsPanel: refreshTable()

AdminBookingsPanel --> Admin: Cancellation confirmed

@enduml
```

---

## 🏗️ Package Diagram

```plantuml
@startuml Package Diagram

package "UI Layer" as UI {
    [MainMenuUI]
    [UserUI]
    [AdminUI]
    [Panels]
}

package "Service Layer" as Service {
    [MovieService]
    [ShowService]
    [BookingService]
}

package "Model Layer" as Model {
    [Movie]
    [Show]
    [Booking]
    [Seat]
    [Screen]
}

package "Persistence" as Persistence {
    [movies.txt]
    [receipts.txt]
}

UI --> Service : uses
Service --> Model : manages
Service --> Persistence : reads/writes

@enduml
```

---

## 📊 Component Diagram

```plantuml
@startuml Component Diagram

component "User Interface" as UI {
    port "User Actions" as UA
    port "Admin Actions" as AA
}

component "Business Logic" as BL {
    port "Movie Operations" as MO
    port "Show Operations" as SO
    port "Booking Operations" as BO
}

component "Data Access" as DA {
    port "File I/O" as IO
}

component "Data Storage" as DS {
    database "movies.txt" as MT
    database "receipts.txt" as RT
}

UA --> MO : movie browsing
UA --> BO : seat booking
AA --> MO : movie management
AA --> SO : show scheduling
AA --> BO : booking management

MO --> IO : read/write movies
SO --> IO : read/write shows
BO --> IO : read/write bookings

IO --> MT : persist movies
IO --> RT : persist receipts

@enduml
```

---

## 🔄 Activity Diagrams

### User Movie Booking Activity

```plantuml
@startuml User Booking Activity

start

:Launch application;
:Choose User Side;

:View movie grid;
:Search/Filter movies;

if (Find desired movie?) then (yes)
    :Click movie card;
    :View seat selection UI;
else (no)
    :Refine search;
    endif

:View seat map with availability;

repeat
    :Click on available seat;
    :Seat toggles selected;
repeat while (want more seats?)

:View total price calculation;

if (Confirm booking?) then (yes)
    :Click "Confirm Booking";
    if (Seats still available?) then (yes)
        :Generate booking ID;
        :Mark seats as booked;
        :Show receipt;
        :Save to receipts.txt;
        stop
    else (no)
        :Show error message;
        :Return to seat selection;
    endif
else (no)
    :Cancel and return to browsing;
endif

@enduml
```

### Admin Show Scheduling Activity

```plantuml
@startuml Admin Scheduling Activity

start

:Launch application;
:Choose Admin Side;
:Navigate to Showtimes tab;

:Select movie from dropdown;
:Select screen (1-5);
:Enter start time (HH:mm);

:Click "Schedule Show";

if (Time conflict check) then (yes)
    :Show conflict error;
    :Return to form;
else (no)
    :Create new Show object;
    :Add to shows list;
    :Update table display;
    :Show success message;
endif

stop

@enduml
```

---

## 🛠️ How to Generate Diagrams

### Using PlantUML
1. Copy the PlantUML code above
2. Use an online PlantUML viewer: https://plantuml.com/
3. Or install PlantUML plugin in VS Code
4. Paste code and generate diagrams

### Using Draw.io
1. Import the PlantUML code
2. Or manually create diagrams using the specifications above

### Key Diagram Elements
- **Classes**: Rectangles with compartments (name, attributes, operations)
- **Relationships**: Lines with different arrowheads
- **Packages**: Large rectangles containing classes
- **Actors**: Stick figures for external users
- **Lifelines**: Dashed vertical lines in sequence diagrams
- **Messages**: Horizontal arrows between lifelines

---

## 📋 Diagram Explanations

### Class Diagram
- Shows the static structure of the system
- Illustrates inheritance, composition, and associations
- Helps understand object relationships and dependencies

### Sequence Diagrams
- Show dynamic interactions between objects
- Demonstrate the flow of messages over time
- Critical for understanding system behavior

### Package Diagram
- Shows high-level organization
- Illustrates dependencies between layers
- Helps understand architectural boundaries

### Component Diagram
- Shows runtime components and their interfaces
- Illustrates data flow between components
- Useful for deployment and integration planning

### Activity Diagrams
- Show workflow and decision points
- Illustrate user journeys through the system
- Help identify bottlenecks and alternative paths

---

*Use these specifications to generate professional UML diagrams for your portfolio or documentation.*