package com.sadotracker.coredatabase.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "program_exercises",
    foreignKeys = [
        ForeignKey(
            entity = ProgramEntity::class,
            parentColumns = ["id"],
            childColumns = ["program_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exercise_id"]
        )
    ]
)
data class ProgramExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "program_id") val programId: Long,
    @ColumnInfo(name = "exercise_id") val exerciseId: Long,
    @ColumnInfo(name = "order_index") val orderIndex: Int,
    @ColumnInfo(name = "default_sets", defaultValue = "3") val defaultSets: Int = 3,
    @ColumnInfo(name = "day_index", defaultValue = "0") val dayIndex: Int = 0,
    @ColumnInfo(name = "custom_rep_min") val customRepMin: Int? = null,
    @ColumnInfo(name = "custom_rep_max") val customRepMax: Int? = null,
    @ColumnInfo(name = "weight_increment_kg", defaultValue = "2.5") val weightIncrementKg: Double = 2.5,
    @ColumnInfo(name = "rest_time_secs", defaultValue = "120") val restTimeSecs: Int = 120
)
