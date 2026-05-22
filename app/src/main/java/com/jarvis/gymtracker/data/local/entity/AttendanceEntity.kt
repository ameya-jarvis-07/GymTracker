package com.jarvis.gymtracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "attendance")
data class AttendanceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: LocalDate,
    val status: String, // "Present", "Skipped", "Rest Day"
    val workoutStarted: Boolean = false
)