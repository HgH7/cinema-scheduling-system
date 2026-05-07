# Cinema Scheduling System

A Java Swing cinema management application with a clean model-service-UI architecture.

## Overview

This project simulates a cinema scheduling system where administrators can manage movies and shows, and users can browse available movies and book seats. The refactored design keeps UI code focused on display and interaction, while services handle business logic and persistence.

## Architecture

- `src/model/` - Domain entities: `Movie`, `Show`, `Booking`, `Seat`, `Screen`, `SeatStatus`
- `src/service/` - Application logic and persistence
  - `CinemaServiceManager` - central service registry
  - `MovieService` - movie operations and storage
  - `ShowService` - show scheduling and conflict detection
  - `BookingService` - seat booking and receipt creation
  - `ReceiptService` - receipt formatting and persistence
  - `AnalyticsService` - sales and popularity reporting
  - `PersistenceService` - file I/O for movies and receipts
- `src/ui/` - Swing interface components and styling
- `src/util/` - input validation helpers

## Features

- Movie management
- Show scheduling with conflict detection
- Seat booking with live price updates
- Receipt generation and persistence
- Analytics dashboard for bookings, revenue, and movie popularity
- Light and consistent UI styling

## Build

```powershell
cd d:\programing\cinema-scheduling-system
mkdir out
javac -d out src/Main.java src/model/*.java src/service/*.java src/ui/*.java src/util/*.java
```

## Run

```powershell
java -cp out Main
```

## Notes

- The UI layer only handles interaction and display logic.
- Business rules and persistence have been moved into the service layer.
- Existing functionality is preserved and the project compiles cleanly.
