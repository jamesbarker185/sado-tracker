package com.sadotracker.coredomain.usecase

import com.sadotracker.coredatabase.dao.SetDao
import com.sadotracker.coredatabase.entity.SetEntity
import javax.inject.Inject

class GetTargetForExerciseUseCase @Inject constructor(
    private val setDao: SetDao
) {
    /**
     * Retrieves the last set performed for a given exercise to act as a target
     * for progressive overload.
     */
    suspend operator fun invoke(exerciseId: Long): SetEntity? {
        return setDao.getLastSetForExercise(exerciseId)
    }
}
