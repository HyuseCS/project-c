# Gemini CLI - Project Context: ProjectC

This document provides foundational mandates, architectural context, and development guidelines for **ProjectC**, a student-focused all-in-one Kotlin Multiplatform (KMP) application.

## Project Overview
- **App**: Student-focused all-in-one mobile app
- **Stack**: Kotlin Multiplatform (KMP), Jetpack Compose, Firebase, Koin, MVVM + Clean Architecture
- **Package**: `com.hyuse.projectc`
- **Modules**: `shared` (commonMain/androidMain/iosMain) + `composeApp` (Android UI) + `iosApp` (Native SwiftUI)

### Local Environment
- **OS**: CachyOS (Arch Linux)
- **IDE**: IntelliJ IDEA
- **Android SDK**: `/opt/android-sdk` (API 35)
- **ADB Path**: `/opt/android-sdk/platform-tools/adb`
- **Test Device**: Pixel 6a (Android 16)

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

## Project Status & Roadmap

### Phase 1: Foundation + Firebase Auth ✅ (2026-04-09)
- Clean Architecture structure implemented in `:shared`.
- Firebase Auth via GitLive SDK in `commonMain`.
- Koin DI setup for both shared logic and Android UI.
- Auth-aware `NavGraph` and Material 3 UI for Login/SignUp/Home.

### Next Phases (TODO)
- **Phase 2**: Firestore User Profiles (`users/{uid}/profile`).
- **Phase 3**: Utility Calculators (Electricity/Water) with history.
- **Phase 4**: Dashboard Home Screen with summary tiles.
- **Phase 5**: Location-Based Reminders (Geofencing + WorkManager).

## Architecture

The project follows **Clean Architecture** principles:

### 1. `:shared` Module (Multiplatform)
- **`domain`**: Models (`User`), Repository interfaces (`AuthRepository`), and Use cases.
- **`data`**: Repository implementations (Firebase Auth/Firestore).
- **`di`**: `SharedModule.kt` for core dependencies.

### 2. `:composeApp` Module (Android UI)
- **`ui`**: ViewModels and Compose screens.
- **`navigation`**: `NavGraph.kt` handling routing.
- **`di`**: `AppModule.kt` for UI-specific DI.

### 3. `iosApp` (iOS UI)
- Native SwiftUI application consuming the shared framework.

## Firestore Data Structure (Planned)
```
users/{uid}
  ├── profile: { name, email, university, course }
  ├── calculators/{calcId}: { type, inputs, result, timestamp }
  └── reminders/{remId}: { title, datetime, recurrence, twist_data }
```

## Development Conventions

### Coding Style
- Follow standard Kotlin conventions.
- ViewModels → Use Cases → Repository Interfaces.
- **DI**: Use Koin. Shared logic in `SharedModule.kt`, Android UI in `AppModule.kt`.

### Firebase Integration
- GitLive KMP SDK allows Firebase usage in `commonMain`.
- Android: `google-services.json` in `composeApp/`.
- iOS: `GoogleService-Info.plist` in `iosApp/`.

## Building and Running

### Android
```bash
./gradlew :composeApp:assembleDebug
```

### iOS
```bash
./gradlew :shared:embedAndSignAppleFrameworkForXcode
```
