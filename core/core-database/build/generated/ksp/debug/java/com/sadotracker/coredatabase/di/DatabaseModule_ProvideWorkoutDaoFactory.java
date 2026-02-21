package com.sadotracker.coredatabase.di;

import com.sadotracker.coredatabase.AppDatabase;
import com.sadotracker.coredatabase.dao.WorkoutDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class DatabaseModule_ProvideWorkoutDaoFactory implements Factory<WorkoutDao> {
  private final Provider<AppDatabase> appDatabaseProvider;

  public DatabaseModule_ProvideWorkoutDaoFactory(Provider<AppDatabase> appDatabaseProvider) {
    this.appDatabaseProvider = appDatabaseProvider;
  }

  @Override
  public WorkoutDao get() {
    return provideWorkoutDao(appDatabaseProvider.get());
  }

  public static DatabaseModule_ProvideWorkoutDaoFactory create(
      Provider<AppDatabase> appDatabaseProvider) {
    return new DatabaseModule_ProvideWorkoutDaoFactory(appDatabaseProvider);
  }

  public static WorkoutDao provideWorkoutDao(AppDatabase appDatabase) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideWorkoutDao(appDatabase));
  }
}
