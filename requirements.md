# Project Tech Stack Requirements

This document outlines the required versions for the technologies, frameworks, and libraries used in **ProjectC**, a Kotlin Multiplatform (KMP) application. Developers should ensure their local environment matches these specifications to guarantee compatibility and a smooth build process.

## Core Languages & Tools

| Technology | Version | Notes |
|---|---|---|
| **Java** | 17 | Required for Gradle build and Android compilation. |
| **Kotlin** | 2.3.20 | Base language for the KMP project. |
| **Gradle** | 8.14.3 | Project build system (configured via Wrapper). |
| **Android Gradle Plugin (AGP)** | 8.13.2 | Required for building Android targets. |

## Android SDK Requirements

| Component | API Level |
|---|---|
| **Compile SDK** | 36 |
| **Target SDK** | 36 |
| **Min SDK** | 24 |

## Frameworks & Primary Libraries

| Library / Framework | Version |
|---|---|
| **Compose Multiplatform** | 1.10.3 |
| **Material 3 (Compose)** | 1.10.0-alpha05 |
| **Firebase GitLive SDK** | 2.4.0 |
| **Google Services Plugin** | 4.4.4 |
| **Koin (Dependency Injection)** | 4.1.1 |
| **Navigation Compose** | 2.9.2 |

## Kotlin Extensions

| Library | Version |
|---|---|
| **kotlinx-coroutines** | 1.10.2 |
| **kotlinx-serialization** | 1.10.0 |
