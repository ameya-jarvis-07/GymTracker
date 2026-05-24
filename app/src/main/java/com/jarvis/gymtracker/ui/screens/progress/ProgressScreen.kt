package com.jarvis.gymtracker.ui.screens.progress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jarvis.gymtracker.ui.components.EmptyState
import java.time.Month

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    navController: NavController,
    viewModel: ProgressViewModel = hiltViewModel()
) {
    val consistency by viewModel.attendanceConsistency.collectAsState()
    val monthlyWorkouts by viewModel.monthlyWorkouts.collectAsState()
    val progressValue by animateFloatAsState(
        targetValue = consistency.toFloat().coerceIn(0f, 1f),
        label = "attendance_progress"
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Progress Analytics") })
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
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Attendance Consistency",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { progressValue },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .semantics {
                                    contentDescription = "Attendance consistency ${(progressValue * 100).toInt()} percent"
                                },
                            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${(consistency * 100).toInt()}% of days tracked",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Workouts per Month",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                ElevatedCard(modifier = Modifier.fillMaxWidth().animateContentSize()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Crossfade(targetState = monthlyWorkouts.isEmpty(), label = "progress_monthly_state") { isEmpty ->
                            if (isEmpty) {
                                EmptyState(message = "No data available yet.", modifier = Modifier.height(120.dp))
                            } else {
                                monthlyWorkouts
                                    .toList()
                                    .sortedBy { (month, _) ->
                                        runCatching { Month.valueOf(month).value }.getOrElse { Int.MAX_VALUE }
                                    }
                                    .forEach { (month, count) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .semantics { contentDescription = "$month: $count workouts" },
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(month)
                                        Text("$count workouts", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            item {
                // Placeholder for weight progress chart
                Text(
                    text = "Weight Progression",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Weight chart coming soon...", color = MaterialTheme.colorScheme.outline)
                }
            }
        }
    }
}
