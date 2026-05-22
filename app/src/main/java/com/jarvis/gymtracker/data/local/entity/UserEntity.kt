package com.jarvis.gymtracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val age: Int,
    val gender: String,
    val height: Double,
    val weight: Double,
    val goal: String,
    val targetWeight: Double,
    val profileImageUri: String? = null
)