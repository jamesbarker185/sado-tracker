package com.sadotracker.coredatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sadotracker.coredatabase.dao.AchievementDao
import com.sadotracker.coredatabase.dao.ExerciseDao
import com.sadotracker.coredatabase.dao.MesocycleDao
import com.sadotracker.coredatabase.dao.ProgramDao
import com.sadotracker.coredatabase.dao.ProgramExerciseDao
import com.sadotracker.coredatabase.dao.SetDao
import com.sadotracker.coredatabase.dao.WorkoutDao
import com.sadotracker.coredatabase.dao.ProgramDayDao
import com.sadotracker.coredatabase.entity.AchievementEntity
import com.sadotracker.coredatabase.entity.ExerciseEntity
import com.sadotracker.coredatabase.entity.MesocycleEntity
import com.sadotracker.coredatabase.entity.ProgramEntity
import com.sadotracker.coredatabase.entity.ProgramExerciseEntity
import com.sadotracker.coredatabase.entity.RomProfileEntity
import com.sadotracker.coredatabase.entity.SetEntity
import com.sadotracker.coredatabase.entity.WorkoutEntity
import com.sadotracker.coredatabase.entity.ProgramDayEntity

@Database(
    entities = [
        ExerciseEntity::class,
        ProgramEntity::class,
        ProgramExerciseEntity::class,
        WorkoutEntity::class,
        SetEntity::class,
        RomProfileEntity::class,
        MesocycleEntity::class,
        AchievementEntity::class,
        ProgramDayEntity::class
    ],
    version = 4,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun programDao(): ProgramDao
    abstract fun programExerciseDao(): ProgramExerciseDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun setDao(): SetDao
    abstract fun mesocycleDao(): MesocycleDao
    abstract fun achievementDao(): AchievementDao
    abstract fun programDayDao(): ProgramDayDao
}
