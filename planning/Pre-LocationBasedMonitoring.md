# Pre-LocationBasedReminders Implementation Plans Summary

This file contains a consolidated summary of all implementation plans for ProjectC prior to Phase 6 (Location-Based Reminders).

---

## Phase 2: Firestore User Profiles
**Objective:** Integrate Firebase Firestore to store and retrieve user profile data (name, nickname, university, course).

### Key Components
- **Domain:** `UserProfile` model, `ProfileRepository` interface, and Use Cases (`GetProfile`, `SaveProfile`, `ObserveProfile`).
- **Data:** `ProfileRepositoryImpl` using GitLive Firestore SDK.
- **UI:** `ProfileViewModel` and `ProfileScreen` for profile management.
- **Onboarding:** Automatic redirection to profile setup if a new user's profile is missing.

---

## Phase 3: Utilities Hub & Electricity Bill Calculator
**Objective:** Introduce a centralized Utilities hub and a tracking/calculation system for electricity bills.

### Key Components
- **Domain:** `ElectricityBillResult` model and `CalculatorRepository` interface.
- **Data:** `CalculatorRepositoryImpl` storing records in `users/{uid}/electricity_bill_history/`.
- **UI:** `UtilitiesHubScreen` for navigation and `ElectricityBillScreen` for inputting meter readings and rates.
- **Features:** Auto-fill of previous readings/rates from history and a dedicated history view.

---

## Phase 3.1: Water Bill Calculator
**Objective:** Extend the Utilities Hub with a water consumption tracking system.

### Key Components
- **Domain:** `WaterBillResult` model.
- **Data:** Extension of `CalculatorRepositoryImpl` to handle `water_bill_history` subcollection.
- **UI:** `WaterBillScreen` and `WaterBillHistoryScreen` for water-specific tracking.

---

## Phase 4: Dashboard & Analytics
**Objective:** Implement a dynamic, component-based Dashboard Home Screen with visual analytics.

### Key Components
- **Architecture:** `DashboardWidget` sealed class for modular components (Electricity Graph, Quick Actions, etc.).
- **Visuals:** Custom `Canvas`-based electricity consumption bar charts.
- **Personalization:** Added "Nickname" support and time-of-day greetings ("Good Morning", etc.).
- **Logic:** `HomeViewModel` aggregates history into monthly data points for graphing.

---

## Phase 5 & 6: Expenses Tracker
**Objective:** Real-time expense tracking with category-based breakdowns and unified currency.

### Key Components
- **Domain:** `Expense` and `ExpenseCategory` models; `ExpenseRepository` for CRUD and observation.
- **Categories:** Hybrid system supporting system-defined and user-customizable categories.
- **Aggregation:** Real-time daily and monthly totals calculated in `ExpensesViewModel`.
- **Currency Sync:** Unified currency selection via the User Profile, propagated to all calculators and expense logs.
- **UI:** `ExpensesDashboardScreen` (list with sticky headers) and `AddExpenseScreen`.

---

## Phase 7: UI & UX Audit Fixes
**Objective:** Elevate the app's visual quality to a "Lucid Glass" aesthetic and resolve UX friction.

### Key Components
- **Styling:** Enhanced `LucidSurface` glassmorphism, removal of standard dividers, and improved typography hierarchy.
- **Contextual Dashboard:** Dynamic widget reordering (e.g., prioritizing bills at the end of the month) and time-aware greetings.
- **Dashboard Editor:** Replacement of legacy bottom sheets with a floating "Edit Hub" pill for managing widget visibility and order.
- **Data Viz:** Improved `Canvas` rendering with gradients and fully rounded bar charts.
- **Interaction:** Long-press "Quick Add" for expenses directly from the dashboard.

---
*Note: This summary excludes the Location-Based Reminders (Phase 6b) plan, which is documented separately in `LocationBasedReminders.md`.*
