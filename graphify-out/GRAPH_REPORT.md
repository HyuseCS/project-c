# Graph Report - .  (2026-05-29)

## Corpus Check
- Corpus is ~30,747 words - fits in a single context window. You may not need a graph.

## Summary
- 563 nodes · 553 edges · 128 communities (43 shown, 85 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 74 edges (avg confidence: 0.82)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]
- [[_COMMUNITY_Components| Components]]

## God Nodes (most connected - your core abstractions)
1. `NavGraph()` - 12 edges
2. `CalculatorRepositoryImpl` - 11 edges
3. `HomeScreen` - 11 edges
4. `HomeScreen()` - 9 edges
5. `CalculatorRepository` - 9 edges
6. `ExpenseRepositoryImpl` - 9 edges
7. `ElectricityBillViewModel` - 8 edges
8. `WaterBillViewModel` - 8 edges
9. `ExpensesViewModel` - 8 edges
10. `NavGraph` - 8 edges

## Surprising Connections (you probably didn't know these)
- `NavGraph` --rationale_for--> `Unshared ViewModel Instances`  [INFERRED]
  composeApp/src/androidMain/kotlin/com/hyuse/projectc/navigation/NavGraph.kt → AUDIT.md
- `HomeViewModel` --rationale_for--> `Uncancelled Observations`  [INFERRED]
  composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/home/HomeViewModel.kt → AUDIT.md
- `LucidSurface` --conceptually_related_to--> `Liquid Glass Style`  [INFERRED]
  composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/theme/Theme.kt → design-system/projectc/MASTER.md
- `ProjectCApplication` --rationale_for--> `Clean Architecture`  [INFERRED]
  composeApp/src/androidMain/kotlin/com/hyuse/projectc/ProjectCApplication.kt → GEMINI.md
- `ElectricityPredictorViewModel` --conceptually_related_to--> `Utilities Hub`  [INFERRED]
  composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/utilities/predictor/ElectricityPredictorViewModel.kt → memory.md

## Hyperedges (group relationships)
- **HomeScreen Components** — homescreen_welcomehero, homescreen_usageinsights, homescreen_spendingoverview, homescreen_activityfeed, homescreen_quickactionslucid, homescreen_dashboardeditorcontent, homescreen_inlinequickaddexpense [EXTRACTED 1.00]
- **Reminder Geofencing Flow** — remindersviewmodel_remindersviewmodel, geofencemanagerimpl_geofencemanagerimpl, geofencebroadcastreceiver_geofencebroadcastreceiver, remindertimeworker_remindertimeworker [INFERRED 0.85]
- **Reminder System Components** — reminder_reminder, reminderrepository_reminderrepository, geofencemanager_geofencemanager, evaluatetriggerusecase_evaluatetriggerusecase, bootcompletedreceiver_bootcompletedreceiver [INFERRED 0.95]

## Communities (128 total, 85 thin omitted)

### Community 0 - " Components"
Cohesion: 0.08
Nodes (19): ActionItem, DashboardWidget, ElectricityGraphWidget, ExpenseSummary, ExpenseSummaryWidget, HomeState, HomeViewModel, MonthlyUsage (+11 more)

### Community 1 - " Components"
Cohesion: 0.08
Nodes (33): AddReminderScreen, App(), appModule, Uncancelled Observations, Unshared ViewModel Instances, Clean Architecture, GeofenceManagerImpl, ActivityFeed (+25 more)

### Community 2 - " Components"
Cohesion: 0.1
Nodes (20): LoginScreen(), SignUpScreen(), NavGraph(), Routes, ProfileScreen(), rememberStringState(), ElectricityBillHistoryScreen(), formatNumber() (+12 more)

### Community 3 - " Components"
Cohesion: 0.08
Nodes (12): ElectricityAppliance, ElectricityPredictorViewModel, PredictorSummary, ObserveProfileUseCase, Calculated, ConfirmOverwrite, ElectricBillUiState, ElectricityBillViewModel (+4 more)

### Community 4 - " Components"
Cohesion: 0.11
Nodes (10): DayExpenses, Error, ExpensesState, ExpensesViewModel, Loading, Success, ExpenseCategory, DeleteExpenseUseCase (+2 more)

### Community 5 - " Components"
Cohesion: 0.12
Nodes (9): LocationData, Reminder, Error, Idle, RemindersUiState, RemindersViewModel, Saving, Success (+1 more)

### Community 6 - " Components"
Cohesion: 0.16
Nodes (16): ExpensesDashboardScreen(), LucidDayHeader(), LucidExpenseItem(), MonthSelectorLucid(), SummaryHero(), ActivityFeed(), DashboardEditorContent(), HomeScreen() (+8 more)

### Community 7 - " Components"
Cohesion: 0.13
Nodes (9): Authenticated, AuthState, AuthViewModel, Error, Loading, Unauthenticated, LoginUseCase, LogoutUseCase (+1 more)

### Community 8 - " Components"
Cohesion: 0.15
Nodes (9): UserProfile, Error, Idle, Loading, ProfileState, ProfileViewModel, SaveSuccess, Success (+1 more)

### Community 9 - " Components"
Cohesion: 0.17
Nodes (8): Calculated, ConfirmOverwrite, Error, Idle, SaveSuccess, Saving, WaterBillUiState, WaterBillViewModel

### Community 11 - " Components"
Cohesion: 0.2
Nodes (12): BootCompletedReceiver, DatabaseDriverFactory, DatabaseDriverFactory (Android), DatabaseDriverFactory (iOS), EvaluateTriggerUseCase, GeofenceManager, LocationData, Reminder (+4 more)

### Community 17 - " Components"
Cohesion: 0.29
Nodes (7): AddApplianceDialog, ApplianceItem, ElectricityPredictorScreen, PredictorHeader, ElectricityPredictorViewModel, PredictorSummary, Utilities Hub

### Community 18 - " Components"
Cohesion: 0.67
Nodes (5): AddApplianceDialog(), ApplianceItem(), ElectricityPredictorScreen(), formatNumber(), PredictorHeader()

### Community 19 - " Components"
Cohesion: 0.4
Nodes (4): ContentView, ContentView_Previews, PreviewProvider, View

### Community 25 - " Components"
Cohesion: 0.4
Nodes (5): CalculateElectricityBillUseCase, CalculatorRepository, CalculatorRepositoryImpl, ElectricityBillResult, WaterBillResult

### Community 26 - " Components"
Cohesion: 0.5
Nodes (5): AddExpenseUseCase, Expense, ExpenseCategory, ExpenseRepository, ExpenseRepositoryImpl

### Community 31 - " Components"
Cohesion: 0.4
Nodes (3): EvaluateTriggerUseCase, TriggerAction, TriggerEvent

### Community 53 - " Components"
Cohesion: 0.67
Nodes (3): HistoryItem, ResultCard, WaterBillScreen

### Community 55 - " Components"
Cohesion: 0.67
Nodes (3): Greeting, AndroidPlatform, Platform

### Community 56 - " Components"
Cohesion: 0.67
Nodes (3): ProfileRepository, ProfileRepositoryImpl, UserProfile

### Community 57 - " Components"
Cohesion: 0.67
Nodes (3): AuthRepository, AuthRepositoryImpl, User

### Community 60 - " Components"
Cohesion: 0.67
Nodes (3): Location-Based Reminders Frontend Plan, Location-Based Reminders Implementation Plan, Pre-LocationBasedReminders Implementation Plans Summary

## Knowledge Gaps
- **121 isolated node(s):** `AuthState`, `Loading`, `Unauthenticated`, `DashboardWidget`, `ProfileState` (+116 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **85 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ObserveProfileUseCase` connect ` Components` to ` Components`, ` Components`, ` Components`?**
  _High betweenness centrality (0.028) - this node is a cross-community bridge._
- **Why does `SaveProfileUseCase` connect ` Components` to ` Components`?**
  _High betweenness centrality (0.012) - this node is a cross-community bridge._
- **Are the 11 inferred relationships involving `NavGraph()` (e.g. with `App()` and `LoginScreen()`) actually correct?**
  _`NavGraph()` has 11 INFERRED edges - model-reasoned connections that need verification._
- **What connects `AuthState`, `Loading`, `Unauthenticated` to the rest of the system?**
  _121 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should ` Components` be split into smaller, more focused modules?**
  _Cohesion score 0.08 - nodes in this community are weakly interconnected._
- **Should ` Components` be split into smaller, more focused modules?**
  _Cohesion score 0.08 - nodes in this community are weakly interconnected._
- **Should ` Components` be split into smaller, more focused modules?**
  _Cohesion score 0.1 - nodes in this community are weakly interconnected._