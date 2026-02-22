package com.sadotracker.featureprograms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sadotracker.coreui.components.SadoButton
import com.sadotracker.coreui.components.SadoCard
import com.sadotracker.coreui.components.SadoTextField

import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.IconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseSearchScreen(
    onNavigateBack: () -> Unit,
    onNavigateToFilter: () -> Unit,
    viewModel: ProgramBuilderViewModel
) {
    val searchResults by viewModel.searchResults.collectAsState()
    val filterState by viewModel.filterState.collectAsState()
    val days by viewModel.days.collectAsState()
    
    val currentDayIndex = filterState.dayIndex
    val currentDayExercises = days.getOrNull(currentDayIndex)?.exercises ?: emptyList()
    val selectedIds = currentDayExercises.map { it.id }.toSet()
    
    // Count active filters (excluding query)
    val activeFiltersCount = listOf(
        filterState.mechanics,
        filterState.forceVectors,
        filterState.categories,
        filterState.equipment,
        filterState.modalities,
        filterState.muscleGroups,
        filterState.primaryMuscles
    ).count { it.isNotEmpty() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Column(modifier = Modifier.padding(16.dp)) {
                SadoButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val count = currentDayExercises.size
                    Text("Done Selecting ($count)", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header / Search Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Add Exercises",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        SadoTextField(
                            value = filterState.query,
                            onValueChange = { viewModel.updateSearchQuery(it) },
                            placeholder = "Search (e.g., Bench Press)"
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    BadgedBox(
                        badge = {
                            if (activeFiltersCount > 0) {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ) {
                                    Text(activeFiltersCount.toString())
                                }
                            }
                        }
                    ) {
                        IconButton(onClick = onNavigateToFilter) {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = "Open Filters",
                                tint = if (activeFiltersCount > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }

            // Results Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = searchResults,
                    key = { it.id },
                    contentType = { "ExerciseCard" }
                ) { exercise ->
                    val isSelected = selectedIds.contains(exercise.id)
                    SadoCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clickable { viewModel.toggleExerciseSelection(exercise) }
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = exercise.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 2
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${exercise.mechanics}\n${exercise.primaryMuscle}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary,
                                    maxLines = 2
                                )
                            }
                            if (isSelected) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
