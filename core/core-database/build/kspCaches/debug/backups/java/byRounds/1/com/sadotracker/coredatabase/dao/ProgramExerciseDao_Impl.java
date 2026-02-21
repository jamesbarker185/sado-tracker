package com.sadotracker.coredatabase.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.sadotracker.coredatabase.entity.ProgramExerciseEntity;
import java.lang.Class;
import java.lang.Exception;
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
public final class ProgramExerciseDao_Impl implements ProgramExerciseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ProgramExerciseEntity> __insertionAdapterOfProgramExerciseEntity;

  private final EntityDeletionOrUpdateAdapter<ProgramExerciseEntity> __deletionAdapterOfProgramExerciseEntity;

  private final EntityDeletionOrUpdateAdapter<ProgramExerciseEntity> __updateAdapterOfProgramExerciseEntity;

  public ProgramExerciseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProgramExerciseEntity = new EntityInsertionAdapter<ProgramExerciseEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `program_exercises` (`id`,`program_id`,`exercise_id`,`order_index`,`default_sets`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProgramExerciseEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProgramId());
        statement.bindLong(3, entity.getExerciseId());
        statement.bindLong(4, entity.getOrderIndex());
        statement.bindLong(5, entity.getDefaultSets());
      }
    };
    this.__deletionAdapterOfProgramExerciseEntity = new EntityDeletionOrUpdateAdapter<ProgramExerciseEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `program_exercises` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProgramExerciseEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfProgramExerciseEntity = new EntityDeletionOrUpdateAdapter<ProgramExerciseEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `program_exercises` SET `id` = ?,`program_id` = ?,`exercise_id` = ?,`order_index` = ?,`default_sets` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProgramExerciseEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProgramId());
        statement.bindLong(3, entity.getExerciseId());
        statement.bindLong(4, entity.getOrderIndex());
        statement.bindLong(5, entity.getDefaultSets());
        statement.bindLong(6, entity.getId());
      }
    };
  }

  @Override
  public Object insertAll(final List<ProgramExerciseEntity> programExercises,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfProgramExerciseEntity.insert(programExercises);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final ProgramExerciseEntity programExercise,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfProgramExerciseEntity.handle(programExercise);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateAll(final List<ProgramExerciseEntity> programExercises,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfProgramExerciseEntity.handleMultiple(programExercises);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ProgramExerciseEntity>> getExercisesForProgram(final long programId) {
    final String _sql = "SELECT * FROM program_exercises WHERE program_id = ? ORDER BY order_index ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, programId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"program_exercises"}, new Callable<List<ProgramExerciseEntity>>() {
      @Override
      @NonNull
      public List<ProgramExerciseEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProgramId = CursorUtil.getColumnIndexOrThrow(_cursor, "program_id");
          final int _cursorIndexOfExerciseId = CursorUtil.getColumnIndexOrThrow(_cursor, "exercise_id");
          final int _cursorIndexOfOrderIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "order_index");
          final int _cursorIndexOfDefaultSets = CursorUtil.getColumnIndexOrThrow(_cursor, "default_sets");
          final List<ProgramExerciseEntity> _result = new ArrayList<ProgramExerciseEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProgramExerciseEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProgramId;
            _tmpProgramId = _cursor.getLong(_cursorIndexOfProgramId);
            final long _tmpExerciseId;
            _tmpExerciseId = _cursor.getLong(_cursorIndexOfExerciseId);
            final int _tmpOrderIndex;
            _tmpOrderIndex = _cursor.getInt(_cursorIndexOfOrderIndex);
            final int _tmpDefaultSets;
            _tmpDefaultSets = _cursor.getInt(_cursorIndexOfDefaultSets);
            _item = new ProgramExerciseEntity(_tmpId,_tmpProgramId,_tmpExerciseId,_tmpOrderIndex,_tmpDefaultSets);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
