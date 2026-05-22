package com.jarvis.gymtracker.data.repository

import com.jarvis.gymtracker.data.local.dao.*
import com.jarvis.gymtracker.data.local.entity.*
import com.jarvis.gymtracker.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val workoutSplitDao: WorkoutSplitDao,
    private val exerciseDao: ExerciseDao,
    private val attendanceDao: AttendanceDao,
    private val workoutSessionDao: WorkoutSessionDao,
    private val exerciseLogDao: ExerciseLogDao
) : WorkoutRepository {

    override fun getAllSplits(): Flow<List<WorkoutSplitEntity>> = workoutSplitDao.getAllSplits()

    override suspend fun getSplitForDay(dayOfWeek: Int): WorkoutSplitEntity? =
        workoutSplitDao.getSplitForDay(dayOfWeek)

    override suspend fun insertSplit(split: WorkoutSplitEntity) {
        workoutSplitDao.insertSplit(split)
    }

    override suspend fun deleteSplit(split: WorkoutSplitEntity) {
        workoutSplitDao.deleteSplit(split)
    }

    override fun getAllExercises(): Flow<List<ExerciseEntity>> = exerciseDao.getAllExercises()

    override fun getExercisesByMuscleGroup(muscleGroup: String): Flow<List<ExerciseEntity>> =
        exerciseDao.getExercisesByMuscleGroup(muscleGroup)

    override suspend fun insertExercise(exercise: ExerciseEntity) {
        exerciseDao.insertExercise(exercise)
    }

    override suspend fun getExerciseCount(): Int = exerciseDao.getExerciseCount()

    override fun getAllAttendance(): Flow<List<AttendanceEntity>> = attendanceDao.getAllAttendance()

    override suspend fun getAttendanceForDate(date: LocalDate): AttendanceEntity? =
        attendanceDao.getAttendanceForDate(date)

    override suspend fun insertAttendance(attendance: AttendanceEntity) {
        attendanceDao.insertAttendance(attendance)
    }

    override fun getAttendanceStreak(): Flow<Int> = attendanceDao.getAttendanceStreak()

    override fun getAllSessions(): Flow<List<WorkoutSessionEntity>> = workoutSessionDao.getAllSessions()

    override fun getActiveSession(): Flow<WorkoutSessionEntity?> = workoutSessionDao.getActiveSession()

    override suspend fun startSession(session: WorkoutSessionEntity): Long =
        workoutSessionDao.insertSession(session)

    override suspend fun endSession(session: WorkoutSessionEntity) {
        workoutSessionDao.updateSession(session)
    }

    override suspend fun deleteSession(session: WorkoutSessionEntity) {
        workoutSessionDao.deleteSession(session)
    }

    override fun getLogsForSession(sessionId: Long): Flow<List<ExerciseLogEntity>> =
        exerciseLogDao.getLogsForSession(sessionId)

    override suspend fun insertLog(log: ExerciseLogEntity) {
        exerciseLogDao.insertLog(log)
    }

    override fun getTotalVolumeForSession(sessionId: Long): Flow<Double?> =
        exerciseLogDao.getTotalVolumeForSession(sessionId)
}