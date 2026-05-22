package com.jarvis.gymtracker.ui.screens.workout_session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.gymtracker.data.local.entity.ExerciseEntity
import com.jarvis.gymtracker.data.local.entity.ExerciseLogEntity
import com.jarvis.gymtracker.data.local.entity.WorkoutSessionEntity
import com.jarvis.gymtracker.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class WorkoutSessionViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    val activeSession = workoutRepository.getActiveSession()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _selectedExercises = MutableStateFlow<List<ExerciseWithLogs>>(emptyList())
    val selectedExercises = _selectedExercises.asStateFlow()

    val todayMuscleGroups: StateFlow<List<String>> = flow {
        val dayOfWeek = LocalDate.now().dayOfWeek.value
        val split = workoutRepository.getSplitForDay(dayOfWeek)
        emit(split?.muscleGroups?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() } ?: emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val availableExercises = workoutRepository.getAllExercises()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            activeSession.collectLatest { session ->
                if (session != null) {
                    loadLogsForSession(session.id)
                } else {
                    _selectedExercises.value = emptyList()
                }
            }
        }
    }

    private fun loadLogsForSession(sessionId: Long) {
        viewModelScope.launch {
            combine(
                workoutRepository.getLogsForSession(sessionId),
                workoutRepository.getAllExercises()
            ) { logs, exercises ->
                val groupedLogs = logs.groupBy { it.exerciseId }
                groupedLogs.mapNotNull { (exerciseId, exerciseLogs) ->
                    exercises.find { it.id == exerciseId }?.let { exercise ->
                        ExerciseWithLogs(exercise, exerciseLogs.sortedBy { it.setNumber })
                    }
                }
            }.collect { exercisesWithLogs ->
                _selectedExercises.value = exercisesWithLogs
            }
        }
    }

    fun startNewSession() {
        viewModelScope.launch {
            val session = WorkoutSessionEntity(
                date = LocalDateTime.now(),
                startTime = LocalDateTime.now()
            )
            workoutRepository.startSession(session)
        }
    }

    fun addExerciseToSession(exercise: ExerciseEntity) {
        val currentSession = activeSession.value ?: return
        viewModelScope.launch {
            val log = ExerciseLogEntity(
                sessionId = currentSession.id,
                exerciseId = exercise.id,
                setNumber = 1,
                reps = 0,
                weight = 0.0
            )
            workoutRepository.insertLog(log)
        }
    }

    fun addSet(exerciseId: Long) {
        val currentSession = activeSession.value ?: return
        val exerciseWithLogs = _selectedExercises.value.find { it.exercise.id == exerciseId } ?: return
        val nextSetNumber = (exerciseWithLogs.logs.maxOfOrNull { it.setNumber } ?: 0) + 1
        
        viewModelScope.launch {
            val log = ExerciseLogEntity(
                sessionId = currentSession.id,
                exerciseId = exerciseId,
                setNumber = nextSetNumber,
                reps = 0,
                weight = 0.0
            )
            workoutRepository.insertLog(log)
        }
    }

    fun updateLog(log: ExerciseLogEntity, reps: Int, weight: Double) {
        viewModelScope.launch {
            workoutRepository.insertLog(log.copy(reps = reps, weight = weight))
        }
    }

    fun endSession() {
        val currentSession = activeSession.value ?: return
        viewModelScope.launch {
            val endTime = LocalDateTime.now()
            val duration = Duration.between(currentSession.startTime, endTime).seconds
            workoutRepository.endSession(currentSession.copy(endTime = endTime, duration = duration))
        }
    }
}

data class ExerciseWithLogs(
    val exercise: ExerciseEntity,
    val logs: List<ExerciseLogEntity>
)
