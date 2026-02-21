package com.sadotracker.coredatabase.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.sadotracker.coredatabase.entity.ExerciseEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
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
public final class ExerciseDao_Impl implements ExerciseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ExerciseEntity> __insertionAdapterOfExerciseEntity;

  public ExerciseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfExerciseEntity = new EntityInsertionAdapter<ExerciseEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `exercises` (`id`,`name`,`muscle_group`,`primary_muscle`,`secondary_muscle`,`category`,`equipment`,`mechanics`,`modality`,`force_vector`,`is_custom`,`instructions`,`image_res_id`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExerciseEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getMuscleGroup());
        statement.bindString(4, entity.getPrimaryMuscle());
        if (entity.getSecondaryMuscle() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getSecondaryMuscle());
        }
        statement.bindString(6, entity.getCategory());
        statement.bindString(7, entity.getEquipment());
        statement.bindString(8, entity.getMechanics());
        statement.bindString(9, entity.getModality());
        statement.bindString(10, entity.getForceVector());
        final int _tmp = entity.isCustom() ? 1 : 0;
        statement.bindLong(11, _tmp);
        if (entity.getInstructions() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getInstructions());
        }
        if (entity.getImageResId() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getImageResId());
        }
      }
    };
  }

  @Override
  public Object insert(final ExerciseEntity exercise,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfExerciseEntity.insertAndReturnId(exercise);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<ExerciseEntity> exercises,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfExerciseEntity.insert(exercises);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ExerciseEntity>> getAll() {
    final String _sql = "SELECT * FROM exercises ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exercises"}, new Callable<List<ExerciseEntity>>() {
      @Override
      @NonNull
      public List<ExerciseEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfMuscleGroup = CursorUtil.getColumnIndexOrThrow(_cursor, "muscle_group");
          final int _cursorIndexOfPrimaryMuscle = CursorUtil.getColumnIndexOrThrow(_cursor, "primary_muscle");
          final int _cursorIndexOfSecondaryMuscle = CursorUtil.getColumnIndexOrThrow(_cursor, "secondary_muscle");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfEquipment = CursorUtil.getColumnIndexOrThrow(_cursor, "equipment");
          final int _cursorIndexOfMechanics = CursorUtil.getColumnIndexOrThrow(_cursor, "mechanics");
          final int _cursorIndexOfModality = CursorUtil.getColumnIndexOrThrow(_cursor, "modality");
          final int _cursorIndexOfForceVector = CursorUtil.getColumnIndexOrThrow(_cursor, "force_vector");
          final int _cursorIndexOfIsCustom = CursorUtil.getColumnIndexOrThrow(_cursor, "is_custom");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfImageResId = CursorUtil.getColumnIndexOrThrow(_cursor, "image_res_id");
          final List<ExerciseEntity> _result = new ArrayList<ExerciseEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExerciseEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpMuscleGroup;
            _tmpMuscleGroup = _cursor.getString(_cursorIndexOfMuscleGroup);
            final String _tmpPrimaryMuscle;
            _tmpPrimaryMuscle = _cursor.getString(_cursorIndexOfPrimaryMuscle);
            final String _tmpSecondaryMuscle;
            if (_cursor.isNull(_cursorIndexOfSecondaryMuscle)) {
              _tmpSecondaryMuscle = null;
            } else {
              _tmpSecondaryMuscle = _cursor.getString(_cursorIndexOfSecondaryMuscle);
            }
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpEquipment;
            _tmpEquipment = _cursor.getString(_cursorIndexOfEquipment);
            final String _tmpMechanics;
            _tmpMechanics = _cursor.getString(_cursorIndexOfMechanics);
            final String _tmpModality;
            _tmpModality = _cursor.getString(_cursorIndexOfModality);
            final String _tmpForceVector;
            _tmpForceVector = _cursor.getString(_cursorIndexOfForceVector);
            final boolean _tmpIsCustom;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCustom);
            _tmpIsCustom = _tmp != 0;
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            final String _tmpImageResId;
            if (_cursor.isNull(_cursorIndexOfImageResId)) {
              _tmpImageResId = null;
            } else {
              _tmpImageResId = _cursor.getString(_cursorIndexOfImageResId);
            }
            _item = new ExerciseEntity(_tmpId,_tmpName,_tmpMuscleGroup,_tmpPrimaryMuscle,_tmpSecondaryMuscle,_tmpCategory,_tmpEquipment,_tmpMechanics,_tmpModality,_tmpForceVector,_tmpIsCustom,_tmpInstructions,_tmpImageResId);
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
  public Object getById(final long id, final Continuation<? super ExerciseEntity> $completion) {
    final String _sql = "SELECT * FROM exercises WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ExerciseEntity>() {
      @Override
      @Nullable
      public ExerciseEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfMuscleGroup = CursorUtil.getColumnIndexOrThrow(_cursor, "muscle_group");
          final int _cursorIndexOfPrimaryMuscle = CursorUtil.getColumnIndexOrThrow(_cursor, "primary_muscle");
          final int _cursorIndexOfSecondaryMuscle = CursorUtil.getColumnIndexOrThrow(_cursor, "secondary_muscle");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfEquipment = CursorUtil.getColumnIndexOrThrow(_cursor, "equipment");
          final int _cursorIndexOfMechanics = CursorUtil.getColumnIndexOrThrow(_cursor, "mechanics");
          final int _cursorIndexOfModality = CursorUtil.getColumnIndexOrThrow(_cursor, "modality");
          final int _cursorIndexOfForceVector = CursorUtil.getColumnIndexOrThrow(_cursor, "force_vector");
          final int _cursorIndexOfIsCustom = CursorUtil.getColumnIndexOrThrow(_cursor, "is_custom");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfImageResId = CursorUtil.getColumnIndexOrThrow(_cursor, "image_res_id");
          final ExerciseEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpMuscleGroup;
            _tmpMuscleGroup = _cursor.getString(_cursorIndexOfMuscleGroup);
            final String _tmpPrimaryMuscle;
            _tmpPrimaryMuscle = _cursor.getString(_cursorIndexOfPrimaryMuscle);
            final String _tmpSecondaryMuscle;
            if (_cursor.isNull(_cursorIndexOfSecondaryMuscle)) {
              _tmpSecondaryMuscle = null;
            } else {
              _tmpSecondaryMuscle = _cursor.getString(_cursorIndexOfSecondaryMuscle);
            }
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpEquipment;
            _tmpEquipment = _cursor.getString(_cursorIndexOfEquipment);
            final String _tmpMechanics;
            _tmpMechanics = _cursor.getString(_cursorIndexOfMechanics);
            final String _tmpModality;
            _tmpModality = _cursor.getString(_cursorIndexOfModality);
            final String _tmpForceVector;
            _tmpForceVector = _cursor.getString(_cursorIndexOfForceVector);
            final boolean _tmpIsCustom;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCustom);
            _tmpIsCustom = _tmp != 0;
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            final String _tmpImageResId;
            if (_cursor.isNull(_cursorIndexOfImageResId)) {
              _tmpImageResId = null;
            } else {
              _tmpImageResId = _cursor.getString(_cursorIndexOfImageResId);
            }
            _result = new ExerciseEntity(_tmpId,_tmpName,_tmpMuscleGroup,_tmpPrimaryMuscle,_tmpSecondaryMuscle,_tmpCategory,_tmpEquipment,_tmpMechanics,_tmpModality,_tmpForceVector,_tmpIsCustom,_tmpInstructions,_tmpImageResId);
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

  @Override
  public Flow<List<ExerciseEntity>> search(final String query) {
    final String _sql = "SELECT * FROM exercises WHERE name LIKE '%' || ? || '%' ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exercises"}, new Callable<List<ExerciseEntity>>() {
      @Override
      @NonNull
      public List<ExerciseEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfMuscleGroup = CursorUtil.getColumnIndexOrThrow(_cursor, "muscle_group");
          final int _cursorIndexOfPrimaryMuscle = CursorUtil.getColumnIndexOrThrow(_cursor, "primary_muscle");
          final int _cursorIndexOfSecondaryMuscle = CursorUtil.getColumnIndexOrThrow(_cursor, "secondary_muscle");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfEquipment = CursorUtil.getColumnIndexOrThrow(_cursor, "equipment");
          final int _cursorIndexOfMechanics = CursorUtil.getColumnIndexOrThrow(_cursor, "mechanics");
          final int _cursorIndexOfModality = CursorUtil.getColumnIndexOrThrow(_cursor, "modality");
          final int _cursorIndexOfForceVector = CursorUtil.getColumnIndexOrThrow(_cursor, "force_vector");
          final int _cursorIndexOfIsCustom = CursorUtil.getColumnIndexOrThrow(_cursor, "is_custom");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfImageResId = CursorUtil.getColumnIndexOrThrow(_cursor, "image_res_id");
          final List<ExerciseEntity> _result = new ArrayList<ExerciseEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExerciseEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpMuscleGroup;
            _tmpMuscleGroup = _cursor.getString(_cursorIndexOfMuscleGroup);
            final String _tmpPrimaryMuscle;
            _tmpPrimaryMuscle = _cursor.getString(_cursorIndexOfPrimaryMuscle);
            final String _tmpSecondaryMuscle;
            if (_cursor.isNull(_cursorIndexOfSecondaryMuscle)) {
              _tmpSecondaryMuscle = null;
            } else {
              _tmpSecondaryMuscle = _cursor.getString(_cursorIndexOfSecondaryMuscle);
            }
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpEquipment;
            _tmpEquipment = _cursor.getString(_cursorIndexOfEquipment);
            final String _tmpMechanics;
            _tmpMechanics = _cursor.getString(_cursorIndexOfMechanics);
            final String _tmpModality;
            _tmpModality = _cursor.getString(_cursorIndexOfModality);
            final String _tmpForceVector;
            _tmpForceVector = _cursor.getString(_cursorIndexOfForceVector);
            final boolean _tmpIsCustom;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCustom);
            _tmpIsCustom = _tmp != 0;
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            final String _tmpImageResId;
            if (_cursor.isNull(_cursorIndexOfImageResId)) {
              _tmpImageResId = null;
            } else {
              _tmpImageResId = _cursor.getString(_cursorIndexOfImageResId);
            }
            _item = new ExerciseEntity(_tmpId,_tmpName,_tmpMuscleGroup,_tmpPrimaryMuscle,_tmpSecondaryMuscle,_tmpCategory,_tmpEquipment,_tmpMechanics,_tmpModality,_tmpForceVector,_tmpIsCustom,_tmpInstructions,_tmpImageResId);
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
  public Flow<List<ExerciseEntity>> filterBy(final List<String> mechanics,
      final List<String> forceVectors, final List<String> categories) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM exercises WHERE mechanics IN (");
    final int _inputSize = mechanics.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(") AND force_vector IN (");
    final int _inputSize_1 = forceVectors.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize_1);
    _stringBuilder.append(") AND category IN (");
    final int _inputSize_2 = categories.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize_2);
    _stringBuilder.append(") ORDER BY name ASC");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize + _inputSize_1 + _inputSize_2;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : mechanics) {
      _statement.bindString(_argIndex, _item);
      _argIndex++;
    }
    _argIndex = 1 + _inputSize;
    for (String _item_1 : forceVectors) {
      _statement.bindString(_argIndex, _item_1);
      _argIndex++;
    }
    _argIndex = 1 + _inputSize + _inputSize_1;
    for (String _item_2 : categories) {
      _statement.bindString(_argIndex, _item_2);
      _argIndex++;
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exercises"}, new Callable<List<ExerciseEntity>>() {
      @Override
      @NonNull
      public List<ExerciseEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfMuscleGroup = CursorUtil.getColumnIndexOrThrow(_cursor, "muscle_group");
          final int _cursorIndexOfPrimaryMuscle = CursorUtil.getColumnIndexOrThrow(_cursor, "primary_muscle");
          final int _cursorIndexOfSecondaryMuscle = CursorUtil.getColumnIndexOrThrow(_cursor, "secondary_muscle");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfEquipment = CursorUtil.getColumnIndexOrThrow(_cursor, "equipment");
          final int _cursorIndexOfMechanics = CursorUtil.getColumnIndexOrThrow(_cursor, "mechanics");
          final int _cursorIndexOfModality = CursorUtil.getColumnIndexOrThrow(_cursor, "modality");
          final int _cursorIndexOfForceVector = CursorUtil.getColumnIndexOrThrow(_cursor, "force_vector");
          final int _cursorIndexOfIsCustom = CursorUtil.getColumnIndexOrThrow(_cursor, "is_custom");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfImageResId = CursorUtil.getColumnIndexOrThrow(_cursor, "image_res_id");
          final List<ExerciseEntity> _result = new ArrayList<ExerciseEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExerciseEntity _item_3;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpMuscleGroup;
            _tmpMuscleGroup = _cursor.getString(_cursorIndexOfMuscleGroup);
            final String _tmpPrimaryMuscle;
            _tmpPrimaryMuscle = _cursor.getString(_cursorIndexOfPrimaryMuscle);
            final String _tmpSecondaryMuscle;
            if (_cursor.isNull(_cursorIndexOfSecondaryMuscle)) {
              _tmpSecondaryMuscle = null;
            } else {
              _tmpSecondaryMuscle = _cursor.getString(_cursorIndexOfSecondaryMuscle);
            }
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpEquipment;
            _tmpEquipment = _cursor.getString(_cursorIndexOfEquipment);
            final String _tmpMechanics;
            _tmpMechanics = _cursor.getString(_cursorIndexOfMechanics);
            final String _tmpModality;
            _tmpModality = _cursor.getString(_cursorIndexOfModality);
            final String _tmpForceVector;
            _tmpForceVector = _cursor.getString(_cursorIndexOfForceVector);
            final boolean _tmpIsCustom;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCustom);
            _tmpIsCustom = _tmp != 0;
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            final String _tmpImageResId;
            if (_cursor.isNull(_cursorIndexOfImageResId)) {
              _tmpImageResId = null;
            } else {
              _tmpImageResId = _cursor.getString(_cursorIndexOfImageResId);
            }
            _item_3 = new ExerciseEntity(_tmpId,_tmpName,_tmpMuscleGroup,_tmpPrimaryMuscle,_tmpSecondaryMuscle,_tmpCategory,_tmpEquipment,_tmpMechanics,_tmpModality,_tmpForceVector,_tmpIsCustom,_tmpInstructions,_tmpImageResId);
            _result.add(_item_3);
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
