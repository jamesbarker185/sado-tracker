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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sadotracker.coreui.components.SadoButton
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.IntrinsicSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseFilterScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProgramBuilderViewModel
) {
    val filterState by viewModel.filterState.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Filters", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Clear, contentDescription = "Close")
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.clearFilters() }) {
                        Text("Clear All", color = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            Column(modifier = Modifier.padding(16.dp)) {
                SadoButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Apply Filters", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                FilterSection(
                    title = "Muscle Groups",
                    options = listOf("Chest", "Back", "Legs", "Shoulders", "Arms", "Core"),
                    selectedOptions = filterState.muscleGroups,
                    onOptionToggle = { viewModel.toggleMuscleGroupFilter(it) }
                )
            }

            item {
                FilterSection(
                    title = "Mechanics",
                    options = listOf("Compound", "Isolation"),
                    selectedOptions = filterState.mechanics,
                    onOptionToggle = { viewModel.toggleMechanicFilter(it) }
                )
            }

            item {
                FilterSection(
                    title = "Force Vector",
                    options = listOf("Horizontal Push", "Vertical Push", "Horizontal Pull", "Vertical Pull", "Knee Dominant", "Hip Dominant"),
                    selectedOptions = filterState.forceVectors,
                    onOptionToggle = { viewModel.toggleForceVectorFilter(it) }
                )
            }

            item {
                FilterSection(
                    title = "Equipment",
                    options = listOf("Barbell", "Dumbbell", "Cable", "Machine", "Kettlebell", "Bodyweight", "E-Z Bar"),
                    selectedOptions = filterState.equipment,
                    onOptionToggle = { viewModel.toggleEquipmentFilter(it) }
                )
            }

            item {
                FilterSection(
                    title = "Modality",
                    options = listOf("Unilateral", "Bilateral"),
                    selectedOptions = filterState.modalities,
                    onOptionToggle = { viewModel.toggleModalityFilter(it) }
                )
            }

            item {
                FilterSection(
                    title = "Category",
                    options = listOf("Barbell", "Dumbbell", "Machine", "Bodyweight", "Cable", "Kettlebell", "Plate", "Other"),
                    selectedOptions = filterState.categories,
                    onOptionToggle = { viewModel.toggleCategoryFilter(it) }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FilterSection(
    title: String,
    options: List<String>,
    selectedOptions: List<String>,
    onOptionToggle: (String) -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            options.forEach { option ->
                FilterChip(
                    selected = selectedOptions.contains(option),
                    onClick = { onOptionToggle(option) },
                    label = { Text(option, style = MaterialTheme.typography.labelSmall) },
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }
    }
}


