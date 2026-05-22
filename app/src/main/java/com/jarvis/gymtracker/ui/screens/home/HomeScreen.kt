package com.jarvis.gymtracker.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jarvis.gymtracker.ui.navigation.Screen
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsState()
    val todaySplit by viewModel.todaySplit.collectAsState()
    val todayAttendance by viewModel.todayAttendance.collectAsState()
    val streak by viewModel.streak.collectAsState()

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Hello, ${user?.name ?: "Athlete"}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = LocalDate.now().toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                StreakCard(streak = streak)
            }

            item {
                TodayWorkoutCard(
                    split = todaySplit,
                    attendance = todayAttendance,
                    onStartWorkout = {
                        if (todayAttendance == null) {
                            navController.navigate(Screen.Attendance.route)
                        } else {
                            navController.navigate(Screen.WorkoutSession.route)
                        }
                    }
                )
            }

            item {
                QuickStatsSection()
            }
        }
    }
}

@Composable
fun StreakCard(streak: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Workout Streak",
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = "$streak Days",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold
                )
            }
            // Add a fire icon or similar here
        }
    }
}

@Composable
fun TodayWorkoutCard(
    split: com.jarvis.gymtracker.data.local.entity.WorkoutSplitEntity?,
    attendance: com.jarvis.gymtracker.data.local.entity.AttendanceEntity?,
    onStartWorkout: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Today's Plan",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            if (split != null) {
                Text(
                    text = split.muscleGroups,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Text(
                    text = "No split scheduled for today",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            val statusText = when (attendance?.status) {
                "Present" -> "Attendance Marked: Present"
                "Skipped" -> "Attendance Marked: Skipped"
                "Rest Day" -> "Today is a Rest Day"
                else -> "Attendance Not Marked"
            }
            
            Text(
                text = statusText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Button(
                onClick = onStartWorkout,
                modifier = Modifier.fillMaxWidth(),
                enabled = attendance?.status != "Rest Day"
            ) {
                Text(if (attendance == null) "Mark Attendance" else "Start Workout")
            }
        }
    }
}

@Composable
fun QuickStatsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Weekly Progress",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        // Placeholder for progress chart/summary
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceVariant,
                            MaterialTheme.colorScheme.surface
                        )
                    ),
                    shape = MaterialTheme.shapes.medium
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("Charts coming soon...", color = MaterialTheme.colorScheme.outline)
        }
    }
}