package com.sadotracker.coredomain.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Create DataStore instance using delegation
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class UserSettingsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // Keys
    private val PREF_UNIT = stringPreferencesKey("preferred_unit")
    private val PREF_ONBOARDING = booleanPreferencesKey("onboarding_complete")
    private val REP_RANGE_MIN = intPreferencesKey("rep_range_min")
    private val REP_RANGE_MAX = intPreferencesKey("rep_range_max")

    // Flows
    val preferredUnit: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PREF_UNIT] ?: "kg"
    }
    
    val onboardingComplete: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PREF_ONBOARDING] ?: false
    }

    val repRangeMin: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[REP_RANGE_MIN] ?: 10
    }

    val repRangeMax: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[REP_RANGE_MAX] ?: 14
    }

    // Setters
    suspend fun setPreferredUnit(unit: String) {
        context.dataStore.edit { preferences ->
            preferences[PREF_UNIT] = unit
        }
    }
    
    suspend fun setOnboardingComplete(complete: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PREF_ONBOARDING] = complete
        }
    }

    suspend fun setRepRangeOptions(min: Int, max: Int) {
        context.dataStore.edit { preferences ->
            preferences[REP_RANGE_MIN] = min
            preferences[REP_RANGE_MAX] = max
        }
    }
}
