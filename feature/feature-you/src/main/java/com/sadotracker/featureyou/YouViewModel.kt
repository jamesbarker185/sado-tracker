package com.sadotracker.featureyou

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadotracker.coredomain.datastore.UserSettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YouViewModel @Inject constructor(
    private val userSettingsManager: UserSettingsManager
) : ViewModel() {

    val preferredUnit: StateFlow<String> = userSettingsManager.preferredUnit
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "kg"
        )
        
    fun setPreferredUnit(unit: String) {
        viewModelScope.launch {
            userSettingsManager.setPreferredUnit(unit)
        }
    }
}
