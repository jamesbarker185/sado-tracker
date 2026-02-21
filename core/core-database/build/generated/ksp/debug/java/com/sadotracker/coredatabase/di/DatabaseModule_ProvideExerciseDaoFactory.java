package com.sadotracker.coredatabase.di;

import com.sadotracker.coredatabase.AppDatabase;
import com.sadotracker.coredatabase.dao.ExerciseDao;
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
public final class DatabaseModule_ProvideExerciseDaoFactory implements Factory<ExerciseDao> {
  private final Provider<AppDatabase> appDatabaseProvider;

  public DatabaseModule_ProvideExerciseDaoFactory(Provider<AppDatabase> appDatabaseProvider) {
    this.appDatabaseProvider = appDatabaseProvider;
  }

  @Override
  public ExerciseDao get() {
    return provideExerciseDao(appDatabaseProvider.get());
  }

  public static DatabaseModule_ProvideExerciseDaoFactory create(
      Provider<AppDatabase> appDatabaseProvider) {
    return new DatabaseModule_ProvideExerciseDaoFactory(appDatabaseProvider);
  }

  public static ExerciseDao provideExerciseDao(AppDatabase appDatabase) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideExerciseDao(appDatabase));
  }
}
