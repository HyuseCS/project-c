
# Phase 6: Location-Based Reminders - Implementation Plan (Revised)

## Objective
Implement a production-ready, location-based reminder system triggered by Geofencing. The system uses a local-first architecture to ensure offline reliability and robust Android background execution. It employs a dual-trigger architecture (geofence + time) to accurately handle lingering presence and features a scalable geofence manager to respect OS limits.

## Key Features & Requirements
1.  **Monetization Limit:** Free tier users are strictly limited to a maximum of **3 location-based reminders per day**.
2.  **Reminder Input:**
    - Title & Description.
    - Location: Map pin (Map provider selection moved to **Global Settings** to reduce friction).
    - Radius: User-defined trigger distance.
    - Date & Time.
    - Immutable Importance Level.
3.  **Immutable Importance System:**
    - **Low:** Silent notification.
    - **Medium:** Standard notification.
    - **High:** Heads-up notification + sound.
    - **Urgent:** Full interruption / DND bypass.
4.  **Robust Trigger Logic:**
    - **Date Validation:** Reminders only trigger on or after their specified date.
    - **Already Inside:** Validates `isUserInsideGeofence()` at creation. If true, triggers/schedules immediately.
    - **Late Arrival:** Entering the geofence *after* the reminder time triggers a high/urgent notification.

## 1. Domain Layer (`shared/src/commonMain`)
- **Models:**
    - `Reminder` data class (includes `lastTriggered` timestamp to prevent flapping, and a `geofenceId`).
    - `ReminderImportance` enum (Low, Medium, High, Urgent).
    - `LocationData` data class (Lat, Lng, Radius).
- **Core Logic (Pure Functions):**
    - `evaluateTrigger(reminder, currentTime, currentEvent)`: Pure function to dictate notification behavior across all triggers (UI, Geofence, Time).
- **Interfaces:**
    - `ReminderRepository`: For CRUD operations.
    - `GeofenceManager`: Abstraction for registering/unregistering grouped geofences.

## 2. Data Layer (`shared/src/commonMain`)
- **Local-First Architecture (Single Source of Truth):**
    - Implement a Local Database (e.g., SQLDelight for KMP or Room for Android) to cache all active reminders.
    - Firestore serves *only* as a sync layer, completely removed from the background execution path.
- **Many-to-One Geofencing:**
    - Group reminders by location/radius. `1 Geofence -> N Reminders`.

## 3. UI Layer (`composeApp/src/androidMain`)
- **Permission Education Flow:**
    - Dedicated onboarding UI explaining *why* background location is needed before redirecting the user to Android 11+ System Settings.
- **Add/Edit Reminder Screen:**
    - Standard input fields (no map provider toggle here).
    - **Monetization Check:** UI validation to prevent creating a 4th reminder for the same day.
- **Global Settings:**
    - Map provider toggle (Google Maps vs. OpenStreetMap).

## 4. Android Platform Implementation (`composeApp/src/androidMain`)
- **Dynamic Geofence Manager:**
    - Enforces the ~100 active geofence OS limit by dynamically registering only the closest/soonest ~90 geofences based on user movement and schedule.
- **Dual Trigger Architecture:**
    1.  **Geofence Trigger (Early Entry):** Receiver triggers -> Queries Local DB -> Dispatches Silent Notification -> Schedules Time-based Alarm.
    2.  **Time-Based Trigger (Presence Aware):** AlarmManager/WorkManager fires at exact reminder time -> Checks if user is still inside the geofence -> Dispatches Audible Notification.
- **Background Reliability:**
    - **Foreground Service Strategy:** Use a persistent foreground service (or highly optimized Worker) initialized by the BroadcastReceiver to handle geofence processing, protecting against OEM battery optimization kills.
    - **Reboot Persistence:** `BOOT_COMPLETED` receiver reloads reminders from the Local DB and re-registers active geofences.
- **Flapping Prevention:**
    - Enforce cooldown windows and update `lastTriggered` in the Local DB upon firing to prevent duplicate boundary-crossing notifications.

## Verification & Testing
- **Unit Tests:** Verify `evaluateTrigger` with early, on-time, and late scenarios.
- **Integration Tests:** Test Local DB CRUD and 1-to-N geofence grouping logic.
- **Device Testing:**
    - Simulate reboot (`adb shell am broadcast -a android.intent.action.BOOT_COMPLETED`).
    - Test long-lingering states (enter early, wait for time trigger).
    - Test offline geofence execution.