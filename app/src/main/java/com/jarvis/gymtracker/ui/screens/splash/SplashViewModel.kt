package com.jarvis.gymtracker.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.gymtracker.data.local.DatabaseInitializer
import com.jarvis.gymtracker.data.local.datastore.UserPreferences
import com.jarvis.gymtracker.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferences,
    private val databaseInitializer: DatabaseInitializer
) : ViewModel() {

    val onboardingCompleted: StateFlow<Boolean?> = userPreferences.onboardingCompleted
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val userExists: StateFlow<Boolean?> = userRepository.getUser()
        .map { it != null }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        viewModelScope.launch {
            databaseInitializer.populateInitialExercises()
        }
    }
}