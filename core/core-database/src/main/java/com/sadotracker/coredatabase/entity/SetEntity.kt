package com.sadotracker.coredatabase.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "sets",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutEntity::class,
            parentColumns = ["id"],
            childColumns = ["workout_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exercise_id"]
        )
    ]
)
data class SetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "workout_id") val workoutId: Long,
    @ColumnInfo(name = "exercise_id") val exerciseId: Long,
    @ColumnInfo(name = "set_number") val setNumber: Int,
    @ColumnInfo(name = "weight_kg") val weightKg: Double,
    val reps: Int,
    val rir: Int?,
    @ColumnInfo(name = "is_partial", defaultValue = "0") val isPartial: Boolean = false,
    @ColumnInfo(name = "rom_consistency_score") val romConsistencyScore: Double?,
    @ColumnInfo(name = "rest_taken_secs", defaultValue = "NULL") val restTakenSecs: Int? = null
)
