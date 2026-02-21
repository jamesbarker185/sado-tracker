package com.sadotracker.coredatabase.di;

import com.sadotracker.coredatabase.AppDatabase;
import com.sadotracker.coredatabase.dao.SetDao;
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
public final class DatabaseModule_ProvideSetDaoFactory implements Factory<SetDao> {
  private final Provider<AppDatabase> appDatabaseProvider;

  public DatabaseModule_ProvideSetDaoFactory(Provider<AppDatabase> appDatabaseProvider) {
    this.appDatabaseProvider = appDatabaseProvider;
  }

  @Override
  public SetDao get() {
    return provideSetDao(appDatabaseProvider.get());
  }

  public static DatabaseModule_ProvideSetDaoFactory create(
      Provider<AppDatabase> appDatabaseProvider) {
    return new DatabaseModule_ProvideSetDaoFactory(appDatabaseProvider);
  }

  public static SetDao provideSetDao(AppDatabase appDatabase) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideSetDao(appDatabase));
  }
}
