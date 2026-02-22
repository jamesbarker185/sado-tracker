package com.sadotracker.coredomain.usecase

import com.sadotracker.coredatabase.dao.WorkoutDao
import com.sadotracker.coredatabase.entity.WorkoutEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWorkoutHistoryUseCase @Inject constructor(
    private val workoutDao: WorkoutDao
) {
    operator fun invoke(): Flow<List<WorkoutEntity>> {
        return workoutDao.getAll()
    }
}
