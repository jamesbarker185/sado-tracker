# Phase 1 Technical Specification: Foundation & Manual Core Flow

**Project:** Sado Tracker
**Phase:** 1 of 4
**Last Updated:** 2026-02-21
**Status:** Pre-Development / Architecture

---

## 1. Objectives

Phase 1 establishes the entire foundation the app will be built on. All subsequent phases depend on the decisions made here being correct.

| Objective | Description |
|-----------|-------------|
| **Project Scaffold** | Set up the Kotlin + CMP + Jetpack Compose base project with correct module boundaries |
| **Room Database** | Design and implement the offline-first local SQLite DB |
| **Exercise Library** | Pre-seed a comprehensive, correctly-tagged exercise list (available offline, no internet required) |
| **Program Builder** | UX to search, filter, and assemble exercises into saved training programs |
| **Live Workout** | The core tracking experience: log sets, see targets, finish & save |

---

## 2. Project Structure

Use a clean **multi-module architecture** from day one to support future phases cleanly.

```
sado-tracker/
├── app/                    # Android entry point, DI root, NavGraph
├── core/
│   ├── core-database/      # Room DB, DAOs, Entities, Migrations
│   ├── core-domain/        # Business logic, Use Cases, Repository interfaces
│   ├── core-data/          # Repository implementations, data mappers
│   └── core-ui/            # Shared Compose components, theme, typography
├── feature/
│   ├── feature-workout/    # Workout Hub + Live Workout screens
│   ├── feature-programs/   # Program creation & management
│   └── feature-you/        # You/Profile/Analytics page
└── build-logic/            # Convention plugins, shared Gradle config
```

**Rationale:** Keeping features isolated prevents AI/camera code (Phase 3) from polluting the core tracking logic and allows for clean unit testing of each layer.

---

## 3. Dependency Injection

Use **Hilt** (recommended over Koin for CMP + Jetpack Compose alignment).

```kotlin
// Hilt modules defined in core-database and core-data
// Each feature module uses @HiltViewModel
```

Key DI bindings to wire up in Phase 1:
- `AppDatabase` → `@Singleton`
- All DAOs → provided from `AppDatabase`
- Repository implementations bound to repository interfaces

---

## 4. Database Architecture (Room / SQLite)

### 4.1 Design Principles
- **Offline-First**: The app must function fully without any internet access. The bundled exercise library is seeded at first launch from a local JSON asset.
- **Single Source of Truth**: Room DB is the only data source. No remote sync in Phase 1.
- **Forward-Compatible**: Schema must support the `rom_consistency_score` and `ROM_Profiles` fields even if unused in Phase 1, to avoid painful migrations later.

---

### 4.2 Entity: `Exercise`
This is the most critical schema decision. Every exercise must be classifiable along multiple independent axes to support powerful filtering.

```sql
CREATE TABLE exercises (
    id              INTEGER PRIMARY KEY,
    name            TEXT NOT NULL,
    -- Muscle Targeting (Anatomical)
    muscle_group    TEXT NOT NULL,       -- General region: "Chest", "Back", "Legs", etc.
    primary_muscle  TEXT NOT NULL,       -- e.g. "Pectoralis Major (Sternal)"
    secondary_muscle TEXT,              -- e.g. "Triceps Brachii, Anterior Deltoid"
    -- Exercise Classification Axes (for filtering & smart suggestions)
    category        TEXT NOT NULL,       -- "Barbell", "Dumbbell", "Machine", "Bodyweight", "Cable"
    equipment       TEXT NOT NULL,       -- "Barbell", "Dumbbell", "None", etc.
    mechanics       TEXT NOT NULL,       -- "Compound" | "Isolation"
    modality        TEXT NOT NULL,       -- "Bilateral" | "Unilateral"
    force_vector    TEXT NOT NULL,       -- "Horizontal Push" | "Vertical Push" | "Horizontal Pull"
                                        -- | "Vertical Pull" | "Hip Hinge" | "Knee Dominant" | "None"
    -- Metadata
    is_custom       INTEGER NOT NULL DEFAULT 0,  -- 0 = seeded, 1 = user-created
    instructions    TEXT,               -- Brief form cue notes
    image_res_id    TEXT                -- Local drawable resource name (optional)
);
```

**Why `force_vector` matters:** This is the key axis for building balanced programs. It lets the app (in Phase 2) warn a user if their program is all horizontal push and no horizontal pull, indicating a rotator-cuff risk. It also powers the "swap exercise" recommendation engine.

**Expanded `force_vector` taxonomy:**
| Value | Example Exercises |
|-------|-------------------|
| `Horizontal Push` | Bench Press, Push-Up, Cable Fly |
| `Vertical Push` | Overhead Press, Lateral Raise |
| `Horizontal Pull` | Barbell Row, Seated Cable Row |
| `Vertical Pull` | Pull-Up, Lat Pulldown |
| `Hip Hinge` | Romanian Deadlift, Good Morning |
| `Knee Dominant` | Squat, Leg Press, Lunge |
| `Carry` | Farmer's Walk, Suitcase Carry |
| `None` | Curl, Tricep Extension (isolated) |

---

### 4.3 Entity: `Program`
```sql
CREATE TABLE programs (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    name        TEXT NOT NULL,
    description TEXT,
    created_at  INTEGER NOT NULL    -- Unix timestamp (milliseconds)
);
```

---

### 4.4 Entity: `program_exercises` (Join Table)
```sql
CREATE TABLE program_exercises (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    program_id  INTEGER NOT NULL REFERENCES programs(id) ON DELETE CASCADE,
    exercise_id INTEGER NOT NULL REFERENCES exercises(id),
    order_index INTEGER NOT NULL,       -- Manual drag-and-drop ordering (Phase 1: just sequential)
    default_sets INTEGER NOT NULL DEFAULT 3  -- How many sets to pre-populate in Live Workout
);
```

---

### 4.5 Entity: `Workout`
```sql
CREATE TABLE workouts (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    date        INTEGER NOT NULL,       -- Unix timestamp (milliseconds)
    duration_ms INTEGER,                -- Null until workout is finished
    program_id  INTEGER REFERENCES programs(id),  -- Null for ad-hoc workouts
    notes       TEXT                    -- Optional free-text session notes
);
```

---

### 4.6 Entity: `Set`
```sql
CREATE TABLE sets (
    id                    INTEGER PRIMARY KEY AUTOINCREMENT,
    workout_id            INTEGER NOT NULL REFERENCES workouts(id) ON DELETE CASCADE,
    exercise_id           INTEGER NOT NULL REFERENCES exercises(id),
    set_number            INTEGER NOT NULL,   -- 1-indexed order within the exercise
    weight_kg             REAL NOT NULL,
    reps                  INTEGER NOT NULL,
    rir                   INTEGER,             -- Reps In Reserve (0-5), nullable if user skips
    is_partial            INTEGER NOT NULL DEFAULT 0,  -- 1 if marked as partial rep set
    rom_consistency_score REAL                -- NULL in Phase 1; used from Phase 3 onwards
);
```

---

### 4.7 Entity: `rom_profiles` (Stub for Phase 3)
Include this table now to avoid a migration that requires user data backfill later.

```sql
CREATE TABLE rom_profiles (
    id                  INTEGER PRIMARY KEY AUTOINCREMENT,
    exercise_id         INTEGER NOT NULL UNIQUE REFERENCES exercises(id) ON DELETE CASCADE,
    top_y_threshold     REAL NOT NULL,
    bottom_y_threshold  REAL NOT NULL,
    calibrated_at       INTEGER NOT NULL    -- Unix timestamp
);
```

---

### 4.8 DAOs Summary

| DAO | Key Methods |
|-----|-------------|
| `ExerciseDao` | `getAll()`, `search(query)`, `filterBy(mechanic, force, category)`, `insert()` |
| `ProgramDao` | `getAll()`, `getById(id)`, `insert()`, `delete()` |
| `ProgramExerciseDao` | `getExercisesForProgram(programId)`, `insert()`, `delete()`, `reorder()` |
| `WorkoutDao` | `getAll()`, `getById(id)`, `insert()`, `update()` |
| `SetDao` | `getSetsForWorkout(workoutId)`, `getSetsForExercise(exerciseId)`, `getLastSetForExercise(exerciseId)`, `insert()`, `delete()` |

**The `getLastSetForExercise` method is critical for Phase 1** — it powers the "Target" display in Live Workout.

---

## 5. Exercise Library (Pre-Seeded Data)

### 5.1 Strategy
- Package a `exercises.json` file in `core-database/src/main/assets/`.
- On first launch (detected by checking if exercises table is empty), parse and insert the full list via a coroutine on the IO dispatcher.
- Use Room's `RoomDatabase.Callback.onCreate()` to trigger seeding.

### 5.2 Recommended Minimum Exercise Set (~60 exercises for Phase 1)

The seeded list should cover every `force_vector` category and major equipment type. Suggested breakdown:

| Category | Count | Example Exercises |
|----------|-------|-------------------|
| **Compound Barbell** | 12 | Squat, Bench Press, Deadlift, OHP, Barbell Row, RDL, Hip Thrust, Front Squat, Close-Grip Bench, Sumo DL, Pendlay Row, Incline Bench |
| **Compound Dumbbell** | 8 | DB Bench, DB Row, DB OHP, Goblet Squat, DB Romanian DL, DB Lunges, DB Hip Thrust, Arnold Press |
| **Compound Bodyweight** | 6 | Pull-Up, Push-Up, Dip, Inverted Row, Nordic Curl, Bulgarian Split Squat |
| **Compound Cable/Machine** | 8 | Cable Row, Lat Pulldown, Leg Press, Hack Squat, Chest Press Machine, Shoulder Press Machine, Assisted Dip, Cable Fly |
| **Isolation Dumbbell** | 12 | Bicep Curl, Hammer Curl, Lateral Raise, DB Skull Crusher, DB Fly, DB Front Raise, Concentration Curl, DB Kickback, DB Pullover, Spider Curl, DB Shrug, Zottman Curl |
| **Isolation Cable/Machine** | 10 | Cable Curl, Tricep Pushdown, Face Pull, Pec Deck, Leg Curl, Leg Extension, Calf Raise, Seated Leg Curl, Cable Lateral Raise, Cable Kickback |
| **Barbell Isolation** | 4 | EZ-Bar Curl, Barbell Skull Crusher, Barbell Shrug, Close-Grip Curl |

### 5.3 Sample JSON Entry Structure
```json
{
  "id": 1,
  "name": "Barbell Bench Press",
  "muscle_group": "Chest",
  "primary_muscle": "Pectoralis Major (Sternal)",
  "secondary_muscle": "Triceps Brachii, Anterior Deltoid",
  "category": "Barbell",
  "equipment": "Barbell, Bench",
  "mechanics": "Compound",
  "modality": "Bilateral",
  "force_vector": "Horizontal Push",
  "is_custom": 0,
  "instructions": "Retract scapula, arch naturally, bar to lower chest, drive through heels."
}
```

---

## 6. Navigation Architecture

Use **Jetpack Navigation with Compose** (type-safe routes via `kotlinx.serialization`).

```
NavGraph
  ├── WorkoutHubRoute        ← Default start destination
  ├── ProgramSelectionRoute
  ├── ProgramCreationRoute
  │     └── ExerciseSearchRoute (modal bottom sheet or separate screen)
  ├── ProgramDetailRoute
  ├── LiveWorkoutRoute
  └── YouRoute
```

The bottom navigation bar hosts two top-level destinations: **Workout** and **You**.

---

## 7. Repository + Use Case Layer

Keep all business logic out of ViewModels. ViewModels only call Use Cases.

**Key Use Cases for Phase 1:**

| Use Case | Logic |
|----------|-------|
| `GetWorkoutHistoryUseCase` | Returns `Flow<List<Workout>>` ordered by date desc |
| `StartWorkoutUseCase` | Creates a new `Workout` row, returns its ID |
| `LogSetUseCase` | Validates and inserts a `Set` row |
| `GetTargetForExerciseUseCase` | Queries `SetDao.getLastSetForExercise()`, returns target weight/rep suggestion |
| `FinishWorkoutUseCase` | Stamps `duration_ms` on the workout row, validates it has at least 1 set |
| `SearchExercisesUseCase` | Full-text search + multi-axis filter on `ExerciseDao` |
| `SaveProgramUseCase` | Wraps insertion of `Program` + `ProgramExercise` in a single Room transaction |

---

## 8. State Management Pattern

Use **`StateFlow` + `UiState` sealed classes** per screen.

```kotlin
// Example for Live Workout
sealed class LiveWorkoutUiState {
    object Loading : LiveWorkoutUiState()
    data class Active(
        val workoutId: Long,
        val exercises: List<ExerciseWithSets>,
        val elapsedTimeMs: Long
    ) : LiveWorkoutUiState()
    object Finished : LiveWorkoutUiState()
    data class Error(val message: String) : LiveWorkoutUiState()
}
```

Each `@HiltViewModel` exposes a single `StateFlow<UiState>` that the Compose UI `collectAsStateWithLifecycle()`.

---

## 9. Key Libraries & Versions (Phase 1)

```toml
# gradle/libs.versions.toml

[versions]
kotlin = "2.0.x"
agp = "8.x"
compose-bom = "2024.x"
hilt = "2.51"
room = "2.6.x"
navigation = "2.7.x"
coroutines = "1.8.x"
kotlinx-serialization = "1.6.x"
datastore = "1.0.x"

[libraries]
# Compose
compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "compose-bom" }
compose-ui = { group = "androidx.compose.ui", name = "ui" }
compose-material3 = { group = "androidx.compose.material3", name = "material3" }

# Room
room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }

# Hilt
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-compiler", version.ref = "hilt" }

# Navigation
navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigation" }
hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version = "1.2.0" }

# DataStore (for user preferences, e.g. unit preference kg/lbs)
datastore-preferences = { group = "androidx.datastore", name = "datastore-preferences", version.ref = "datastore" }
```

> **Note:** Pin exact versions once the project is initialized. The above are indicative of stable versions as of early 2026.

---

## 10. DataStore: User Preferences

Use **Jetpack DataStore** (not SharedPreferences) for small persistent user settings.

Preferences to store in Phase 1:
- `preferred_unit`: `"kg"` or `"lbs"` (conversion is purely display-layer)
- `onboarding_complete`: `Boolean`

In future phases: `mediapipe_model_downloaded`, `deload_notification_enabled`.

---

## 11. Thread Safety & Coroutines

- **All Room queries** must be called from the IO dispatcher (`Dispatchers.IO`). DAOs marked `suspend` or returning `Flow` handle this automatically.
- **Seeding** happens in `RoomDatabase.Callback.onCreate()` using `CoroutineScope(Dispatchers.IO).launch {}`.
- **ViewModels** use `viewModelScope` exclusively. Never expose raw coroutine jobs to the UI.

---

## 12. Testing Strategy

| Layer | Test Type | Tool |
|-------|-----------|------|
| DAOs | Instrumented | `androidx.room:room-testing`, in-memory DB |
| Use Cases | Unit | JUnit 5 + Mockk |
| ViewModels | Unit | JUnit 5 + Turbine (Flow testing) |
| Compose UI | UI Snapshot | Paparazzi or Compose `ComposeTestRule` |

Write DAO tests before the UI. The DB layer is the most critical correctness boundary in Phase 1.

---

## 13. Phase 1 Delivery Checklist

- [ ] Multi-module project scaffold created and building
- [ ] Room DB implemented with all Phase 1 + stub entities/DAOs
- [ ] `exercises.json` asset created with ≥60 exercises, all fields correctly tagged
- [ ] Seeding logic implemented and verified on fresh install
- [ ] Hilt DI wired across all modules
- [ ] Bottom navigation with Workout and You tabs functional
- [ ] Exercise search + multi-axis filter UI working (offline)
- [ ] Program creation, naming, and saving working
- [ ] Program list browsable
- [ ] Live Workout screen: set table renders, sets can be added/deleted
- [ ] Target weight/rep display working from previous session data
- [ ] Finish Workout saves correctly to DB
- [ ] Workout history list renders in Workout Hub
- [ ] DataStore preferences for kg/lbs unit working
- [ ] DAO unit tests passing
- [ ] Use Case unit tests passing
