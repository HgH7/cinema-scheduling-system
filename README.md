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
- Persistent storage using `movies.txt`, `shows.txt`, and `receipts.txt`
- Simple analytics and admin dashboard support

## Project Structure

- `src/model/` - data entities
- `src/service/` - application logic and persistence
- `src/ui/` - Swing user interface screens
- `src/util/` - validation helpers

## Build and Run

```bash
cd cinema-scheduling-system
mkdir -p out
javac -d out src/Main.java src/model/*.java src/service/*.java src/ui/*.java src/util/*.java
java -cp out Main
```

## Notes for Instructor

- `Main.java` starts the app and opens the Swing launcher.
- `CinemaServiceManager` initializes shared services and seed data.
- Persistence is implemented with plain text files for easy verification.
- The UI is kept separate from business logic to make the application easier to maintain.
