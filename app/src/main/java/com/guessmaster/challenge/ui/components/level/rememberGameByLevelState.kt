package com.guessmaster.challenge.ui.components.level

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.guessmaster.challenge.data.models.MutableGameByLevelState
import com.guessmaster.challenge.ui.screen.levelscreen.LevelsViewModel
import com.guessmaster.challenge.ui.screen.settingscreen.SettingsViewModel

@Composable
fun rememberGameByLevelState(
    viewModel: LevelsViewModel,
    settingsViewModel: SettingsViewModel
): MutableGameByLevelState {
    val state = remember { MutableGameByLevelState() }

    state.gameState = viewModel.gameState.collectAsState().value
    state.currentLevel = viewModel.currentLevel.collectAsState().value
    state.maxLevel = viewModel.maxLevel.collectAsState().value
    state.isHapticEnabled = settingsViewModel.isHapticEnabled.collectAsState(initial = true).value
    state.guessHistory = viewModel.guessHistory.collectAsState().value

    state.attemptsLeft = viewModel.getMaxAttemptsForCurrentLevel() - state.guessHistory.size

    return state
}