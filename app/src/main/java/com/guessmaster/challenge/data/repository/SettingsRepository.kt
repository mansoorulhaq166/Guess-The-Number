package com.guessmaster.challenge.data.repository

import android.util.Log
import com.guessmaster.challenge.utils.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {
    val isSoundEnabled: Flow<Boolean> = dataStoreManager.isSoundEnabled
        .catch { emit(true) }
        .map { it }

    val isHapticEnabled: Flow<Boolean> = dataStoreManager.isHapticEnabled
        .catch { emit(true) }
        .map { it }

    suspend fun saveSoundEnabled(enabled: Boolean) {
        try {
            dataStoreManager.saveSoundEnabled(enabled)
        } catch (e: Exception) {
            Log.d("TAG", "saveSoundEnabled: " + e.message)
        }
    }

    suspend fun saveHapticEnabled(enabled: Boolean) {
        try {
            dataStoreManager.saveHapticEnabled(enabled)
        } catch (e: Exception) {
            Log.d("TAG", "saveHapticEnabled: " + e.message)
        }
    }
} 