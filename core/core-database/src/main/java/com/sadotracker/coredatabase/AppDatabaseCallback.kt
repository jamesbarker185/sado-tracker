package com.sadotracker.coredatabase

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sadotracker.coredatabase.dao.AchievementDao
import com.sadotracker.coredatabase.dao.ExerciseDao
import com.sadotracker.coredatabase.dao.MesocycleDao
import com.sadotracker.coredatabase.dao.ProgramDao
import com.sadotracker.coredatabase.dao.ProgramDayDao
import com.sadotracker.coredatabase.dao.ProgramExerciseDao
import com.sadotracker.coredatabase.entity.AchievementEntity
import com.sadotracker.coredatabase.entity.ExerciseEntity
import com.sadotracker.coredatabase.entity.MesocycleEntity
import com.sadotracker.coredatabase.entity.ProgramDayEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit
import javax.inject.Provider

class AppDatabaseCallback(
    private val context: Context,
    private val exerciseDaoProvider: Provider<ExerciseDao>,
    private val programDaoProvider: Provider<ProgramDao>,
    private val programExerciseDaoProvider: Provider<ProgramExerciseDao>,
    private val mesocycleDaoProvider: Provider<MesocycleDao>,
    private val achievementDaoProvider: Provider<AchievementDao>,
    private val programDayDaoProvider: Provider<ProgramDayDao>
) : RoomDatabase.Callback() {

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
                val exercises = exerciseDaoProvider.get().getAll().first()
                if (exercises.isEmpty()) {
                    Log.d("AppDatabaseCallback", "Database is empty, initiating seeding...")
                    seedDatabase()
                    val programId = seedStarterProgram()
                    seedMockData(programId)
                } else {
                    Log.d("AppDatabaseCallback", "Database already seeded with ${exercises.size} exercises.")
                }
            } catch (e: Exception) {
                Log.e("AppDatabaseCallback", "Error checking database state during onOpen", e)
            }
        }
    }

    private suspend fun seedDatabase() {
        try {
            Log.d("AppDatabaseCallback", "Starting database seeding...")
            val jsonString = context.assets.open("exercises.json").bufferedReader().use { it.readText() }
            val jsonDecoder = Json { ignoreUnknownKeys = true }
            val seedExercises = jsonDecoder.decodeFromString<List<ExerciseSeedModel>>(jsonString)
            
            val entities = seedExercises.map { seed ->
                ExerciseEntity(
                    name = seed.name,
                    muscleGroup = seed.muscleGroup,
                    primaryMuscle = seed.primaryMuscle,
                    secondaryMuscle = seed.secondaryMuscle,
                    category = seed.category,
                    equipment = seed.equipment,
                    mechanics = seed.mechanics,
                    modality = seed.modality,
                    forceVector = seed.forceVector,
                    isCustom = seed.isCustom,
                    instructions = seed.instructions,
                    imageResId = seed.imageResId,
                    restTimeSecs = seed.restTimeSecs
                )
            }
            
            exerciseDaoProvider.get().insertAll(entities)
            Log.d("AppDatabaseCallback", "Successfully seeded ${entities.size} exercises.")
        } catch (e: Exception) {
            Log.e("AppDatabaseCallback", "Error seeding database", e)
        }
    }
    private suspend fun seedStarterProgram(): Long {
        try {
            val programDao = programDaoProvider.get()
            val programExerciseDao = programExerciseDaoProvider.get()
            
            // Check if we've already seeded
            // A simple way is to check if any preset program exists. We don't have a direct query
            // for it in the Dao yet, but we will assume it's safe to just check if there's any program
            // at all for this basic seeding, or simply try to insert and rely on we only do this on creation.
            // Since this runs in onCreate, it's a fresh DB.

            val program = com.sadotracker.coredatabase.entity.ProgramEntity(
                name = "7-Day Full-Body Hypertrophy Starter",
                description = "Balancing frequency and volume for optimal growth. Designed for beginners.",
                isPreset = true,
                cycleDays = 7,
                createdAt = System.currentTimeMillis()
            )

            val programId = programDao.insert(program)
            val exerciseDao = exerciseDaoProvider.get()

            // Helper to find exercise by name
            suspend fun findExercise(name: String): com.sadotracker.coredatabase.entity.ExerciseEntity? {
                return exerciseDao.getByName(name)
            }

            val programExercises = mutableListOf<com.sadotracker.coredatabase.entity.ProgramExerciseEntity>()
            
            // Map Day 1 to Day 6 exercises
            val presetLayout = mapOf(
                0 to listOf("Barbell Bench Press", "Incline Dumbbell Bench Press", "Triceps Pushdown", "Overhead Triceps Extension"),
                1 to listOf("Pull-Up", "Seated Cable Row", "Barbell Curl", "Hammer Curl"),
                2 to listOf("Barbell Squat", "Leg Press", "Leg Extension", "Bulgarian Split Squat"),
                3 to listOf("Overhead Press", "Lateral Raise", "Face Pull", "Dumbbell Shrug"),
                4 to listOf("Cable Crossover", "Barbell Row", "Dumbbell Pullover"), // Wait, Dumbbell Pullover not in JSON, using Straight-Arm Pulldown
                5 to listOf("Romanian Deadlift", "Lying Leg Curl", "Standing Calf Raise", "Seated Calf Raise")
                // Day 6 is empty (rest)
            )

            // Adjust any missing exercises to something existing in seed
            val safePresetLayout = presetLayout.mapValues { (_, exercises) ->
                exercises.map { name ->
                    if (name == "Dumbbell Pullover") "Straight-Arm Pulldown" else name
                }
            }

            safePresetLayout.forEach { (dayIndex, exerciseNames) ->
                exerciseNames.forEachIndexed { orderIndex, name ->
                    val exercise = findExercise(name)
                    if (exercise != null) {
                        // Custom rep range for Lateral Raise
                        val customRepMin = if (name == "Lateral Raise") 15 else null
                        val customRepMax = if (name == "Lateral Raise") 20 else null

                        programExercises.add(
                            com.sadotracker.coredatabase.entity.ProgramExerciseEntity(
                                programId = programId,
                                exerciseId = exercise.id,
                                orderIndex = orderIndex,
                                dayIndex = dayIndex,
                                defaultSets = 3,
                                customRepMin = customRepMin,
                                customRepMax = customRepMax,
                                weightIncrementKg = 2.5,
                                restTimeSecs = exercise.restTimeSecs
                            )
                        )
                    } else {
                        Log.w("AppDatabaseCallback", "Could not find seeded exercise: $name")
                    }
                }
            }

            // Seed all 7 days (including rest days)
            val daysToInsert = (1..7).map { dayNumber ->
                ProgramDayEntity(
                    programId = programId,
                    dayNumber = dayNumber,
                    isRestDay = dayNumber == 7 // Day 7 is rest in this split
                )
            }
            programDayDaoProvider.get().insertAll(daysToInsert)

            if (programExercises.isNotEmpty()) {
                programExerciseDao.insertAll(programExercises)
                Log.d("AppDatabaseCallback", "Successfully seeded starter program with ${programExercises.size} exercises across ${daysToInsert.size} days.")
            }
            return programId
        } catch (e: Exception) {
            Log.e("AppDatabaseCallback", "Error seeding starter program", e)
            return -1L
        }
    }

    private suspend fun seedMockData(programId: Long) {
        if (programId == -1L) return

        try {
            val mesocycleDao = mesocycleDaoProvider.get()
            val achievementDao = achievementDaoProvider.get()

            val now = System.currentTimeMillis()
            val oneWeekMs = TimeUnit.DAYS.toMillis(7)

            // Past completed mesocycle (Started 13 weeks ago, ended 1 week ago)
            mesocycleDao.insert(
                MesocycleEntity(
                    programId = programId,
                    startedAt = now - (13 * oneWeekMs),
                    endedAt = now - oneWeekMs,
                    recommendedWeeks = 12
                )
            )

            // Active mesocycle (Started 1 week ago)
            mesocycleDao.insert(
                MesocycleEntity(
                    programId = programId,
                    startedAt = now - oneWeekMs,
                    endedAt = null,
                    recommendedWeeks = 12
                )
            )

            // Some mock achievements
            achievementDao.insert(
                AchievementEntity(
                    id = "meso_1_week",
                    title = "1-Week Streak",
                    description = "Completed your first week of the mesocycle.",
                    unlockedAt = now - (12 * oneWeekMs) // Unlocked during past meso
                )
            )
            achievementDao.insert(
                AchievementEntity(
                    id = "meso_4_weeks",
                    title = "4-Week Consistency",
                    description = "A full month of consistent training!",
                    unlockedAt = now - (9 * oneWeekMs) // Unlocked during past meso
                )
            )
            achievementDao.insert(
                AchievementEntity(
                    id = "meso_12_weeks",
                    title = "Mesocycle Master",
                    description = "12 weeks of dedication!",
                    unlockedAt = now - oneWeekMs // Unlocked at end of past meso
                )
            )

            Log.d("AppDatabaseCallback", "Successfully seeded mock mesocycles and achievements.")
        } catch (e: Exception) {
            Log.e("AppDatabaseCallback", "Error seeding mock data", e)
        }
    }
}
