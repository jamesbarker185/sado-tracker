package com.sadotracker.coredomain.usecase

import androidx.room.withTransaction
import com.sadotracker.coredatabase.AppDatabase
import com.sadotracker.coredatabase.entity.ProgramEntity
import com.sadotracker.coredatabase.entity.ProgramExerciseEntity
import javax.inject.Inject

class SaveProgramUseCase @Inject constructor(
    private val database: AppDatabase
) {
    suspend operator fun invoke(
        name: String, 
        description: String?, 
        exerciseIds: List<Long>
    ): Long {
        require(name.isNotBlank()) { "Program name cannot be empty" }
        require(exerciseIds.isNotEmpty()) { "Program must contain at least one exercise" }

        return database.withTransaction {
            val program = ProgramEntity(
                name = name,
                description = description,
                createdAt = System.currentTimeMillis()
            )
            
            val programId = database.programDao().insert(program)
            
            val programExercises = exerciseIds.mapIndexed { index, exerciseId ->
                ProgramExerciseEntity(
                    programId = programId,
                    exerciseId = exerciseId,
                    orderIndex = index,
                    defaultSets = 3 // Per Phase 1 spec
                )
            }
            
            database.programExerciseDao().insertAll(programExercises)
            programId
        }
    }
}
