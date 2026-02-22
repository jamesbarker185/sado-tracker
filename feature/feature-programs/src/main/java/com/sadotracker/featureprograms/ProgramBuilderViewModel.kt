package com.sadotracker.featureprograms

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadotracker.coredatabase.entity.ExerciseEntity
import com.sadotracker.coredomain.usecase.GetProgramDetailsUseCase
import com.sadotracker.coredomain.usecase.SaveProgramUseCase
import com.sadotracker.coredomain.usecase.SearchExercisesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FilterState(
    val query: String = "",
    val mechanics: List<String> = emptyList(),
    val forceVectors: List<String> = emptyList(),
    val categories: List<String> = emptyList(),
    val equipment: List<String> = emptyList(),
    val modalities: List<String> = emptyList(),
    val muscleGroups: List<String> = emptyList(),
    val primaryMuscles: List<String> = emptyList(),
    val dayIndex: Int = 0 // Track which day we are adding exercises to
)

data class DayBuilderState(
    val isRestDay: Boolean = false,
    val exercises: List<ExerciseEntity> = emptyList(),
    val isExpanded: Boolean = true
)

@HiltViewModel
class ProgramBuilderViewModel @Inject constructor(
    private val searchExercisesUseCase: SearchExercisesUseCase,
    private val saveProgramUseCase: SaveProgramUseCase,
    private val getProgramDetailsUseCase: GetProgramDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val programId: Long? = savedStateHandle.get<Long>("programId")

    init {
        programId?.let { id ->
            if (id > 0) {
                loadProgram(id)
            }
        }
    }

    private fun loadProgram(id: Long) {
        viewModelScope.launch {
            getProgramDetailsUseCase(id)?.let { details ->
                _programName.value = details.program.name
                _programDescription.value = details.program.description ?: ""
                _days.value = details.days.map { dayDetails ->
                    DayBuilderState(
                        isRestDay = dayDetails.day.isRestDay,
                        exercises = dayDetails.exercises,
                        isExpanded = false
                    )
                }
            }
        }
    }

    // --- Program Metadata State ---
    private val _programName = MutableStateFlow("")
    val programName = _programName.asStateFlow()

    private val _programDescription = MutableStateFlow("")
    val programDescription = _programDescription.asStateFlow()
    
    // Day-based exercise state
    private val _days = MutableStateFlow(listOf(DayBuilderState()))
    val days = _days.asStateFlow()

    // Composed state for UI convenience (e.g. for the "Save" button check)
    val hasExercises = _days.combine(_days) { list, _ -> 
        list.any { it.exercises.isNotEmpty() } || list.any { it.isRestDay }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val isEditMode = programId != null

    // --- Search & Filter State ---
    private val _filterState = MutableStateFlow(FilterState())
    val filterState = _filterState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults: StateFlow<List<ExerciseEntity>> = _filterState
        .flatMapLatest { filter ->
            searchExercisesUseCase(
                query = filter.query,
                mechanicsFilter = filter.mechanics,
                forceVectorFilter = filter.forceVectors,
                categoryFilter = filter.categories,
                equipmentFilter = filter.equipment,
                modalityFilter = filter.modalities,
                muscleGroupFilter = filter.muscleGroups,
                primaryMuscleFilter = filter.primaryMuscles
            )
        }
        .onEach { Log.d("ProgramBuilder", "Search Results size: ${it.size}") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // --- Events ---
    fun updateProgramName(name: String) {
        _programName.value = name
    }

    fun updateProgramDescription(description: String) {
        _programDescription.value = description
    }

    fun updateSearchQuery(query: String) {
        _filterState.value = _filterState.value.copy(query = query)
    }

    fun toggleMechanicFilter(mechanic: String) {
        val current = _filterState.value.mechanics
        val newMechanics = if(current.contains(mechanic)) current - mechanic else current + mechanic
        _filterState.value = _filterState.value.copy(mechanics = newMechanics)
    }

    fun toggleForceVectorFilter(forceVector: String) {
        val current = _filterState.value.forceVectors
        val newForceVectors = if(current.contains(forceVector)) current - forceVector else current + forceVector
        _filterState.value = _filterState.value.copy(forceVectors = newForceVectors)
    }

    fun toggleCategoryFilter(category: String) {
        val current = _filterState.value.categories
        val newCategories = if(current.contains(category)) current - category else current + category
        _filterState.value = _filterState.value.copy(categories = newCategories)
    }

    fun toggleEquipmentFilter(equipment: String) {
        val current = _filterState.value.equipment
        val newEquipment = if(current.contains(equipment)) current - equipment else current + equipment
        _filterState.value = _filterState.value.copy(equipment = newEquipment)
    }

    fun toggleModalityFilter(modality: String) {
        val current = _filterState.value.modalities
        val newModalities = if(current.contains(modality)) current - modality else current + modality
        _filterState.value = _filterState.value.copy(modalities = newModalities)
    }

    fun toggleMuscleGroupFilter(muscleGroup: String) {
        val current = _filterState.value.muscleGroups
        val newMuscleGroups = if(current.contains(muscleGroup)) current - muscleGroup else current + muscleGroup
        _filterState.value = _filterState.value.copy(muscleGroups = newMuscleGroups)
    }

    fun togglePrimaryMuscleFilter(primaryMuscle: String) {
        val current = _filterState.value.primaryMuscles
        val newPrimaryMuscles = if(current.contains(primaryMuscle)) current - primaryMuscle else current + primaryMuscle
        _filterState.value = _filterState.value.copy(primaryMuscles = newPrimaryMuscles)
    }

    fun clearFilters() {
        val query = _filterState.value.query
        _filterState.value = FilterState(query = query)
    }

    fun addDay() {
        _days.update { it + DayBuilderState() }
    }

    fun removeDay(index: Int) {
        _days.update { list ->
            if (list.size > 1) list.filterIndexed { i, _ -> i != index } else list
        }
    }

    fun toggleRestDay(index: Int) {
        _days.update { list ->
            val updated = list.toMutableList()
            updated[index] = updated[index].copy(isRestDay = !updated[index].isRestDay)
            updated
        }
    }

    fun toggleDayExpansion(index: Int) {
        _days.update { list ->
            val updated = list.toMutableList()
            updated[index] = updated[index].copy(isExpanded = !updated[index].isExpanded)
            updated
        }
    }

    fun setEditingDay(index: Int) {
        _filterState.update { it.copy(dayIndex = index) }
    }

    fun toggleExerciseSelection(exercise: ExerciseEntity) {
        val dayIdx = _filterState.value.dayIndex
        _days.update { list ->
            val updatedList = list.toMutableList()
            val day = updatedList[dayIdx]
            val currentExercises = day.exercises
            
            val newExercises = if (currentExercises.any { it.id == exercise.id }) {
                currentExercises.filter { it.id != exercise.id }
            } else {
                currentExercises + exercise
            }
            
            updatedList[dayIdx] = day.copy(exercises = newExercises)
            updatedList
        }
    }
    
    fun removeExercise(dayIndex: Int, exerciseId: Long) {
        _days.update { list ->
            val updatedList = list.toMutableList()
            val day = updatedList[dayIndex]
            updatedList[dayIndex] = day.copy(exercises = day.exercises.filter { it.id != exerciseId })
            updatedList
        }
    }

    fun saveProgram(onSuccess: (Long) -> Unit) {
        val name = _programName.value
        val description = _programDescription.value.takeIf { it.isNotBlank() }
        val daysData = _days.value

        if (name.isBlank() || daysData.isEmpty()) return

        viewModelScope.launch {
            val id = saveProgramUseCase(
                name = name,
                description = description,
                days = daysData.map { day ->
                    com.sadotracker.coredomain.usecase.SaveProgramUseCase.DayInput(
                        isRestDay = day.isRestDay,
                        exerciseIds = day.exercises.map { it.id }
                    )
                },
                existingProgramId = programId
            )
            onSuccess(id)
        }
    }
}
