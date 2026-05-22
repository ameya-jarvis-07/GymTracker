package com.jarvis.gymtracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jarvis.gymtracker.data.local.converter.DateConverter
import com.jarvis.gymtracker.data.local.dao.*
import com.jarvis.gymtracker.data.local.entity.*

@Database(
    entities = [
        UserEntity::class,
        WorkoutSplitEntity::class,
        AttendanceEntity::class,
        ExerciseEntity::class,
        WorkoutSessionEntity::class,
        ExerciseLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun workoutSplitDao(): WorkoutSplitDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutSessionDao(): WorkoutSessionDao
    abstract fun exerciseLogDao(): ExerciseLogDao
}