package com.sadotracker.coredomain.usecase

import com.sadotracker.coredatabase.dao.SetDao
import com.sadotracker.coredatabase.entity.SetEntity
import javax.inject.Inject

class LogSetUseCase @Inject constructor(
    private val setDao: SetDao
) {
    suspend operator fun invoke(
        workoutId: Long,
        exerciseId: Long,
        setNumber: Int,
        weightKg: Double,
        reps: Int,
        rir: Int? = null,
        isPartial: Boolean = false
    ): Long {
        require(weightKg >= 0.0) { "Weight cannot be negative" }
        require(reps > 0) { "Reps must be greater than 0" }

        val newSet = SetEntity(
            workoutId = workoutId,
            exerciseId = exerciseId,
            setNumber = setNumber,
            weightKg = weightKg,
            reps = reps,
            rir = rir,
            isPartial = isPartial,
            romConsistencyScore = null, // Not used in Phase 1
            restTakenSecs = null
        )
        return setDao.insert(newSet)
    }
}
