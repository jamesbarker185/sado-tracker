package com.sadotracker.featureworkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sadotracker.coreui.components.SadoButton
import com.sadotracker.coreui.components.SadoCard
import com.sadotracker.coreui.components.SadoTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveWorkoutScreen(
    onNavigateBack: () -> Unit,
    onAddExercise: () -> Unit,
    viewModel: LiveWorkoutViewModel = hiltViewModel()
) {
    val exercises by viewModel.exercises.collectAsState()
    val restTimerState by viewModel.restTimerState.collectAsState()
    val elapsedTimeSeconds by viewModel.elapsedTimeSeconds.collectAsState()
    val splitDays by viewModel.splitDays.collectAsState()
    val currentDayIndex by viewModel.currentDayIndex.collectAsState()

    val minutes = elapsedTimeSeconds / 60
    val seconds = elapsedTimeSeconds % 60
    val timeStr = String.format("%02d:%02d", minutes, seconds)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Column(modifier = Modifier.padding(16.dp)) {
                SadoButton(
                    onClick = { viewModel.finishWorkout(onNavigateBack) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Finish Workout", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Active Workout",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = timeStr,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            if (splitDays.isNotEmpty()) {
                item {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(
                            text = "Split Progress",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            splitDays.forEachIndexed { index, day ->
                                val isPastOrCurrent = currentDayIndex?.let { index <= it } ?: false
                                
                                val color = when {
                                    day.isRestDay -> Color(0xFF4CAF50) // Green
                                    isPastOrCurrent -> Color(0xFFFF9800) // Orange
                                    else -> MaterialTheme.colorScheme.surfaceVariant // Grey
                                }
                                
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(32.dp)
                                        .background(
                                            color = color,
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .then(
                                            if (index == currentDayIndex) {
                                                Modifier.border(
                                                    2.dp,
                                                    MaterialTheme.colorScheme.primary,
                                                    RoundedCornerShape(4.dp)
                                                )
                                            } else Modifier
                                        )
                                )
                            }
                        }
                    }
                }
            }

            if (exercises.isEmpty()) {
                item {
                    Text(
                        text = "No exercises yet. Normally this loads from Program or you add Ad-hoc.",
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            } else {
                items(exercises.size) { exIndex ->
                    val exState = exercises[exIndex]
                    
                    SadoCard(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.toggleExerciseExpansion(exIndex) },
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = exState.exercise.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = if (exState.isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = if (exState.isExpanded) "Collapse" else "Expand",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            
                            AnimatedVisibility(
                                visible = exState.isExpanded,
                                enter = expandVertically(),
                                exit = shrinkVertically()
                            ) {
                                Column {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    
                                    // Header Row
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "SET",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.secondary,
                                            modifier = Modifier.weight(0.15f),
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = "PREVIOUS",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.secondary,
                                            modifier = Modifier.weight(0.35f),
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = "KG",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.secondary,
                                            modifier = Modifier.weight(0.25f),
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = "REPS",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.secondary,
                                            modifier = Modifier.weight(0.25f),
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.width(48.dp)) // space for action icon
                                    }

                                    exState.sets.forEachIndexed { setIndex, set ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "${set.setNumber}",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.secondary,
                                                modifier = Modifier.weight(0.15f),
                                                textAlign = TextAlign.Center
                                            )
                                            
                                            Text(
                                                text = set.previousTarget ?: "-",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.secondary,
                                                modifier = Modifier.weight(0.35f),
                                                textAlign = TextAlign.Center
                                            )

                                            if (set.isCompleted) {
                                                Text(
                                                    text = set.weightKg,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier.weight(0.25f)
                                                )
                                                Text(
                                                    text = set.reps,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier.weight(0.25f)
                                                )
                                                IconButton(onClick = { /* Could allow edit */ }) {
                                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Completed", tint = MaterialTheme.colorScheme.primary)
                                                }
                                            } else {
                                                SadoTextField(
                                                    value = set.weightKg,
                                                    onValueChange = { viewModel.updateSet(exIndex, setIndex, it, set.reps) },
                                                    placeholder = "0",
                                                    modifier = Modifier.weight(0.25f)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                SadoTextField(
                                                    value = set.reps,
                                                    onValueChange = { viewModel.updateSet(exIndex, setIndex, set.weightKg, it) },
                                                    placeholder = "0",
                                                    modifier = Modifier.weight(0.25f)
                                                )
                                                androidx.compose.material3.SuggestionChip(
                                                    onClick = { viewModel.completeSet(exIndex, setIndex) },
                                                    label = {
                                                        val mins = exState.restTimeSecs / 60
                                                        val secs = exState.restTimeSecs % 60
                                                        Text("${mins}:${secs.toString().padStart(2, '0')}")
                                                    }
                                                )
                                            }
                                        }
                                    }
                                    
                                    AnimatedVisibility(
                                        visible = restTimerState.isActive && restTimerState.exerciseIndex == exIndex,
                                        enter = expandVertically(),
                                        exit = shrinkVertically()
                                    ) {
                                        RestTimerStrip(
                                            state = restTimerState,
                                            onPause = { viewModel.pauseRestTimer() },
                                            onResume = { viewModel.resumeRestTimer() },
                                            onStop = { viewModel.stopRestTimer() }
                                        )
                                    }

                                    SadoButton(
                                        onClick = { viewModel.addSet(exIndex) },
                                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                                    ) {
                                        Text("+ Add Set")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                OutlinedButton(
                    onClick = onAddExercise,
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 32.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Exercise")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Exercise")
                }
            }
        }
    }
}
@Composable
fun RestTimerStrip(
    state: RestTimerState,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit
) {
    val isOvertime = state.isOvertime
    val isPaused = state.isPaused
    val backgroundColor = when {
        isOvertime -> Color(0xFFFFE082)
        isPaused -> MaterialTheme.colorScheme.surfaceVariant
        else -> MaterialTheme.colorScheme.primaryContainer
    }
    val contentColor = when {
        isOvertime -> Color(0xFF5D4037)
        isPaused -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> MaterialTheme.colorScheme.onPrimaryContainer
    }

    val displaySecs = if (isOvertime) state.elapsedSecs - state.targetSecs else state.targetSecs - state.elapsedSecs
    val mins = displaySecs / 60
    val secs = displaySecs % 60
    val timeText = if (isOvertime) {
        "+${mins}:${secs.toString().padStart(2, '0')}"
    } else {
        "${mins}:${secs.toString().padStart(2, '0')}"
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { if (isPaused) onResume() else onPause() },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                    contentDescription = if (isPaused) "Resume" else "Pause",
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = timeText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = onStop,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = "Stop",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
