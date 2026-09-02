# ProjectC - All Context

Last updated: 2026-09-02 (harness install / initial STUDY)

This file is the root context entrypoint for the repo.

Use it for two things:

1. quick routing to the right context pack or root file
2. broad architecture and repository understanding

Start here before loading deeper context files.

---

## Project Description

**ProjectC** is a student-focused all-in-one mobile app: a "hub" that lets university students
managing independent living track bills, expenses, and location-based reminders in one place.
Solo project (`HyuseCS`). Package `com.hyuse.projectc`.

Kotlin Multiplatform. Android is the working target and the only one exercised on device
(Pixel 6a, Android 16, installed over ADB). `iosApp` is a native SwiftUI shell that consumes the
shared framework; it is scaffolded but not the active development surface.

Feature areas shipped so far (see `GEMINI.md` §Project Status for the full phase log):
Firebase Auth, Firestore user profiles, electricity + water utility calculators, a customizable
real-time dashboard with Canvas charts, an expense tracker with custom categories, and
location-based reminders (Play Services geofencing + WorkManager time fallback, SQLDelight
local-first storage, free tier capped at 3 active reminders).

Product intent and design language live in `PRODUCT.md` and `DESIGN.md`: brand personality is
"Sophisticated, Premium, Lucid" — restrained color, invisible hierarchy, tactile glass surfaces.
Treat those two files as the source of truth for UI work.

**Machine-local files:** `GEMINI.md` and `memory.md` are git-ignored. They hold the richest
project context on this machine but are not visible to CI or to a fresh clone. Anything durable
that lives only there should be mirrored into this file.

---

## How This File Works (the `all-*.md` Convention)

Every `process/context/` directory has one `all-*.md` entrypoint that acts as an attachable quick router for that domain. This root file (`all-context.md`) is the top-level router. Context groups each have their own `all-{group}.md` entrypoint.

**The pattern:**

```
process/context/
  all-context.md                      <-- THIS FILE: root router
  planning/
    all-planning.md                   <-- group router for planning
    example-simple-prd.md             <-- deep doc within the group
    example-complex-prd.md            <-- deep doc within the group
  tests/
    all-tests.md                      <-- group router for tests
    debugging-and-pitfalls.md         <-- deep doc within the group
    e2e-tests.md                      <-- deep doc within the group
  database/
    all-database.md                   <-- group router for database
    schema-guide.md                   <-- deep doc within the group
    migration-procedures.md           <-- deep doc within the group
```

**How agents use it:**

1. Agent reads `all-context.md` first (this file)
2. Finds the relevant context group from the routing tables below
3. Reads that group's `all-{group}.md` entrypoint
4. Only then loads the specific deep doc needed

This layered routing keeps context windows small. Never load the whole `process/context/` tree.

**What each `all-{group}.md` must contain:**

- Scope (what the group covers and does NOT cover)
- Read-when rules (when an agent should load this group)
- Quick procedures or decision rules
- Source paths (list of deeper docs in the group)
- Update triggers (when to refresh this group's content)
- Routing to deeper docs within the group

---

## Quick Start

For most substantial tasks:

1. read this file first
2. choose the smallest relevant root file or context group from the tables below
3. only then load deeper files

---

## Current Root Entry Points

<!-- The two tables below (Root Entry Points + Context Groups) are GENERATED from each
     context doc's frontmatter by `discover-context.mjs --emit-routing`. Do NOT hand-edit
     between the GENERATED markers — your edits will be overwritten on the next rebuild.
     To change a row, edit the owning doc's frontmatter (description / keywords) and re-emit.
     `--check-routing` fails lint if this block drifts from the frontmatter on disk. -->

<!-- GENERATED:routing -->
| File | Read when |
|---|---|
| `process/context/all-context.md` | any substantial planning, research, review, or implementation task |
| `process/context/planning/all-planning.md` | SIMPLE vs COMPLEX plan calibration and example PRD references for ProjectC |
| `process/context/tests/all-tests.md` | Test runner selection, commands, and verification order — Gradle/Kotlin unit tests in :shared and :composeApp, Android Lint, on-device ADB verification |

## Current Context Groups

| Group | Entry point | Scope |
|---|---|---|
| `planning/` | `process/context/planning/all-planning.md` | SIMPLE vs COMPLEX plan calibration and example PRD references for ProjectC |
| `tests/` | `process/context/tests/all-tests.md` | Test runner selection, commands, and verification order — Gradle/Kotlin unit tests in :shared and :composeApp, Android Lint, on-device ADB verification |
<!-- /GENERATED:routing -->

## Task Routing Table

| If the task involves... | Load first | Then load |
|---|---|---|
| architecture or stack questions | this file | `GEMINI.md` for dependency versions and phase history |
| testing or verification | this file, `tests/all-tests.md` | the module's test source under `*/src/*Test/` |
| creating a new plan | this file, `planning/all-planning.md` | the relevant active plan in `process/general-plans/active/` |
| UI / UX work | this file | `PRODUCT.md`, `DESIGN.md`, `design-system/projectc/` |
| reminders / geofencing / background work | this file | `composeApp/.../platform/`, `shared/.../data/database/` |
| Firestore or data-model work | this file (§Data Storage below) | `shared/.../domain/model/`, `shared/.../data/repository/` |
| build, CI, or release | this file (§Environment and Configuration) | `.github/workflows/ci.yml`, `gradle/libs.versions.toml` |
| context maintenance | this file | run the `vc-audit-context` skill after edits |

## Context Group Lifecycle

Context groups are durable knowledge domains, not feature folders.

Create a group when:

- a topic has 3+ durable docs
- a single doc exceeds roughly 800 lines with separable subtopics
- multiple agents repeatedly need only one slice of a large context file
- the topic maps to a stable operational domain (tests, infra, database, auth, UI, workflows, etc.)

Do not create a group when:

- the content is a temporary report
- the content is a plan or execution artifact
- the topic is feature-specific and belongs in `process/features/...`

Move or split one group at a time. Use `all-{group}.md` entrypoints. Run the `audit-context` skill after every context organization change.

## Naming Convention

There are no `README.md` files inside `process/context/`.

Canonical entrypoints use `all-*.md`:

- root: `process/context/all-context.md`
- group: `process/context/{group}/all-{group}.md`

Each `all-{group}.md` file should act as the attachable quick router for that domain:

- tell the agent what the group covers
- give quick procedures and decision rules
- route to smaller deeper files

## Context Update Protocol

When durable project knowledge changes:

1. update the smallest relevant context file
2. update this file if routing, ownership, naming, or groups changed
3. update the owning `all-{group}.md` entrypoint when a group exists
4. run `audit-context`

---

## Repository Structure

```
Project_C/
  shared/                                  -- KMP shared module
    src/commonMain/kotlin/com/hyuse/projectc/
      domain/{model,repository,usecase}    -- models, repo interfaces, use cases
      data/{repository,database}           -- Firebase + SQLDelight implementations
      di/                                  -- SharedModule.kt (Koin)
    src/commonMain/sqldelight/com/hyuse/projectc/shared/database/
      ReminderEntity.sq, 1.sqm             -- reminder table + migration
    src/{androidMain,iosMain}/kotlin/      -- expect/actual platform code
    src/commonTest/kotlin/                 -- shared unit tests
  composeApp/                              -- Android app (Compose UI)
    src/androidMain/kotlin/com/hyuse/projectc/
      ui/{auth,home,profile,utilities,expenses,reminders,components,theme}
      navigation/                          -- NavGraph.kt, auth/profile gating
      platform/{geofencing,worker,receiver,notification}
      di/                                  -- AppModule.kt (Koin)
    src/androidMain/res/                   -- Android resources
    src/androidUnitTest/kotlin/            -- Android unit tests
  iosApp/                                  -- native SwiftUI shell (not actively developed)
  design-system/projectc/pages/            -- design references
  gradle/libs.versions.toml                -- version catalog (single source for deps)
  process/                                 -- RIPER-5 harness: context, plans, protocols
  .github/workflows/ci.yml                 -- build + lint + test gate for PRs into main
  graphify-out/                            -- generated knowledge graph
```

Root docs: `GEMINI.md` (authoritative context, git-ignored), `PRODUCT.md`, `DESIGN.md`,
`README.md` (KMP template boilerplate, low value), `requirements.md`, `AUDIT.md`, `planning/`.

## Technology Stack

- **Language:** Kotlin 2.3.20 (Kotlin Multiplatform), Swift for the iOS shell
- **UI:** Compose Multiplatform 1.10.3, Material 3; Android UI lives in `:composeApp`
- **Architecture:** MVVM + Clean Architecture (ViewModel -> UseCase -> Repository interface)
- **DI:** Koin 4.1.1 (`SharedModule.kt` for shared, `AppModule.kt` for Android UI)
- **Cloud:** Firebase via the GitLive KMP SDK 2.4.0 (Auth + Firestore, usable from `commonMain`);
  Google Services plugin 4.4.4
- **Local DB:** SQLDelight 2.0.2 — reminders only, chosen because background
  `BroadcastReceiver` code needs synchronous local reads
- **Navigation:** Navigation Compose 2.9.2
- **Background:** WorkManager 2.10.0 (time-trigger fallback),
  Play Services Location 21.3.0 (geofencing), Maps Compose 6.1.1
- **Async / serialization:** kotlinx-coroutines 1.10.2, kotlinx-serialization 1.10.0
- **Build:** Gradle wrapper + AGP 8.11.2, JDK 17, Android SDK API 36
- **CI:** GitHub Actions (`.github/workflows/ci.yml`) on PRs into `main` and pushes to `main`

Dependency versions are declared in `gradle/libs.versions.toml`. Change versions there, never
inline in a `build.gradle.kts`.

## Key Patterns and Conventions

**Layering:** ViewModel -> UseCase -> Repository *interface* (in `shared/domain/repository/`).
Implementations live in `shared/data/repository/` and are bound in Koin. UI never touches a
repository implementation or Firebase directly.

**Where code goes:** anything platform-neutral goes in `shared/commonMain`. Android-only APIs
(geofencing, WorkManager, notifications, receivers) go in
`composeApp/src/androidMain/.../platform/`, behind an interface declared in `shared/domain`.
`GeofenceManager` is the model to copy for that pattern.

**Storage split:** cloud state (profile, bills, expenses, categories) is Firestore and syncs in
real time via snapshot listeners. Reminders are local-first in SQLDelight, because a geofence
`BroadcastReceiver` must evaluate triggers synchronously without a network round trip.

**Naming:** standard Kotlin conventions. PascalCase composables and classes, camelCase
functions and properties, screens named `<Area>Screen.kt`, view models `<Area>ViewModel.kt`.

**DI registration:** every new repository, use case, or view model must be registered in
`SharedModule.kt` (shared) or `AppModule.kt` (Android UI), or Koin fails at runtime, not at
compile time.

**Navigation:** all routes are declared in `NavGraph.kt`, which also gates on auth state and
profile completion. Routes with arguments use the `route?arg={arg}` form
(e.g. `add_reminder?reminder_id={id}`).

**UI language:** follow `PRODUCT.md` and `DESIGN.md` — restrained palette with one accent,
typography and whitespace for hierarchy instead of borders, semi-transparent "glass" surfaces.
Do not introduce 1px technical borders or dense card lists.

**Known product limits, keep them intact:** free tier allows 3 active reminders; the OS caps
geofences at 100 (the project budgets 90).

## Environment and Configuration

**Config files:** `settings.gradle.kts`, `build.gradle.kts` (root + per module),
`gradle/libs.versions.toml` (version catalog), `gradle.properties`,
`local.properties` (git-ignored, holds the Android SDK path).

**Secrets and git-ignored files (never commit these):**
- `composeApp/google-services.json` — Firebase Android config, contains API keys
- `iosApp/GoogleService-Info.plist` — Firebase iOS config
- `local.properties`, `GEMINI.md`, `memory.md`, `.gemini/`, `.antigravitycli/`

CI receives the Firebase config through the `GOOGLE_SERVICES_JSON` repo secret
(base64 of `composeApp/google-services.json`), decoded in the workflow.

**Local environment (this machine):**
- OS CachyOS (Arch), IDE IntelliJ IDEA, JDK 17
- Android SDK `/home/hyuse/Android/Sdk` (API 36)
- ADB `/home/hyuse/Android/Sdk/platform-tools/adb`
- Test device: Pixel 6a, Android 16. Install with `./gradlew :composeApp:installDebug`.

**Build commands:**
```bash
./gradlew :composeApp:assembleDebug      # build Android debug APK
./gradlew :composeApp:installDebug       # build + install on the connected device
./gradlew :composeApp:lintDebug          # Android Lint
./gradlew :shared:embedAndSignAppleFrameworkForXcode   # iOS framework
```

## Scan Metadata

- Generated: 2026-09-02
- HEAD: 6c70e1a (main)
- Mode: initial STUDY during harness install (vibecode-pro-max-kit 3.2.5)
- Build system: Gradle wrapper (no JS package manager in this repo)
- Sources read: `GEMINI.md`, `PRODUCT.md`, `DESIGN.md`, `README.md`, `.github/workflows/ci.yml`,
  repo tree for `shared/`, `composeApp/`, `iosApp/`, `design-system/`

---

## Agent Harness Install Record

Installed 2026-09-02 (issue #7). Kit: `vibecode-pro-max-kit` 3.2.5 (`.vc-version`), ported by hand
from `/home/hyuse/Desktop/VeentApps/jojo-mobile` rather than via `install.sh`, so nothing was
backed up or overwritten.

**Installed surfaces**

| Surface | State |
|---|---|
| `.claude/skills/` (33) | installed — discovered by Claude Code and by opencode natively |
| `.claude/agents/` (15) | installed — RIPER-5 mode agents + specialists |
| `.claude/hooks/` (12) + `.claude/settings.json` | installed — Claude Code only |
| `.agents/skills` | symlink to `.claude/skills/` (Codex discovery path) |
| `.codex/agents/*.toml` (15) | installed — mirrors required by the parity validators |
| `process/` | protocols, seeds, context, `general-plans/{active,backlog,completed}`, `features/` |
| `CLAUDE.md`, `AGENTS.md` | managed protocol files, taken from a clean install |

**Deliberately NOT installed**

- `.codex/hooks/`, `.codex/config.toml` — Codex is not used on this repo.
- `.opencode/` agents and the opencode hook bridge. opencode reads `.claude/skills/` and
  `AGENTS.md` natively, but it ignores `.claude/agents/` and `.claude/settings.json` hooks.
  Running the harness under opencode needs `.opencode/agents/*.md` conversions plus a plugin
  (`.opencode/plugin/`) that shells out to the `.claude/hooks/*.cjs` files. `PreToolUse` maps to
  `tool.execute.before`, `PostToolUse` to `tool.execute.after`, `SessionStart` to `event`;
  `SubagentStart` and `Stop` have no direct equivalent. Not done — see issue #7.

**Known-red validator**

`vc-audit-vc/scripts/validate-guide-sync.mjs` fails. It expects the root `README.md` to carry the
kit's agent and skill catalog tables. This repo's `README.md` is the Kotlin Multiplatform project
readme and was left alone on purpose. Every other install-time validator passes (15/15).
The five `vc-audit-plans` validators require a plan file argument and are not install-time checks.

**Pre-existing kit bug:** 12 of the 15 agents declare a `PreToolUse` hook pointing at
`.claude/hooks/agent-write-guard.mjs`, which does not ship with the kit. Those per-agent write
guards are inert here and in every other install of 3.2.5.
