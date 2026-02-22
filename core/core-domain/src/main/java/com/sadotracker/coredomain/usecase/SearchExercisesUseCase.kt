package com.sadotracker.coredomain.usecase

import com.sadotracker.coredatabase.dao.ExerciseDao
import com.sadotracker.coredatabase.entity.ExerciseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class SearchExercisesUseCase @Inject constructor(
    private val exerciseDao: ExerciseDao
) {
    operator fun invoke(
        query: String = "",
        mechanicsFilter: List<String> = emptyList(),
        forceVectorFilter: List<String> = emptyList(),
        categoryFilter: List<String> = emptyList(),
        equipmentFilter: List<String> = emptyList(),
        modalityFilter: List<String> = emptyList(),
        muscleGroupFilter: List<String> = emptyList(),
        primaryMuscleFilter: List<String> = emptyList()
    ): Flow<List<ExerciseEntity>> {
        
        val baseFlow = if (query.isBlank()) {
            exerciseDao.getAll()
        } else {
            exerciseDao.search(query)
        }
        
        return baseFlow.map { exercises ->
            exercises.filter { exercise ->
                val matchMechanics = mechanicsFilter.isEmpty() || mechanicsFilter.contains(exercise.mechanics)
                val matchForceVector = forceVectorFilter.isEmpty() || forceVectorFilter.contains(exercise.forceVector)
                val matchCategory = categoryFilter.isEmpty() || categoryFilter.contains(exercise.category)
                val matchEquipment = equipmentFilter.isEmpty() || equipmentFilter.any { filterItem -> 
                    exercise.equipment.split(",").map { it.trim() }.contains(filterItem) 
                }
                val matchModality = modalityFilter.isEmpty() || modalityFilter.contains(exercise.modality)
                val matchMuscleGroup = muscleGroupFilter.isEmpty() || muscleGroupFilter.contains(exercise.muscleGroup)
                val matchPrimaryMuscle = primaryMuscleFilter.isEmpty() || primaryMuscleFilter.contains(exercise.primaryMuscle)
                
                matchMechanics && matchForceVector && matchCategory && 
                matchEquipment && matchModality && matchMuscleGroup && matchPrimaryMuscle
            }
        }.flowOn(Dispatchers.Default)
    }
}
