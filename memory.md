# Project C — Development Memory

> This file tracks development progress and context for AI-assisted development.
> **Do not commit this file** — it's in .gitignore.

## Project Overview
- **App**: Student-focused all-in-one mobile app
- **Stack**: KMP (Kotlin Multiplatform), Jetpack Compose, Firebase, Koin, MVVM + Clean Architecture
- **Package**: `com.hyuse.projectc`
- **Modules**: `shared` (commonMain/androidMain/iosMain) + `composeApp` (Android UI) + `iosApp` (deferred)
- **OS**: CachyOS (Arch Linux), IntelliJ IDEA
- **Android SDK**: /opt/android-sdk (API 35), adb at `/opt/android-sdk/platform-tools/adb`
- **Test device**: Pixel 6a (Android 16, USB debugging)
- **Deploy command**: `./gradlew :composeApp:installDebug`

## Dependency Versions
| Library | Version | Catalog Key |
|---|---|---|
| Kotlin | 2.3.20 | `kotlin` |
| Compose Multiplatform | 1.10.3 | `composeMultiplatform` |
| AGP | 8.11.2 | `agp` |
| Firebase GitLive SDK | 2.4.0 | `firebaseGitlive` |
| Google Services Plugin | 4.4.4 | `googleServices` |
| Koin | 4.1.1 | `koin` |
| Navigation Compose | 2.9.2 | `navigationCompose` |
| kotlinx-coroutines | 1.10.2 | `kotlinxCoroutines` |
| kotlinx-serialization | 1.10.0 | `kotlinxSerialization` |

## Completed Phases

### Phase 1: Foundation + Firebase Auth ✅ (2026-04-09)
- Clean Architecture in `:shared` with domain/data/di layers.
- Firebase Auth via GitLive SDK in `commonMain`.
- Koin DI for shared logic and Android UI.
- Auth-aware `NavGraph` and Material 3 UI for Login/SignUp/Home.

### Phase 2: Firestore User Profiles ✅ (2026-04-10)
- Domain: `UserProfile` model, `ProfileRepository` interface.
- Data: `ProfileRepositoryImpl` using GitLive Firestore SDK.
- Use cases: `GetProfileUseCase`, `SaveProfileUseCase`, `ObserveProfileUseCase`.
- UI: `ProfileViewModel`, `ProfileScreen`, `HomeViewModel`.
- Navigation: Profile onboarding check after login — redirects to profile setup if missing.
- HomeScreen: Clickable Profile tile navigates to profile edit.
- Firestore path: `users/{uid}` (top-level document, not subcollection).
- JVM target upgraded to 17 in both modules.
- **Verified**: Builds pass, Auth works, Profile flow works on device.

### Phase 3: Utilities Hub & Electricity Bill Calculator ✅ (2026-04-10)
- **Utilities Hub**: Replaced "Calculators" tile on Home with "Utilities" hub screen.
- **Electricity Bill Calculator**: Meter reading × rate = bill.
- **Key features**:
  - `Billing Period` support (Month + Year tracking).
  - Automatically fetches the true previous chronological reading.
  - Rate automatically persists globally per user.
  - Native Android Toast alerts and Confirmation dialogs.
  - Pre-defined list of global currencies with country names.
  - `ElectricityBillHistoryScreen` for detached viewing.
- **Electricity Usage Predictor**: Implemented session-based prediction component. Calculate potential load profiles and query local billing rates dynamically.

### Phase 4: Dashboard Home Screen ✅ (2026-04-12)
- **Profile Update**: Added an optional `nickname` field to `UserProfile` and integrated it into the profile setup/edit flow.
- **Dynamic Dashboard**: Migrated Home screen to a component-based `DashboardWidget` architecture.
- **Electricity Consumption Graph**: Built a custom `Canvas`-based bar chart widget displaying the current calendar year's monthly electricity usage.
- **Real-time Updates**: Hooked `ObserveProfileUseCase` into `HomeViewModel` for live personalized greetings ("Welcome back, {nickname}").
- **Quick Actions**: Refactored the dashboard tiles to only show active modules ("Utilities", "Profile").

## Current Phase

### Planning Next Phase...

## Future Phases (TODO)

### Phase 5: Location-Based Reminders
- Geofencing, WorkManager, FCM push notifications
- Reminders stored at `users/{uid}/reminders/{remId}`

## Architecture Reference

```
shared/src/commonMain/kotlin/com/hyuse/projectc/
├── domain/
│   ├── model/{User,UserProfile,ElectricityBillResult}.kt
│   ├── repository/{AuthRepository,ProfileRepository,CalculatorRepository}.kt
│   └── usecase/
│       ├── {SignUp,Login,Logout,GetCurrentUser}UseCase.kt
│       ├── {GetProfile,SaveProfile,ObserveProfile}UseCase.kt
│       └── {CalculateElectricityBill,SaveElectricityBill,GetElectricityBillHistory}UseCase.kt
├── data/repository/{AuthRepositoryImpl,ProfileRepositoryImpl,CalculatorRepositoryImpl}.kt
└── di/SharedModule.kt

composeApp/src/androidMain/kotlin/com/hyuse/projectc/
├── ProjectCApplication.kt, MainActivity.kt, App.kt
├── di/AppModule.kt
├── navigation/NavGraph.kt
├── ui/auth/{AuthViewModel,LoginScreen,SignUpScreen}.kt
├── ui/home/{HomeViewModel,HomeScreen}.kt
├── ui/profile/{ProfileViewModel,ProfileScreen}.kt
├── ui/utilities/
│   ├── UtilitiesHubScreen.kt
│   ├── ElectricityBillScreen.kt
│   ├── ElectricityBillHistoryScreen.kt
│   └── ElectricityBillViewModel.kt
└── ui/theme/Theme.kt
```

## Firestore Data Structure
```
users/{uid}                          ← profile data (name, email, university, course)
users/{uid}/calculators/{calcId}     ← calculation history
users/{uid}/reminders/{remId}        ← future: reminders
```

## Known Non-Issues
- `GoogleApiManager` / `FlagRegistrar` DEVELOPER_ERROR logs — GMS internal noise, does not affect Firebase Auth or Firestore. Safe to ignore.

## iOS Notes (deferred)
- Add `GoogleService-Info.plist` to iOS project
- Add Firebase iOS SDK via Swift Package Manager
- Call `FirebaseApp.configure()` in SwiftUI entry point
- All shared code works as-is
