package com.sadotracker.coredomain.usecase

import androidx.room.withTransaction
import com.sadotracker.coredatabase.AppDatabase
import com.sadotracker.coredatabase.entity.ProgramDayEntity
import com.sadotracker.coredatabase.entity.ProgramEntity
import com.sadotracker.coredatabase.entity.ProgramExerciseEntity
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class SaveProgramUseCase @Inject constructor(
    private val database: AppDatabase
) {
    data class DayInput(
        val isRestDay: Boolean,
        val exerciseIds: List<Long>
    )

    suspend operator fun invoke(
        name: String, 
        description: String?, 
        days: List<DayInput>,
        existingProgramId: Long? = null
    ): Long {
        require(name.isNotBlank()) { "Program name cannot be empty" }
        require(days.isNotEmpty()) { "Program must have at least one day" }

        return database.withTransaction {
            val programId = if (existingProgramId != null) {
                val existing = database.programDao().getById(existingProgramId).firstOrNull()
                    ?: throw IllegalArgumentException("Program not found")
                
                val updated = existing.copy(
                    name = name,
                    description = description,
                    cycleDays = days.size
                )
                database.programDao().insert(updated) // REPLACE conflict strategy will update
                
                // Clear existing days and exercises
                database.programDayDao().deleteForProgram(existingProgramId)
                database.programExerciseDao().deleteForProgram(existingProgramId)
                
                existingProgramId
            } else {
                val program = ProgramEntity(
                    name = name,
                    description = description,
                    cycleDays = days.size,
                    createdAt = System.currentTimeMillis()
                )
                database.programDao().insert(program)
            }
            
            days.forEachIndexed { dayNumberZeroBased, dayInput ->
                val dayNumber = dayNumberZeroBased + 1
                
                // 1. Insert Day metadata
                database.programDayDao().insertAll(listOf(
                    ProgramDayEntity(
                        programId = programId,
                        dayNumber = dayNumber,
                        isRestDay = dayInput.isRestDay
                    )
                ))
                
                // 2. Insert exercises for this day if not a rest day
                if (!dayInput.isRestDay) {
                    val programExercises = dayInput.exerciseIds.mapIndexed { index, exerciseId ->
                        ProgramExerciseEntity(
                            programId = programId,
                            exerciseId = exerciseId,
                            orderIndex = index,
                            dayIndex = dayNumberZeroBased,
                            defaultSets = 3
                        )
                    }
                    database.programExerciseDao().insertAll(programExercises)
                }
            }
            
            programId
        }
    }
}
