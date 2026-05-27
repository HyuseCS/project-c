# Phase 6b: Location-Based Reminders Frontend Plan

## 1. Design Vision & Aesthetic
This feature extends the app's core **"Lucid Lens"** aesthetic to spatial and map-based interfaces. The design embraces **Invisible Hierarchy** and **Tactile Glass**, ensuring the map feels immersive while controls remain crisp and authoritative.

- **Color Strategy:** Restrained. The UI is built on `Lucid White` (`#FCFCFF`) and `Lucid Gray`, with `Lucid Indigo` (`#6200EE`) functioning as the sole, high-contrast action color. 
- **Typography:** 800-weight headings (32sp) and generous line-heights drive the visual structure instead of bounding boxes.
- **Material:** The map serves as the z=0 base layer. Menus and inputs float above it using **Tactile Glass** surfaces (70% opacity, 20dp blur, subtle 1px translucent borders).

## 2. Screen Anatomy & Architecture

### A. Permission Education Flow (Onboarding)
*Context: Students are cautious about background location tracking.*
- **Layout:** A focused, full-screen view. 
- **Visual:** A softly glowing, pulsing map pin vector graphic centered in deep whitespace.
- **Copy:** 
  - **Headline:** "Never miss a beat."
  - **Body:** "Project C needs background location access to trigger your reminders precisely when you arrive on campus or at the store—even if your phone is in your pocket."
- **Action:** A massive, `Lucid Indigo` button ("Enable Location") with an ease-out-quint tap animation. Secondary text button ("Not Now").

### B. Reminders Dashboard
*Context: A unified hub integrated seamlessly into the Home/Utilities flow.*
- **Layout:** 
  - **Headline:** "Reminders" (800 weight, 32sp, tracking -0.02em).
  - **List:** Invisible containers. List items are not wrapped in cards. Instead, they are separated by 24dp vertical spacing.
- **Item Design:**
  - A left-aligned vertical indicator line mapped to the Immutable Importance level (e.g., subtle gray for Low, vibrant red/indigo for Urgent).
  - High-contrast Title and Location text.
- **Action:** A floating, glassy `+` FAB in the bottom right.

### C. Add/Edit Reminder Screen (The Spatial Hub)
*Context: A map-centric creation flow ensuring spatial precision.*
- **Base Layer:** Edge-to-edge Map view. Safe areas (status bar, navigation bar) are strictly respected for floating controls.
- **Map Interaction:** A fixed `Lucid Indigo` vector pin in the center of the screen. Users pan the map beneath the pin for exact placement. 
- **Floating Controls (Top):**
  - A glassy, circular "Back" button.
  - A glassy search pill for address autocomplete.
- **Persistent Bottom Panel (Tactile Glass):**
  - A non-modal sheet anchored to the bottom with 32px top-rounded corners.
  - **Header:** "New Reminder"
  - **Inputs:**
    - **Title & Description:** Borderless text fields. They rely on typography and bottom-strokes rather than rigid boxes.
    - **Radius Slider:** A custom slider with haptic feedback ticks at key intervals (100m, 250m, 500m). The radius circle on the map animates smoothly in tandem with the slider.
    - **Date & Time:** Tappable inline text triggering native pickers.
    - **Importance:** A horizontal segmented control (Low, Medium, High, Urgent).
  - **Monetization Limit:** 
    - If the user has reached the 3-per-day limit, an inline, high-contrast warning appears: "Daily limit of 3 reminders reached." 
    - The "Set Reminder" button enters a visually distinct disabled state (reduced opacity, no ripple) to prevent frustration.
  - **Action:** Full-width "Set Reminder" button.

### D. Global Settings Integration
- **Map Provider Toggle:** A clean, single-choice selector in the main Settings screen allowing users to switch between Google Maps and OpenStreetMap.

## 3. UI/UX Pro-Max Interaction Guidelines

- **Vector-Only Assets:** No emoji or raster icons. All pins, arrows, and glyphs must be crisp SVGs/VectorDrawables to scale perfectly and adapt to themes.
- **Touch Targets:** Enforce a strict minimum of 44x44pt for all interactive elements (back buttons, sliders, map controls).
- **Motion & Feedback:**
  - Use `ease-out-quint` or spring physics for all transitions (e.g., panel sliding up, map pin dropping).
  - Immediate ripple feedback on all buttons to acknowledge taps instantly.
- **Accessibility:**
  - **Contrast:** Ensure the `Lucid Indigo` action text (`#FFFFFF`) against the button base meets the 4.5:1 WCAG AA standard.
  - **Screen Reader Order:** Back -> Search -> Map -> Title -> Desc -> Radius -> Date/Time -> Importance -> Save.
- **Dark Mode Parity:**
  - The map component switches to a dark style.
  - The Tactile Glass panel shifts to a deep `Lucid Gray` (`#2B2B2B` or similar) with a 10% opacity white border, maintaining foreground legibility against the dark map.

## 4. Jetpack Compose (KMP) Implementation Strategy
1. **State Management:** Define `ReminderUiState` to manage map coordinates, form validation, and monetization limits.
2. **Custom Composables:**
   - `LucidGlassSurface(modifier, content)`: Reusable glassy container.
   - `ImportanceSegmentedControl(selected, onSelect)`: Tactile selector for priority levels.
3. **Map Abstraction:** Implement a `MapWrapper` composable that dynamically swaps the underlying map SDK based on the user's Global Settings choice.
4. **Integration:** Connect the UI strictly to the Local-First repository and the `evaluateTrigger` pure functions defined in Phase 6a.