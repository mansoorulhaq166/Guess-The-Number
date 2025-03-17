package com.guessmaster.challenge.ui.screen.settingscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guessmaster.challenge.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val isSoundEnabled: StateFlow<Boolean> = settingsRepository.isSoundEnabled
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val isHapticEnabled: StateFlow<Boolean> = settingsRepository.isHapticEnabled
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    fun toggleSound(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.saveSoundEnabled(enabled)
        }
    }

    fun toggleHaptic(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.saveHapticEnabled(enabled)
        }
    }
}