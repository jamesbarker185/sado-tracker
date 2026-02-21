package com.sadotracker.coredatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sadotracker.coredatabase.dao.ExerciseDao
import com.sadotracker.coredatabase.dao.ProgramDao
import com.sadotracker.coredatabase.dao.ProgramExerciseDao
import com.sadotracker.coredatabase.dao.SetDao
import com.sadotracker.coredatabase.dao.WorkoutDao
import com.sadotracker.coredatabase.entity.ExerciseEntity
import com.sadotracker.coredatabase.entity.ProgramEntity
import com.sadotracker.coredatabase.entity.ProgramExerciseEntity
import com.sadotracker.coredatabase.entity.RomProfileEntity
import com.sadotracker.coredatabase.entity.SetEntity
import com.sadotracker.coredatabase.entity.WorkoutEntity

@Database(
    entities = [
        ExerciseEntity::class,
        ProgramEntity::class,
        ProgramExerciseEntity::class,
        WorkoutEntity::class,
        SetEntity::class,
        RomProfileEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun programDao(): ProgramDao
    abstract fun programExerciseDao(): ProgramExerciseDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun setDao(): SetDao
}
