# Phase 4: Dashboard Home Screen Roadmap ✅ (Completed: 2026-04-12)

## Objective
Implement a dynamic, component-based Dashboard Home Screen that users can eventually customize. Introduce an electricity consumption graph widget, limit current tiles to working modules, and add a "Nickname" field to the user profile for personalized greetings.

## Key Files & Context
- `shared/src/commonMain/kotlin/com/hyuse/projectc/domain/model/UserProfile.kt`
- `composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/profile/ProfileViewModel.kt`
- `composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/profile/ProfileScreen.kt`
- `composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/home/HomeScreen.kt`
- `composeApp/src/androidMain/kotlin/com/hyuse/projectc/ui/home/HomeViewModel.kt`

## Implementation Steps

### 1. Update Profile (Domain & UI)
- Add `nickname: String? = null` property to `UserProfile.kt`.
- Update `ProfileViewModel` and `ProfileScreen` to include a "Nickname" text field.
- Ensure the save mechanism handles the new field properly.

### 2. Component-Based Architecture (Dashboard Widgets)
- Create a sealed class `DashboardWidget` in the presentation layer (e.g., `HomeState.kt`):
  - `ElectricityGraphWidget(dataPoints: List<MonthlyUsage>)`
  - `QuickActionsWidget(actions: List<ActionItem>)`

### 3. HomeViewModel Updates
- Inject `ObserveProfileUseCase` and `GetElectricityBillHistoryUseCase` into `HomeViewModel`.
- Modify `HomeState` to include the dashboard widgets alongside profile existence.
- Observe the user profile to extract `nickname` (fallback to `name` if null).
- Fetch electricity bills, filter them for the current calendar year, and aggregate total cost or usage by month to feed the `ElectricityGraphWidget`.

### 4. HomeScreen UI Updates
- Update the "Welcome Card" to display `Welcome back, {nickname ?: name}`.
- Refactor the hardcoded tiles into a dynamic layout (`LazyColumn` or `Column` iterating over `DashboardWidget`s).
- Build a custom minimal `ElectricityGraph` composable (using `Canvas` or basic bar heights via `Box`) to plot monthly consumption.
- Limit quick action tiles strictly to "Utilities" (which links to calculators) and "Profile", dropping "Settings" and "Reminders" for now since they are not active.

## Verification & Testing
- Ensure the profile correctly saves and loads the new nickname field.
- Confirm the Home screen automatically updates the welcome message when the nickname is changed.
- Verify the electricity graph properly aggregates bills into months and scales visually on the dashboard.
- Check that the dashboard renders correctly without crashing if no electricity bills exist (empty state widget).