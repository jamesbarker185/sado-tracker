package com.sadotracker.featureworkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadotracker.coredatabase.entity.WorkoutEntity
import com.sadotracker.coredomain.usecase.GetWorkoutHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

import com.sadotracker.coredomain.usecase.StartWorkoutUseCase
import kotlinx.coroutines.launch

@HiltViewModel
class WorkoutHubViewModel @Inject constructor(
    getWorkoutHistoryUseCase: GetWorkoutHistoryUseCase,
    private val startWorkoutUseCase: StartWorkoutUseCase
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
            val id = startWorkoutUseCase(programId = programId)
            onNav(id)
        }
    }
}
