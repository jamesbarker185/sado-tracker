package com.sadotracker.coredatabase;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.sadotracker.coredatabase.dao.ExerciseDao;
import com.sadotracker.coredatabase.dao.ExerciseDao_Impl;
import com.sadotracker.coredatabase.dao.ProgramDao;
import com.sadotracker.coredatabase.dao.ProgramDao_Impl;
import com.sadotracker.coredatabase.dao.ProgramExerciseDao;
import com.sadotracker.coredatabase.dao.ProgramExerciseDao_Impl;
import com.sadotracker.coredatabase.dao.SetDao;
import com.sadotracker.coredatabase.dao.SetDao_Impl;
import com.sadotracker.coredatabase.dao.WorkoutDao;
import com.sadotracker.coredatabase.dao.WorkoutDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile ExerciseDao _exerciseDao;

  private volatile ProgramDao _programDao;

  private volatile ProgramExerciseDao _programExerciseDao;

  private volatile WorkoutDao _workoutDao;

  private volatile SetDao _setDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `exercises` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `muscle_group` TEXT NOT NULL, `primary_muscle` TEXT NOT NULL, `secondary_muscle` TEXT, `category` TEXT NOT NULL, `equipment` TEXT NOT NULL, `mechanics` TEXT NOT NULL, `modality` TEXT NOT NULL, `force_vector` TEXT NOT NULL, `is_custom` INTEGER NOT NULL DEFAULT 0, `instructions` TEXT, `image_res_id` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `programs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `description` TEXT, `created_at` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `program_exercises` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `program_id` INTEGER NOT NULL, `exercise_id` INTEGER NOT NULL, `order_index` INTEGER NOT NULL, `default_sets` INTEGER NOT NULL DEFAULT 3, FOREIGN KEY(`program_id`) REFERENCES `programs`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`exercise_id`) REFERENCES `exercises`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        db.execSQL("CREATE TABLE IF NOT EXISTS `workouts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `date` INTEGER NOT NULL, `duration_ms` INTEGER, `program_id` INTEGER, `notes` TEXT, FOREIGN KEY(`program_id`) REFERENCES `programs`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sets` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `workout_id` INTEGER NOT NULL, `exercise_id` INTEGER NOT NULL, `set_number` INTEGER NOT NULL, `weight_kg` REAL NOT NULL, `reps` INTEGER NOT NULL, `rir` INTEGER, `is_partial` INTEGER NOT NULL DEFAULT 0, `rom_consistency_score` REAL, FOREIGN KEY(`workout_id`) REFERENCES `workouts`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`exercise_id`) REFERENCES `exercises`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        db.execSQL("CREATE TABLE IF NOT EXISTS `rom_profiles` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `exercise_id` INTEGER NOT NULL, `top_y_threshold` REAL NOT NULL, `bottom_y_threshold` REAL NOT NULL, `calibrated_at` INTEGER NOT NULL, FOREIGN KEY(`exercise_id`) REFERENCES `exercises`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_rom_profiles_exercise_id` ON `rom_profiles` (`exercise_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'fef3786385636f57dda00a1c49471d4f')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `exercises`");
        db.execSQL("DROP TABLE IF EXISTS `programs`");
        db.execSQL("DROP TABLE IF EXISTS `program_exercises`");
        db.execSQL("DROP TABLE IF EXISTS `workouts`");
        db.execSQL("DROP TABLE IF EXISTS `sets`");
        db.execSQL("DROP TABLE IF EXISTS `rom_profiles`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsExercises = new HashMap<String, TableInfo.Column>(13);
        _columnsExercises.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("muscle_group", new TableInfo.Column("muscle_group", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("primary_muscle", new TableInfo.Column("primary_muscle", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("secondary_muscle", new TableInfo.Column("secondary_muscle", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("equipment", new TableInfo.Column("equipment", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("mechanics", new TableInfo.Column("mechanics", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("modality", new TableInfo.Column("modality", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("force_vector", new TableInfo.Column("force_vector", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("is_custom", new TableInfo.Column("is_custom", "INTEGER", true, 0, "0", TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("instructions", new TableInfo.Column("instructions", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExercises.put("image_res_id", new TableInfo.Column("image_res_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExercises = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesExercises = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExercises = new TableInfo("exercises", _columnsExercises, _foreignKeysExercises, _indicesExercises);
        final TableInfo _existingExercises = TableInfo.read(db, "exercises");
        if (!_infoExercises.equals(_existingExercises)) {
          return new RoomOpenHelper.ValidationResult(false, "exercises(com.sadotracker.coredatabase.entity.ExerciseEntity).\n"
                  + " Expected:\n" + _infoExercises + "\n"
                  + " Found:\n" + _existingExercises);
        }
        final HashMap<String, TableInfo.Column> _columnsPrograms = new HashMap<String, TableInfo.Column>(4);
        _columnsPrograms.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPrograms.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPrograms = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPrograms = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPrograms = new TableInfo("programs", _columnsPrograms, _foreignKeysPrograms, _indicesPrograms);
        final TableInfo _existingPrograms = TableInfo.read(db, "programs");
        if (!_infoPrograms.equals(_existingPrograms)) {
          return new RoomOpenHelper.ValidationResult(false, "programs(com.sadotracker.coredatabase.entity.ProgramEntity).\n"
                  + " Expected:\n" + _infoPrograms + "\n"
                  + " Found:\n" + _existingPrograms);
        }
        final HashMap<String, TableInfo.Column> _columnsProgramExercises = new HashMap<String, TableInfo.Column>(5);
        _columnsProgramExercises.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramExercises.put("program_id", new TableInfo.Column("program_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramExercises.put("exercise_id", new TableInfo.Column("exercise_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramExercises.put("order_index", new TableInfo.Column("order_index", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgramExercises.put("default_sets", new TableInfo.Column("default_sets", "INTEGER", true, 0, "3", TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProgramExercises = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysProgramExercises.add(new TableInfo.ForeignKey("programs", "CASCADE", "NO ACTION", Arrays.asList("program_id"), Arrays.asList("id")));
        _foreignKeysProgramExercises.add(new TableInfo.ForeignKey("exercises", "NO ACTION", "NO ACTION", Arrays.asList("exercise_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesProgramExercises = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProgramExercises = new TableInfo("program_exercises", _columnsProgramExercises, _foreignKeysProgramExercises, _indicesProgramExercises);
        final TableInfo _existingProgramExercises = TableInfo.read(db, "program_exercises");
        if (!_infoProgramExercises.equals(_existingProgramExercises)) {
          return new RoomOpenHelper.ValidationResult(false, "program_exercises(com.sadotracker.coredatabase.entity.ProgramExerciseEntity).\n"
                  + " Expected:\n" + _infoProgramExercises + "\n"
                  + " Found:\n" + _existingProgramExercises);
        }
        final HashMap<String, TableInfo.Column> _columnsWorkouts = new HashMap<String, TableInfo.Column>(5);
        _columnsWorkouts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("duration_ms", new TableInfo.Column("duration_ms", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("program_id", new TableInfo.Column("program_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkouts.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWorkouts = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysWorkouts.add(new TableInfo.ForeignKey("programs", "NO ACTION", "NO ACTION", Arrays.asList("program_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesWorkouts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoWorkouts = new TableInfo("workouts", _columnsWorkouts, _foreignKeysWorkouts, _indicesWorkouts);
        final TableInfo _existingWorkouts = TableInfo.read(db, "workouts");
        if (!_infoWorkouts.equals(_existingWorkouts)) {
          return new RoomOpenHelper.ValidationResult(false, "workouts(com.sadotracker.coredatabase.entity.WorkoutEntity).\n"
                  + " Expected:\n" + _infoWorkouts + "\n"
                  + " Found:\n" + _existingWorkouts);
        }
        final HashMap<String, TableInfo.Column> _columnsSets = new HashMap<String, TableInfo.Column>(9);
        _columnsSets.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSets.put("workout_id", new TableInfo.Column("workout_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSets.put("exercise_id", new TableInfo.Column("exercise_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSets.put("set_number", new TableInfo.Column("set_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSets.put("weight_kg", new TableInfo.Column("weight_kg", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSets.put("reps", new TableInfo.Column("reps", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSets.put("rir", new TableInfo.Column("rir", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSets.put("is_partial", new TableInfo.Column("is_partial", "INTEGER", true, 0, "0", TableInfo.CREATED_FROM_ENTITY));
        _columnsSets.put("rom_consistency_score", new TableInfo.Column("rom_consistency_score", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSets = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysSets.add(new TableInfo.ForeignKey("workouts", "CASCADE", "NO ACTION", Arrays.asList("workout_id"), Arrays.asList("id")));
        _foreignKeysSets.add(new TableInfo.ForeignKey("exercises", "NO ACTION", "NO ACTION", Arrays.asList("exercise_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesSets = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSets = new TableInfo("sets", _columnsSets, _foreignKeysSets, _indicesSets);
        final TableInfo _existingSets = TableInfo.read(db, "sets");
        if (!_infoSets.equals(_existingSets)) {
          return new RoomOpenHelper.ValidationResult(false, "sets(com.sadotracker.coredatabase.entity.SetEntity).\n"
                  + " Expected:\n" + _infoSets + "\n"
                  + " Found:\n" + _existingSets);
        }
        final HashMap<String, TableInfo.Column> _columnsRomProfiles = new HashMap<String, TableInfo.Column>(5);
        _columnsRomProfiles.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRomProfiles.put("exercise_id", new TableInfo.Column("exercise_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRomProfiles.put("top_y_threshold", new TableInfo.Column("top_y_threshold", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRomProfiles.put("bottom_y_threshold", new TableInfo.Column("bottom_y_threshold", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRomProfiles.put("calibrated_at", new TableInfo.Column("calibrated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRomProfiles = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysRomProfiles.add(new TableInfo.ForeignKey("exercises", "CASCADE", "NO ACTION", Arrays.asList("exercise_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesRomProfiles = new HashSet<TableInfo.Index>(1);
        _indicesRomProfiles.add(new TableInfo.Index("index_rom_profiles_exercise_id", true, Arrays.asList("exercise_id"), Arrays.asList("ASC")));
        final TableInfo _infoRomProfiles = new TableInfo("rom_profiles", _columnsRomProfiles, _foreignKeysRomProfiles, _indicesRomProfiles);
        final TableInfo _existingRomProfiles = TableInfo.read(db, "rom_profiles");
        if (!_infoRomProfiles.equals(_existingRomProfiles)) {
          return new RoomOpenHelper.ValidationResult(false, "rom_profiles(com.sadotracker.coredatabase.entity.RomProfileEntity).\n"
                  + " Expected:\n" + _infoRomProfiles + "\n"
                  + " Found:\n" + _existingRomProfiles);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "fef3786385636f57dda00a1c49471d4f", "2684b54bd099632bd20771fc1f14dccf");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "exercises","programs","program_exercises","workouts","sets","rom_profiles");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `exercises`");
      _db.execSQL("DELETE FROM `programs`");
      _db.execSQL("DELETE FROM `program_exercises`");
      _db.execSQL("DELETE FROM `workouts`");
      _db.execSQL("DELETE FROM `sets`");
      _db.execSQL("DELETE FROM `rom_profiles`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ExerciseDao.class, ExerciseDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ProgramDao.class, ProgramDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ProgramExerciseDao.class, ProgramExerciseDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(WorkoutDao.class, WorkoutDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SetDao.class, SetDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ExerciseDao exerciseDao() {
    if (_exerciseDao != null) {
      return _exerciseDao;
    } else {
      synchronized(this) {
        if(_exerciseDao == null) {
          _exerciseDao = new ExerciseDao_Impl(this);
        }
        return _exerciseDao;
      }
    }
  }

  @Override
  public ProgramDao programDao() {
    if (_programDao != null) {
      return _programDao;
    } else {
      synchronized(this) {
        if(_programDao == null) {
          _programDao = new ProgramDao_Impl(this);
        }
        return _programDao;
      }
    }
  }

  @Override
  public ProgramExerciseDao programExerciseDao() {
    if (_programExerciseDao != null) {
      return _programExerciseDao;
    } else {
      synchronized(this) {
        if(_programExerciseDao == null) {
          _programExerciseDao = new ProgramExerciseDao_Impl(this);
        }
        return _programExerciseDao;
      }
    }
  }

  @Override
  public WorkoutDao workoutDao() {
    if (_workoutDao != null) {
      return _workoutDao;
    } else {
      synchronized(this) {
        if(_workoutDao == null) {
          _workoutDao = new WorkoutDao_Impl(this);
        }
        return _workoutDao;
      }
    }
  }

  @Override
  public SetDao setDao() {
    if (_setDao != null) {
      return _setDao;
    } else {
      synchronized(this) {
        if(_setDao == null) {
          _setDao = new SetDao_Impl(this);
        }
        return _setDao;
      }
    }
  }
}
