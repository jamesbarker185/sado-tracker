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
    @ColumnInfo(name = "created_at") val createdAt: Long
)
