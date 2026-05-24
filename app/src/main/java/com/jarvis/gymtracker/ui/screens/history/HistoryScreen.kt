package com.jarvis.gymtracker.ui.screens.history

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jarvis.gymtracker.data.local.entity.WorkoutSessionEntity
import com.jarvis.gymtracker.ui.components.EmptyState
import com.jarvis.gymtracker.ui.navigation.Screen
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val haptics = LocalHapticFeedback.current
    val sessions by viewModel.sessions.collectAsState()
    val formatter = DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy")
    var sessionToDelete by remember { mutableStateOf<WorkoutSessionEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Workout History") })
        }
    ) { padding ->
        Crossfade(targetState = sessions.isEmpty(), label = "history_state") { isEmpty ->
            if (isEmpty) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyState(message = "No workouts logged yet.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = sessions, key = { session -> session.id }) { session ->
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize()
                                .clickable {
                                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                    navController.navigate(Screen.WorkoutSummary.createRoute(session.id))
                                }
                                .semantics { contentDescription = "Workout on ${session.date.format(formatter)}" }
                        ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = session.date.format(formatter),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${session.duration / 60} min",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "View Summary",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                            IconButton(
                                modifier = Modifier.minimumInteractiveComponentSize(),
                                onClick = {
                                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                    sessionToDelete = session
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Workout",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                    }
                }
            }
        }

        if (sessionToDelete != null) {
            AlertDialog(
                onDismissRequest = { sessionToDelete = null },
                title = { Text("Delete Workout") },
                text = { Text("Are you sure you want to delete this workout session? This action cannot be undone.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            sessionToDelete?.let { viewModel.deleteSession(it) }
                            sessionToDelete = null
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { sessionToDelete = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}