# Tech README

هذا الملف يشرح كيف يعمل المشروع من الداخل and how the classes are connected.

## Project Goal

The project is a cinema scheduling and booking system built in Java Swing. الهدف هو فصل واجهة المستخدم عن المنطق التجاري وحفظ البيانات في ملفات نصية بسيطة.

## Main Flow

1. `Main.java` يبدأ التطبيق وينادي `AppMainMenu.startApplication()`.
2. `AppMainMenu` يفتح نافذة Swing رئيسية ويعرض خيارين: User Side و Admin Side.
3. عند اختيار أي وضع، يتم فتح الشاشة المناسبة من `src/ui/`.

## Core Services

### `CinemaServiceManager`

- This is the central singleton service registry.
- يحتوي على مثيل من:
  - `PersistenceService`
  - `MovieService`
  - `ShowService`
  - `BookingService`
  - `ReceiptService`
  - `AnalyticsService`
- It also seeds default movies and screens, ثم يحمل عروض saved from `shows.txt`.
- UI components can use `CinemaServiceManager.getInstance()` to get shared services.

### `PersistenceService`

- Handles file I/O for:
  - `movies.txt`
  - `shows.txt`
  - `receipts.txt`
- `loadMovies()` reads lines with `id|title|duration|genre|imagePath`.
- `saveMovies()` writes the same format.
- `saveShows()` writes show entries as `id|movieId|screenId|showTime`.
- `saveReceipt()` appends receipt text to `receipts.txt`.

## Movie and Show Management

### `MovieService`

- Holds a list of `Movie` objects in memory.
- `loadMovies()` loads from persistence.
- `addMovie(...)` creates and stores a new movie.
- `removeMovie(id)` deletes a movie by ID.
- `getAllMovies()` returns a copy of the movie list.

### `ShowService`

- Holds scheduled `Show` objects.
- `addShow(...)` creates a new show only if no screen-time conflict exists.
- `loadShows(movieService, screens)` rebuilds shows from `shows.txt` using saved movie IDs and screen IDs.
- `hasTimeConflict(...)` compares show start and end times to avoid overlapping bookings on the same screen.

## Booking and Receipts

### `BookingService`

- Manages bookings and seat reservations.
- `createBooking(...)` checks each seat for availability, marks them booked, and saves a receipt.
- It also notifies registered listeners after data changes so UI elements can refresh.
- `cancelBooking(...)` can free seats again and remove saved receipts when needed.

### `ReceiptService`

- Builds a text receipt in a human-readable layout.
- `generateAndSaveReceipt()` writes the receipt to `receipts.txt`.
- `getAllReceiptData()` reads saved receipts and parses them into structured data.
- This is used to restore booking state when the app starts again.

## Models

### `model/Movie.java`
- Contains movie metadata: id, title, duration, genre, image path.

### `model/Show.java`
- Connects a `Movie` to a `Screen` and a showtime.
- Contains seat layout and booking status for that show.

### `model/Booking.java`
- Represents one booking transaction with selected seats and user name.
- Computes the total price.

### `model/Seat.java` and `model/SeatStatus.java`
- `Seat` contains row, number, price, and status.
- `SeatStatus` is an enum for `AVAILABLE`, `BOOKED`, etc.

### `model/Screen.java`
- Defines a screen number, name, and dimensions.
- Used by `ShowService` and `Show` to build seat maps.

## UI Layer

- All Swing screens are in `src/ui/`.
- `AppMainMenu` is the launcher screen.
- `UserHomeScreen` and admin panels provide user interaction.
- UI classes call services to get data and execute operations.

## Data Files

- `movies.txt` stores movie records.
- `shows.txt` stores scheduled show records.
- `receipts.txt` stores generated receipts as plain text.

## Interaction Summary

- The UI does not handle persistence directly.
- UI classes call service methods like `movieService.getAllMovies()` and `bookingService.createBooking()`.
- Services use `PersistenceService` for reading and writing files.
- `CinemaServiceManager` connects all services in one place.

## How the AI structured this project

- The AI made the project modular by separating responsibilities.
- UI is kept in `src/ui`, business logic in `src/service`, and domain data in `src/model`.
- This design simplifies maintenance and makes it easier to extend features later.

## Notes for creators

- إذا أردتم توسعة المشروع، ركزوا أولاً على `service/` لأن هناك تتم جميع قواعد العمل.
- لو حابين تضيفوا أنواع جديدة من الإحصائيات أو تقارير، `AnalyticsService` هو المكان المناسب.
- لتغيير طريقة حفظ البيانات، عدلوا `PersistenceService` فقط، والباقي سيستمر بالعمل بنفس الواجهة.
