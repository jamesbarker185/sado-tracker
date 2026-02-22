package com.sadotracker.featureprograms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sadotracker.coreui.components.SadoCard
import com.sadotracker.coreui.components.SadoButton

@Composable
fun ProgramListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCreateProgram: () -> Unit,
    onProgramSelected: (Long) -> Unit,
    viewModel: ProgramListViewModel = hiltViewModel()
) {
    val programs by viewModel.programs.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "My Programs",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))
                SadoButton(
                    onClick = onNavigateToCreateProgram,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Create New Program")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (programs.isEmpty()) {
                item {
                    Text(
                        text = "You haven't created any programs yet. Build one to get started!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            } else {
                items(programs) { program ->
                    SadoCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onProgramSelected(program.id) }
                    ) {
                        Text(
                            text = program.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (!program.description.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = program.description!!,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary,
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }
        
        // Bottom nav logic managed by Scaffold mostly, but we add a back button since this is a sub-screen
        SadoButton(
            onClick = onNavigateBack,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text("Back")
        }
    }
}
