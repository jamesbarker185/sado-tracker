package com.sadotracker.coredatabase.di

import android.content.Context
import androidx.room.Room
import com.sadotracker.coredatabase.AppDatabase
import com.sadotracker.coredatabase.dao.ExerciseDao
import com.sadotracker.coredatabase.dao.ProgramDao
import com.sadotracker.coredatabase.dao.ProgramExerciseDao
import com.sadotracker.coredatabase.dao.SetDao
import com.sadotracker.coredatabase.dao.WorkoutDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "sado_tracker.db"
        ).build()
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
}
