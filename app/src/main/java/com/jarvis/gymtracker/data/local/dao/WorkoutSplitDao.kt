package com.jarvis.gymtracker.data.local.dao

import androidx.room.*
import com.jarvis.gymtracker.data.local.entity.WorkoutSplitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutSplitDao {
    @Query("SELECT * FROM workout_splits ORDER BY dayOfWeek ASC")
    fun getAllSplits(): Flow<List<WorkoutSplitEntity>>

    @Query("SELECT * FROM workout_splits WHERE dayOfWeek = :dayOfWeek LIMIT 1")
    suspend fun getSplitForDay(dayOfWeek: Int): WorkoutSplitEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSplit(split: WorkoutSplitEntity)

    @Update
    suspend fun updateSplit(split: WorkoutSplitEntity)

    @Delete
    suspend fun deleteSplit(split: WorkoutSplitEntity)
}