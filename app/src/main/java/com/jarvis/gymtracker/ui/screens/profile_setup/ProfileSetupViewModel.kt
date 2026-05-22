package com.jarvis.gymtracker.ui.screens.profile_setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.gymtracker.data.local.entity.UserEntity
import com.jarvis.gymtracker.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSetupViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun saveProfile(
        name: String,
        age: String,
        gender: String,
        height: String,
        weight: String,
        goal: String,
        targetWeight: String
    ) {
        viewModelScope.launch {
            if (name.isBlank() || age.isBlank() || height.isBlank() || weight.isBlank() || targetWeight.isBlank()) {
                _eventFlow.emit(UiEvent.ShowSnackbar("Please fill all fields"))
                return@launch
            }

            try {
                val user = UserEntity(
                    name = name,
                    age = age.toInt(),
                    gender = gender,
                    height = height.toDouble(),
                    weight = weight.toDouble(),
                    goal = goal,
                    targetWeight = targetWeight.toDouble()
                )
                userRepository.insertUser(user)
                _eventFlow.emit(UiEvent.SaveSuccess)
            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackbar("Invalid input: ${e.localizedMessage}"))
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveSuccess : UiEvent()
    }
}