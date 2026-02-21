package com.sadotracker.coredatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sadotracker.coredatabase.entity.ProgramEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgramDao {
    @Query("SELECT * FROM programs ORDER BY created_at DESC")
    fun getAll(): Flow<List<ProgramEntity>>

    @Query("SELECT * FROM programs WHERE id = :id")
    fun getById(id: Long): Flow<ProgramEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(program: ProgramEntity): Long

    @Delete
    suspend fun delete(program: ProgramEntity)
}
