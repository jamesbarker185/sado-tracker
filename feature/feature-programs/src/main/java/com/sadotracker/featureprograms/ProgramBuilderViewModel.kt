package com.sadotracker.featureprograms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadotracker.coredatabase.entity.ExerciseEntity
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
    val primaryMuscles: List<String> = emptyList()
)

@HiltViewModel
class ProgramBuilderViewModel @Inject constructor(
    private val searchExercisesUseCase: SearchExercisesUseCase,
    private val saveProgramUseCase: SaveProgramUseCase
) : ViewModel() {

    // --- Program Metadata State ---
    private val _programName = MutableStateFlow("")
    val programName = _programName.asStateFlow()

    private val _programDescription = MutableStateFlow("")
    val programDescription = _programDescription.asStateFlow()

    private val _selectedExercises = MutableStateFlow<List<ExerciseEntity>>(emptyList())
    val selectedExercises = _selectedExercises.asStateFlow()

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

    fun toggleExerciseSelection(exercise: ExerciseEntity) {
        val current = _selectedExercises.value
        if (current.any { it.id == exercise.id }) {
            _selectedExercises.value = current.filter { it.id != exercise.id }
        } else {
            _selectedExercises.value = current + exercise
        }
    }
    
    fun removeExercise(exerciseId: Long) {
        _selectedExercises.value = _selectedExercises.value.filter { it.id != exerciseId }
    }

    fun saveProgram(onSuccess: (Long) -> Unit) {
        val name = _programName.value
        val description = _programDescription.value.takeIf { it.isNotBlank() }
        val exercises = _selectedExercises.value

        if (name.isBlank() || exercises.isEmpty()) return

        viewModelScope.launch {
            val id = saveProgramUseCase(
                name = name,
                description = description,
                exerciseIds = exercises.map { it.id }
            )
            onSuccess(id)
        }
    }
}
