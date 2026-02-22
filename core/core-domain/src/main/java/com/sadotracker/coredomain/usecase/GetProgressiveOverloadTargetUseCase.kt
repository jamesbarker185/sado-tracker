package com.sadotracker.coredomain.usecase

import com.sadotracker.coredatabase.dao.ProgramExerciseDao
import com.sadotracker.coredatabase.dao.SetDao
import com.sadotracker.coredomain.datastore.UserSettingsManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetProgressiveOverloadTargetUseCase @Inject constructor(
    private val setDao: SetDao,
    private val programExerciseDao: ProgramExerciseDao,
    private val userSettingsManager: UserSettingsManager,
    private val engine: ProgressiveOverloadEngine
) {
    suspend operator fun invoke(exerciseId: Long, programExerciseId: Long?): OverloadSuggestion? {
        val lastSet = setDao.getLastSetForExercise(exerciseId) ?: return null

        var repMin = userSettingsManager.repRangeMin.first()
        var repMax = userSettingsManager.repRangeMax.first()
        var weightIncrement = 2.5

        if (programExerciseId != null) {
            programExerciseDao.getById(programExerciseId)?.let { programExercise ->
                val customMin = programExercise.customRepMin
                val customMax = programExercise.customRepMax
                if (customMin != null && customMax != null) {
                    repMin = customMin
                    repMax = customMax
                }
                weightIncrement = programExercise.weightIncrementKg
            }
        }

        return engine.suggest(
            lastWeightKg = lastSet.weightKg,
            lastReps = lastSet.reps,
            repMin = repMin,
            repMax = repMax,
            weightIncrementKg = weightIncrement
        )
    }
}
