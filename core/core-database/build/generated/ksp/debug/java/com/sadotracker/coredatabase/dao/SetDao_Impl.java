package com.sadotracker.coredatabase.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.sadotracker.coredatabase.entity.SetEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SetDao_Impl implements SetDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SetEntity> __insertionAdapterOfSetEntity;

  private final EntityDeletionOrUpdateAdapter<SetEntity> __deletionAdapterOfSetEntity;

  private final EntityDeletionOrUpdateAdapter<SetEntity> __updateAdapterOfSetEntity;

  public SetDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSetEntity = new EntityInsertionAdapter<SetEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `sets` (`id`,`workout_id`,`exercise_id`,`set_number`,`weight_kg`,`reps`,`rir`,`is_partial`,`rom_consistency_score`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SetEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getWorkoutId());
        statement.bindLong(3, entity.getExerciseId());
        statement.bindLong(4, entity.getSetNumber());
        statement.bindDouble(5, entity.getWeightKg());
        statement.bindLong(6, entity.getReps());
        if (entity.getRir() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getRir());
        }
        final int _tmp = entity.isPartial() ? 1 : 0;
        statement.bindLong(8, _tmp);
        if (entity.getRomConsistencyScore() == null) {
          statement.bindNull(9);
        } else {
          statement.bindDouble(9, entity.getRomConsistencyScore());
        }
      }
    };
    this.__deletionAdapterOfSetEntity = new EntityDeletionOrUpdateAdapter<SetEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `sets` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SetEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfSetEntity = new EntityDeletionOrUpdateAdapter<SetEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `sets` SET `id` = ?,`workout_id` = ?,`exercise_id` = ?,`set_number` = ?,`weight_kg` = ?,`reps` = ?,`rir` = ?,`is_partial` = ?,`rom_consistency_score` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SetEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getWorkoutId());
        statement.bindLong(3, entity.getExerciseId());
        statement.bindLong(4, entity.getSetNumber());
        statement.bindDouble(5, entity.getWeightKg());
        statement.bindLong(6, entity.getReps());
        if (entity.getRir() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getRir());
        }
        final int _tmp = entity.isPartial() ? 1 : 0;
        statement.bindLong(8, _tmp);
        if (entity.getRomConsistencyScore() == null) {
          statement.bindNull(9);
        } else {
          statement.bindDouble(9, entity.getRomConsistencyScore());
        }
        statement.bindLong(10, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final SetEntity setEntity, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSetEntity.insertAndReturnId(setEntity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<SetEntity> sets,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSetEntity.insert(sets);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final SetEntity setEntity, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSetEntity.handle(setEntity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final SetEntity setEntity, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSetEntity.handle(setEntity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SetEntity>> getSetsForWorkout(final long workoutId) {
    final String _sql = "SELECT * FROM sets WHERE workout_id = ? ORDER BY set_number ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, workoutId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sets"}, new Callable<List<SetEntity>>() {
      @Override
      @NonNull
      public List<SetEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWorkoutId = CursorUtil.getColumnIndexOrThrow(_cursor, "workout_id");
          final int _cursorIndexOfExerciseId = CursorUtil.getColumnIndexOrThrow(_cursor, "exercise_id");
          final int _cursorIndexOfSetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "set_number");
          final int _cursorIndexOfWeightKg = CursorUtil.getColumnIndexOrThrow(_cursor, "weight_kg");
          final int _cursorIndexOfReps = CursorUtil.getColumnIndexOrThrow(_cursor, "reps");
          final int _cursorIndexOfRir = CursorUtil.getColumnIndexOrThrow(_cursor, "rir");
          final int _cursorIndexOfIsPartial = CursorUtil.getColumnIndexOrThrow(_cursor, "is_partial");
          final int _cursorIndexOfRomConsistencyScore = CursorUtil.getColumnIndexOrThrow(_cursor, "rom_consistency_score");
          final List<SetEntity> _result = new ArrayList<SetEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SetEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpWorkoutId;
            _tmpWorkoutId = _cursor.getLong(_cursorIndexOfWorkoutId);
            final long _tmpExerciseId;
            _tmpExerciseId = _cursor.getLong(_cursorIndexOfExerciseId);
            final int _tmpSetNumber;
            _tmpSetNumber = _cursor.getInt(_cursorIndexOfSetNumber);
            final double _tmpWeightKg;
            _tmpWeightKg = _cursor.getDouble(_cursorIndexOfWeightKg);
            final int _tmpReps;
            _tmpReps = _cursor.getInt(_cursorIndexOfReps);
            final Integer _tmpRir;
            if (_cursor.isNull(_cursorIndexOfRir)) {
              _tmpRir = null;
            } else {
              _tmpRir = _cursor.getInt(_cursorIndexOfRir);
            }
            final boolean _tmpIsPartial;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPartial);
            _tmpIsPartial = _tmp != 0;
            final Double _tmpRomConsistencyScore;
            if (_cursor.isNull(_cursorIndexOfRomConsistencyScore)) {
              _tmpRomConsistencyScore = null;
            } else {
              _tmpRomConsistencyScore = _cursor.getDouble(_cursorIndexOfRomConsistencyScore);
            }
            _item = new SetEntity(_tmpId,_tmpWorkoutId,_tmpExerciseId,_tmpSetNumber,_tmpWeightKg,_tmpReps,_tmpRir,_tmpIsPartial,_tmpRomConsistencyScore);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<SetEntity>> getSetsForExercise(final long exerciseId) {
    final String _sql = "SELECT * FROM sets WHERE exercise_id = ? ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, exerciseId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sets"}, new Callable<List<SetEntity>>() {
      @Override
      @NonNull
      public List<SetEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWorkoutId = CursorUtil.getColumnIndexOrThrow(_cursor, "workout_id");
          final int _cursorIndexOfExerciseId = CursorUtil.getColumnIndexOrThrow(_cursor, "exercise_id");
          final int _cursorIndexOfSetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "set_number");
          final int _cursorIndexOfWeightKg = CursorUtil.getColumnIndexOrThrow(_cursor, "weight_kg");
          final int _cursorIndexOfReps = CursorUtil.getColumnIndexOrThrow(_cursor, "reps");
          final int _cursorIndexOfRir = CursorUtil.getColumnIndexOrThrow(_cursor, "rir");
          final int _cursorIndexOfIsPartial = CursorUtil.getColumnIndexOrThrow(_cursor, "is_partial");
          final int _cursorIndexOfRomConsistencyScore = CursorUtil.getColumnIndexOrThrow(_cursor, "rom_consistency_score");
          final List<SetEntity> _result = new ArrayList<SetEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SetEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpWorkoutId;
            _tmpWorkoutId = _cursor.getLong(_cursorIndexOfWorkoutId);
            final long _tmpExerciseId;
            _tmpExerciseId = _cursor.getLong(_cursorIndexOfExerciseId);
            final int _tmpSetNumber;
            _tmpSetNumber = _cursor.getInt(_cursorIndexOfSetNumber);
            final double _tmpWeightKg;
            _tmpWeightKg = _cursor.getDouble(_cursorIndexOfWeightKg);
            final int _tmpReps;
            _tmpReps = _cursor.getInt(_cursorIndexOfReps);
            final Integer _tmpRir;
            if (_cursor.isNull(_cursorIndexOfRir)) {
              _tmpRir = null;
            } else {
              _tmpRir = _cursor.getInt(_cursorIndexOfRir);
            }
            final boolean _tmpIsPartial;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPartial);
            _tmpIsPartial = _tmp != 0;
            final Double _tmpRomConsistencyScore;
            if (_cursor.isNull(_cursorIndexOfRomConsistencyScore)) {
              _tmpRomConsistencyScore = null;
            } else {
              _tmpRomConsistencyScore = _cursor.getDouble(_cursorIndexOfRomConsistencyScore);
            }
            _item = new SetEntity(_tmpId,_tmpWorkoutId,_tmpExerciseId,_tmpSetNumber,_tmpWeightKg,_tmpReps,_tmpRir,_tmpIsPartial,_tmpRomConsistencyScore);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getLastSetForExercise(final long exerciseId,
      final Continuation<? super SetEntity> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM sets \n"
            + "        WHERE exercise_id = ? \n"
            + "        ORDER BY id DESC LIMIT 1\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, exerciseId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SetEntity>() {
      @Override
      @Nullable
      public SetEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWorkoutId = CursorUtil.getColumnIndexOrThrow(_cursor, "workout_id");
          final int _cursorIndexOfExerciseId = CursorUtil.getColumnIndexOrThrow(_cursor, "exercise_id");
          final int _cursorIndexOfSetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "set_number");
          final int _cursorIndexOfWeightKg = CursorUtil.getColumnIndexOrThrow(_cursor, "weight_kg");
          final int _cursorIndexOfReps = CursorUtil.getColumnIndexOrThrow(_cursor, "reps");
          final int _cursorIndexOfRir = CursorUtil.getColumnIndexOrThrow(_cursor, "rir");
          final int _cursorIndexOfIsPartial = CursorUtil.getColumnIndexOrThrow(_cursor, "is_partial");
          final int _cursorIndexOfRomConsistencyScore = CursorUtil.getColumnIndexOrThrow(_cursor, "rom_consistency_score");
          final SetEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpWorkoutId;
            _tmpWorkoutId = _cursor.getLong(_cursorIndexOfWorkoutId);
            final long _tmpExerciseId;
            _tmpExerciseId = _cursor.getLong(_cursorIndexOfExerciseId);
            final int _tmpSetNumber;
            _tmpSetNumber = _cursor.getInt(_cursorIndexOfSetNumber);
            final double _tmpWeightKg;
            _tmpWeightKg = _cursor.getDouble(_cursorIndexOfWeightKg);
            final int _tmpReps;
            _tmpReps = _cursor.getInt(_cursorIndexOfReps);
            final Integer _tmpRir;
            if (_cursor.isNull(_cursorIndexOfRir)) {
              _tmpRir = null;
            } else {
              _tmpRir = _cursor.getInt(_cursorIndexOfRir);
            }
            final boolean _tmpIsPartial;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPartial);
            _tmpIsPartial = _tmp != 0;
            final Double _tmpRomConsistencyScore;
            if (_cursor.isNull(_cursorIndexOfRomConsistencyScore)) {
              _tmpRomConsistencyScore = null;
            } else {
              _tmpRomConsistencyScore = _cursor.getDouble(_cursorIndexOfRomConsistencyScore);
            }
            _result = new SetEntity(_tmpId,_tmpWorkoutId,_tmpExerciseId,_tmpSetNumber,_tmpWeightKg,_tmpReps,_tmpRir,_tmpIsPartial,_tmpRomConsistencyScore);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
