package com.sadotracker.coredatabase.di;

import com.sadotracker.coredatabase.AppDatabase;
import com.sadotracker.coredatabase.dao.ProgramDao;
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
public final class DatabaseModule_ProvideProgramDaoFactory implements Factory<ProgramDao> {
  private final Provider<AppDatabase> appDatabaseProvider;

  public DatabaseModule_ProvideProgramDaoFactory(Provider<AppDatabase> appDatabaseProvider) {
    this.appDatabaseProvider = appDatabaseProvider;
  }

  @Override
  public ProgramDao get() {
    return provideProgramDao(appDatabaseProvider.get());
  }

  public static DatabaseModule_ProvideProgramDaoFactory create(
      Provider<AppDatabase> appDatabaseProvider) {
    return new DatabaseModule_ProvideProgramDaoFactory(appDatabaseProvider);
  }

  public static ProgramDao provideProgramDao(AppDatabase appDatabase) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideProgramDao(appDatabase));
  }
}
