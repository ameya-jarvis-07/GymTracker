package com.jarvis.gymtracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_splits")
data class WorkoutSplitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dayOfWeek: Int, // 1 for Monday, 7 for Sunday
    val muscleGroups: String // Comma separated list like "Chest, Triceps"
)