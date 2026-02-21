package com.sadotracker.coredatabase.di;

import com.sadotracker.coredatabase.AppDatabase;
import com.sadotracker.coredatabase.dao.ProgramExerciseDao;
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
public final class DatabaseModule_ProvideProgramExerciseDaoFactory implements Factory<ProgramExerciseDao> {
  private final Provider<AppDatabase> appDatabaseProvider;

  public DatabaseModule_ProvideProgramExerciseDaoFactory(
      Provider<AppDatabase> appDatabaseProvider) {
    this.appDatabaseProvider = appDatabaseProvider;
  }

  @Override
  public ProgramExerciseDao get() {
    return provideProgramExerciseDao(appDatabaseProvider.get());
  }

  public static DatabaseModule_ProvideProgramExerciseDaoFactory create(
      Provider<AppDatabase> appDatabaseProvider) {
    return new DatabaseModule_ProvideProgramExerciseDaoFactory(appDatabaseProvider);
  }

  public static ProgramExerciseDao provideProgramExerciseDao(AppDatabase appDatabase) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideProgramExerciseDao(appDatabase));
  }
}
