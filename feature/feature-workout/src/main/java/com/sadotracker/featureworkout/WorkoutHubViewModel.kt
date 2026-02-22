package com.sadotracker.featureworkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadotracker.coredatabase.entity.WorkoutEntity
import com.sadotracker.coredatabase.entity.ProgramEntity
import com.sadotracker.coredomain.usecase.GetWorkoutHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

import com.sadotracker.coredomain.usecase.StartWorkoutUseCase
import kotlinx.coroutines.launch

import com.sadotracker.coredatabase.dao.ProgramDao
import com.sadotracker.coredatabase.dao.WorkoutDao
import kotlinx.coroutines.flow.firstOrNull

@HiltViewModel
class WorkoutHubViewModel @Inject constructor(
    getWorkoutHistoryUseCase: GetWorkoutHistoryUseCase,
    private val startWorkoutUseCase: StartWorkoutUseCase,
    private val programDao: ProgramDao,
    private val workoutDao: WorkoutDao
) : ViewModel() {

    val workoutHistory: StateFlow<List<WorkoutEntity>> = getWorkoutHistoryUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun startEmptyWorkout(onNav: (Long) -> Unit) {
        viewModelScope.launch {
            val id = startWorkoutUseCase()
            onNav(id)
        }
    }

    fun startWorkoutFromProgram(programId: Long, onNav: (Long) -> Unit) {
        viewModelScope.launch {
            val program = programDao.getById(programId).firstOrNull() ?: return@launch
            val lastWorkout = workoutDao.getWorkoutsForProgram(programId).firstOrNull()?.firstOrNull()
            
            val nextDayIndex = if (lastWorkout != null) {
                val lastDay = lastWorkout.programDayIndex ?: 0
                (lastDay + 1) % program.cycleDays
            } else {
                0
            }

            val id = startWorkoutUseCase(programId = programId, programDayIndex = nextDayIndex)
            onNav(id)
        }
    }
}
