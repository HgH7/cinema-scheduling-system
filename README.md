# Cinema Scheduling System

A Java Swing cinema management application with a clear MVC-like structure and separated business logic.

## Summary

This application allows:
- Admin users to manage movies and schedule shows.
- Regular users to browse films, select showtimes, choose seats, and book tickets.
- The system to save movies, shows, and receipts in text files.

## Key Features

- Movie creation, listing, and removal
- Show scheduling with screen conflict detection
- Seat selection and booking with receipt generation
- Persistent storage using `data/movies.txt`, `data/shows.txt`, and `data/receipts.txt`
- Simple analytics and admin dashboard support

## Project Structure

- `src/model/` - data entities
- `src/service/` - application logic and persistence
- `src/ui/` - Swing user interface screens
- `src/util/` - validation helpers
- `data/` - text files for movies, shows, and receipts (loaded and updated by the app)
- `img/` - movie poster images referenced in `data/movies.txt`

## Data Storage

The app reads and writes three text files in the `data/` folder:

| File | Description |
|------|-------------|
| `data/movies.txt` | Movie metadata (id, title, duration, genre, image path) |
| `data/shows.txt` | Scheduled shows (show id, movie id, screen id, time) |
| `data/receipts.txt` | Booking receipts (appended after each booking) |

`PersistenceService` handles load/save. Poster paths use relative paths such as `img/avengers.jpeg` (run the app from the project root).

To reset receipts after testing, clear `data/receipts.txt` or replace it with an empty file.

## Build and Run

Requires Java 17 or newer.

```bash
cd cinema-scheduling-system
mkdir -p out
javac -d out src/Main.java src/model/*.java src/service/*.java src/ui/*.java src/util/*.java
java -cp out Main
```

Run from the project root so `data/` and `img/` paths resolve correctly.

## Notes for Instructor

- `Main.java` starts the app and opens the Swing launcher.
- `CinemaServiceManager` initializes shared services and seed data.
- Persistence is implemented with plain text files for easy verification.
- The UI is kept separate from business logic to make the application easier to maintain.
