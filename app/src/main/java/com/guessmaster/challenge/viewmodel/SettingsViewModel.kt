package com.guessmaster.challenge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guessmaster.challenge.utils.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    val isSoundEnabled: Flow<Boolean> = dataStoreManager.isSoundEnabled
    val isHapticEnabled: Flow<Boolean> = dataStoreManager.isHapticEnabled

    fun saveSoundEnabled(enabled: Boolean) {
        viewModelScope.launch { dataStoreManager.saveSoundEnabled(enabled) }
    }

    fun saveHapticEnabled(enabled: Boolean) {
        viewModelScope.launch { dataStoreManager.saveHapticEnabled(enabled) }
    }
}