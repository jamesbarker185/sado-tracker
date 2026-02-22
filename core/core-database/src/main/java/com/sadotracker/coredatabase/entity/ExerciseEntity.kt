package com.sadotracker.coredatabase.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "muscle_group") val muscleGroup: String,
    @ColumnInfo(name = "primary_muscle") val primaryMuscle: String,
    @ColumnInfo(name = "secondary_muscle") val secondaryMuscle: String?,
    val category: String,
    val equipment: String,
    val mechanics: String,
    val modality: String,
    @ColumnInfo(name = "force_vector") val forceVector: String,
    @ColumnInfo(name = "is_custom", defaultValue = "0") val isCustom: Boolean = false,
    val instructions: String?,
    @ColumnInfo(name = "image_res_id") val imageResId: String?,
    @ColumnInfo(name = "rest_time_seconds", defaultValue = "120") val restTimeSecs: Int = 120
)
