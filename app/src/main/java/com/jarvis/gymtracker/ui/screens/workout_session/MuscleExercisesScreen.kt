package com.jarvis.gymtracker.ui.screens.workout_session

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuscleExercisesScreen(
    navController: NavController,
    muscleGroup: String,
    viewModel: WorkoutSessionViewModel = hiltViewModel()
) {
    val activeSession by viewModel.activeSession.collectAsState()
    val exercisesForGroup by viewModel.exercisesForMuscle(muscleGroup).collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.exerciseAddedEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(muscleGroup) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
                    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                                Text(text = exercise.exerciseName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(text = exercise.muscleGroup, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                            }
                            Row {
                                // Add & back
                                IconButton(onClick = {
                                    if (activeSession != null) {
                                        viewModel.addExerciseToSession(exercise)
                                        navController.popBackStack()
                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Start a session first")
                                        }
                                    }
                                }) {
                                    Icon(Icons.Default.Add, contentDescription = "Add and back")
                                }

                                // Add & stay
                                IconButton(onClick = {
                                    if (activeSession != null) {
                                        viewModel.addExerciseToSession(exercise)
                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Start a session first")
                                        }
                                    }
                                }) {
                                    Icon(Icons.Default.Check, contentDescription = "Add and keep")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
