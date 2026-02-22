package com.sadotracker.coredomain.usecase

import com.sadotracker.coredatabase.dao.ExerciseDao
import com.sadotracker.coredatabase.dao.ProgramDao
import com.sadotracker.coredatabase.dao.ProgramDayDao
import com.sadotracker.coredatabase.dao.ProgramExerciseDao
import com.sadotracker.coredatabase.entity.ExerciseEntity
import com.sadotracker.coredatabase.entity.ProgramDayEntity
import com.sadotracker.coredatabase.entity.ProgramEntity
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetProgramDetailsUseCase @Inject constructor(
    private val programDao: ProgramDao,
    private val programDayDao: ProgramDayDao,
    private val programExerciseDao: ProgramExerciseDao,
    private val exerciseDao: ExerciseDao
) {
    data class ProgramDetails(
        val program: ProgramEntity,
        val days: List<DayDetails>
    )

    data class DayDetails(
        val day: ProgramDayEntity,
        val exercises: List<ExerciseEntity>,
        val restOverrides: Map<Long, Int> = emptyMap()
    )

    suspend operator fun invoke(programId: Long): ProgramDetails? {
        val program = programDao.getById(programId).firstOrNull() ?: return null
        val days = programDayDao.getForProgram(programId).firstOrNull() ?: emptyList()
        val exercises = programExerciseDao.getExercisesForProgram(programId).firstOrNull() ?: emptyList()
        
        val exerciseCache = mutableMapOf<Long, ExerciseEntity>()
        
        val dayDetails = days.map { day ->
            val dayProgramExercises = exercises.filter { it.dayIndex == day.dayNumber - 1 }
            val dayExercises = dayProgramExercises.map { progEx ->
                exerciseCache.getOrPut(progEx.exerciseId) {
                    exerciseDao.getById(progEx.exerciseId)!!
                }
            }
            val restOverrides = dayProgramExercises.associate { it.exerciseId to it.restTimeSecs }
            DayDetails(day, dayExercises, restOverrides)
        }

        return ProgramDetails(program, dayDetails)
    }
}
