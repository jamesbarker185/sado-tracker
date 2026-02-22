package com.sadotracker.coredomain.usecase

import com.sadotracker.coredatabase.dao.WorkoutDao
import com.sadotracker.coredatabase.entity.WorkoutEntity
import javax.inject.Inject

class StartWorkoutUseCase @Inject constructor(
    private val workoutDao: WorkoutDao
) {
    suspend operator fun invoke(
        programId: Long? = null,
        mesocycleId: Long? = null,
        programDayIndex: Int? = null,
        notes: String? = null
    ): Long {
        val workout = WorkoutEntity(
            date = System.currentTimeMillis(),
            durationMs = null,
            programId = programId,
            mesocycleId = mesocycleId,
            programDayIndex = programDayIndex,
            notes = notes
        )
        return workoutDao.insert(workout)
    }
}
