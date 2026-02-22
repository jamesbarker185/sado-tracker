package com.sadotracker.featureworkout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadotracker.coredatabase.dao.ExerciseDao
import com.sadotracker.coredatabase.entity.ExerciseEntity
import com.sadotracker.coredatabase.entity.SetEntity
import com.sadotracker.coredomain.usecase.FinishWorkoutUseCase
import com.sadotracker.coredomain.usecase.GetTargetForExerciseUseCase
import com.sadotracker.coredomain.usecase.LogSetUseCase
import com.sadotracker.coredatabase.dao.WorkoutDao
import com.sadotracker.coredatabase.dao.ProgramExerciseDao
import kotlinx.coroutines.flow.firstOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LiveSetState(
    val setNumber: Int,
    val weightKg: String = "",
    val reps: String = "",
    val isCompleted: Boolean = false,
    val previousTarget: String? = null
)

data class LiveExerciseState(
    val exercise: ExerciseEntity,
    val sets: List<LiveSetState>,
    val isExpanded: Boolean = false
)

@HiltViewModel
class LiveWorkoutViewModel @Inject constructor(
    private val finishWorkoutUseCase: FinishWorkoutUseCase,
    private val logSetUseCase: LogSetUseCase,
    private val exerciseDao: ExerciseDao,
    private val workoutDao: WorkoutDao,
    private val programExerciseDao: ProgramExerciseDao,
    private val getTargetForExerciseUseCase: GetTargetForExerciseUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val workoutId: Long = checkNotNull(savedStateHandle["workoutId"])

    private val _elapsedTimeSeconds = MutableStateFlow(0L)
    val elapsedTimeSeconds = _elapsedTimeSeconds.asStateFlow()

    private val _exercises = MutableStateFlow<List<LiveExerciseState>>(emptyList())
    val exercises = _exercises.asStateFlow()
    
    val allExercises = exerciseDao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList<ExerciseEntity>()
    )
    
    // For mock ad-hoc adding of exercises. In a real app we'd navigate to the exercise search.
    // For now we'll just query all exercises and grab one.
    
    init {
        startTimer()
        loadProgramExercisesIfNeeded()
    }

    private fun loadProgramExercisesIfNeeded() {
        viewModelScope.launch {
            val workout = workoutDao.getById(workoutId).firstOrNull() ?: return@launch
            val programId = workout.programId ?: return@launch
            
            val programExercises = programExerciseDao.getExercisesForProgram(programId).firstOrNull() ?: return@launch
            programExercises.forEach { progEx ->
                addExercise(progEx.exerciseId, initiallyExpanded = false)
            }
        }
    }
    
    private fun startTimer() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                _elapsedTimeSeconds.update { it + 1 }
            }
        }
    }

    fun addExercise(exerciseId: Long, initiallyExpanded: Boolean = true) {
        viewModelScope.launch {
            val exercise = exerciseDao.getById(exerciseId) ?: return@launch
            val previousSet = getTargetForExerciseUseCase(exerciseId)
            val targetStr = previousSet?.let { "${it.weightKg}kg x ${it.reps}" }
            
            val newExerciseState = LiveExerciseState(
                exercise = exercise,
                sets = listOf(LiveSetState(setNumber = 1, previousTarget = targetStr)),
                isExpanded = initiallyExpanded
            )
            _exercises.update { it + newExerciseState }
        }
    }

    fun addSet(exerciseIndex: Int) {
        _exercises.update { list ->
            val updated = list.toMutableList()
            val exState = updated[exerciseIndex]
            val prevSet = exState.sets.lastOrNull()
            
            val newSet = LiveSetState(
                setNumber = exState.sets.size + 1,
                weightKg = prevSet?.weightKg ?: "",
                reps = prevSet?.reps ?: "",
                previousTarget = prevSet?.previousTarget
            )
            updated[exerciseIndex] = exState.copy(sets = exState.sets + newSet)
            updated
        }
    }
    
    fun updateSet(exerciseIndex: Int, setIndex: Int, weight: String, reps: String) {
        _exercises.update { list ->
            val updated = list.toMutableList()
            val exState = updated[exerciseIndex]
            val setList = exState.sets.toMutableList()
            setList[setIndex] = setList[setIndex].copy(weightKg = weight, reps = reps)
            updated[exerciseIndex] = exState.copy(sets = setList)
            updated
        }
    }

    fun completeSet(exerciseIndex: Int, setIndex: Int) {
        val exState = _exercises.value[exerciseIndex]
        val setState = exState.sets[setIndex]
        if (setState.isCompleted || setState.weightKg.isBlank() || setState.reps.isBlank()) return

        viewModelScope.launch {
            logSetUseCase(
                workoutId = workoutId,
                exerciseId = exState.exercise.id,
                setNumber = setState.setNumber,
                weightKg = setState.weightKg.toDoubleOrNull() ?: 0.0,
                reps = setState.reps.toIntOrNull() ?: 0
            )
            
            _exercises.update { list ->
                val updated = list.toMutableList()
                val currentEx = updated[exerciseIndex]
                val setList = currentEx.sets.toMutableList()
                setList[setIndex] = setList[setIndex].copy(isCompleted = true)
                updated[exerciseIndex] = currentEx.copy(sets = setList)
                updated
            }
        }
    }

    fun toggleExerciseExpansion(exerciseIndex: Int) {
        _exercises.update { list ->
            val updated = list.toMutableList()
            updated[exerciseIndex] = updated[exerciseIndex].copy(isExpanded = !updated[exerciseIndex].isExpanded)
            updated
        }
    }

    fun finishWorkout(onFinished: () -> Unit) {
        viewModelScope.launch {
            finishWorkoutUseCase(workoutId)
            onFinished()
        }
    }
}
