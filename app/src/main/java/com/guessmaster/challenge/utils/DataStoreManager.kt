package com.guessmaster.challenge.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager private constructor(private val context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: DataStoreManager? = null

        fun getInstance(context: Context): DataStoreManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DataStoreManager(context.applicationContext).also { INSTANCE = it }
            }
        }

        private val SOUND_ENABLED_KEY = booleanPreferencesKey("sound_enabled")
        private val HAPTIC_ENABLED_KEY = booleanPreferencesKey("haptic_enabled")
        val SELECTED_LANGUAGE_CODE = stringPreferencesKey("selected_language_code")
    }

    private val Context.dataStore by preferencesDataStore(name = "settings_prefs")

    // Save language code
    suspend fun saveSelectedLanguageCode(code: String) {
        context.dataStore.edit { prefs ->
            prefs[SELECTED_LANGUAGE_CODE] = code
        }
    }

    // Observe selected language code
    val selectedLanguageCode: Flow<String?> = context.dataStore.data.map {
        it[SELECTED_LANGUAGE_CODE]
    }

    // Sound preference (default = true)
    val isSoundEnabled: Flow<Boolean> = context.dataStore.data.map {
        it[SOUND_ENABLED_KEY] != false
    }

    // Haptic preference (default = true)
    val isHapticEnabled: Flow<Boolean> = context.dataStore.data.map {
        it[HAPTIC_ENABLED_KEY] != false
    }

    suspend fun saveSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[SOUND_ENABLED_KEY] = enabled
        }
    }

    suspend fun saveHapticEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[HAPTIC_ENABLED_KEY] = enabled
        }
    }
}