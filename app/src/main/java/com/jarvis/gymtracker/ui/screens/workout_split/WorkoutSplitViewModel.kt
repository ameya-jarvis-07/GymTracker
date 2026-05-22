package com.jarvis.gymtracker.ui.screens.workout_split

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.gymtracker.data.local.entity.WorkoutSplitEntity
import com.jarvis.gymtracker.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutSplitViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    val splits: StateFlow<List<WorkoutSplitEntity>> = workoutRepository.getAllSplits()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSplit(dayOfWeek: Int, muscleGroups: String) {
        viewModelScope.launch {
            val existingSplit = workoutRepository.getSplitForDay(dayOfWeek)
            if (existingSplit != null) {
                workoutRepository.insertSplit(existingSplit.copy(muscleGroups = muscleGroups))
            } else {
                workoutRepository.insertSplit(WorkoutSplitEntity(dayOfWeek = dayOfWeek, muscleGroups = muscleGroups))
            }
        }
    }
}