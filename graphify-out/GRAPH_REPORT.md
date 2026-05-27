# Graph Report - Project_C  (2026-05-27)

## Corpus Check
- 93 files · ~28,562 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 679 nodes · 659 edges · 120 communities (53 shown, 67 thin omitted)
- Extraction: 90% EXTRACTED · 10% INFERRED · 0% AMBIGUOUS · INFERRED: 65 edges (avg confidence: 0.83)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `e5a47265`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

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
- [[_COMMUNITY_Community 57|Community 57]]
- [[_COMMUNITY_Community 58|Community 58]]
- [[_COMMUNITY_Community 60|Community 60]]
- [[_COMMUNITY_Community 61|Community 61]]
- [[_COMMUNITY_Community 62|Community 62]]
- [[_COMMUNITY_Community 63|Community 63]]
- [[_COMMUNITY_Community 66|Community 66]]
- [[_COMMUNITY_Community 67|Community 67]]
- [[_COMMUNITY_Community 68|Community 68]]
- [[_COMMUNITY_Community 69|Community 69]]
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
- [[_COMMUNITY_Community 111|Community 111]]
- [[_COMMUNITY_Community 112|Community 112]]
- [[_COMMUNITY_Community 113|Community 113]]
- [[_COMMUNITY_Community 114|Community 114]]
- [[_COMMUNITY_Community 116|Community 116]]
- [[_COMMUNITY_Community 117|Community 117]]
- [[_COMMUNITY_Community 118|Community 118]]
- [[_COMMUNITY_Community 119|Community 119]]

## God Nodes (most connected - your core abstractions)
1. `NavGraph()` - 12 edges
2. `CalculatorRepositoryImpl` - 11 edges
3. `Project Status & Roadmap` - 10 edges
4. `HomeScreen` - 10 edges
5. `HomeScreen()` - 9 edges
6. `CalculatorRepository` - 9 edges
7. `ExpenseRepositoryImpl` - 9 edges
8. `Project C — Development Memory` - 9 edges
9. `ElectricityBillViewModel` - 8 edges
10. `WaterBillViewModel` - 8 edges

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

## Communities (120 total, 67 thin omitted)

### Community 0 - "App Screens and Navigation"
Cohesion: 0.08
Nodes (19): ActionItem, DashboardWidget, ElectricityGraphWidget, ExpenseSummary, ExpenseSummaryWidget, HomeState, HomeViewModel, MonthlyUsage (+11 more)

### Community 1 - "Home & Expense Use Cases"
Cohesion: 0.08
Nodes (25): LoginScreen(), SignUpScreen(), NavGraph(), Routes, AddApplianceDialog(), ApplianceItem(), ElectricityPredictorScreen(), formatNumber() (+17 more)

### Community 2 - "Expense Management Logic"
Cohesion: 0.07
Nodes (29): 1. `:shared` Module (Multiplatform), 2. `:composeApp` Module (Android UI), 3. `iosApp` (iOS UI), Android, Architecture, Building and Running, code:block1 (users/{uid}                          ← profile data (name, e), code:bash (./gradlew :composeApp:assembleDebug) (+21 more)

### Community 3 - "Home Screen UI & Rationale"
Cohesion: 0.07
Nodes (29): 1. Shared Module — Domain Layer, 2. Shared Module — Data Layer, 3. ComposeApp — Presentation Layer, 4. File Structure Summary, Automated, code:kotlin (@Serializable), code:kotlin (interface CalculatorRepository {), code:block3 (users/{uid}/calculators/{autoId}) (+21 more)

### Community 4 - "Authentication Logic"
Cohesion: 0.08
Nodes (12): ElectricityAppliance, ElectricityPredictorViewModel, PredictorSummary, ObserveProfileUseCase, Calculated, ConfirmOverwrite, ElectricBillUiState, ElectricityBillViewModel (+4 more)

### Community 5 - "Lucid UI Components"
Cohesion: 0.11
Nodes (10): DayExpenses, Error, ExpensesState, ExpensesViewModel, Loading, Success, ExpenseCategory, DeleteExpenseUseCase (+2 more)

### Community 6 - "User Profile Management"
Cohesion: 0.11
Nodes (24): App(), appModule, Clean Architecture, ActivityFeed, DashboardEditorContent, HomeScreen, InlineQuickAddExpense, QuickActionsLucid (+16 more)

### Community 7 - "Electricity Bill Management"
Cohesion: 0.09
Nodes (21): Additional Forbidden Patterns, Anti-Patterns (Do NOT Use), Buttons, Cards, code:css (@import url('https://fonts.googleapis.com/css2?family=Inter:), code:css (/* Primary Button */), code:css (.card {), code:css (.input {) (+13 more)

### Community 8 - "Water Bill Management"
Cohesion: 0.16
Nodes (16): ExpensesDashboardScreen(), LucidDayHeader(), LucidExpenseItem(), MonthSelectorLucid(), SummaryHero(), ActivityFeed(), DashboardEditorContent(), HomeScreen() (+8 more)

### Community 9 - "Utility Predictor & History"
Cohesion: 0.1
Nodes (20): 1. Objectives, 2. Firestore Data Model, 3. Shared Module Implementation (`:shared`), 4. UI Layer Implementation (`:composeApp`), 5. Implementation Roadmap, 6. Testing Strategy, code:json ({), code:json ({) (+12 more)

### Community 10 - "Calculator Repository Implementation"
Cohesion: 0.13
Nodes (9): Authenticated, AuthState, AuthViewModel, Error, Loading, Unauthenticated, LoginUseCase, LogoutUseCase (+1 more)

### Community 11 - "Calculator Repository Interface"
Cohesion: 0.1
Nodes (19): Architecture Reference, code:block1 (shared/src/commonMain/kotlin/com/hyuse/projectc/), code:block2 (users/{uid}                          ← profile data (includi), Completed Phases, Dependency Versions, Firestore Data Structure, Future Phases (TODO), iOS Notes (deferred) (+11 more)

### Community 12 - "Expense Repository Implementation"
Cohesion: 0.17
Nodes (8): Calculated, ConfirmOverwrite, Error, Idle, SaveSuccess, Saving, WaterBillUiState, WaterBillViewModel

### Community 13 - "Expense Repository Interface"
Cohesion: 0.15
Nodes (9): UserProfile, Error, Idle, Loading, ProfileState, ProfileViewModel, SaveSuccess, Success (+1 more)

### Community 14 - "Auth Repository Implementation"
Cohesion: 0.13
Nodes (14): 1. Overview, 2. Colors, 3. Typography, 4. Elevation & Surface, 5. Components, 6. Do's and Don'ts, Design System: Project C (Lucid Glass), Do: (+6 more)

### Community 15 - "Electricity Predictor UI"
Cohesion: 0.13
Nodes (14): Automated Tests, Goal Description, Home Screen UI Components, Home Screen UX & Logic, Manual Verification, [MODIFY] composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/home/HomeScreen.kt, [MODIFY] composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/home/HomeViewModel.kt, [MODIFY] composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/theme/Theme.kt (+6 more)

### Community 16 - "Electricity Bill UI Screens"
Cohesion: 0.14
Nodes (13): Automated Tests, ComposeApp — Presentation Layer ✅, Implementation Status, JVM Target Mismatch (2026-04-10), Manual Verification, Next Steps 🛠️, Overview, Project C — Phase 2: Firestore Repository (+5 more)

### Community 20 - "Profile Repository Interface"
Cohesion: 0.2
Nodes (9): 1. Update Profile (Domain & UI), 2. Component-Based Architecture (Dashboard Widgets), 3. HomeViewModel Updates, 4. HomeScreen UI Updates, Implementation Steps, Key Files & Context, Objective, Phase 4: Dashboard Home Screen Roadmap ✅ (Completed: 2026-04-12) (+1 more)

### Community 21 - "Electricity Bill Use Cases"
Cohesion: 0.2
Nodes (9): Color Overrides, Component Overrides, Dashboard Page Overrides, Layout Overrides, Page-Specific Components, Page-Specific Rules, Recommendations, Spacing Overrides (+1 more)

### Community 22 - "Profile Repository Implementation"
Cohesion: 0.22
Nodes (8): Accessibility & Inclusion, Anti-references, Brand Personality, Design Principles, Product, Product Purpose, Register, Users

### Community 23 - "Community 23"
Cohesion: 0.22
Nodes (8): 1. Domain Layer (`shared`), 2. Data Layer (`shared`), 3. Presentation Layer (`composeApp`), 4. Navigation and DI, Completed Actions, Objective, Phase 3.1: Water Bill Calculator Implementation, Verification

### Community 26 - "Community 26"
Cohesion: 0.29
Nodes (6): Build and Run Android Application, Build and Run iOS Application, code:shell (./gradlew :composeApp:assembleDebug), code:shell (.\gradlew.bat :composeApp:assembleDebug), code:shell (./gradlew :composeApp:installDebug), code:shell (.\gradlew.bat :composeApp:installDebug)

### Community 27 - "Community 27"
Cohesion: 0.29
Nodes (7): AddApplianceDialog, ApplianceItem, ElectricityPredictorScreen, PredictorHeader, ElectricityPredictorViewModel, PredictorSummary, Utilities Hub

### Community 28 - "Community 28"
Cohesion: 0.33
Nodes (5): Android SDK Requirements, Core Languages & Tools, Frameworks & Primary Libraries, Kotlin Extensions, Project Tech Stack Requirements

### Community 29 - "Community 29"
Cohesion: 0.4
Nodes (4): ContentView, ContentView_Previews, PreviewProvider, View

### Community 35 - "Community 35"
Cohesion: 0.4
Nodes (5): CalculateElectricityBillUseCase, CalculatorRepository, CalculatorRepositoryImpl, ElectricityBillResult, WaterBillResult

### Community 36 - "Community 36"
Cohesion: 0.5
Nodes (5): AddExpenseUseCase, Expense, ExpenseCategory, ExpenseRepository, ExpenseRepositoryImpl

### Community 58 - "Community 58"
Cohesion: 0.67
Nodes (3): HistoryItem, ResultCard, WaterBillScreen

### Community 60 - "Community 60"
Cohesion: 0.67
Nodes (3): Greeting, AndroidPlatform, Platform

### Community 61 - "Community 61"
Cohesion: 0.67
Nodes (3): ProfileRepository, ProfileRepositoryImpl, UserProfile

### Community 62 - "Community 62"
Cohesion: 0.67
Nodes (3): AuthRepository, AuthRepositoryImpl, User

## Knowledge Gaps
- **229 isolated node(s):** `AuthState`, `Loading`, `Unauthenticated`, `DashboardWidget`, `ProfileState` (+224 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **67 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ObserveProfileUseCase` connect `Authentication Logic` to `App Screens and Navigation`, `Expense Repository Implementation`, `Lucid UI Components`?**
  _High betweenness centrality (0.019) - this node is a cross-community bridge._
- **Why does `SaveProfileUseCase` connect `App Screens and Navigation` to `Expense Repository Interface`?**
  _High betweenness centrality (0.008) - this node is a cross-community bridge._
- **Are the 11 inferred relationships involving `NavGraph()` (e.g. with `App()` and `LoginScreen()`) actually correct?**
  _`NavGraph()` has 11 INFERRED edges - model-reasoned connections that need verification._
- **What connects `AuthState`, `Loading`, `Unauthenticated` to the rest of the system?**
  _229 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `App Screens and Navigation` be split into smaller, more focused modules?**
  _Cohesion score 0.08 - nodes in this community are weakly interconnected._
- **Should `Home & Expense Use Cases` be split into smaller, more focused modules?**
  _Cohesion score 0.08 - nodes in this community are weakly interconnected._
- **Should `Expense Management Logic` be split into smaller, more focused modules?**
  _Cohesion score 0.07 - nodes in this community are weakly interconnected._