package com.jarvis.gymtracker.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.gymtracker.data.local.AppDatabase
import com.jarvis.gymtracker.data.local.datastore.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val database: AppDatabase
) : ViewModel() {

    val darkMode: StateFlow<Boolean?> = userPreferences.darkMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun setDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            userPreferences.setDarkMode(isDark)
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            database.clearAllTables()
            userPreferences.clearAllData()
        }
    }
}