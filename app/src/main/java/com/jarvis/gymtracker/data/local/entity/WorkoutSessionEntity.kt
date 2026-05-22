package com.jarvis.gymtracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "workout_sessions")
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: LocalDateTime,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime? = null,
    val duration: Long = 0 // in seconds
)