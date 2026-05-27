# Graph Report - .  (2026-05-27)

## Corpus Check
- Corpus is ~27,270 words - fits in a single context window. You may not need a graph.

## Summary
- 459 nodes · 453 edges · 107 communities (40 shown, 67 thin omitted)
- Extraction: 86% EXTRACTED · 14% INFERRED · 0% AMBIGUOUS · INFERRED: 64 edges (avg confidence: 0.83)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- [[_COMMUNITY_App Screens and Navigation|App Screens and Navigation]]
- [[_COMMUNITY_Home & Expense Use Cases|Home & Expense Use Cases]]
- [[_COMMUNITY_Expense Management Logic|Expense Management Logic]]
- [[_COMMUNITY_Home Screen UI & Rationale|Home Screen UI & Rationale]]
- [[_COMMUNITY_Authentication Logic|Authentication Logic]]
- [[_COMMUNITY_Lucid UI Components|Lucid UI Components]]
- [[_COMMUNITY_User Profile Management|User Profile Management]]
- [[_COMMUNITY_Electricity Bill Management|Electricity Bill Management]]
- [[_COMMUNITY_Water Bill Management|Water Bill Management]]
- [[_COMMUNITY_Utility Predictor & History|Utility Predictor & History]]
- [[_COMMUNITY_Calculator Repository Implementation|Calculator Repository Implementation]]
- [[_COMMUNITY_Calculator Repository Interface|Calculator Repository Interface]]
- [[_COMMUNITY_Expense Repository Implementation|Expense Repository Implementation]]
- [[_COMMUNITY_Expense Repository Interface|Expense Repository Interface]]
- [[_COMMUNITY_Auth Repository Implementation|Auth Repository Implementation]]
- [[_COMMUNITY_Electricity Predictor UI|Electricity Predictor UI]]
- [[_COMMUNITY_Electricity Bill UI Screens|Electricity Bill UI Screens]]
- [[_COMMUNITY_iOS App UI (SwiftUI)|iOS App UI (SwiftUI)]]
- [[_COMMUNITY_Water Bill Use Cases|Water Bill Use Cases]]
- [[_COMMUNITY_Auth Repository Interface|Auth Repository Interface]]
- [[_COMMUNITY_Profile Repository Interface|Profile Repository Interface]]
- [[_COMMUNITY_Electricity Bill Use Cases|Electricity Bill Use Cases]]
- [[_COMMUNITY_Profile Repository Implementation|Profile Repository Implementation]]
- [[_COMMUNITY_Community 23|Community 23]]
- [[_COMMUNITY_Community 24|Community 24]]
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
- [[_COMMUNITY_Community 48|Community 48]]
- [[_COMMUNITY_Community 49|Community 49]]
- [[_COMMUNITY_Community 50|Community 50]]
- [[_COMMUNITY_Community 51|Community 51]]
- [[_COMMUNITY_Community 54|Community 54]]
- [[_COMMUNITY_Community 55|Community 55]]
- [[_COMMUNITY_Community 56|Community 56]]
- [[_COMMUNITY_Community 57|Community 57]]
- [[_COMMUNITY_Community 73|Community 73]]
- [[_COMMUNITY_Community 74|Community 74]]
- [[_COMMUNITY_Community 75|Community 75]]
- [[_COMMUNITY_Community 76|Community 76]]
- [[_COMMUNITY_Community 77|Community 77]]
- [[_COMMUNITY_Community 78|Community 78]]
- [[_COMMUNITY_Community 79|Community 79]]
- [[_COMMUNITY_Community 80|Community 80]]
- [[_COMMUNITY_Community 81|Community 81]]
- [[_COMMUNITY_Community 82|Community 82]]
- [[_COMMUNITY_Community 83|Community 83]]
- [[_COMMUNITY_Community 84|Community 84]]
- [[_COMMUNITY_Community 85|Community 85]]
- [[_COMMUNITY_Community 86|Community 86]]
- [[_COMMUNITY_Community 87|Community 87]]
- [[_COMMUNITY_Community 88|Community 88]]
- [[_COMMUNITY_Community 89|Community 89]]
- [[_COMMUNITY_Community 90|Community 90]]
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
- [[_COMMUNITY_Community 103|Community 103]]
- [[_COMMUNITY_Community 104|Community 104]]
- [[_COMMUNITY_Community 105|Community 105]]
- [[_COMMUNITY_Community 106|Community 106]]

## God Nodes (most connected - your core abstractions)
1. `NavGraph()` - 12 edges
2. `CalculatorRepositoryImpl` - 11 edges
3. `HomeScreen` - 10 edges
4. `HomeScreen()` - 9 edges
5. `CalculatorRepository` - 9 edges
6. `ExpenseRepositoryImpl` - 9 edges
7. `ElectricityBillViewModel` - 8 edges
8. `WaterBillViewModel` - 8 edges
9. `ExpensesViewModel` - 8 edges
10. `ExpenseRepository` - 7 edges

## Surprising Connections (you probably didn't know these)
- `Clean Architecture` --rationale_for--> `ProjectCApplication`  [INFERRED]
  GEMINI.md → composeApp/src/androidMain/kotlin/com/hyuse/projectc/ProjectCApplication.kt
- `ElectricityPredictorViewModel` --conceptually_related_to--> `Utilities Hub`  [INFERRED]
  composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/utilities/predictor/ElectricityPredictorViewModel.kt → memory.md
- `Lucid Glass Aesthetic` --rationale_for--> `QuickActionsLucid`  [EXTRACTED]
  planning/phase7_audit_fixes_plan.md → composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/home/HomeScreen.kt
- `Inline Quick Add` --rationale_for--> `QuickActionsLucid`  [EXTRACTED]
  planning/phase7_audit_fixes_plan.md → composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/home/HomeScreen.kt
- `Contextual Dashboard` --rationale_for--> `HomeViewModel`  [EXTRACTED]
  planning/phase7_audit_fixes_plan.md → composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/home/HomeViewModel.kt

## Hyperedges (group relationships)
- **Dashboard Widget UI Components** — homescreen_welcomehero, homescreen_usageinsights, homescreen_spendingoverview, homescreen_activityfeed, homescreen_quickactionslucid [INFERRED 0.95]
- **Dashboard Widget Data Models** — homeviewmodel_dashboardwidget, homeviewmodel_monthlyusage, homeviewmodel_expensesummary, homeviewmodel_recentactivity, homeviewmodel_actionitem [INFERRED 0.95]

## Communities (107 total, 67 thin omitted)

### Community 0 - "App Screens and Navigation"
Cohesion: 0.09
Nodes (21): LoginScreen(), SignUpScreen(), NavGraph(), Routes, AddApplianceDialog(), ApplianceItem(), ElectricityPredictorScreen(), formatNumber() (+13 more)

### Community 1 - "Home & Expense Use Cases"
Cohesion: 0.1
Nodes (17): ActionItem, DashboardWidget, ElectricityGraphWidget, ExpenseSummary, ExpenseSummaryWidget, HomeState, HomeViewModel, MonthlyUsage (+9 more)

### Community 2 - "Expense Management Logic"
Cohesion: 0.09
Nodes (11): DayExpenses, Error, ExpensesState, ExpensesViewModel, Loading, Success, ExpenseCategory, DeleteExpenseUseCase (+3 more)

### Community 3 - "Home Screen UI & Rationale"
Cohesion: 0.11
Nodes (24): App(), appModule, Clean Architecture, ActivityFeed, DashboardEditorContent, HomeScreen, InlineQuickAddExpense, QuickActionsLucid (+16 more)

### Community 4 - "Authentication Logic"
Cohesion: 0.13
Nodes (9): Authenticated, AuthState, AuthViewModel, Error, Loading, Unauthenticated, LoginUseCase, LogoutUseCase (+1 more)

### Community 5 - "Lucid UI Components"
Cohesion: 0.17
Nodes (16): ExpensesDashboardScreen(), LucidDayHeader(), LucidExpenseItem(), MonthSelectorLucid(), SummaryHero(), ActivityFeed(), DashboardEditorContent(), HomeScreen() (+8 more)

### Community 6 - "User Profile Management"
Cohesion: 0.12
Nodes (10): UserProfile, Error, Idle, Loading, ProfileState, ProfileViewModel, SaveSuccess, Success (+2 more)

### Community 7 - "Electricity Bill Management"
Cohesion: 0.17
Nodes (8): Calculated, ConfirmOverwrite, ElectricBillUiState, ElectricityBillViewModel, Error, Idle, SaveSuccess, Saving

### Community 8 - "Water Bill Management"
Cohesion: 0.18
Nodes (8): Calculated, ConfirmOverwrite, Error, Idle, SaveSuccess, Saving, WaterBillUiState, WaterBillViewModel

### Community 9 - "Utility Predictor & History"
Cohesion: 0.15
Nodes (4): ElectricityAppliance, ElectricityPredictorViewModel, PredictorSummary, ObserveProfileUseCase

### Community 15 - "Electricity Predictor UI"
Cohesion: 0.29
Nodes (7): AddApplianceDialog, ApplianceItem, ElectricityPredictorScreen, PredictorHeader, ElectricityPredictorViewModel, PredictorSummary, Utilities Hub

### Community 16 - "Electricity Bill UI Screens"
Cohesion: 0.8
Nodes (4): ElectricityBillScreen(), formatNumber(), HistoryItem(), ResultCard()

### Community 17 - "iOS App UI (SwiftUI)"
Cohesion: 0.4
Nodes (4): ContentView, ContentView_Previews, PreviewProvider, View

### Community 23 - "Community 23"
Cohesion: 0.4
Nodes (5): CalculateElectricityBillUseCase, CalculatorRepository, CalculatorRepositoryImpl, ElectricityBillResult, WaterBillResult

### Community 24 - "Community 24"
Cohesion: 0.5
Nodes (5): AddExpenseUseCase, Expense, ExpenseCategory, ExpenseRepository, ExpenseRepositoryImpl

### Community 46 - "Community 46"
Cohesion: 0.67
Nodes (3): HistoryItem, ResultCard, WaterBillScreen

### Community 49 - "Community 49"
Cohesion: 0.67
Nodes (3): Greeting, AndroidPlatform, Platform

### Community 50 - "Community 50"
Cohesion: 0.67
Nodes (3): AuthRepository, AuthRepositoryImpl, User

### Community 51 - "Community 51"
Cohesion: 0.67
Nodes (3): ProfileRepository, ProfileRepositoryImpl, UserProfile

## Knowledge Gaps
- **90 isolated node(s):** `AuthState`, `Loading`, `Unauthenticated`, `DashboardWidget`, `ProfileState` (+85 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **67 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ObserveProfileUseCase` connect `Utility Predictor & History` to `Home & Expense Use Cases`, `Expense Management Logic`, `Electricity Bill Management`?**
  _High betweenness centrality (0.042) - this node is a cross-community bridge._
- **Why does `HomeViewModel` connect `Home & Expense Use Cases` to `User Profile Management`?**
  _High betweenness centrality (0.024) - this node is a cross-community bridge._
- **Are the 11 inferred relationships involving `NavGraph()` (e.g. with `App()` and `LoginScreen()`) actually correct?**
  _`NavGraph()` has 11 INFERRED edges - model-reasoned connections that need verification._
- **What connects `AuthState`, `Loading`, `Unauthenticated` to the rest of the system?**
  _90 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `App Screens and Navigation` be split into smaller, more focused modules?**
  _Cohesion score 0.09 - nodes in this community are weakly interconnected._
- **Should `Home & Expense Use Cases` be split into smaller, more focused modules?**
  _Cohesion score 0.1 - nodes in this community are weakly interconnected._
- **Should `Expense Management Logic` be split into smaller, more focused modules?**
  _Cohesion score 0.09 - nodes in this community are weakly interconnected._