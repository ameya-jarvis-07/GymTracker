package com.jarvis.gymtracker.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object ProfileSetup : Screen("profile_setup")
    object Home : Screen("home")
    object WorkoutSplit : Screen("workout_split")
    object Attendance : Screen("attendance")
    object WorkoutSession : Screen("workout_session")
    object WorkoutSummary : Screen("workout_summary/{sessionId}") {
        fun createRoute(sessionId: Long) = "workout_summary/$sessionId"
    }
    object History : Screen("history")
    object Progress : Screen("progress")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object EditProfile : Screen("edit_profile")
}