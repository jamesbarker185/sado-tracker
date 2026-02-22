package com.sadotracker.coredatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sadotracker.coredatabase.entity.ProgramDayEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgramDayDao {
    @Query("SELECT * FROM program_days WHERE program_id = :programId ORDER BY day_number ASC")
    fun getForProgram(programId: Long): Flow<List<ProgramDayEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(days: List<ProgramDayEntity>)

    @Query("DELETE FROM program_days WHERE program_id = :programId")
    suspend fun deleteForProgram(programId: Long)
}
