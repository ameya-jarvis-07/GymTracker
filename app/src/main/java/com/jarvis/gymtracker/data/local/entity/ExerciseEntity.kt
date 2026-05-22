package com.jarvis.gymtracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val muscleGroup: String,
    val exerciseName: String,
    val imagePath: String? = null // Can be a drawable resource name or local path
)