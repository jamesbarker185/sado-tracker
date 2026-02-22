package com.sadotracker.coredatabase.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "programs")
data class ProgramEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String?,
    @ColumnInfo(name = "is_preset", defaultValue = "0") val isPreset: Boolean = false,
    @ColumnInfo(name = "cycle_days") val cycleDays: Int = 7,
    @ColumnInfo(name = "created_at") val createdAt: Long
)

