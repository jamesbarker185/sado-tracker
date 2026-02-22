package com.sadotracker.coredatabase.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sadotracker.coredatabase.AppDatabase
import com.sadotracker.coredatabase.dao.AchievementDao
import com.sadotracker.coredatabase.dao.ExerciseDao
import com.sadotracker.coredatabase.dao.MesocycleDao
import com.sadotracker.coredatabase.dao.ProgramDao
import com.sadotracker.coredatabase.dao.ProgramExerciseDao
import com.sadotracker.coredatabase.dao.SetDao
import com.sadotracker.coredatabase.dao.WorkoutDao
import com.sadotracker.coredatabase.dao.ProgramDayDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private val MIGRATION_5_6 = object : Migration(5, 6) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE sets ADD COLUMN rest_taken_secs INTEGER DEFAULT NULL")
        }
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        exerciseDaoProvider: javax.inject.Provider<ExerciseDao>,
        programDaoProvider: javax.inject.Provider<ProgramDao>,
        programExerciseDaoProvider: javax.inject.Provider<ProgramExerciseDao>,
        mesocycleDaoProvider: javax.inject.Provider<MesocycleDao>,
        achievementDaoProvider: javax.inject.Provider<AchievementDao>,
        programDayDaoProvider: javax.inject.Provider<ProgramDayDao>
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "sado_tracker.db"
        )
        .addMigrations(MIGRATION_5_6)
        .fallbackToDestructiveMigration()
        .addCallback(com.sadotracker.coredatabase.AppDatabaseCallback(
            context,
            exerciseDaoProvider,
            programDaoProvider,
            programExerciseDaoProvider,
            mesocycleDaoProvider,
            achievementDaoProvider,
            programDayDaoProvider
        ))
        .build()
    }

    @Provides
    fun provideExerciseDao(appDatabase: AppDatabase): ExerciseDao = appDatabase.exerciseDao()

    @Provides
    fun provideProgramDao(appDatabase: AppDatabase): ProgramDao = appDatabase.programDao()

    @Provides
    fun provideProgramExerciseDao(appDatabase: AppDatabase): ProgramExerciseDao = appDatabase.programExerciseDao()

    @Provides
    fun provideWorkoutDao(appDatabase: AppDatabase): WorkoutDao = appDatabase.workoutDao()

    @Provides
    fun provideSetDao(appDatabase: AppDatabase): SetDao = appDatabase.setDao()

    @Provides
    fun provideMesocycleDao(appDatabase: AppDatabase): MesocycleDao = appDatabase.mesocycleDao()

    @Provides
    fun provideAchievementDao(appDatabase: AppDatabase): AchievementDao = appDatabase.achievementDao()

    @Provides
    fun provideProgramDayDao(appDatabase: AppDatabase): ProgramDayDao = appDatabase.programDayDao()
}
