# Graph Report - .  (2026-08-02)

## Corpus Check
- 10 files · ~34,430 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 275 nodes · 512 edges · 34 communities (19 shown, 15 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 67 edges (avg confidence: 0.83)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- [[_COMMUNITY_Dashboard & Calculators|Dashboard & Calculators]]
- [[_COMMUNITY_Reminders UI & Notifications|Reminders UI & Notifications]]
- [[_COMMUNITY_Profile, Geocoding & Triggers|Profile, Geocoding & Triggers]]
- [[_COMMUNITY_Auth, Predictor & Bill Screens|Auth, Predictor & Bill Screens]]
- [[_COMMUNITY_Expense Management|Expense Management]]
- [[_COMMUNITY_UI Components & Theme|UI Components & Theme]]
- [[_COMMUNITY_Geofencing & Scheduling Backend|Geofencing & Scheduling Backend]]
- [[_COMMUNITY_App Shell & Navigation|App Shell & Navigation]]
- [[_COMMUNITY_Bill ViewModels & Save Flow|Bill ViewModels & Save Flow]]
- [[_COMMUNITY_Authentication Flow|Authentication Flow]]
- [[_COMMUNITY_KMP Platform Layer|KMP Platform Layer]]
- [[_COMMUNITY_Project Context & Docs|Project Context & Docs]]
- [[_COMMUNITY_Design Language|Design Language]]
- [[_COMMUNITY_Tests|Tests]]
- [[_COMMUNITY_Location Reminder Plans|Location Reminder Plans]]
- [[_COMMUNITY_Design System Files|Design System Files]]
- [[_COMMUNITY_App Build Configuration|App Build Configuration]]
- [[_COMMUNITY_Compose Multiplatform|Compose Multiplatform]]
- [[_COMMUNITY_Clean Architecture|Clean Architecture]]
- [[_COMMUNITY_Reminder System|Reminder System]]
- [[_COMMUNITY_ProjectC Application|ProjectC Application]]
- [[_COMMUNITY_Firestore Security Gaps|Firestore Security Gaps]]
- [[_COMMUNITY_ViewModel Sharing Issue|ViewModel Sharing Issue]]
- [[_COMMUNITY_Profile Save Bug|Profile Save Bug]]
- [[_COMMUNITY_Uncancelled Observations|Uncancelled Observations]]
- [[_COMMUNITY_Dual-Trigger Architecture|Dual-Trigger Architecture]]
- [[_COMMUNITY_Monetization Limit|Monetization Limit]]
- [[_COMMUNITY_Liquid Glass Style|Liquid Glass Style]]
- [[_COMMUNITY_Launcher Icon|Launcher Icon]]

## God Nodes (most connected - your core abstractions)
1. `HomeViewModel` - 29 edges
2. `NavGraph()` - 25 edges
3. `ElectricityBillViewModel` - 22 edges
4. `ProfileViewModel` - 18 edges
5. `Error` - 17 edges
6. `RemindersViewModel` - 15 edges
7. `Reminder` - 14 edges
8. `ExpensesViewModel` - 14 edges
9. `HomeScreen` - 14 edges
10. `CalculatorRepository` - 12 edges

## Surprising Connections (you probably didn't know these)
- `Play Services Geofencing API Integration` --references--> `GeofenceManager`  [EXTRACTED]
  memory.md → shared/src/commonMain/kotlin/com/hyuse/projectc/domain/repository/GeofenceManager.kt
- `Play Services Geofencing API Integration` --references--> `NotificationHelper`  [EXTRACTED]
  memory.md → composeApp/src/androidMain/kotlin/com/hyuse/projectc/platform/notification/NotificationHelper.kt
- `Play Services Geofencing API Integration` --references--> `RemindersScreen`  [EXTRACTED]
  memory.md → composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/reminders/RemindersScreen.kt
- `CategoryManagerScreen` --shares_data_with--> `ExpenseCategory`  [INFERRED]
  composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/expenses/CategoryManagerScreen.kt → shared/src/commonMain/kotlin/com/hyuse/projectc/domain/model/ExpenseCategory.kt
- `Play Services Geofencing API Integration` --references--> `ReminderRepository`  [EXTRACTED]
  memory.md → shared/src/commonMain/kotlin/com/hyuse/projectc/domain/repository/ReminderRepository.kt

## Hyperedges (group relationships)
- **Reminder Authoring UI Flow** — navigation_navgraph_navgraph, reminders_remindersscreen_remindersscreen, reminders_addreminderscreen_addreminderscreen, reminders_permissionscreen_permissionscreen, reminders_addreminderscreen_remindermapdialog, reminders_remindersviewmodel_remindersviewmodel [EXTRACTED 0.95]
- **Reminder Triggering Pipeline** — repository_geofencemanager_geofencemanager, geofencing_geofencemanagerimpl_geofencemanagerimpl, geofencing_geofencebroadcastreceiver_geofencebroadcastreceiver, worker_remindertimeworker_remindertimeworker [INFERRED 0.85]

## Communities (34 total, 15 thin omitted)

### Community 0 - "Dashboard & Calculators"
Cohesion: 0.12
Nodes (25): CalculateElectricityBillUseCase, CalculateWaterBillUseCase, CalculatorRepository, ElectricityBillResult, GetCurrentUserUseCase, GetElectricityBillHistoryUseCase, GetWaterBillHistoryUseCase, ActionItem (+17 more)

### Community 1 - "Reminders UI & Notifications"
Cohesion: 0.13
Nodes (19): AddReminderScreen, registerGeofences, LocationData, Reminder, NotificationHelper, Location-Based Reminders Refactor Progress, Play Services Geofencing API Integration, Daily Reminder Limit Policy (+11 more)

### Community 2 - "Profile, Geocoding & Triggers"
Cohesion: 0.11
Nodes (20): AddressSearchDialog, sharedModule, EvaluateTriggerUseCase, GetProfileUseCase, PhotonFeature, PhotonGeometry, PhotonProperties, PhotonResponse (+12 more)

### Community 3 - "Auth, Predictor & Bill Screens"
Cohesion: 0.15
Nodes (22): LoginScreen, SignUpScreen, AuthState, ElectricityAppliance, ElectricityPredictorViewModel, PredictorSummary, NavGraph(), AddApplianceDialog (+14 more)

### Community 4 - "Expense Management"
Cohesion: 0.13
Nodes (9): AddExpenseUseCase, DeleteExpenseUseCase, Expense, ExpenseCategory, ExpenseRepository, DayExpenses, ExpensesViewModel, GetMergedCategoriesUseCase (+1 more)

### Community 5 - "UI Components & Theme"
Cohesion: 0.11
Nodes (20): AppConstants, CategoryManagerScreen, ExpensesDashboardScreen, LucidDayHeader, LucidExpenseItem, MonthSelectorLucid, SummaryHero, ExpensesState (+12 more)

### Community 6 - "Geofencing & Scheduling Backend"
Cohesion: 0.14
Nodes (15): BootCompletedReceiver, DatabaseDriverFactory, appModule, Geofence Capacity Management, Geofence Debouncing Mechanism, Geofence Persistence Safety Net, GeofenceBroadcastReceiver, GeofenceManager (+7 more)

### Community 7 - "App Shell & Navigation"
Cohesion: 0.16
Nodes (15): App, Gradle Plugins Configuration, LocalDrawerState, LucidBottomNavigation(), ContentView, ContentView_Previews, iOSApp, BottomNavItem (+7 more)

### Community 8 - "Bill ViewModels & Save Flow"
Cohesion: 0.23
Nodes (10): Error, ObserveProfileUseCase, Idle, SaveSuccess, ElectricBillUiState, ElectricityBillViewModel, Calculated, ConfirmOverwrite (+2 more)

### Community 9 - "Authentication Flow"
Cohesion: 0.21
Nodes (9): Authenticated, Loading, Unauthenticated, AuthRepository, AuthViewModel, LoginUseCase, LogoutUseCase, SignUpUseCase (+1 more)

### Community 10 - "KMP Platform Layer"
Cohesion: 0.31
Nodes (5): Greeting, AndroidPlatform, getPlatform, IOSPlatform, Platform

### Community 11 - "Project Context & Docs"
Cohesion: 0.5
Nodes (5): ADB Install Workflow, GEMINI.md, Kotlin Multiplatform App, Pixel 6a Test Device, Project C Tech Stack

### Community 12 - "Design Language"
Cohesion: 0.5
Nodes (4): Deep OLED Black Theme, Lucid Glass Component Style, Lucid Gold Color Scheme, Mobile Cinema OLED Aesthetic

### Community 14 - "Location Reminder Plans"
Cohesion: 0.67
Nodes (3): Location-Based Reminders Frontend Plan, Location-Based Reminders Implementation Plan, Pre-LocationBasedReminders Implementation Plans Summary

## Knowledge Gaps
- **48 isolated node(s):** `Unauthenticated`, `TriggerEvent`, `TriggerAction`, `ComposeApp Build Configuration`, `ProjectCApplication` (+43 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **15 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `NavGraph()` connect `Auth, Predictor & Bill Screens` to `Dashboard & Calculators`, `Reminders UI & Notifications`, `Profile, Geocoding & Triggers`, `Expense Management`, `UI Components & Theme`, `App Shell & Navigation`, `Bill ViewModels & Save Flow`, `Authentication Flow`?**
  _High betweenness centrality (0.249) - this node is a cross-community bridge._
- **Why does `HomeViewModel` connect `Dashboard & Calculators` to `Profile, Geocoding & Triggers`, `Auth, Predictor & Bill Screens`, `Expense Management`, `UI Components & Theme`, `Geofencing & Scheduling Backend`, `Bill ViewModels & Save Flow`?**
  _High betweenness centrality (0.137) - this node is a cross-community bridge._
- **Why does `appModule` connect `Geofencing & Scheduling Backend` to `Dashboard & Calculators`, `Reminders UI & Notifications`, `Profile, Geocoding & Triggers`, `Auth, Predictor & Bill Screens`, `Expense Management`, `Bill ViewModels & Save Flow`, `Authentication Flow`?**
  _High betweenness centrality (0.099) - this node is a cross-community bridge._
- **What connects `Unauthenticated`, `TriggerEvent`, `TriggerAction` to the rest of the system?**
  _48 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Dashboard & Calculators` be split into smaller, more focused modules?**
  _Cohesion score 0.12 - nodes in this community are weakly interconnected._
- **Should `Reminders UI & Notifications` be split into smaller, more focused modules?**
  _Cohesion score 0.13 - nodes in this community are weakly interconnected._
- **Should `Profile, Geocoding & Triggers` be split into smaller, more focused modules?**
  _Cohesion score 0.11 - nodes in this community are weakly interconnected._