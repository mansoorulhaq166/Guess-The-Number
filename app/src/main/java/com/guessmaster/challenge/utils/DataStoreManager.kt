package com.guessmaster.challenge.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings_prefs")

class DataStoreManager(private val context: Context) {
    companion object {
        private val SOUND_ENABLED_KEY = booleanPreferencesKey("sound_enabled")
        private val HAPTIC_ENABLED_KEY = booleanPreferencesKey("haptic_enabled")
    }

    val isSoundEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[SOUND_ENABLED_KEY] ?: true }

    val isHapticEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[HAPTIC_ENABLED_KEY] ?: true }

    suspend fun saveSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SOUND_ENABLED_KEY] = enabled
        }
    }

    suspend fun saveHapticEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[HAPTIC_ENABLED_KEY] = enabled
        }
    }
}