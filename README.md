# 🎬 Cinema Movie Scheduling System

A comprehensive Java Swing application for managing cinema operations, built with clean architecture and modern UI design.

## 📋 Overview

This application simulates a real-world cinema management system where administrators can manage movies and showtimes, while users can browse movies and book seats. The system features conflict detection, seat persistence, and receipt generation.

## ✨ Features

### 👨‍💼 Admin Features
- **Movie Management**: Add movies with title, duration, genre, and poster images
- **Show Scheduling**: Schedule movie shows on different screens with time conflict detection
- **Booking Oversight**: View all bookings, cancel bookings, and manage reservations
- **Sales Analytics**: View ticket sales reports by movie and date
- **Dashboard**: Analytics summary with movie popularity ranking and key metrics

### 🍿 User Features
- **Movie Browsing**: Browse movies in an attractive grid layout with search functionality
- **Seat Selection**: Interactive 2D seat map (4 rows × 10 seats) with visual status indicators
- **Booking System**: Select multiple seats, view real-time pricing, and receive booking receipts
- **Search & Filter**: Search movies by title or genre

### 🛠️ Technical Features
- **Clean Architecture**: Model-Service-UI layered design
- **Data Persistence**: Movies and receipts saved to files
- **Conflict Detection**: Prevents double-booking of screens
- **Seat State Management**: Booked seats persist across sessions
- **Modern UI**: Dark theme with professional design

## 🚀 Getting Started

### Prerequisites
- Java 11 or higher
- Windows/Linux/Mac OS

### Installation & Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd cinema-scheduling-system
   ```

2. **Compile the application**
   ```bash
   javac -d out src/Main.java src/model/*.java src/service/*.java src/ui/*.java src/exception/*.java src/util/*.java
   ```

3. **Run the application**
   ```bash
   java -cp out Main
   ```

### Usage

1. **Launch**: Run the application to see the main menu
2. **Choose Mode**:
   - **User Side**: Browse movies and book tickets
   - **Admin Side**: Manage movies, shows, and bookings

3. **Admin Workflow**:
   - Add movies via Movies tab
   - Schedule shows via Showtimes tab
   - Monitor bookings via Bookings tab
   - View sales via Sales tab

4. **User Workflow**:
   - Browse movies in grid view
   - Use search to find specific movies
   - Click on movie to select seats
   - Choose available seats and confirm booking

## 🏗️ Architecture

### Project Structure
```
cinema-scheduling-system/
├── src/
│   ├── Main.java                    # Application entry point
│   ├── model/                       # Data models
│   │   ├── Movie.java
│   │   ├── Show.java
│   │   ├── Booking.java
│   │   ├── Seat.java
│   │   ├── SeatStatus.java
│   │   └── Screen.java
│   ├── service/                     # Business logic
│   │   ├── MovieService.java
│   │   ├── ShowService.java
│   │   └── BookingService.java
│   └── ui/                          # User interface
│       ├── MainMenuUI.java
│       ├── UserUI.java
│       ├── AdminUI.java
│       ├── AdminMoviesPanel.java
│       ├── AdminShowsPanel.java
│       ├── AdminBookingsPanel.java
│       ├── AdminSalesPanel.java
│       ├── MovieBrowsingPanel.java
│       ├── SeatSelectionUI.java
│       ├── ServiceContext.java
│       └── UIConstants.java
├── movies.txt                       # Movie data persistence
├── receipts.txt                     # Booking receipts
└── img/                             # Movie posters
```

### Design Patterns
- **Singleton**: ServiceContext for service management
- **MVC**: Model-View-Controller separation
- **Observer**: UI components listen to data changes
- **Factory**: Service instantiation

## 📊 Data Flow

1. **Movie Addition**: Admin → MovieService → movies.txt
2. **Show Scheduling**: Admin → ShowService → Show objects
3. **Seat Booking**: User → BookingService → Seat status update → receipts.txt
4. **Booking Cancellation**: Admin → BookingService → receipts.txt update

## 🔧 Configuration

### Seat Layout
- 4 rows (A-D) × 10 seats per row = 40 seats total
- 5 screens available (Screen 1-5)
- Price per seat: $14.00

### File Formats
- **movies.txt**: Tab-separated values (ID, Title, Duration, Genre, ImagePath)
- **receipts.txt**: Formatted receipt text with booking details

## 🧪 Testing

The application has been thoroughly tested for:
- ✅ Seat booking and persistence
- ✅ Booking cancellation (exact match)
- ✅ Show scheduling conflicts
- ✅ UI responsiveness
- ✅ Data persistence
- ✅ Error handling

## 📈 Future Enhancements

- User account system
- Payment integration
- Email notifications
- Advanced reporting
- Mobile app companion
- API endpoints for third-party integration

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make changes with proper documentation
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is developed for educational purposes as part of CSCI 217 – Advanced Computer Programming.

## 👥 Authors

- Developed as a comprehensive cinema management system demonstration

## 🙏 Acknowledgments

- Built with Java Swing for GUI
- Uses clean code principles and OOP design
- Inspired by real-world cinema booking systems