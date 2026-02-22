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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
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
    val selectedExercises by viewModel.selectedExercises.collectAsState()

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
                    enabled = programName.isNotBlank() && selectedExercises.isNotEmpty()
                ) {
                    Text("Save Program", style = MaterialTheme.typography.labelLarge)
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
                    text = "New Program",
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
                        text = "Exercises",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    IconButton(onClick = onNavigateToExerciseSearch) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Exercise",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            if (selectedExercises.isEmpty()) {
                item {
                    SadoCard(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "No exercises added yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        SadoButton(
                            onClick = onNavigateToExerciseSearch,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Add an Exercise")
                        }
                    }
                }
            } else {
                items(selectedExercises) { exercise ->
                    SadoCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = exercise.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${exercise.mechanics} • ${exercise.primaryMuscle}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                            IconButton(onClick = { viewModel.removeExercise(exercise.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Remove Exercise",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
