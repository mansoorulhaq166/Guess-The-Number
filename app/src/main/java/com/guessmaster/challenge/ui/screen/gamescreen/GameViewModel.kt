package com.guessmaster.challenge.ui.screen.gamescreen

import android.content.res.Resources
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guessmaster.challenge.R
import com.guessmaster.challenge.domain.EvaluateGuessUseCase
import com.guessmaster.challenge.domain.ProvideHintUseCase
import com.guessmaster.challenge.data.models.GameState
import com.guessmaster.challenge.data.repository.SettingsRepository
import com.guessmaster.challenge.domain.GetGameSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val evaluateGuessUseCase: EvaluateGuessUseCase,
    private val provideHintUseCase: ProvideHintUseCase,
    private val getGameSettingsUseCase: GetGameSettingsUseCase,
    private val resources: Resources,
    settingsRepository: SettingsRepository
) : ViewModel() {

    private var selectedDifficulty: String = "Medium"
    val guessHistory = mutableStateListOf<Int>()
    private var hintCount = 2

    private val _gameState = MutableStateFlow<GameState>(GameState.NotStarted)
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val isSoundEnabled: StateFlow<Boolean> = settingsRepository.isSoundEnabled
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val isHapticEnabled: StateFlow<Boolean> = settingsRepository.isHapticEnabled
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    fun updateDifficulty(newDifficulty: String) {
        selectedDifficulty = newDifficulty
        resetGame()
    }

    fun getMaxAttemptsForDifficulty(difficulty: String): Int {
        return getGameSettingsUseCase.getMaxAttempts(difficulty)
    }

    fun submitGuess(guess: Int) {
        guessHistory.add(guess)
        _gameState.value = evaluateGuessUseCase.execute(guess)
    }

    fun requestHint(): String {
        if (hintCount <= 0) return resources.getString(R.string.no_more_hints)

        val currentState = gameState.value
        if (currentState !is GameState.InProgress) {
            return resources.getString(R.string.start_game_first)
        }

        hintCount--
        val hintLevel = 2 - hintCount
        val hint = provideHintUseCase.execute(hintLevel)

        if (isSoundEnabled.value) {
            playHintSound()
        }

        return hint
    }

    private fun playHintSound() {
        // TODO: Implement sound playing logic
    }

    fun restartGame() {
        resetGame()
    }

    private fun resetGame() {
        guessHistory.clear()
        hintCount = 2
        _gameState.value = GameState.NotStarted
    }
}