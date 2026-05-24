package com.jarvis.gymtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import com.jarvis.gymtracker.ui.MainViewModel
import com.jarvis.gymtracker.ui.navigation.Screen
import com.jarvis.gymtracker.ui.screens.attendance.AttendanceScreen
import com.jarvis.gymtracker.ui.screens.history.HistoryScreen
import com.jarvis.gymtracker.ui.screens.home.HomeScreen
import com.jarvis.gymtracker.ui.screens.onboarding.OnboardingScreen
import com.jarvis.gymtracker.ui.screens.profile.EditProfileScreen
import com.jarvis.gymtracker.ui.screens.profile.ProfileScreen
import com.jarvis.gymtracker.ui.screens.profile_setup.ProfileSetupScreen
import com.jarvis.gymtracker.ui.screens.progress.ProgressScreen
import com.jarvis.gymtracker.ui.screens.settings.SettingsScreen
import com.jarvis.gymtracker.ui.screens.splash.SplashScreen
import android.net.Uri
import com.jarvis.gymtracker.ui.screens.workout_session.WorkoutSessionScreen
import com.jarvis.gymtracker.ui.screens.workout_session.MuscleExercisesScreen
import com.jarvis.gymtracker.ui.screens.workout_split.WorkoutSplitScreen
import com.jarvis.gymtracker.ui.screens.workout_summary.WorkoutSummaryScreen
import com.jarvis.gymtracker.ui.theme.GymTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel: MainViewModel = hiltViewModel()
            val darkMode by mainViewModel.darkMode.collectAsState()
            
            val isDarkTheme = when (darkMode) {
                true -> true
                false -> false
                null -> isSystemInDarkTheme()
            }

            GymTrackerTheme(darkTheme = isDarkTheme) {
                IronLogMainContent()
            }
        }
    }
}

@Composable
fun IronLogMainContent() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val navItems = listOf(
        IronLogNavItem.HOME,
        IronLogNavItem.WORKOUT,
        IronLogNavItem.HISTORY,
        IronLogNavItem.PROGRESS,
        IronLogNavItem.PROFILE
    )

    val currentRoute = currentDestination?.route
    val isBottomBarVisible = navItems.any { it.route == currentRoute }

    if (isBottomBarVisible) {
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                navItems.forEach { item ->
                    item(
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        ) {
            IronLogNavGraph(navController)
        }
    } else {
        IronLogNavGraph(navController)
    }
}

@Composable
fun IronLogNavGraph(navController: androidx.navigation.NavHostController) {
    // Reusable transition animations
    val forward: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = tween(300)
        ) + fadeIn(animationSpec = tween(300))
    }
    val backward: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(300)
        ) + fadeOut(animationSpec = tween(300))
    }
    val backEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = tween(300)
        ) + fadeIn(animationSpec = tween(300))
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(
            Screen.Splash.route,
            enterTransition = { fadeIn(animationSpec = tween(500)) },
            exitTransition = { fadeOut(animationSpec = tween(500)) }
        ) { SplashScreen(navController) }

        composable(
            Screen.Onboarding.route,
            enterTransition = forward,
            exitTransition = backward
        ) { OnboardingScreen(navController) }

        composable(
            Screen.ProfileSetup.route,
            enterTransition = forward,
            exitTransition = backward,
            popEnterTransition = backEnter
        ) { ProfileSetupScreen(navController) }

        composable(
            Screen.Home.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) { HomeScreen(navController) }

        composable(
            Screen.WorkoutSplit.route,
            enterTransition = forward,
            exitTransition = backward
        ) { WorkoutSplitScreen(navController) }

        composable(
            Screen.Attendance.route,
            enterTransition = forward,
            exitTransition = backward,
            popEnterTransition = backEnter
        ) { AttendanceScreen(navController) }

        composable(
            Screen.WorkoutSession.route,
            enterTransition = forward,
            exitTransition = backward,
            popEnterTransition = backEnter
        ) { WorkoutSessionScreen(navController) }

        composable(
            route = Screen.MuscleExercises.route,
            enterTransition = forward,
            exitTransition = backward,
            popEnterTransition = backEnter,
            arguments = listOf(navArgument("muscleGroup") { type = NavType.StringType })
        ) { backStackEntry ->
            val muscleGroup = backStackEntry.arguments?.getString("muscleGroup")?.let { Uri.decode(it) } ?: ""
            MuscleExercisesScreen(navController, muscleGroup)
        }

        composable(
            route = Screen.WorkoutSummary.route,
            enterTransition = forward,
            exitTransition = backward,
            popEnterTransition = backEnter,
            arguments = listOf(navArgument("sessionId") { type = NavType.LongType })
        ) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getLong("sessionId") ?: -1L
            WorkoutSummaryScreen(navController, sessionId)
        }

        composable(
            Screen.History.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) { HistoryScreen(navController) }

        composable(
            Screen.Progress.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) { ProgressScreen(navController) }

        composable(
            Screen.Profile.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) { ProfileScreen(navController) }

        composable(
            Screen.Settings.route,
            enterTransition = forward,
            exitTransition = backward,
            popEnterTransition = backEnter
        ) { SettingsScreen(navController) }

        composable(
            Screen.EditProfile.route,
            enterTransition = forward,
            exitTransition = backward,
            popEnterTransition = backEnter
        ) { EditProfileScreen(navController) }
    }
}

enum class IronLogNavItem(
    val label: String,
    val icon: Int,
    val route: String
) {
    HOME("Home", R.drawable.ic_home, Screen.Home.route),
    WORKOUT("Split", R.drawable.ic_calendar, Screen.WorkoutSplit.route),
    HISTORY("History", R.drawable.ic_history, Screen.History.route),
    PROGRESS("Stats", R.drawable.ic_analytics, Screen.Progress.route),
    PROFILE("Profile", R.drawable.ic_person, Screen.Profile.route),
}
