package com.jarvis.gymtracker.di

import android.content.Context
import androidx.room.Room
import com.jarvis.gymtracker.data.local.AppDatabase
import com.jarvis.gymtracker.data.local.dao.*
import com.jarvis.gymtracker.data.local.datastore.UserPreferences
import com.jarvis.gymtracker.data.repository.UserRepositoryImpl
import com.jarvis.gymtracker.data.repository.WorkoutRepositoryImpl
import com.jarvis.gymtracker.domain.repository.UserRepository
import com.jarvis.gymtracker.domain.repository.WorkoutRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "iron_log_db"
        ).build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    @Provides
    fun provideWorkoutSplitDao(database: AppDatabase): WorkoutSplitDao = database.workoutSplitDao()

    @Provides
    fun provideAttendanceDao(database: AppDatabase): AttendanceDao = database.attendanceDao()

    @Provides
    fun provideExerciseDao(database: AppDatabase): ExerciseDao = database.exerciseDao()

    @Provides
    fun provideWorkoutSessionDao(database: AppDatabase): WorkoutSessionDao = database.workoutSessionDao()

    @Provides
    fun provideExerciseLogDao(database: AppDatabase): ExerciseLogDao = database.exerciseLogDao()

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepositoryImpl(userDao)
    }

    @Provides
    @Singleton
    fun provideWorkoutRepository(
        workoutSplitDao: WorkoutSplitDao,
        exerciseDao: ExerciseDao,
        attendanceDao: AttendanceDao,
        workoutSessionDao: WorkoutSessionDao,
        exerciseLogDao: ExerciseLogDao
    ): WorkoutRepository {
        return WorkoutRepositoryImpl(
            workoutSplitDao,
            exerciseDao,
            attendanceDao,
            workoutSessionDao,
            exerciseLogDao
        )
    }

    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }
}