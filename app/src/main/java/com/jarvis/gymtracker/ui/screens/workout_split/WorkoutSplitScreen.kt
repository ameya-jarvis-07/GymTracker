package com.jarvis.gymtracker.ui.screens.workout_split

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutSplitScreen(
    navController: NavController,
    viewModel: WorkoutSplitViewModel = hiltViewModel()
) {
    val splits by viewModel.splits.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var selectedDay by remember { mutableStateOf(1) }
    var muscleGroupsText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Weekly Split") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items((1..7).toList()) { day ->
                val split = splits.find { it.dayOfWeek == day }
                SplitDayItem(
                    day = day,
                    muscleGroups = split?.muscleGroups ?: "Rest Day",
                    onEditClick = {
                        selectedDay = day
                        muscleGroupsText = split?.muscleGroups ?: ""
                        showDialog = true
                    }
                )
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Edit Split for ${DayOfWeek.of(selectedDay).getDisplayName(TextStyle.FULL, Locale.getDefault())}") },
                text = {
                    OutlinedTextField(
                        value = muscleGroupsText,
                        onValueChange = { muscleGroupsText = it },
                        label = { Text("Muscle Groups (e.g., Chest, Triceps)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        viewModel.updateSplit(selectedDay, muscleGroupsText)
                        showDialog = false
                    }) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun SplitDayItem(
    day: Int,
    muscleGroups: String,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onEditClick
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = DayOfWeek.of(day).getDisplayName(TextStyle.FULL, Locale.getDefault()),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = muscleGroups,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (muscleGroups == "Rest Day") MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary
                )
            }
            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
        }
    }
}
