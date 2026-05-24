package com.jarvis.gymtracker.ui.screens.workout_session

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuscleExercisesScreen(
    navController: NavController,
    muscleGroup: String,
    viewModel: WorkoutSessionViewModel = hiltViewModel()
) {
    val activeSession by viewModel.activeSession.collectAsState()
    val exercisesForGroup by viewModel.exercisesForMuscle(muscleGroup).collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(muscleGroup) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (exercisesForGroup.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No exercises available for $muscleGroup")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(exercisesForGroup) { exercise ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = exercise.exerciseName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(text = exercise.muscleGroup, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                            }
                            // Add & return
                            IconButton(onClick = {
                                if (activeSession != null) {
                                    viewModel.addExerciseToSession(exercise)
                                    navController.popBackStack()
                                } else {
                                    // No active session — navigate back and show a hint (could be a Snackbar)
                                    navController.popBackStack()
                                }
                            }) {
                                Icon(Icons.Default.Add, contentDescription = "Add and back")
                            }
                        }
                    }
                }
            }
        }
    }
}

