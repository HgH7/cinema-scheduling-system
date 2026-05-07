# Cinema Scheduling System - Refactor Change Log

## Scope

This document records all refactors completed in this session.
Goals were to reduce duplicate code/files, improve naming clarity, keep UI simple/maintainable, and preserve existing behavior.

## 1) Full project review

- Reviewed all markdown documentation:
  - `README.md`
  - `TECHNICAL_DOCUMENTATION.md`
  - `UML_DIAGRAMS.md`
- Reviewed all Java source files in `src/` and data files:
  - `movies.txt`
  - `receipts.txt`

## 2) Admin analytics/dashboard consolidation

### What changed

- Combined separate admin sales + dashboard experiences into one screen.
- Removed redundant dashboard file and moved dashboard metrics into the analytics panel.
- Updated admin sidebar navigation to open one combined analytics dashboard page.

### Functional result

- Admin still has access to:
  - sales by movie
  - sales by date
  - total revenue
  - total tickets
  - total bookings
  - average price per booking
  - top movie
  - movie popularity ranking
- Data source remains `receipts.txt`.

### Files affected

- Updated (old names):
  - `src/ui/AdminUI.java`
  - `src/ui/AdminSalesPanel.java`
- Deleted:
  - `src/ui/DashboardPanel.java`

## 3) Exception/util consolidation

### What changed

- Merged custom exception classes into `InputValidator` as nested static classes.
- Removed separate exception files/folder usage.

### Files affected

- Updated:
  - `src/util/InputValidator.java`
- Deleted:
  - `src/exception/CinemaException.java`
  - `src/exception/ValidationException.java`
  - `src/exception/BookingException.java`
  - `src/exception/ShowSchedulingException.java`

## 4) UI cleanup and deduplication

### What changed

- Introduced shared UI styling/helper utility to reduce repeated UI code.
- Replaced duplicated table/button/field styling and label+component wrapper code across multiple UI screens.
- Removed unused imports and duplicated helper methods.

### Files affected (after rename)

- Added:
  - `src/ui/UIStyles.java`
- Updated:
  - `src/ui/AdminMovieManagementPanel.java`
  - `src/ui/AdminShowManagementPanel.java`
  - `src/ui/AdminBookingManagementPanel.java`
  - `src/ui/AdminAnalyticsDashboardPanel.java`
  - `src/ui/AdminDashboard.java`
  - `src/ui/UserHomeScreen.java`
  - `src/ui/UserSeatSelectionScreen.java`

## 5) UI class/file renaming for clarity

All UI file/class names were changed to match functionality.

### Rename map

- `MainMenuUI` -> `AppMainMenu`
- `AdminUI` -> `AdminDashboard`
- `UserUI` -> `UserHomeScreen`
- `MovieBrowsingPanel` -> `UserMovieCatalogPanel`
- `SeatSelectionUI` -> `UserSeatSelectionScreen`
- `AdminMoviesPanel` -> `AdminMovieManagementPanel`
- `AdminShowsPanel` -> `AdminShowManagementPanel`
- `AdminBookingsPanel` -> `AdminBookingManagementPanel`
- `AdminSalesPanel` -> `AdminAnalyticsDashboardPanel`
- `ServiceContext` -> `ApplicationServices`
- `UIConstants` -> `UITheme`
- `UIStyle` -> `UIStyles`

### Reference updates

- Updated all imports/usages in `src` to the new names.
- Updated entry point usage in `src/Main.java` to call `AppMainMenu`.

## Behavior preservation notes

- Service-layer behavior was not moved into UI.
- Booking, movie management, show scheduling, and receipt-writing flows were preserved.
- UI visual style and interactions were kept consistent while reducing duplication.

## Validation performed

- Linter checks run on edited UI and related files; no linter errors reported.
- Symbol/reference checks run to ensure old UI class names were replaced.
- Compile/run could not be fully executed in this environment because Java runtime is not installed (`Unable to locate a Java Runtime`).
