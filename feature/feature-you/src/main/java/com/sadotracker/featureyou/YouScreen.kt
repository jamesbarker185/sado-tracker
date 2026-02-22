package com.sadotracker.featureyou

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sadotracker.coreui.components.SadoCard

@Composable
fun YouScreen(
    viewModel: YouViewModel = hiltViewModel()
) {
    val preferredUnit by viewModel.preferredUnit.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("You", style = MaterialTheme.typography.displayLarge)
        Spacer(modifier = Modifier.height(24.dp))
        
        SadoCard {
            Text("Preferences", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Preferred Unit", style = MaterialTheme.typography.bodyMedium)
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = preferredUnit == "kg",
                    onClick = { viewModel.setPreferredUnit("kg") }
                )
                Text("Kilograms (kg)", style = MaterialTheme.typography.bodyMedium)
                
                Spacer(modifier = Modifier.weight(1f))
                
                RadioButton(
                    selected = preferredUnit == "lbs",
                    onClick = { viewModel.setPreferredUnit("lbs") }
                )
                Text("Pounds (lbs)", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
