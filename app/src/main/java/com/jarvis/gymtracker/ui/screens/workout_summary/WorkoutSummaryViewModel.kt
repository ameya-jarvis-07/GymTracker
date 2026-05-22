package com.jarvis.gymtracker.ui.screens.workout_summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.gymtracker.data.local.entity.WorkoutSessionEntity
import com.jarvis.gymtracker.domain.repository.WorkoutRepository
import com.jarvis.gymtracker.ui.screens.workout_session.ExerciseWithLogs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutSummaryViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<WorkoutSummaryUiState>(WorkoutSummaryUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadSummary(sessionId: Long) {
        viewModelScope.launch {
            combine(
                workoutRepository.getAllSessions().map { sessions -> sessions.find { it.id == sessionId } },
                workoutRepository.getLogsForSession(sessionId),
                workoutRepository.getAllExercises()
            ) { session, logs, exercises ->
                if (session != null) {
                    val groupedLogs = logs.groupBy { it.exerciseId }
                    val exercisesWithLogs = groupedLogs.map { (exerciseId, exerciseLogs) ->
                        val exercise = exercises.find { it.id == exerciseId }!!
                        ExerciseWithLogs(exercise, exerciseLogs)
                    }
                    val totalVolume = logs.sumOf { it.reps * it.weight }
                    WorkoutSummaryUiState.Success(session, exercisesWithLogs, totalVolume)
                } else {
                    WorkoutSummaryUiState.Error("Session not found")
                }
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}

sealed class WorkoutSummaryUiState {
    object Loading : WorkoutSummaryUiState()
    data class Success(
        val session: WorkoutSessionEntity,
        val exercises: List<ExerciseWithLogs>,
        val totalVolume: Double
    ) : WorkoutSummaryUiState()
    data class Error(val message: String) : WorkoutSummaryUiState()
}
