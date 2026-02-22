package com.sadotracker.coredatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sadotracker.coredatabase.entity.ProgramExerciseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgramExerciseDao {
    @Query("SELECT * FROM program_exercises WHERE program_id = :programId ORDER BY order_index ASC")
    fun getExercisesForProgram(programId: Long): Flow<List<ProgramExerciseEntity>>

    @Query("SELECT * FROM program_exercises WHERE id = :id")
    suspend fun getById(id: Long): ProgramExerciseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(programExercises: List<ProgramExerciseEntity>)

    @Update
    suspend fun updateAll(programExercises: List<ProgramExerciseEntity>)

    @Delete
    suspend fun delete(programExercise: ProgramExerciseEntity)

    @Query("DELETE FROM program_exercises WHERE program_id = :programId")
    suspend fun deleteForProgram(programId: Long)
}
