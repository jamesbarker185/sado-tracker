package com.sadotracker.coredatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sadotracker.coredatabase.entity.MesocycleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MesocycleDao {
    @Query("SELECT * FROM mesocycles ORDER BY started_at DESC")
    fun getAll(): Flow<List<MesocycleEntity>>

    @Query("SELECT * FROM mesocycles WHERE id = :id")
    suspend fun getById(id: Long): MesocycleEntity?

    @Query("SELECT * FROM mesocycles WHERE ended_at IS NULL ORDER BY started_at DESC LIMIT 1")
    fun getActive(): Flow<MesocycleEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mesocycle: MesocycleEntity): Long

    @Update
    suspend fun update(mesocycle: MesocycleEntity)
}
