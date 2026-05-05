# 🎯 Cinema Scheduling System - Technical Documentation

## 📂 Project Overview

Java Swing application for cinema management with MVC architecture. Handles movie scheduling, seat booking, and admin operations with file-based persistence.

**Architecture**: MVC with Service Layer  
**UI**: Java Swing with custom dark theme  
**Persistence**: File-based (movies.txt, receipts.txt)  
**Key Features**: Seat booking, conflict detection, receipt generation, sales analytics dashboard

## 🔧 Architecture & Design Decisions

### MVC Pattern Implementation
- **Model**: Data entities (Movie, Show, Booking, Seat, Screen)
- **View**: Swing UI components (AdminUI, UserUI, panels)
- **Controller**: Service classes (MovieService, ShowService, BookingService)
- **Singleton Context**: ServiceContext manages service instances

### Key Design Decisions
- **File-based persistence**: Simple, no database dependency
- **Show-specific seat maps**: Each Show clones Screen seats to prevent cross-show conflicts
- **Validation layer**: InputValidator with custom exceptions
- **Analytics summary**: Dashboard displays popularity ranking and metrics

## 📋 Core Classes Overview

### Model Layer
- **Movie**: id, title, duration, genre, imagePath
- **Show**: id, movie, screen, showTime, pricePerSeat + cloned seat list
- **Booking**: id, show, seats list, userName, totalPrice
- **Seat**: row, number, status (AVAILABLE/BOOKED)
- **Screen**: id, name, rows, cols, seat grid

### Service Layer
- **MovieService**: CRUD operations for movies
- **ShowService**: Show scheduling with conflict detection
- **BookingService**: Atomic seat booking with validation

### UI Layer
- **MainMenuUI**: Application entry, mode selection
- **UserUI**: Movie browsing, seat selection
- **AdminUI**: Dashboard with Movies, Shows, Bookings, Sales, Analytics tabs
- **DashboardPanel**: Analytics summary + metrics cards

### Utility Layer
- **InputValidator**: Static validation methods with ValidationException
- **UIConstants**: Centralized colors, fonts, themes

## 🔄 Key Workflows

### Booking Process
1. User selects movie → Show selection
2. SeatSelectionUI displays show-specific seat map
3. User selects seats → BookingService validates & books atomically
4. Receipt generated and saved to receipts.txt

### Admin Operations
1. Movie management: Add/edit movies with validation
2. Show scheduling: Conflict detection (same screen/time)
3. Booking management: View/cancel bookings
4. Sales analytics: Parse receipts.txt for metrics and popularity ranking

## 🛠️ Build & Run

```bash
# Compile
javac -d out src/Main.java src/model/*.java src/service/*.java src/ui/*.java src/exception/*.java src/util/*.java

# Run
java -cp out Main
```

## 📊 Data Files

- **movies.txt**: Tab-separated movie data
- **receipts.txt**: Formatted booking receipts

## 🎯 Interview Prep Highlights

- **State Management**: Show clones seats to isolate bookings per showtime
- **Atomic Operations**: BookingService uses two-phase validation + booking
- **Custom UI**: Swing with consistent theming via UIConstants
- **Error Handling**: ValidationException for user input errors
- **Visualization**: UI summary panels without external libraries
