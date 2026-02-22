package com.sadotracker.featureprograms

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sadotracker.coreui.components.SadoButton
import com.sadotracker.coreui.components.SadoCard
import com.sadotracker.coreui.components.SadoTextField

@Composable
fun ProgramCreationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToExerciseSearch: () -> Unit,
    viewModel: ProgramBuilderViewModel
) {
    val programName by viewModel.programName.collectAsState()
    val programDescription by viewModel.programDescription.collectAsState()
    val days by viewModel.days.collectAsState()
    val hasExercises by viewModel.hasExercises.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Column(modifier = Modifier.padding(16.dp)) {
                SadoButton(
                    onClick = {
                        viewModel.saveProgram {
                            onNavigateBack() // Save successful, go back to Hub
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = programName.isNotBlank() && hasExercises
                ) {
                    val text = if (viewModel.isEditMode) "Save Changes" else "Save Program"
                    Text(text, style = MaterialTheme.typography.labelLarge)
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
                Text(
                    text = if (viewModel.isEditMode) "Edit Program" else "New Program",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                SadoTextField(
                    value = programName,
                    onValueChange = { viewModel.updateProgramName(it) },
                    label = "Program Name",
                    placeholder = "e.g., Push Day"
                )
                Spacer(modifier = Modifier.height(8.dp))
                SadoTextField(
                    value = programDescription,
                    onValueChange = { viewModel.updateProgramDescription(it) },
                    label = "Description (Optional)",
                    placeholder = "e.g., Heavy focus on chest over shoulders"
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Split Schedule",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    IconButton(onClick = { viewModel.addDay() }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Day",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            items(days.size) { dayIndex ->
                val day = days[dayIndex]
                SadoCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.toggleDayExpansion(dayIndex) }
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Day ${dayIndex + 1}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (day.isRestDay) "Rest Day" else "${day.exercises.size} exercises",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                            
                            Row {
                                IconButton(onClick = { viewModel.toggleRestDay(dayIndex) }) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Toggle Rest Day",
                                        tint = if (day.isRestDay) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                                    )
                                }
                                if (days.size > 1) {
                                    IconButton(onClick = { viewModel.removeDay(dayIndex) }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Remove Day",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }

                        if (day.isExpanded && !day.isRestDay) {
                            Spacer(modifier = Modifier.height(12.dp))
                            day.exercises.forEach { exercise ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = exercise.name,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    IconButton(onClick = { viewModel.removeExercise(dayIndex, exercise.id) }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Remove",
                                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                            }
                            
                            SadoButton(
                                onClick = { 
                                    viewModel.setEditingDay(dayIndex)
                                    onNavigateToExerciseSearch()
                                },
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Add Exercise")
                            }
                        }
                    }
                }
            }
        }
    }
}
