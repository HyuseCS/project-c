# Graph Report - .  (2026-05-28)

## Corpus Check
- Corpus is ~27,478 words - fits in a single context window. You may not need a graph.

## Summary
- 456 nodes · 447 edges · 111 communities (41 shown, 70 thin omitted)
- Extraction: 87% EXTRACTED · 13% INFERRED · 0% AMBIGUOUS · INFERRED: 58 edges (avg confidence: 0.82)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- [[_COMMUNITY_Home ViewModel & Widgets Flow|Home ViewModel & Widgets Flow]]
- [[_COMMUNITY_App Screens & Navigation Graph|App Screens & Navigation Graph]]
- [[_COMMUNITY_Expenses ViewModel & Categories|Expenses ViewModel & Categories]]
- [[_COMMUNITY_Authentication State & Use Cases|Authentication State & Use Cases]]
- [[_COMMUNITY_Liquid Glass Design System Specs|Liquid Glass Design System Specs]]
- [[_COMMUNITY_Profile ViewModel & Database Settings|Profile ViewModel & Database Settings]]
- [[_COMMUNITY_Water Bill ViewModel Logic|Water Bill ViewModel Logic]]
- [[_COMMUNITY_Lucid Glass Home Screen UI|Lucid Glass Home Screen UI]]
- [[_COMMUNITY_Electricity Bill ViewModel Logic|Electricity Bill ViewModel Logic]]
- [[_COMMUNITY_Electricity Appliance Predictor|Electricity Appliance Predictor]]
- [[_COMMUNITY_Calculator Repository Firestore Data|Calculator Repository Firestore Data]]
- [[_COMMUNITY_Calculator Repository Domain Interface|Calculator Repository Domain Interface]]
- [[_COMMUNITY_Expense Repository Firestore Data|Expense Repository Firestore Data]]
- [[_COMMUNITY_Expense Repository Domain Interface|Expense Repository Domain Interface]]
- [[_COMMUNITY_Authentication Repository Data Layer|Authentication Repository Data Layer]]
- [[_COMMUNITY_Electricity Appliance Predictor UI|Electricity Appliance Predictor UI]]
- [[_COMMUNITY_Lucid Expenses Dashboard Screen UI|Lucid Expenses Dashboard Screen UI]]
- [[_COMMUNITY_Electricity Bill Calculator UI|Electricity Bill Calculator UI]]
- [[_COMMUNITY_SwiftUI iOS App UI|SwiftUI iOS App UI]]
- [[_COMMUNITY_Authentication Repository Domain Interface|Authentication Repository Domain Interface]]
- [[_COMMUNITY_Profile Repository Domain Interface|Profile Repository Domain Interface]]
- [[_COMMUNITY_Electricity Bill Domain Use Cases|Electricity Bill Domain Use Cases]]
- [[_COMMUNITY_Water Bill Domain Use Cases|Water Bill Domain Use Cases]]
- [[_COMMUNITY_Profile Repository Firestore Data|Profile Repository Firestore Data]]
- [[_COMMUNITY_Expense Use Cases & Shared Domain Models|Expense Use Cases & Shared Domain Models]]
- [[_COMMUNITY_Calculator Repository Domain & Models|Calculator Repository Domain & Models]]
- [[_COMMUNITY_Android MainActivity Lifecycle|Android MainActivity Lifecycle]]
- [[_COMMUNITY_Android Application Entry Point|Android Application Entry Point]]
- [[_COMMUNITY_Android Compose UI Tests|Android Compose UI Tests]]
- [[_COMMUNITY_iOS Native App Architecture|iOS Native App Architecture]]
- [[_COMMUNITY_Android Platform Specific Bridge|Android Platform Specific Bridge]]
- [[_COMMUNITY_Greeting Service Sample Logic|Greeting Service Sample Logic]]
- [[_COMMUNITY_KMP Multiplatform Bridge Types|KMP Multiplatform Bridge Types]]
- [[_COMMUNITY_Currency Format Helpers & Constants|Currency Format Helpers & Constants]]
- [[_COMMUNITY_Get Active User Session Use Case|Get Active User Session Use Case]]
- [[_COMMUNITY_Save Electricity Invoice Use Case|Save Electricity Invoice Use Case]]
- [[_COMMUNITY_Observe Electricity Invoices Use Case|Observe Electricity Invoices Use Case]]
- [[_COMMUNITY_Save Water Invoice Use Case|Save Water Invoice Use Case]]
- [[_COMMUNITY_Observe Water Invoices Use Case|Observe Water Invoices Use Case]]
- [[_COMMUNITY_Delete Electricity Invoice Use Case|Delete Electricity Invoice Use Case]]
- [[_COMMUNITY_Delete Water Invoice Use Case|Delete Water Invoice Use Case]]
- [[_COMMUNITY_Common Multiplatform Unit Tests|Common Multiplatform Unit Tests]]
- [[_COMMUNITY_iOS Platform Specific Bridge|iOS Platform Specific Bridge]]
- [[_COMMUNITY_Gradle Dependency Build Configurations|Gradle Dependency Build Configurations]]
- [[_COMMUNITY_User Profile Screen UI|User Profile Screen UI]]
- [[_COMMUNITY_Water Bill Calculator UI|Water Bill Calculator UI]]
- [[_COMMUNITY_Multiplatform Bridge Interface Setup|Multiplatform Bridge Interface Setup]]
- [[_COMMUNITY_Authentication Dependency Setup|Authentication Dependency Setup]]
- [[_COMMUNITY_User Profile Dependency Setup|User Profile Dependency Setup]]
- [[_COMMUNITY_Location-Based Reminders Planning|Location-Based Reminders Planning]]
- [[_COMMUNITY_Authentication View Model Setup|Authentication View Model Setup]]
- [[_COMMUNITY_Expenses View Model Setup|Expenses View Model Setup]]
- [[_COMMUNITY_iOS Application Delegate UI|iOS Application Delegate UI]]
- [[_COMMUNITY_iOS Native Platform Specific Bridge|iOS Native Platform Specific Bridge]]
- [[_COMMUNITY_Project Dependencies & Prerequisites|Project Dependencies & Prerequisites]]
- [[_COMMUNITY_Dashboard Widget Design System Rules|Dashboard Widget Design System Rules]]
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
- [[_COMMUNITY_Community 102|Community 102]]
- [[_COMMUNITY_Community 103|Community 103]]
- [[_COMMUNITY_Community 104|Community 104]]
- [[_COMMUNITY_Community 105|Community 105]]
- [[_COMMUNITY_Community 106|Community 106]]
- [[_COMMUNITY_Community 107|Community 107]]
- [[_COMMUNITY_Community 108|Community 108]]
- [[_COMMUNITY_Community 109|Community 109]]
- [[_COMMUNITY_Community 110|Community 110]]

## God Nodes (most connected - your core abstractions)
1. `NavGraph()` - 12 edges
2. `CalculatorRepositoryImpl` - 11 edges
3. `HomeScreen()` - 9 edges
4. `CalculatorRepository` - 9 edges
5. `ExpenseRepositoryImpl` - 9 edges
6. `HomeScreen` - 9 edges
7. `ElectricityBillViewModel` - 8 edges
8. `WaterBillViewModel` - 8 edges
9. `ExpensesViewModel` - 8 edges
10. `ExpenseRepository` - 7 edges

## Surprising Connections (you probably didn't know these)
- `LucidSurface` --conceptually_related_to--> `Liquid Glass Style`  [INFERRED]
  composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/theme/Theme.kt → design-system/projectc/MASTER.md
- `Clean Architecture` --rationale_for--> `ProjectCApplication`  [INFERRED]
  GEMINI.md → composeApp/src/androidMain/kotlin/com/hyuse/projectc/ProjectCApplication.kt
- `ElectricityPredictorViewModel` --conceptually_related_to--> `Utilities Hub`  [INFERRED]
  composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/utilities/predictor/ElectricityPredictorViewModel.kt → memory.md
- `App()` --calls--> `NavGraph()`  [INFERRED]
  composeApp/src/androidMain/kotlin/com/hyuse/projectc/App.kt → composeApp/src/androidMain/kotlin/com/hyuse/projectc/navigation/NavGraph.kt
- `NavGraph()` --calls--> `LoginScreen()`  [INFERRED]
  composeApp/src/androidMain/kotlin/com/hyuse/projectc/navigation/NavGraph.kt → composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/auth/LoginScreen.kt

## Hyperedges (group relationships)
- **Lucid Glass Theme Implementation** — theme_lucidsurface, master_liquid_glass, homescreen_homescreen [INFERRED 0.85]

## Communities (111 total, 70 thin omitted)

### Community 0 - "Home ViewModel & Widgets Flow"
Cohesion: 0.08
Nodes (19): ActionItem, DashboardWidget, ElectricityGraphWidget, ExpenseSummary, ExpenseSummaryWidget, HomeState, HomeViewModel, MonthlyUsage (+11 more)

### Community 1 - "App Screens & Navigation Graph"
Cohesion: 0.09
Nodes (21): LoginScreen(), SignUpScreen(), NavGraph(), Routes, AddApplianceDialog(), ApplianceItem(), ElectricityPredictorScreen(), formatNumber() (+13 more)

### Community 2 - "Expenses ViewModel & Categories"
Cohesion: 0.11
Nodes (10): DayExpenses, Error, ExpensesState, ExpensesViewModel, Loading, Success, ExpenseCategory, DeleteExpenseUseCase (+2 more)

### Community 3 - "Authentication State & Use Cases"
Cohesion: 0.13
Nodes (9): Authenticated, AuthState, AuthViewModel, Error, Loading, Unauthenticated, LoginUseCase, LogoutUseCase (+1 more)

### Community 4 - "Liquid Glass Design System Specs"
Cohesion: 0.15
Nodes (18): App(), appModule, Clean Architecture, ActivityFeed, HomeScreen, InlineQuickAddExpense, QuickActionsLucid, SpendingOverview (+10 more)

### Community 5 - "Profile ViewModel & Database Settings"
Cohesion: 0.15
Nodes (9): UserProfile, Error, Idle, Loading, ProfileState, ProfileViewModel, SaveSuccess, Success (+1 more)

### Community 6 - "Water Bill ViewModel Logic"
Cohesion: 0.17
Nodes (8): Calculated, ConfirmOverwrite, Error, Idle, SaveSuccess, Saving, WaterBillUiState, WaterBillViewModel

### Community 7 - "Lucid Glass Home Screen UI"
Cohesion: 0.23
Nodes (11): ActivityFeed(), DashboardEditorContent(), HomeScreen(), InlineQuickAddExpense(), QuickActionsLucid(), SpendingOverview(), UsageInsights(), WelcomeHero() (+3 more)

### Community 8 - "Electricity Bill ViewModel Logic"
Cohesion: 0.18
Nodes (8): Calculated, ConfirmOverwrite, ElectricBillUiState, ElectricityBillViewModel, Error, Idle, SaveSuccess, Saving

### Community 9 - "Electricity Appliance Predictor"
Cohesion: 0.15
Nodes (4): ElectricityAppliance, ElectricityPredictorViewModel, PredictorSummary, ObserveProfileUseCase

### Community 15 - "Electricity Appliance Predictor UI"
Cohesion: 0.29
Nodes (7): AddApplianceDialog, ApplianceItem, ElectricityPredictorScreen, PredictorHeader, ElectricityPredictorViewModel, PredictorSummary, Utilities Hub

### Community 16 - "Lucid Expenses Dashboard Screen UI"
Cohesion: 0.6
Nodes (5): ExpensesDashboardScreen(), LucidDayHeader(), LucidExpenseItem(), MonthSelectorLucid(), SummaryHero()

### Community 17 - "Electricity Bill Calculator UI"
Cohesion: 0.8
Nodes (4): ElectricityBillScreen(), formatNumber(), HistoryItem(), ResultCard()

### Community 18 - "SwiftUI iOS App UI"
Cohesion: 0.4
Nodes (4): ContentView, ContentView_Previews, PreviewProvider, View

### Community 24 - "Expense Use Cases & Shared Domain Models"
Cohesion: 0.5
Nodes (5): AddExpenseUseCase, Expense, ExpenseCategory, ExpenseRepository, ExpenseRepositoryImpl

### Community 25 - "Calculator Repository Domain & Models"
Cohesion: 0.4
Nodes (5): CalculateElectricityBillUseCase, CalculatorRepository, CalculatorRepositoryImpl, ElectricityBillResult, WaterBillResult

### Community 45 - "Water Bill Calculator UI"
Cohesion: 0.67
Nodes (3): HistoryItem, ResultCard, WaterBillScreen

### Community 47 - "Multiplatform Bridge Interface Setup"
Cohesion: 0.67
Nodes (3): Greeting, AndroidPlatform, Platform

### Community 48 - "Authentication Dependency Setup"
Cohesion: 0.67
Nodes (3): AuthRepository, AuthRepositoryImpl, User

### Community 49 - "User Profile Dependency Setup"
Cohesion: 0.67
Nodes (3): ProfileRepository, ProfileRepositoryImpl, UserProfile

### Community 52 - "Location-Based Reminders Planning"
Cohesion: 0.67
Nodes (3): Location-Based Reminders Frontend Plan, Location-Based Reminders Implementation Plan, Pre-LocationBasedReminders Implementation Plans Summary

## Knowledge Gaps
- **93 isolated node(s):** `AuthState`, `Loading`, `Unauthenticated`, `DashboardWidget`, `ProfileState` (+88 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **70 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ObserveProfileUseCase` connect `Electricity Appliance Predictor` to `Home ViewModel & Widgets Flow`, `Expenses ViewModel & Categories`, `Water Bill ViewModel Logic`?**
  _High betweenness centrality (0.042) - this node is a cross-community bridge._
- **Why does `SaveProfileUseCase` connect `Home ViewModel & Widgets Flow` to `Profile ViewModel & Database Settings`?**
  _High betweenness centrality (0.018) - this node is a cross-community bridge._
- **Are the 11 inferred relationships involving `NavGraph()` (e.g. with `App()` and `LoginScreen()`) actually correct?**
  _`NavGraph()` has 11 INFERRED edges - model-reasoned connections that need verification._
- **What connects `AuthState`, `Loading`, `Unauthenticated` to the rest of the system?**
  _93 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Home ViewModel & Widgets Flow` be split into smaller, more focused modules?**
  _Cohesion score 0.08 - nodes in this community are weakly interconnected._
- **Should `App Screens & Navigation Graph` be split into smaller, more focused modules?**
  _Cohesion score 0.09 - nodes in this community are weakly interconnected._
- **Should `Expenses ViewModel & Categories` be split into smaller, more focused modules?**
  _Cohesion score 0.11 - nodes in this community are weakly interconnected._