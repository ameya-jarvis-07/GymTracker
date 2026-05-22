package com.jarvis.gymtracker.ui.screens.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.gymtracker.data.local.entity.AttendanceEntity
import com.jarvis.gymtracker.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun markAttendance(status: String) {
        viewModelScope.launch {
            val attendance = AttendanceEntity(
                date = LocalDate.now(),
                status = status
            )
            workoutRepository.insertAttendance(attendance)
            _eventFlow.emit(UiEvent.AttendanceMarked)
        }
    }

    sealed class UiEvent {
        object AttendanceMarked : UiEvent()
    }
}