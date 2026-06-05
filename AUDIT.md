# Comprehensive Security and Architecture Audit
**ProjectC Kotlin Multiplatform Application**

## Executive Summary
This audit was conducted across the shared logic, compose multiplatform UI, and underlying infrastructure of ProjectC. The assessment reveals a fundamentally sound architectural pattern (Clean Architecture + KMP) but exposes critical gaps in security, concurrency management, and state scoping that would prevent this application from being production-ready.

The most severe vulnerabilities stem from missing Firestore security configurations and inappropriate ViewModel scoping leading to data loss across navigation events.

## 1. Security & Vulnerabilities 🔴
### 1.1 Firestore Security Gaps
* **Missing Rules File**: There is no version-controlled `firestore.rules` or `firebase.json` file. The app relies entirely on whatever is configured in the Firebase Console. If default rules are in place, any authenticated user might be able to read/write another user's data since the domain layer directly passes `uid` strings to create document paths (`users/{uid}/...`).
* **Client-Side Enforced Isolation**: Security relies completely on the client supplying the correct `uid`.

### 1.2 Android Build Security
* **No Obfuscation in Release**: `composeApp/build.gradle.kts` sets `isMinifyEnabled = false` for release builds. No R8/ProGuard rules exist. The application code, including strings, is fully readable in the APK.
* **No Release Signing Configuration**: No `signingConfigs` block is defined.
* **Data Backup Enabled**: `android:allowBackup="true"` in the manifest allows extracting app data via ADB, a potential data leakage risk.

### 1.3 Missing Input Validation
* **Silent Math Errors**: `CalculateElectricityBillUseCase` and `CalculateWaterBillUseCase` do not validate that `currentReading >= previousReading`. Negative consumption and negative costs are silently calculated and persisted.
* **Unbounded Strings**: Profile fields (name, university, course) have no max length checks, allowing users to bloat the Firestore database.

## 2. Architectural & Implementation Flaws 🟠
### 2.1 Critical ViewModel Scoping Issue
* **Unshared Instances**: ViewModels (`ElectricityBillViewModel`, `WaterBillViewModel`, `ExpensesViewModel`) are instantiated via `koinViewModel()` in multiple navigation routes (e.g., in both `ELECTRICITY_CALCULATOR` and `ELECTRICITY_HISTORY`). This creates *separate instances* rather than sharing state. History loaded in the calculator is lost when navigating to the history screen.

### 2.2 Profile Save Bug
* **Dashboard Wipe**: `ProfileViewModel.saveProfile()` creates a new `UserProfile` instance without copying the existing `dashboardWidgets` list. Every time a user updates their profile, their custom dashboard layout is permanently reset to empty.

### 2.3 Dependency Injection Fragility
* **Positional Injection**: `AppModule.kt` relies entirely on positional `get()` parameters for ViewModel injection (e.g., `HomeViewModel(get(), get(), get(), get(), get(), get())`). Any change in constructor signature order could cause silent logical failures.
* **Hidden Singletons**: Repository implementations use `Firebase.auth` and `Firebase.firestore` as default parameters rather than having them injected via Koin, severely degrading testability.

### 2.4 Domain Knowledge Leakage
* **Data Layer Hardcoding**: System expense categories are hardcoded inside `ExpenseRepositoryImpl`, leaking domain logic into the data layer.

## 3. Concurrency & Performance Issues 🟡
### 3.1 Coroutine Accumulation & Memory Leaks
* **Uncancelled Observations**: Methods like `loadHistory()`, `loadDashboard()`, and `observeData()` repeatedly launch new `collect` coroutines without cancelling previous ones. Calling these methods multiple times (e.g., config changes or navigating back and forth) causes compounding parallel flow collections that will drain memory and battery.

### 3.2 Inefficient Client-Side Filtering
* **Bandwidth Waste**: `ObserveMonthlyExpensesUseCase` fetches the *entire* expenses collection from Firestore and then filters it client-side by month. As the user's history grows, this will cause massive unnecessary network reads and potential OOM errors. It must use Firestore `.where()` server-side queries.

### 3.3 Recomposition Triggers
* **Home Dashboard Thrashing**: `HomeViewModel.loadDashboard()` rebuilds the widget list on every emission from any of the four underlying real-time flows, triggering full recomposition of the dashboard.
* **Unstable Keys**: `HomeScreen.kt`'s `LazyColumn` uses `widget.javaClass.name` as item keys. If multiple widgets of the same type exist, it will crash.

## 4. UI/UX & Platform Inconsistencies 🔵
### 4.1 Destructive Actions Without Undo
* Swipe-to-delete in history screens and the "×" button in the expenses dashboard delete Firestore records immediately with no confirmation dialogs or undo functionality.

### 4.2 Platform-Specific Code in Shared UI
* `ElectricityBillScreen` uses `android.widget.Toast`, which will crash or fail to compile on iOS, breaking KMP principles.

### 4.3 Error Handling Disparities
* `ExpenseRepository` cleanly uses `Result<Unit>`, but all other repositories throw raw exceptions.
* Silent Failures: `CalculatorRepositoryImpl` catches all exceptions and returns `emptyList()`. Network errors look exactly like empty data states.
* Raw Exception Exposure: `AuthViewModel` exposes raw Firebase `e.message` to the UI.

## 5. Infrastructure & Maintenance Debt 🟣
### 5.1 No Test Coverage
* There are zero business logic tests. Only default template scaffolding exists.

### 5.2 Dependency Drift
* Version catalogs (`libs.versions.toml`) are out of sync with documentation (e.g., AGP is 9.0.1 actual vs. 8.11.2 documented). Material3 is pinned to an alpha release (`1.10.0-alpha05`) in production.

### 5.3 Missing CI/CD & Linting
* No GitHub Actions, `detekt`, or `ktlint`. The lack of static analysis contributes directly to the unhandled coroutine and import issues identified above.

---
## 🎯 Action Plan for Remediation

**Phase 1: Immediate Security Hotfixes**
1. Author and version-control strict `firestore.rules` enforcing `request.auth.uid == resource.data.uid`.
2. Enable `isMinifyEnabled = true` and configure ProGuard rules for release builds.
3. Fix the `CalculateElectricityBillUseCase` and `WaterBillUseCase` math validation.

**Phase 2: State & Concurrency Hardening**
1. Restructure `NavGraph.kt` to share ViewModels across related sub-flows (e.g., using a shared Koin module or NavBackStackEntry scoping).
2. Cancel existing jobs before launching new flow collectors in ViewModels, or use `stateIn`/`shareIn` exclusively.
3. Fix the `ProfileViewModel.saveProfile()` bug wiping dashboard widgets.

**Phase 3: Architecture & Performance**
1. Migrate `ObserveMonthlyExpensesUseCase` to use proper Firestore server-side queries.
2. Standardize error handling to use Kotlin `Result` across all repositories.
3. Remove Android-specific `Toast` calls from Compose code.
