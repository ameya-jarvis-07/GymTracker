package com.jarvis.gymtracker.ui.screens.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.gymtracker.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    val attendanceConsistency: StateFlow<Double> = workoutRepository.getAllAttendance()
        .map { attendanceList ->
            if (attendanceList.isEmpty()) 0.0
            else {
                val presentCount = attendanceList.count { it.status == "Present" }
                presentCount.toDouble() / attendanceList.size
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val monthlyWorkouts: StateFlow<Map<String, Int>> = workoutRepository.getAllSessions()
        .map { sessions ->
            sessions.groupBy { it.date.month.name }
                .mapValues { it.value.size }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())
}