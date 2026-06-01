# Graph Report - .  (2026-06-01)

## Corpus Check
- 125 files · ~0 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 611 nodes · 621 edges · 129 communities (44 shown, 85 thin omitted)
- Extraction: 85% EXTRACTED · 15% INFERRED · 0% AMBIGUOUS · INFERRED: 91 edges (avg confidence: 0.84)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- [[_COMMUNITY_UI Components & Navigation|UI Components & Navigation]]
- [[_COMMUNITY_App Core & Maps UI|App Core & Maps UI]]
- [[_COMMUNITY_Home Dashboard Logic|Home Dashboard Logic]]
- [[_COMMUNITY_Utility Predictors & Water Tracking|Utility Predictors & Water Tracking]]
- [[_COMMUNITY_Expense Management Logic|Expense Management Logic]]
- [[_COMMUNITY_Feature Screens & Theme|Feature Screens & Theme]]
- [[_COMMUNITY_Reminders Logic|Reminders Logic]]
- [[_COMMUNITY_Authentication Logic|Authentication Logic]]
- [[_COMMUNITY_Profile Management Logic|Profile Management Logic]]
- [[_COMMUNITY_Electricity Billing Logic|Electricity Billing Logic]]
- [[_COMMUNITY_Community 10|Community 10]]
- [[_COMMUNITY_Community 11|Community 11]]
- [[_COMMUNITY_Community 12|Community 12]]
- [[_COMMUNITY_Community 13|Community 13]]
- [[_COMMUNITY_Community 14|Community 14]]
- [[_COMMUNITY_Community 15|Community 15]]
- [[_COMMUNITY_Community 16|Community 16]]
- [[_COMMUNITY_Community 17|Community 17]]
- [[_COMMUNITY_Community 18|Community 18]]
- [[_COMMUNITY_Community 19|Community 19]]
- [[_COMMUNITY_Community 20|Community 20]]
- [[_COMMUNITY_Community 21|Community 21]]
- [[_COMMUNITY_Community 22|Community 22]]
- [[_COMMUNITY_Community 23|Community 23]]
- [[_COMMUNITY_Community 24|Community 24]]
- [[_COMMUNITY_Community 25|Community 25]]
- [[_COMMUNITY_Community 26|Community 26]]
- [[_COMMUNITY_Community 27|Community 27]]
- [[_COMMUNITY_Community 28|Community 28]]
- [[_COMMUNITY_Community 29|Community 29]]
- [[_COMMUNITY_Community 30|Community 30]]
- [[_COMMUNITY_Community 31|Community 31]]
- [[_COMMUNITY_Community 32|Community 32]]
- [[_COMMUNITY_Community 33|Community 33]]
- [[_COMMUNITY_Community 34|Community 34]]
- [[_COMMUNITY_Community 35|Community 35]]
- [[_COMMUNITY_Community 36|Community 36]]
- [[_COMMUNITY_Community 37|Community 37]]
- [[_COMMUNITY_Community 38|Community 38]]
- [[_COMMUNITY_Community 39|Community 39]]
- [[_COMMUNITY_Community 40|Community 40]]
- [[_COMMUNITY_Community 41|Community 41]]
- [[_COMMUNITY_Community 42|Community 42]]
- [[_COMMUNITY_Community 43|Community 43]]
- [[_COMMUNITY_Community 44|Community 44]]
- [[_COMMUNITY_Community 45|Community 45]]
- [[_COMMUNITY_Community 46|Community 46]]
- [[_COMMUNITY_Community 47|Community 47]]
- [[_COMMUNITY_Community 48|Community 48]]
- [[_COMMUNITY_Community 49|Community 49]]
- [[_COMMUNITY_Community 50|Community 50]]
- [[_COMMUNITY_Community 51|Community 51]]
- [[_COMMUNITY_Community 52|Community 52]]
- [[_COMMUNITY_Community 53|Community 53]]
- [[_COMMUNITY_Community 54|Community 54]]
- [[_COMMUNITY_Community 55|Community 55]]
- [[_COMMUNITY_Community 56|Community 56]]
- [[_COMMUNITY_Community 58|Community 58]]
- [[_COMMUNITY_Community 59|Community 59]]
- [[_COMMUNITY_Community 60|Community 60]]
- [[_COMMUNITY_Community 63|Community 63]]
- [[_COMMUNITY_Community 64|Community 64]]
- [[_COMMUNITY_Community 65|Community 65]]
- [[_COMMUNITY_Community 66|Community 66]]
- [[_COMMUNITY_Community 69|Community 69]]
- [[_COMMUNITY_Community 70|Community 70]]
- [[_COMMUNITY_Community 71|Community 71]]
- [[_COMMUNITY_Community 72|Community 72]]
- [[_COMMUNITY_Community 73|Community 73]]
- [[_COMMUNITY_Community 74|Community 74]]
- [[_COMMUNITY_Community 75|Community 75]]
- [[_COMMUNITY_Community 91|Community 91]]
- [[_COMMUNITY_Community 92|Community 92]]
- [[_COMMUNITY_Community 93|Community 93]]
- [[_COMMUNITY_Community 94|Community 94]]
- [[_COMMUNITY_Community 95|Community 95]]
- [[_COMMUNITY_Community 96|Community 96]]
- [[_COMMUNITY_Community 97|Community 97]]
- [[_COMMUNITY_Community 98|Community 98]]
- [[_COMMUNITY_Community 99|Community 99]]
- [[_COMMUNITY_Community 100|Community 100]]
- [[_COMMUNITY_Community 101|Community 101]]
- [[_COMMUNITY_Community 102|Community 102]]
- [[_COMMUNITY_Community 103|Community 103]]
- [[_COMMUNITY_Community 104|Community 104]]
- [[_COMMUNITY_Community 105|Community 105]]
- [[_COMMUNITY_Community 106|Community 106]]
- [[_COMMUNITY_Community 107|Community 107]]
- [[_COMMUNITY_Community 108|Community 108]]
- [[_COMMUNITY_Community 109|Community 109]]
- [[_COMMUNITY_Community 110|Community 110]]
- [[_COMMUNITY_Community 111|Community 111]]
- [[_COMMUNITY_Community 112|Community 112]]
- [[_COMMUNITY_Community 113|Community 113]]
- [[_COMMUNITY_Community 114|Community 114]]
- [[_COMMUNITY_Community 115|Community 115]]
- [[_COMMUNITY_Community 116|Community 116]]
- [[_COMMUNITY_Community 117|Community 117]]
- [[_COMMUNITY_Community 118|Community 118]]
- [[_COMMUNITY_Community 119|Community 119]]
- [[_COMMUNITY_Community 120|Community 120]]
- [[_COMMUNITY_Community 121|Community 121]]
- [[_COMMUNITY_Community 122|Community 122]]
- [[_COMMUNITY_Community 123|Community 123]]
- [[_COMMUNITY_Community 124|Community 124]]
- [[_COMMUNITY_Community 125|Community 125]]
- [[_COMMUNITY_Community 126|Community 126]]
- [[_COMMUNITY_Community 127|Community 127]]
- [[_COMMUNITY_Community 128|Community 128]]

## God Nodes (most connected - your core abstractions)
1. `NavGraph()` - 14 edges
2. `CalculatorRepositoryImpl` - 11 edges
3. `HomeScreen Composable` - 11 edges
4. `HomeScreen()` - 9 edges
5. `CalculatorRepository` - 9 edges
6. `ExpenseRepositoryImpl` - 9 edges
7. `NavGraph Composable` - 9 edges
8. `LucidSurface()` - 8 edges
9. `ElectricityBillViewModel` - 8 edges
10. `WaterBillViewModel` - 8 edges

## Surprising Connections (you probably didn't know these)
- `NavGraph Composable` --rationale_for--> `Unshared ViewModel Instances`  [INFERRED]
  composeApp/src/androidMain/kotlin/com/hyuse/projectc/navigation/NavGraph.kt → AUDIT.md
- `HomeViewModel` --rationale_for--> `Uncancelled Observations`  [INFERRED]
  composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/home/HomeViewModel.kt → AUDIT.md
- `LucidSurface` --conceptually_related_to--> `Liquid Glass Style`  [INFERRED]
  composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/theme/Theme.kt → design-system/projectc/MASTER.md
- `Lucid Glass Component Style` --rationale_for--> `LucidSurface Composable`  [INFERRED]
  DESIGN.md → composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/theme/LucidSurface.kt
- `ProjectCApplication` --rationale_for--> `Clean Architecture Layout`  [INFERRED]
  composeApp/src/androidMain/kotlin/com/hyuse/projectc/ProjectCApplication.kt → GEMINI.md

## Communities (129 total, 85 thin omitted)

### Community 0 - "UI Components & Navigation"
Cohesion: 0.07
Nodes (27): LoginScreen(), SignUpScreen(), AddressSearchDialog(), LucidGlassSurface(), NavGraph(), Routes, MapPinningDialog(), ProfileScreen() (+19 more)

### Community 1 - "App Core & Maps UI"
Cohesion: 0.07
Nodes (39): AddReminderScreen Composable, Dark Map Style JSON, App(), appModule, Uncancelled Observations, Unshared ViewModel Instances, Deep OLED Black Theme, Lucid Glass Component Style (+31 more)

### Community 2 - "Home Dashboard Logic"
Cohesion: 0.08
Nodes (19): ActionItem, DashboardWidget, ElectricityGraphWidget, ExpenseSummary, ExpenseSummaryWidget, HomeState, HomeViewModel, MonthlyUsage (+11 more)

### Community 3 - "Utility Predictors & Water Tracking"
Cohesion: 0.08
Nodes (12): ElectricityAppliance, ElectricityPredictorViewModel, PredictorSummary, ObserveProfileUseCase, Calculated, ConfirmOverwrite, Error, Idle (+4 more)

### Community 4 - "Expense Management Logic"
Cohesion: 0.11
Nodes (10): DayExpenses, Error, ExpensesState, ExpensesViewModel, Loading, Success, ExpenseCategory, DeleteExpenseUseCase (+2 more)

### Community 5 - "Feature Screens & Theme"
Cohesion: 0.14
Nodes (17): ExpensesDashboardScreen(), LucidDayHeader(), LucidExpenseItem(), MonthSelectorLucid(), SummaryHero(), ActivityFeed(), DashboardEditorContent(), HomeScreen() (+9 more)

### Community 6 - "Reminders Logic"
Cohesion: 0.12
Nodes (9): LocationData, Reminder, Error, Idle, RemindersUiState, RemindersViewModel, Saving, Success (+1 more)

### Community 7 - "Authentication Logic"
Cohesion: 0.13
Nodes (9): Authenticated, AuthState, AuthViewModel, Error, Loading, Unauthenticated, LoginUseCase, LogoutUseCase (+1 more)

### Community 8 - "Profile Management Logic"
Cohesion: 0.12
Nodes (10): UserProfile, Error, Idle, Loading, ProfileState, ProfileViewModel, SaveSuccess, Success (+2 more)

### Community 9 - "Electricity Billing Logic"
Cohesion: 0.17
Nodes (8): Calculated, ConfirmOverwrite, ElectricBillUiState, ElectricityBillViewModel, Error, Idle, SaveSuccess, Saving

### Community 12 - "Community 12"
Cohesion: 0.2
Nodes (12): BootCompletedReceiver, DatabaseDriverFactory, DatabaseDriverFactory (Android), DatabaseDriverFactory (iOS), EvaluateTriggerUseCase, GeofenceManager, LocationData, Reminder (+4 more)

### Community 18 - "Community 18"
Cohesion: 0.29
Nodes (7): AddApplianceDialog, ApplianceItem, ElectricityPredictorScreen, PredictorHeader, ElectricityPredictorViewModel, PredictorSummary, Utilities Hub

### Community 19 - "Community 19"
Cohesion: 0.67
Nodes (5): AddApplianceDialog(), ApplianceItem(), ElectricityPredictorScreen(), formatNumber(), PredictorHeader()

### Community 20 - "Community 20"
Cohesion: 0.53
Nodes (6): SQLDelight Database Configuration, Location-Based Reminders System, GeofenceBroadcastReceiver Class, GeofenceManagerImpl Class, NotificationHelper Object, ReminderTimeWorker Class

### Community 22 - "Community 22"
Cohesion: 0.33
Nodes (4): PhotonFeature, PhotonGeometry, PhotonProperties, PhotonResponse

### Community 23 - "Community 23"
Cohesion: 0.4
Nodes (4): ContentView, ContentView_Previews, PreviewProvider, View

### Community 29 - "Community 29"
Cohesion: 0.4
Nodes (5): CalculateElectricityBillUseCase, CalculatorRepository, CalculatorRepositoryImpl, ElectricityBillResult, WaterBillResult

### Community 30 - "Community 30"
Cohesion: 0.5
Nodes (5): AddExpenseUseCase, Expense, ExpenseCategory, ExpenseRepository, ExpenseRepositoryImpl

### Community 35 - "Community 35"
Cohesion: 0.4
Nodes (3): EvaluateTriggerUseCase, TriggerAction, TriggerEvent

### Community 56 - "Community 56"
Cohesion: 0.67
Nodes (3): HistoryItem, ResultCard, WaterBillScreen

### Community 58 - "Community 58"
Cohesion: 0.67
Nodes (3): Greeting, AndroidPlatform, Platform

### Community 59 - "Community 59"
Cohesion: 0.67
Nodes (3): ProfileRepository, ProfileRepositoryImpl, UserProfile

### Community 60 - "Community 60"
Cohesion: 0.67
Nodes (3): AuthRepository, AuthRepositoryImpl, User

### Community 63 - "Community 63"
Cohesion: 0.67
Nodes (3): Location-Based Reminders Frontend Plan, Location-Based Reminders Implementation Plan, Pre-LocationBasedReminders Implementation Plans Summary

## Knowledge Gaps
- **121 isolated node(s):** `AuthState`, `Loading`, `Unauthenticated`, `DashboardWidget`, `ProfileState` (+116 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **85 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ObserveProfileUseCase` connect `Utility Predictors & Water Tracking` to `Electricity Billing Logic`, `Home Dashboard Logic`, `Expense Management Logic`?**
  _High betweenness centrality (0.025) - this node is a cross-community bridge._
- **Why does `SaveProfileUseCase` connect `Home Dashboard Logic` to `Profile Management Logic`?**
  _High betweenness centrality (0.012) - this node is a cross-community bridge._
- **Are the 13 inferred relationships involving `NavGraph()` (e.g. with `App()` and `LoginScreen()`) actually correct?**
  _`NavGraph()` has 13 INFERRED edges - model-reasoned connections that need verification._
- **What connects `AuthState`, `Loading`, `Unauthenticated` to the rest of the system?**
  _121 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `UI Components & Navigation` be split into smaller, more focused modules?**
  _Cohesion score 0.07 - nodes in this community are weakly interconnected._
- **Should `App Core & Maps UI` be split into smaller, more focused modules?**
  _Cohesion score 0.07 - nodes in this community are weakly interconnected._
- **Should `Home Dashboard Logic` be split into smaller, more focused modules?**
  _Cohesion score 0.08 - nodes in this community are weakly interconnected._