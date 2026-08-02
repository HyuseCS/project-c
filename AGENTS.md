# Project Context

Read `GEMINI.md` in the repo root for authoritative project context: architecture, modules, dependency versions, conventions, and build/run instructions.

Key points to remember:
- Kotlin Multiplatform app (`shared` + `composeApp` + `iosApp`), package `com.hyuse.projectc`.
- Android test device is a Pixel 6a; install builds via ADB only (`/home/hyuse/Android/Sdk/platform-tools/adb`, or `./gradlew :composeApp:installDebug`).
- Stack: Compose Multiplatform, Firebase, Koin, MVVM + Clean Architecture, SQLDelight.
