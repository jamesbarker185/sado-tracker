package com.sadotracker.featureworkout

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadotracker.coredatabase.dao.ExerciseDao
import com.sadotracker.coredatabase.entity.ExerciseEntity
import com.sadotracker.coredatabase.entity.SetEntity
import com.sadotracker.coredomain.usecase.FinishWorkoutUseCase
import com.sadotracker.coredomain.usecase.GetTargetForExerciseUseCase
import com.sadotracker.coredomain.usecase.LogSetUseCase
import com.sadotracker.coredomain.usecase.UpdateSetRestDurationUseCase
import com.sadotracker.coredatabase.dao.WorkoutDao
import com.sadotracker.coredatabase.dao.ProgramExerciseDao
import com.sadotracker.coredatabase.dao.ProgramDayDao
import com.sadotracker.coredatabase.entity.ProgramDayEntity
import kotlinx.coroutines.flow.firstOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RestTimerState(
    val isActive: Boolean = false,
    val isPaused: Boolean = false,
    val targetSecs: Int = 0,
    val elapsedSecs: Int = 0,
    val isOvertime: Boolean = false,
    val exerciseName: String = "",
    val exerciseIndex: Int? = null,
    val loggedSetId: Long? = null
)

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
    val isExpanded: Boolean = false,
    val restTimeSecs: Int = 120
)

@HiltViewModel
class LiveWorkoutViewModel @Inject constructor(
    private val finishWorkoutUseCase: FinishWorkoutUseCase,
    private val logSetUseCase: LogSetUseCase,
    private val updateSetRestDurationUseCase: UpdateSetRestDurationUseCase,
    private val exerciseDao: ExerciseDao,
    private val workoutDao: WorkoutDao,
    private val programExerciseDao: ProgramExerciseDao,
    private val programDayDao: ProgramDayDao,
    private val getTargetForExerciseUseCase: GetTargetForExerciseUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val workoutId: Long = checkNotNull(savedStateHandle["workoutId"])
    
    private var restTimerJob: Job? = null
    private val toneGenerator = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)

    private val _restTimerState = MutableStateFlow(RestTimerState())
    val restTimerState = _restTimerState.asStateFlow()

    private val _elapsedTimeSeconds = MutableStateFlow(0L)
    val elapsedTimeSeconds = _elapsedTimeSeconds.asStateFlow()

    private val _exercises = MutableStateFlow<List<LiveExerciseState>>(emptyList())
    val exercises = _exercises.asStateFlow()
    
    val allExercises = exerciseDao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList<ExerciseEntity>()
    )
    
    private val _splitDays = MutableStateFlow<List<ProgramDayEntity>>(emptyList())
    val splitDays = _splitDays.asStateFlow()

    private val _currentDayIndex = MutableStateFlow<Int?>(null)
    val currentDayIndex = _currentDayIndex.asStateFlow()
    
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
            val dayIndex = workout.programDayIndex ?: 0
            _currentDayIndex.value = dayIndex
            
            // Load split days for progress bar
            val days = programDayDao.getForProgram(programId).firstOrNull() ?: emptyList()
            _splitDays.value = days

            // Load exercises ONLY for the specific day index
            val programExercises = programExerciseDao.getExercisesForProgram(programId).firstOrNull() ?: return@launch
            programExercises
                .filter { it.dayIndex == dayIndex }
                .forEach { progEx ->
                    addExercise(progEx.exerciseId, initiallyExpanded = false, restTimeSecs = progEx.restTimeSecs)
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

    fun addExercise(exerciseId: Long, initiallyExpanded: Boolean = true, restTimeSecs: Int? = null) {
        viewModelScope.launch {
            val exercise = exerciseDao.getById(exerciseId) ?: return@launch
            val previousSet = getTargetForExerciseUseCase(exerciseId)
            val targetStr = previousSet?.let { "${it.weightKg}kg x ${it.reps}" }
            
            val newExerciseState = LiveExerciseState(
                exercise = exercise,
                sets = listOf(LiveSetState(setNumber = 1, previousTarget = targetStr)),
                isExpanded = initiallyExpanded,
                restTimeSecs = restTimeSecs ?: exercise.restTimeSecs
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

        // 1. Mark as completed in UI immediately
        _exercises.update { list ->
            val updated = list.toMutableList()
            val currentEx = updated[exerciseIndex]
            val setList = currentEx.sets.toMutableList()
            setList[setIndex] = setList[setIndex].copy(isCompleted = true)
            updated[exerciseIndex] = currentEx.copy(sets = setList)
            updated
        }

        // 2. Log set and start timer
        viewModelScope.launch {
            val setId = logSetUseCase(
                workoutId = workoutId,
                exerciseId = exState.exercise.id,
                setNumber = setState.setNumber,
                weightKg = setState.weightKg.toDoubleOrNull() ?: 0.0,
                reps = setState.reps.toIntOrNull() ?: 0
            )
            
            startRestTimer(
                exerciseName = exState.exercise.name, 
                targetSecs = exState.restTimeSecs,
                exerciseIndex = exerciseIndex,
                loggedSetId = setId
            )
        }
    }

    private fun startRestTimer(exerciseName: String, targetSecs: Int, exerciseIndex: Int, loggedSetId: Long) {
        if (_restTimerState.value.isActive) {
            stopRestTimer()
        }

        _restTimerState.value = RestTimerState(
            isActive = true,
            targetSecs = targetSecs,
            exerciseName = exerciseName,
            exerciseIndex = exerciseIndex,
            loggedSetId = loggedSetId
        )
        
        runTimer()
    }

    private fun runTimer() {
        restTimerJob?.cancel()
        restTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _restTimerState.update { state ->
                    if (state.isPaused) return@update state
                    
                    val newElapsed = state.elapsedSecs + 1
                    val nowOvertime = newElapsed >= state.targetSecs
                    
                    if (nowOvertime && !state.isOvertime) {
                        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP)
                        // Vibration logic usually requires Context, opting out for now 
                        // to keep VM clean and avoid build issues.
                    }
                    
                    state.copy(
                        elapsedSecs = newElapsed,
                        isOvertime = nowOvertime
                    )
                }
            }
        }
    }

    fun pauseRestTimer() {
        _restTimerState.update { it.copy(isPaused = true) }
    }

    fun resumeRestTimer() {
        _restTimerState.update { it.copy(isPaused = false) }
    }

    fun stopRestTimer() {
        val currentState = _restTimerState.value
        restTimerJob?.cancel()
        
        if (currentState.isActive && currentState.loggedSetId != null) {
            val duration = currentState.elapsedSecs
            val setId = currentState.loggedSetId
            viewModelScope.launch {
                updateSetRestDurationUseCase(setId, duration)
            }
        }
        
        _restTimerState.value = RestTimerState()
    }

    override fun onCleared() {
        super.onCleared()
        toneGenerator.release()
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
