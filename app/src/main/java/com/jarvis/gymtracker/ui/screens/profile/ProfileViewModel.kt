package com.jarvis.gymtracker.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.gymtracker.data.local.entity.UserEntity
import com.jarvis.gymtracker.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val user: StateFlow<UserEntity?> = userRepository.getUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun updateProfile(
        name: String,
        age: String,
        gender: String,
        height: String,
        weight: String,
        goal: String,
        targetWeight: String
    ) {
        viewModelScope.launch {
            val currentUser = user.value ?: return@launch
            
            if (name.isBlank() || age.isBlank() || height.isBlank() || weight.isBlank() || targetWeight.isBlank()) {
                _eventFlow.emit(UiEvent.ShowSnackbar("Please fill all fields"))
                return@launch
            }

            try {
                val updatedUser = currentUser.copy(
                    name = name,
                    age = age.toInt(),
                    gender = gender,
                    height = height.toDouble(),
                    weight = weight.toDouble(),
                    goal = goal,
                    targetWeight = targetWeight.toDouble()
                )
                userRepository.updateUser(updatedUser)
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
