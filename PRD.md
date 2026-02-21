# Product Requirements Document (PRD): Sado Tracker

## 1. Product Vision & Core Mission
**Mission:** Use proven science (hypertrophy mechanics) to grow muscle for 90% of the population.
**Strict Constraint:** OMIT all dieting and nutrition features. Focus 100% on lifting progression.
**Privacy & Security:** All AI/Gait analysis must happen strictly "on-edge" (on-device). No video uploads or cloud processing of visual data for privacy reasons.
**Target Audience:** Beginners to intermediate lifters looking for consistent, science-based muscle growth without the friction of overcomplicated tracking.

## 2. Core Training Philosophy (The Logic Layer)
- **Consistency First:** Scale volume for beginners. Prioritize lifestyle-fit over "optimal" volume to prevent quitting and promote long-term adherence.
- **Mechanical Tension:** Target 0–3 Reps in Reserve (RIR). Sets must reach high intensity to stimulate hypertrophy effectively.
- **Reactive Deload System:** If performance (weight × reps) drops for 14 consecutive days on a given exercise or globally, automatically trigger a 50% volume deload week to allow for central nervous system (CNS) and muscular recovery.
- **Form Consistency:** Use the "My Form" AI system to ensure every rep hits a consistent Range of Motion (ROM), preventing ego-lifting and ensuring standardisation across training blocks.

## 3. Technical Stack
- **Language:** Kotlin
- **UI Framework:** Compose Multiplatform (CMP) + Jetpack Compose
- **Design System:** Material 3 (Styled as "Modern Dark/Slate" - Shadcn-Mobile aesthetic)
- **AI/Computer Vision:** MediaPipe Pose Landmarker (Primary), MoveNet (Fallback)
- **Camera API:** CameraX API
- **Local Database:** Room Database (SQLite)

## 4. UI/UX Component Requirements (Shadcn Aesthetic)
- **Palette:** "Shadcn-Mobile" (Slate/Zinc palette, high-contrast, modern dark mode).
- **Styling:** Rounded corners, 1px borders (Neutral-800), sleek and minimal.
- **Card Design:** Neutral-900 background, 12px corner radius.
- **Typography:** Inter/System Sans for general text. JetBrains Mono for numerical data to ensure tabular alignment (Kg, Reps, Time).
- **Icons:** Lucide-style minimalist icons.
- **Grids:** Responsive grid layouts for exercise tiles (e.g., 2-column on mobile displays).
- **Haptics:** Strategic use of vibration feedback (e.g., on successful rep completion during "My Form" tracking).

## 5. Data Schema (Room/SQL)
The local database will be structured to support offline-first, private-by-design data tracking.

- **`Exercises`**: `id`, `name`, `category`, `muscle_group` (General), `primary_muscle`, `secondary_muscle`, `equipment`, `mechanics` (Compound/Isolation), `modality` (Bilateral/Unilateral), `force_vector` (Push/Pull/None), `is_custom`.
- **`Programs`**: `id`, `name`, `created_at`.
- **`Program_Exercises`**: `id`, `program_id`, `exercise_id`, `order_index`.
- **`Workouts`**: `id`, `date`, `duration`, `program_id` (optional), `notes`.
- **`Sets`**: `id`, `workout_id`, `exercise_id`, `weight`, `reps`, `rir`, `is_partial`, `rom_consistency_score`.
- **`ROM_Profiles`**: `id`, `exercise_id`, `top_y_threshold`, `bottom_y_threshold` (Calibration data for AI tracking).

## 6. User Flow & Architecture

```mermaid
graph TD
    A[Root: Navigation Bar] --> B[Workout Page]
    A --> C[You Page]
    
    B --> B1[Big Card: Add New Workout]
    B --> B2[Vertical List: Previous Workouts]
    
    B1 --> D[Program Page / Selection]
    D --> D1[Create New Program]
    D --> D2[Search Existing Programs]
    
    D1 --> E[Exercise Search/Filter Grid]
    E --> E1[Add to Program]
    E1 --> E2[Name & Save Program]
    
    D2 --> F[Program Details View]
    F --> G[Live Workout Page]
    
    G --> G1[Exercise List]
    G1 --> G2[Set Table: Kg | Reps]
    G2 --> G3[Add/Delete Set]
    G --> H[Finish & Save]
    H --> B
```

## 7. Page Specifications

### A. You Page (Analytics & Profile)
- **User Statistics:** Total volume lifted, workout frequency (visualized via a GitHub-style heatmap), and personal records.
- **Settings:** Privacy settings, on-device AI model management (downloading/updating models locally).

### B. Workout Page (The Hub)
- **Top Component:** "Add New Workout" (Large, highly visible Action Card).
- **History Feed:** Vertical list of "Big Cards" representing previous sessions, ordered by date (most recent first). Tapping a card opens a detailed view of that specific session's performance.

### C. Program Page (Creation & Management)
- **Creation UX:** Search & Filter interface utilizing small grid tiles (with placeholder images/icons for muscle groups).
- **Metadata Tagging:** Exercises can be heavily filtered by Equipment, Mechanics, Modality, and Force Vector allowing users to easily swap equivalent exercises.

### D. Live Workout Page (The Core Experience)
- **Structure:** Scrolling list of exercises for the current session, pre-populated with 3-set default tables (or matching previous volume).
- **Interactions:** Inline add/delete functionality for sets. 
- **Smart Targets:** Prominently display "Target" data (e.g., previous weight × previous reps + 1) to encourage progressive overload.

## 8. The "My Form" Algorithm (The USP)
The core unique selling proposition (USP) relies on on-device pose estimation to ensure rep consistency.

**Calibration Phase:**
1. Stream CameraX frames to the MediaPipe Pose Landmarker.
2. User performs 2 warm-up/calibration reps.
3. System captures $y$ coordinates for the primary joint involved in the movement.
4. Baseline Calculation: $\text{ROM\_Baseline} = |y_{max} - y_{min}|$.

**Rep Counting State Machine:**
- **`IDLE`**: $y = y_{max}$ (Starting position).
- **`DESCENDING`**: $y \to y_{min}$ (Eccentric phase tracking).
- **`BOTTOM_REACHED`**: $y \le (y_{min} + 10\% \text{ leeway})$ (Bottom of the movement threshold).
- **`ASCENDING`**: $y \to y_{max}$ (Concentric phase tracking).
- **`REFRESH`**: $y \ge y_{max} \implies \text{Rep\_Count}++$ (Rep completed successfully, trigger haptic feedback).

*Failure to reach `BOTTOM_REACHED` triggers a "partial rep" flag rather than a full rep.*

## 9. Proposed Development Phases

### Phase 1: Foundation & Manual Core Flow (Priority)
- **Infrastructure:** Room DB setup establishing `Exercise`, `Program`, and `Workout` schemas.
- **Workout Hub:** Implement the "Workout Page" featuring the "Add New" card and basic history list.
- **Program Builder:** Develop the UX for searching/filtering exercises and saving custom programs.
- **Manual Tracking:** Build the Live Workout page with an editable Kg/Reps table and "Finish Workout" save logic.

### Phase 2: Intelligence & Progressive Overload
- **Smart Targets:** Implement background logic to fetch data from the previous same exercise/program and seamlessly display "Target: [Prev Weight] x [Prev Reps + 1]".
- **Analytics (You Page):** Introduce heatmaps, volume charts, and PR tracking based on Phase 1 data accumulation.
- **History Deep-dives:** Enable the ability to tap previous workout cards to review detailed session performance.

### Phase 3: The "My Form" USP (AI Integration)
- **Camera Integration:** Implement CameraX with real-time MediaPipe skeleton overlay logic.
- **Calibration UI:** Design specialized UX for the 2-rep warm-up to set ROM baselines effectively without frustrating the user.
- **Auto-Rep Counter:** Deploy the real-time state machine for rep counting and partial rep detection during Live Workouts.
- **Haptics:** Integrate vibration feedback synchronized with successful rep completion state changes.

### Phase 4: Reactive Systems & Polish
- **Reactive Deload:** Implement background algorithms analyzing 14-day strength trends to intelligently trigger automatic deload notifications.
- **Performance Optimizations:** Ensure integration of the GPU delegate for MediaPipe to guarantee a steady 30fps without draining the battery excessively.
- **Final UX Polish:** Refinement of the "Shadcn-Mobile" aesthetic, smoothing animations, UI state transitions, and overall polish.

## 10. Minimum Specification Guardrails
- **OS Target:** Android API 29+ (Android 10 minimum).
- **AI Processing:** Must use GPU Delegate for MediaPipe Pose to maintain a strict 30fps real-time target.
- **Camera Stream:** Must utilize non-blocking `ImageAnalysis.Analyzer` to prevent UI thread freezing during frame processing.
