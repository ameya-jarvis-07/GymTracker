package com.jarvis.gymtracker.ui.screens.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    navController: NavController,
    viewModel: ProgressViewModel = hiltViewModel()
) {
    val consistency by viewModel.attendanceConsistency.collectAsState()
    val monthlyWorkouts by viewModel.monthlyWorkouts.collectAsState()

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
                            progress = { consistency.toFloat() },
                            modifier = Modifier.fillMaxWidth().height(8.dp),
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
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (monthlyWorkouts.isEmpty()) {
                            Text("No data available yet.", color = MaterialTheme.colorScheme.outline)
                        } else {
                            monthlyWorkouts.forEach { (month, count) ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
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
