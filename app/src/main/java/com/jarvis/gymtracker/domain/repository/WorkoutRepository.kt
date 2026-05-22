package com.jarvis.gymtracker.domain.repository

import com.jarvis.gymtracker.data.local.entity.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface WorkoutRepository {
    // Splits
    fun getAllSplits(): Flow<List<WorkoutSplitEntity>>
    suspend fun getSplitForDay(dayOfWeek: Int): WorkoutSplitEntity?
    suspend fun insertSplit(split: WorkoutSplitEntity)
    suspend fun deleteSplit(split: WorkoutSplitEntity)

    // Exercises
    fun getAllExercises(): Flow<List<ExerciseEntity>>
    fun getExercisesByMuscleGroup(muscleGroup: String): Flow<List<ExerciseEntity>>
    suspend fun insertExercise(exercise: ExerciseEntity)
    suspend fun getExerciseCount(): Int

    // Attendance
    fun getAllAttendance(): Flow<List<AttendanceEntity>>
    suspend fun getAttendanceForDate(date: LocalDate): AttendanceEntity?
    suspend fun insertAttendance(attendance: AttendanceEntity)
    fun getAttendanceStreak(): Flow<Int>

    // Sessions
    fun getAllSessions(): Flow<List<WorkoutSessionEntity>>
    fun getActiveSession(): Flow<WorkoutSessionEntity?>
    suspend fun startSession(session: WorkoutSessionEntity): Long
    suspend fun endSession(session: WorkoutSessionEntity)
    suspend fun deleteSession(session: WorkoutSessionEntity)
    
    // Logs
    fun getLogsForSession(sessionId: Long): Flow<List<ExerciseLogEntity>>
    suspend fun insertLog(log: ExerciseLogEntity)
    fun getTotalVolumeForSession(sessionId: Long): Flow<Double?>
}