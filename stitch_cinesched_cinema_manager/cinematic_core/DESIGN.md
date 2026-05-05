---
name: Cinematic Core
colors:
  surface: '#200e0c'
  surface-dim: '#200e0c'
  surface-bright: '#4a3330'
  surface-container-lowest: '#1a0908'
  surface-container-low: '#2a1614'
  surface-container: '#2e1a18'
  surface-container-high: '#3a2522'
  surface-container-highest: '#462f2c'
  on-surface: '#ffdad5'
  on-surface-variant: '#e9bcb6'
  inverse-surface: '#ffdad5'
  inverse-on-surface: '#412b28'
  outline: '#af8782'
  outline-variant: '#5e3f3b'
  surface-tint: '#ffb4aa'
  primary: '#ffb4aa'
  on-primary: '#690003'
  primary-container: '#e50914'
  on-primary-container: '#fff7f6'
  inverse-primary: '#c0000c'
  secondary: '#c7c6c6'
  on-secondary: '#303031'
  secondary-container: '#464747'
  on-secondary-container: '#b6b5b5'
  tertiary: '#a7c8ff'
  on-tertiary: '#003061'
  tertiary-container: '#0072d7'
  on-tertiary-container: '#f8f9ff'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#ffdad5'
  primary-fixed-dim: '#ffb4aa'
  on-primary-fixed: '#410001'
  on-primary-fixed-variant: '#930007'
  secondary-fixed: '#e4e2e2'
  secondary-fixed-dim: '#c7c6c6'
  on-secondary-fixed: '#1b1c1c'
  on-secondary-fixed-variant: '#464747'
  tertiary-fixed: '#d5e3ff'
  tertiary-fixed-dim: '#a7c8ff'
  on-tertiary-fixed: '#001b3c'
  on-tertiary-fixed-variant: '#004689'
  background: '#200e0c'
  on-background: '#ffdad5'
  surface-variant: '#462f2c'
typography:
  h1:
    fontFamily: Be Vietnam Pro
    fontSize: 40px
    fontWeight: '700'
    lineHeight: '1.2'
    letterSpacing: -0.02em
  h2:
    fontFamily: Be Vietnam Pro
    fontSize: 32px
    fontWeight: '600'
    lineHeight: '1.3'
  h3:
    fontFamily: Be Vietnam Pro
    fontSize: 24px
    fontWeight: '600'
    lineHeight: '1.4'
  body-lg:
    fontFamily: Inter
    fontSize: 18px
    fontWeight: '400'
    lineHeight: '1.6'
  body-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: '1.6'
  label-caps:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '700'
    lineHeight: '1.0'
    letterSpacing: 0.05em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  unit: 8px
  container-padding: 32px
  gutter: 24px
  sidebar-width: 280px
  section-gap: 48px
---

## Brand & Style

The design system is rooted in the immersive atmosphere of a physical theater, translating the "lights-down" experience into a high-end digital interface. The brand personality is premium, focused, and cinematic, prioritizing content—movie posters and showtimes—above all else. 

The design style utilizes **Modern Minimalism** with a touch of **Tonal Layering**. By using a deep charcoal foundation, the interface recedes to let the primary brand color and film imagery take center stage. The aesthetic avoids unnecessary decoration, relying instead on generous negative space and crisp typography to create a sense of luxury and ease of use.

## Colors

This design system utilizes a high-contrast dark mode palette to mirror the cinema environment. 

- **Primary:** Cinematic Red (#E50914) is used sparingly for primary actions, branding, and highlighting active states.
- **Backgrounds:** A deep charcoal (#0F0F0F) serves as the base layer, while a slightly lighter Slate Grey (#1A1A1A) is used for cards and grouped sections to create subtle depth.
- **Seat Map Logic:** 
    - **Available:** A vibrant emerald green to signify opportunity.
    - **Booked:** The primary red, indicating "occupied" or "unavailable."
    - **Selected:** A bright sunflower yellow for immediate visual feedback during the booking flow.
- **Typography Colors:** Pure white (#FFFFFF) for headings and a muted slate (#A0A0A0) for secondary body text to reduce eye strain.

## Typography

The typography strategy pairs the contemporary, slightly geometric personality of **Be Vietnam Pro** for headlines with the functional precision of **Inter** for UI elements and body copy. 

- **Headlines:** Set with tight letter-spacing and bold weights to evoke movie title aesthetics.
- **Body:** Focused on legibility within the dark theme; using Inter ensures high readability for movie synopses and scheduling details.
- **Labels:** Small caps are used for metadata (e.g., Rating, Duration, Genre) to create a clear structural hierarchy without cluttering the layout.

## Layout & Spacing

The layout follows a **Fixed Sidebar / Fluid Content** model for desktop, transitioning to a bottom-navigation or "hamburger" drawer for mobile.

- **Grid:** A 12-column grid is used for the main content area with 24px gutters.
- **Rhythm:** An 8px linear scale governs all padding and margins. 
- **Grouping:** Related information (like showtimes for a specific theater) should be grouped into cards with 48px of vertical separation between distinct movie entries to ensure the interface feels "airy" despite the dark palette.
- **Navigation:** The sidebar is docked to the left, using 32px of internal padding for a spacious, breathable list of menu items.

## Elevation & Depth

Depth is achieved through **Tonal Layering** and **Ambient Shadows** rather than heavy borders.

- **Z-0 (Background):** Deep Charcoal (#0F0F0F).
- **Z-1 (Cards/Sidebar):** Slate Grey (#1A1A1A).
- **Shadows:** Elements on Z-1 utilize a very soft, large-radius shadow (Blur: 20px, Spread: 0, Opacity: 40% Black) to lift them off the background without creating "glow" artifacts.
- **Overlays:** Modals and seat-selection drawers use a 60% background blur (Glassmorphism) to maintain the cinematic context of the underlying screen while focusing the user's attention.

## Shapes

This design system uses a **Rounded** shape language to soften the industrial feel of the charcoal palette.

- **Standard Radius:** 8px (0.5rem) for small components like buttons and input fields.
- **Large Radius:** 16px (1rem) for movie posters, container cards, and the sidebar background.
- **Interactive Elements:** Buttons should never be fully pill-shaped; they maintain a 8px radius to feel modern and structural.
- **Seat Map:** Seats are rendered as squares with a 4px radius, providing a tactile, grid-like appearance that feels organized.

## Components

- **Buttons:** Primary buttons are solid Cinematic Red with white text. Secondary buttons are ghost-style with a Slate Grey border. High-tap-area padding (12px 24px).
- **Movie Cards:** High-aspect-ratio (2:3) images with 16px border radius. Titles appear below the card or as a subtle gradient overlay at the bottom.
- **Seat Picker:** A stylized grid where "Selected" seats (Yellow) feature a subtle outer glow to indicate the active focus.
- **Showtime Chips:** Small, 8px rounded rectangles. Available times are Slate Grey with white text; selected times flip to Cinematic Red.
- **Sidebar:** Icons are stroke-based (2px weight) for a clean, architectural look. The active state is indicated by a vertical red bar on the far left edge of the menu item.
- **Inputs:** Darker than the surface color (#121212) with a subtle 1px border that turns Red on focus.