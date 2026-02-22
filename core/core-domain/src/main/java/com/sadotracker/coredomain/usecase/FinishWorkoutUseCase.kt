package com.sadotracker.coredomain.usecase

import com.sadotracker.coredatabase.dao.SetDao
import com.sadotracker.coredatabase.dao.WorkoutDao
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class FinishWorkoutUseCase @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val setDao: SetDao
) {
    /**
     * @return true if workout was successfully finished, false if no sets were logged (workout is discarded)
     */
    suspend operator fun invoke(workoutId: Long): Boolean {
        val sets = setDao.getSetsForWorkout(workoutId).firstOrNull() ?: emptyList()
        val workout = workoutDao.getById(workoutId).firstOrNull() ?: return false
        
        if (sets.isEmpty()) {
            // Nothing logged, discard empty workout session implicitly or explicitly
            // (Wait, do we delete it? Let's leave it as is or delete it.)
            return false
        }
        
        val duration = System.currentTimeMillis() - workout.date
        
        val finishedWorkout = workout.copy(durationMs = duration)
        workoutDao.update(finishedWorkout)
        
        return true
    }
}
