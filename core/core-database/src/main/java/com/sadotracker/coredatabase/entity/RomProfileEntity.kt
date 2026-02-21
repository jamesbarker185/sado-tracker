package com.sadotracker.coredatabase.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "rom_profiles",
    indices = [Index(value = ["exercise_id"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exercise_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RomProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "exercise_id") val exerciseId: Long,
    @ColumnInfo(name = "top_y_threshold") val topYThreshold: Double,
    @ColumnInfo(name = "bottom_y_threshold") val bottomYThreshold: Double,
    @ColumnInfo(name = "calibrated_at") val calibratedAt: Long
)
