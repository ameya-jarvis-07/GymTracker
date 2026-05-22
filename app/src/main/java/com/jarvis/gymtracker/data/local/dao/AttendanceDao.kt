package com.jarvis.gymtracker.data.local.dao

import androidx.room.*
import com.jarvis.gymtracker.data.local.entity.AttendanceEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface AttendanceDao {
    @Query("SELECT * FROM attendance ORDER BY date DESC")
    fun getAllAttendance(): Flow<List<AttendanceEntity>>

    @Query("SELECT * FROM attendance WHERE date = :date LIMIT 1")
    suspend fun getAttendanceForDate(date: LocalDate): AttendanceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: AttendanceEntity)

    @Update
    suspend fun updateAttendance(attendance: AttendanceEntity)

    @Query("SELECT COUNT(*) FROM attendance WHERE status = 'Present'")
    fun getAttendanceStreak(): Flow<Int>
}