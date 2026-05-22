package com.jarvis.gymtracker.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.gymtracker.data.local.entity.AttendanceEntity
import com.jarvis.gymtracker.data.local.entity.UserEntity
import com.jarvis.gymtracker.data.local.entity.WorkoutSplitEntity
import com.jarvis.gymtracker.domain.repository.UserRepository
import com.jarvis.gymtracker.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    val user: StateFlow<UserEntity?> = userRepository.getUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val todaySplit: StateFlow<WorkoutSplitEntity?> = flow {
        val dayOfWeek = LocalDate.now().dayOfWeek.value // 1-7
        emit(workoutRepository.getSplitForDay(dayOfWeek))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val todayAttendance: StateFlow<AttendanceEntity?> = flow {
        emit(workoutRepository.getAttendanceForDate(LocalDate.now()))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val streak: StateFlow<Int> = workoutRepository.getAttendanceStreak()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun markAttendance(status: String) {
        viewModelScope.launch {
            val attendance = AttendanceEntity(
                date = LocalDate.now(),
                status = status
            )
            workoutRepository.insertAttendance(attendance)
        }
    }
}