package com.sadotracker.featureprograms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadotracker.coredatabase.dao.ProgramDao
import com.sadotracker.coredatabase.entity.ProgramEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProgramListViewModel @Inject constructor(
    programDao: ProgramDao
) : ViewModel() {

    val programs: StateFlow<List<ProgramEntity>> = programDao.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
