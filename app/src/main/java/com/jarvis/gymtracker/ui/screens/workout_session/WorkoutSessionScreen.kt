package com.jarvis.gymtracker.ui.screens.workout_session

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jarvis.gymtracker.data.local.entity.ExerciseEntity
import com.jarvis.gymtracker.data.local.entity.ExerciseLogEntity
import com.jarvis.gymtracker.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutSessionScreen(
    navController: NavController,
    viewModel: WorkoutSessionViewModel = hiltViewModel()
) {
    val activeSession by viewModel.activeSession.collectAsState()
    val selectedExercises by viewModel.selectedExercises.collectAsState()
    val availableExercises by viewModel.availableExercises.collectAsState()
    val todayMuscleGroups by viewModel.todayMuscleGroups.collectAsState()
    
    var showExercisePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workout Session") },
                actions = {
                    if (activeSession != null) {
                        TextButton(onClick = {
                            viewModel.endSession()
                            activeSession?.id?.let { id ->
                                navController.navigate(Screen.WorkoutSummary.createRoute(id)) {
                                    popUpTo(Screen.WorkoutSession.route) { inclusive = true }
                                }
                            }
                        }) {
                            Text("Finish")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (activeSession != null) {
                ExtendedFloatingActionButton(
                    onClick = { showExercisePicker = true },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Add Exercise") }
                )
            }
        }
    ) { padding ->
        if (activeSession == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Today's Focus: ${todayMuscleGroups.joinToString(", ").ifEmpty { "Full Body" }}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(onClick = { viewModel.startNewSession() }) {
                        Text("Start New Session")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(selectedExercises) { exerciseWithLogs ->
                    ExerciseCard(
                        exerciseWithLogs = exerciseWithLogs,
                        onAddSet = { viewModel.addSet(exerciseWithLogs.exercise.id) },
                        onUpdateLog = { log, reps, weight -> viewModel.updateLog(log, reps, weight) }
                    )
                }
            }
        }

        if (showExercisePicker) {
            ExercisePickerDialog(
                exercises = availableExercises,
                todayMuscleGroups = todayMuscleGroups,
                onDismiss = { showExercisePicker = false },
                onExerciseSelected = { exercise ->
                    viewModel.addExerciseToSession(exercise)
                    showExercisePicker = false
                }
            )
        }
    }
}

@Composable
fun ExerciseCard(
    exerciseWithLogs: ExerciseWithLogs,
    onAddSet: () -> Unit,
    onUpdateLog: (ExerciseLogEntity, Int, Double) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = exerciseWithLogs.exercise.exerciseName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            exerciseWithLogs.logs.forEachIndexed { index, log ->
                SetRow(
                    setNumber = index + 1,
                    log = log,
                    onUpdate = { reps, weight -> onUpdateLog(log, reps, weight) }
                )
            }
            
            TextButton(
                onClick = onAddSet,
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text("Add Set")
            }
        }
    }
}

@Composable
fun SetRow(
    setNumber: Int,
    log: ExerciseLogEntity,
    onUpdate: (Int, Double) -> Unit
) {
    var reps by remember(log.id) { mutableStateOf(if (log.reps == 0) "" else log.reps.toString()) }
    var weight by remember(log.id) { mutableStateOf(if (log.weight == 0.0) "" else log.weight.toString()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Set $setNumber", modifier = Modifier.width(50.dp))
        
        OutlinedTextField(
            value = reps,
            onValueChange = { 
                reps = it
                it.toIntOrNull()?.let { r -> onUpdate(r, weight.toDoubleOrNull() ?: 0.0) }
            },
            label = { Text("Reps") },
            modifier = Modifier.weight(1f)
        )
        
        OutlinedTextField(
            value = weight,
            onValueChange = { 
                weight = it
                it.toDoubleOrNull()?.let { w -> onUpdate(reps.toIntOrNull() ?: 0, w) }
            },
            label = { Text("Kg") },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ExercisePickerDialog(
    exercises: List<ExerciseEntity>,
    todayMuscleGroups: List<String>,
    onDismiss: () -> Unit,
    onExerciseSelected: (ExerciseEntity) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Exercise") },
        text = {
            LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                if (todayMuscleGroups.isNotEmpty()) {
                    item {
                        Text(
                            text = "Recommended for Today",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(exercises.filter { it.muscleGroup in todayMuscleGroups }) { exercise ->
                        ExerciseListItem(exercise, onExerciseSelected)
                    }
                    item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) }
                }
                
                item {
                    Text(
                        text = "All Exercises",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(exercises) { exercise ->
                    ExerciseListItem(exercise, onExerciseSelected)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}

@Composable
fun ExerciseListItem(exercise: ExerciseEntity, onExerciseSelected: (ExerciseEntity) -> Unit) {
    ListItem(
        headlineContent = { Text(exercise.exerciseName) },
        supportingContent = { Text(exercise.muscleGroup) },
        modifier = Modifier.fillMaxWidth(),
        trailingContent = {
            IconButton(onClick = { onExerciseSelected(exercise) }) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    )
}
