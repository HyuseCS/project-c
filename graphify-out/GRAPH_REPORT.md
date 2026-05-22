# Graph Report - .  (2026-05-22)

## Corpus Check
- Corpus is ~24,618 words - fits in a single context window. You may not need a graph.

## Summary
- 417 nodes · 426 edges · 83 communities (25 shown, 58 thin omitted)
- Extraction: 88% EXTRACTED · 12% INFERRED · 0% AMBIGUOUS · INFERRED: 50 edges (avg confidence: 0.82)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- [[_COMMUNITY_UI Screens & Navigation|UI Screens & Navigation]]
- [[_COMMUNITY_Expense Tracking Feature|Expense Tracking Feature]]
- [[_COMMUNITY_Dashboard & Observers|Dashboard & Observers]]
- [[_COMMUNITY_Core App Framework|Core App Framework]]
- [[_COMMUNITY_Authentication Flow|Authentication Flow]]
- [[_COMMUNITY_Profile Management|Profile Management]]
- [[_COMMUNITY_Domain Models & Repositories|Domain Models & Repositories]]
- [[_COMMUNITY_Water Bill Logic|Water Bill Logic]]
- [[_COMMUNITY_Electricity Bill Logic|Electricity Bill Logic]]
- [[_COMMUNITY_Utility Predictor System|Utility Predictor System]]
- [[_COMMUNITY_Firestore Calculator Data|Firestore Calculator Data]]
- [[_COMMUNITY_Utility UI Fragments|Utility UI Fragments]]
- [[_COMMUNITY_Calculator Repository API|Calculator Repository API]]
- [[_COMMUNITY_Firestore Expense Data|Firestore Expense Data]]
- [[_COMMUNITY_Home UI Layout|Home UI Layout]]
- [[_COMMUNITY_Expense Repository API|Expense Repository API]]
- [[_COMMUNITY_Firebase Auth Impl|Firebase Auth Impl]]
- [[_COMMUNITY_Predictor UI|Predictor UI]]
- [[_COMMUNITY_Expenses UI|Expenses UI]]
- [[_COMMUNITY_iOS Native UI|iOS Native UI]]
- [[_COMMUNITY_Electricity Calc UseCase|Electricity Calc UseCase]]
- [[_COMMUNITY_Auth Repository API|Auth Repository API]]
- [[_COMMUNITY_Profile Repository API|Profile Repository API]]
- [[_COMMUNITY_Water Calc UseCase|Water Calc UseCase]]
- [[_COMMUNITY_Firestore Profile Data|Firestore Profile Data]]
- [[_COMMUNITY_Android Activity|Android Activity]]
- [[_COMMUNITY_Android Application|Android Application]]
- [[_COMMUNITY_Android Tests|Android Tests]]
- [[_COMMUNITY_iOS Entry|iOS Entry]]
- [[_COMMUNITY_Android Platform|Android Platform]]
- [[_COMMUNITY_Greeting Service|Greeting Service]]
- [[_COMMUNITY_Common Platform|Common Platform]]
- [[_COMMUNITY_App Constants|App Constants]]
- [[_COMMUNITY_Get User UseCase|Get User UseCase]]
- [[_COMMUNITY_Save Electricity UseCase|Save Electricity UseCase]]
- [[_COMMUNITY_Electricity History UseCase|Electricity History UseCase]]
- [[_COMMUNITY_Save Water UseCase|Save Water UseCase]]
- [[_COMMUNITY_Water History UseCase|Water History UseCase]]
- [[_COMMUNITY_Shared Tests|Shared Tests]]
- [[_COMMUNITY_iOS Platform|iOS Platform]]
- [[_COMMUNITY_Community 40|Community 40]]
- [[_COMMUNITY_Community 43|Community 43]]
- [[_COMMUNITY_Community 44|Community 44]]
- [[_COMMUNITY_Community 45|Community 45]]
- [[_COMMUNITY_Community 52|Community 52]]
- [[_COMMUNITY_Community 53|Community 53]]
- [[_COMMUNITY_Community 54|Community 54]]
- [[_COMMUNITY_Community 56|Community 56]]
- [[_COMMUNITY_Community 57|Community 57]]
- [[_COMMUNITY_Community 58|Community 58]]
- [[_COMMUNITY_Community 59|Community 59]]
- [[_COMMUNITY_Community 60|Community 60]]
- [[_COMMUNITY_Community 61|Community 61]]
- [[_COMMUNITY_Community 62|Community 62]]
- [[_COMMUNITY_Community 63|Community 63]]
- [[_COMMUNITY_Community 64|Community 64]]
- [[_COMMUNITY_Community 65|Community 65]]
- [[_COMMUNITY_Community 66|Community 66]]
- [[_COMMUNITY_Community 67|Community 67]]
- [[_COMMUNITY_Community 68|Community 68]]
- [[_COMMUNITY_Community 69|Community 69]]
- [[_COMMUNITY_Community 70|Community 70]]
- [[_COMMUNITY_Community 71|Community 71]]
- [[_COMMUNITY_Community 72|Community 72]]
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

## God Nodes (most connected - your core abstractions)
1. `NavGraph()` - 12 edges
2. `CalculatorRepositoryImpl` - 11 edges
3. `NavGraph()` - 10 edges
4. `CalculatorRepository` - 9 edges
5. `ExpenseRepositoryImpl` - 9 edges
6. `HomeScreen()` - 8 edges
7. `ExpensesViewModel` - 8 edges
8. `ElectricityBillViewModel` - 7 edges
9. `WaterBillViewModel` - 7 edges
10. `ExpenseRepository` - 7 edges

## Surprising Connections (you probably didn't know these)
- `Clean Architecture` --rationale_for--> `ProjectCApplication`  [INFERRED]
  GEMINI.md → composeApp/src/androidMain/kotlin/com/hyuse/projectc/ProjectCApplication.kt
- `WaterBillViewModel` --conceptually_related_to--> `Utilities Hub`  [INFERRED]
  composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/utilities/WaterBillViewModel.kt → memory.md
- `ElectricityPredictorViewModel` --conceptually_related_to--> `Utilities Hub`  [INFERRED]
  composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/utilities/predictor/ElectricityPredictorViewModel.kt → memory.md
- `NavGraph()` --calls--> `LoginScreen()`  [INFERRED]
  composeApp/src/androidMain/kotlin/com/hyuse/projectc/navigation/NavGraph.kt → composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/auth/LoginScreen.kt
- `NavGraph()` --calls--> `SignUpScreen()`  [INFERRED]
  composeApp/src/androidMain/kotlin/com/hyuse/projectc/navigation/NavGraph.kt → composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/auth/SignUpScreen.kt

## Hyperedges (group relationships)
- **MVVM triad - Auth** — loginscreen_loginscreen, signupscreen_signupscreen, authviewmodel_authviewmodel, authviewmodel_authstate [INFERRED 0.85]
- **MVVM triad - Home** — homescreen_homescreen, homeviewmodel_homeviewmodel, homeviewmodel_homestate [INFERRED 0.85]
- **MVVM triad - Profile** — profilescreen_profilescreen, profileviewmodel_profileviewmodel, profileviewmodel_profilestate [INFERRED 0.85]
- **MVVM triad - Electricity Bill** — electricitybillscreen_electricitybillscreen, electricitybillviewmodel_electricitybillviewmodel, electricitybillviewmodel_electricbilluistate [INFERRED 0.85]
- **MVVM triad - Expenses** — expensesdashboardscreen_expensesdashboardscreen, expensesviewmodel_expensesviewmodel, expensesviewmodel_expensesstate [INFERRED 0.85]

## Communities (83 total, 58 thin omitted)

### Community 0 - "UI Screens & Navigation"
Cohesion: 0.08
Nodes (22): LoginScreen(), SignUpScreen(), NavGraph(), Routes, ProfileScreen(), rememberStringState(), App(), ProjectCTheme() (+14 more)

### Community 1 - "Expense Tracking Feature"
Cohesion: 0.08
Nodes (12): DayExpenses, Error, ExpensesState, ExpensesViewModel, Loading, Success, Expense, ExpenseCategory (+4 more)

### Community 2 - "Dashboard & Observers"
Cohesion: 0.11
Nodes (16): ActionItem, DashboardWidget, ElectricityGraphWidget, ExpenseSummary, ExpenseSummaryWidget, HomeState, HomeViewModel, MonthlyUsage (+8 more)

### Community 3 - "Core App Framework"
Cohesion: 0.1
Nodes (24): App(), appModule, AuthState, AuthViewModel, ElectricityBillHistoryScreen(), ElectricityBillScreen(), ElectricBillUiState, ElectricityBillViewModel (+16 more)

### Community 4 - "Authentication Flow"
Cohesion: 0.13
Nodes (9): Authenticated, AuthState, AuthViewModel, Error, Loading, Unauthenticated, LoginUseCase, LogoutUseCase (+1 more)

### Community 5 - "Profile Management"
Cohesion: 0.12
Nodes (10): UserProfile, Error, Idle, Loading, ProfileState, ProfileViewModel, SaveSuccess, Success (+2 more)

### Community 6 - "Domain Models & Repositories"
Cohesion: 0.12
Nodes (17): AddExpenseUseCase, AuthRepository, AuthRepositoryImpl, CalculateElectricityBillUseCase, CalculatorRepository, CalculatorRepositoryImpl, ElectricityBillResult, Expense (+9 more)

### Community 7 - "Water Bill Logic"
Cohesion: 0.17
Nodes (8): Calculated, ConfirmOverwrite, Error, Idle, SaveSuccess, Saving, WaterBillUiState, WaterBillViewModel

### Community 8 - "Electricity Bill Logic"
Cohesion: 0.19
Nodes (8): Calculated, ConfirmOverwrite, ElectricBillUiState, ElectricityBillViewModel, Error, Idle, SaveSuccess, Saving

### Community 9 - "Utility Predictor System"
Cohesion: 0.15
Nodes (4): ElectricityAppliance, ElectricityPredictorViewModel, PredictorSummary, ObserveProfileUseCase

### Community 11 - "Utility UI Fragments"
Cohesion: 0.17
Nodes (12): AddApplianceDialog, ApplianceItem, ElectricityPredictorScreen, PredictorHeader, ElectricityPredictorViewModel, PredictorSummary, Utilities Hub, HistoryItem (+4 more)

### Community 14 - "Home UI Layout"
Cohesion: 0.42
Nodes (8): ClickableActionTile(), DashboardEditorContent(), ExpenseSummaryCard(), HomeScreen(), QuickActions(), RecentActivityFeed(), UsageGraph(), WelcomeCard()

### Community 17 - "Predictor UI"
Cohesion: 0.67
Nodes (5): AddApplianceDialog(), ApplianceItem(), ElectricityPredictorScreen(), formatNumber(), PredictorHeader()

### Community 18 - "Expenses UI"
Cohesion: 0.6
Nodes (5): DayHeader(), ExpenseItem(), ExpensesDashboardScreen(), MonthSelector(), SummaryCard()

### Community 19 - "iOS Native UI"
Cohesion: 0.4
Nodes (4): ContentView, ContentView_Previews, PreviewProvider, View

### Community 40 - "Community 40"
Cohesion: 0.67
Nodes (3): Greeting, AndroidPlatform, Platform

## Knowledge Gaps
- **77 isolated node(s):** `AuthState`, `Loading`, `Unauthenticated`, `DashboardWidget`, `ProfileState` (+72 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **58 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ObserveProfileUseCase` connect `Utility Predictor System` to `Expense Tracking Feature`, `Dashboard & Observers`, `Water Bill Logic`?**
  _High betweenness centrality (0.050) - this node is a cross-community bridge._
- **Why does `SaveProfileUseCase` connect `Profile Management` to `Dashboard & Observers`?**
  _High betweenness centrality (0.021) - this node is a cross-community bridge._
- **Are the 11 inferred relationships involving `NavGraph()` (e.g. with `App()` and `LoginScreen()`) actually correct?**
  _`NavGraph()` has 11 INFERRED edges - model-reasoned connections that need verification._
- **What connects `AuthState`, `Loading`, `Unauthenticated` to the rest of the system?**
  _77 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `UI Screens & Navigation` be split into smaller, more focused modules?**
  _Cohesion score 0.08 - nodes in this community are weakly interconnected._
- **Should `Expense Tracking Feature` be split into smaller, more focused modules?**
  _Cohesion score 0.08 - nodes in this community are weakly interconnected._
- **Should `Dashboard & Observers` be split into smaller, more focused modules?**
  _Cohesion score 0.11 - nodes in this community are weakly interconnected._