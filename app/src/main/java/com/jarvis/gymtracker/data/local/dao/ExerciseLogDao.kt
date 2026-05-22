package com.jarvis.gymtracker.data.local.dao

import androidx.room.*
import com.jarvis.gymtracker.data.local.entity.ExerciseLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseLogDao {
    @Query("SELECT * FROM exercise_logs WHERE sessionId = :sessionId")
    fun getLogsForSession(sessionId: Long): Flow<List<ExerciseLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: ExerciseLogEntity)

    @Update
    suspend fun updateLog(log: ExerciseLogEntity)

    @Delete
    suspend fun deleteLog(log: ExerciseLogEntity)

    @Query("SELECT SUM(reps * weight) FROM exercise_logs WHERE sessionId = :sessionId")
    fun getTotalVolumeForSession(sessionId: Long): Flow<Double?>
}