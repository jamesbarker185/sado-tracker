package com.sadotracker.coredatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sadotracker.coredatabase.entity.SetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SetDao {
    @Query("SELECT * FROM sets WHERE workout_id = :workoutId ORDER BY set_number ASC")
    fun getSetsForWorkout(workoutId: Long): Flow<List<SetEntity>>

    @Query("SELECT * FROM sets WHERE exercise_id = :exerciseId ORDER BY id DESC")
    fun getSetsForExercise(exerciseId: Long): Flow<List<SetEntity>>

    @Query("""
        SELECT * FROM sets 
        WHERE exercise_id = :exerciseId 
        ORDER BY id DESC LIMIT 1
    """)
    suspend fun getLastSetForExercise(exerciseId: Long): SetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(setEntity: SetEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sets: List<SetEntity>)

    @Update
    suspend fun update(setEntity: SetEntity)

    @Delete
    suspend fun delete(setEntity: SetEntity)
}
