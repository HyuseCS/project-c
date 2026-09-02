---
name: context:all-tests
description: "Test runner selection, commands, and verification order — Gradle/Kotlin unit tests in :shared and :composeApp, Android Lint, on-device ADB verification"
keywords: test, tests, testing, gradle, kotlin, junit, lint, verification, runner, adb, instrumentation, kmp, android
related: []
date: 02-09-26
---

# ProjectC - All Tests

Last updated: 2026-09-02 (harness install / initial STUDY)

Attach this file first when the task involves testing, verification, or test debugging.

This is the fast operator guide for the testing surface:

- which runner to use
- what command to start with
- how to quickly debug common failures
- which deeper file to read next

Do not load the whole `process/context/tests/` folder by default. Start here, then drill down.

---

## How This File Works

This is the `all-tests.md` entrypoint for the `tests/` context group. It follows the `all-*.md` routing convention:

1. Agents read `all-context.md` first and get routed here for testing tasks
2. This file gives quick decision rules and commands
3. For deeper details, agents follow the routing table below to specific docs

As the project grows, add deeper docs to this group (e.g., `e2e-tests.md`, `debugging-and-pitfalls.md`) and add routing entries below. This file stays the fast-start entrypoint.

---

## What This Covers

- test runner selection
- quick commands by package
- fast debugging procedures
- current testing gaps worth remembering

## Read This When

Use this file when you need to:

- run tests after implementation
- decide between test runners
- debug failing tests

## Quick Routing

<!-- STUDY: Replace with routing entries to deeper test docs as they are created. -->
<!-- Start with an empty table. Add rows as deeper docs are created during the project lifecycle. -->

<!-- Example of what a filled-in routing table looks like (from a mature project): -->

<!--
| If you need... | Read next |
|---|---|
| commands and scripts by package | `scripts-and-commands.md` |
| architecture, mocks, auth model, and runner split | `architecture-and-patterns.md` |
| Playwright setup, auth flow, and current specs | `e2e-tests.md` |
| failing-test triage and runtime debugging | `debugging-and-pitfalls.md` |
| known gaps and future test-system fixes | `known-issues.md` |
-->

(No deeper test docs yet. Add routing entries here as they are created.)

## Quick Decision Guide

This project has **no JS test runner**. Everything runs through the Gradle wrapper (`./gradlew`).

- **Kotlin unit tests (`kotlin.test` / JUnit)** — the only automated tests that exist today.
  - `:shared` common tests: `shared/src/commonTest/kotlin/`
  - `:composeApp` Android unit tests: `composeApp/src/androidUnitTest/kotlin/`
- **Android Lint** — static analysis, gates CI. Not a test runner but part of every verification.
- **Compile check** — on a KMP project, `compileDebugKotlinAndroid` catches most breakage and is
  much faster than a full assemble. Use it as the first gate.
- **On-device verification (manual)** — Pixel 6a over ADB. Required for anything touching
  geofencing, WorkManager, notifications, or permissions: those cannot be proven by unit tests.
- **No instrumentation (`androidTest`), no Compose UI tests, no e2e.** See §Known Gaps.

## Default Verification Order

Unless the task clearly needs a different path:

1. run the narrowest existing automated test
2. use unit/integration tests before browser tests
3. use end-to-end tests only when the real UI is the thing being verified

## Commands

| Target | Runner | Command |
|---|---|---|
| `:shared` | Kotlin/JUnit | `./gradlew :shared:testDebugUnitTest` |
| `:composeApp` | Kotlin/JUnit | `./gradlew :composeApp:testDebugUnitTest` |
| both | Kotlin/JUnit | `./gradlew :shared:testDebugUnitTest :composeApp:testDebugUnitTest` |
| compile gate | Kotlin compiler | `./gradlew :shared:compileDebugKotlinAndroid` |
| lint | Android Lint | `./gradlew :composeApp:lintDebug` |
| build | AGP | `./gradlew :composeApp:assembleDebug` |
| install on device | AGP + ADB | `./gradlew :composeApp:installDebug` |

**Full local gate — the same steps CI runs, in CI's order:**
```bash
./gradlew :shared:compileDebugKotlinAndroid
./gradlew :shared:testDebugUnitTest :composeApp:testDebugUnitTest
./gradlew :composeApp:lintDebug
./gradlew :composeApp:assembleDebug
```

Lint report lands in `composeApp/build/reports/lint-results-debug.html`.

## Debugging Quick Reference

- **Firebase config is required to build.** `composeApp/google-services.json` is git-ignored.
  Without it the Google Services plugin fails the build. CI decodes it from the
  `GOOGLE_SERVICES_JSON` secret; locally the file must already exist.
- **JDK 17.** A different JDK on `JAVA_HOME` produces confusing AGP/Kotlin errors.
- **Gradle daemon staleness.** After changing `gradle/libs.versions.toml` or a `build.gradle.kts`,
  add `--refresh-dependencies`, or `./gradlew --stop` if the build behaves impossibly.
- **SQLDelight is code-generated.** After editing `ReminderEntity.sq`, the generated API only
  exists once the module compiles. Compile `:shared` before trusting an IDE error.
- **Koin fails at runtime, not compile time.** A missing registration in `SharedModule.kt` or
  `AppModule.kt` surfaces as a crash on screen entry, not a build error.
- **Background behavior cannot be unit tested here.** Geofence entry, WorkManager firing, and
  notification delivery need a real device plus `adb logcat`. Budget device time for them.
- **Device install path:** `/home/hyuse/Android/Sdk/platform-tools/adb`.

## Known Gaps

Automated coverage is close to zero. Both existing test files are template placeholders
(`SharedCommonTest.kt`, `ComposeAppAndroidUnitTest.kt`), not real coverage.

- No tests for use cases, repositories, or view models in `:shared`.
- No Compose UI tests and no `androidTest` instrumentation source set.
- No tests for the reminder trigger logic (`EvaluateTriggerUseCase`), the geofence receiver, or
  `ReminderTimeWorker` — the highest-risk code in the app is entirely unverified by automation.
- No Firestore rules tests, no offline/sync tests.
- iOS is never built or tested in CI.

Consequence for planning: any claim of "verified" on reminder, permission, or notification work
must cite an on-device check, not a green `./gradlew test` run.
