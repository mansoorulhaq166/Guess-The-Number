package com.guessmaster.challenge.ui.screen.levelscreen

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guessmaster.challenge.R
import com.guessmaster.challenge.data.models.GameState
import com.guessmaster.challenge.data.repository.SettingsRepository
import com.guessmaster.challenge.domain.EvaluateGuessUseCase
import com.guessmaster.challenge.domain.ProvideHintUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LevelsViewModel @Inject constructor(
    private val evaluateGuessUseCase: EvaluateGuessUseCase,
    private val provideHintUseCase: ProvideHintUseCase,
    private val resources: Resources,
    settingsRepository: SettingsRepository
) : ViewModel() {

    private val _currentLevel = MutableStateFlow(1)
    val currentLevel: StateFlow<Int> = _currentLevel.asStateFlow()

    private val _maxLevel = MutableStateFlow(10)
    val maxLevel: StateFlow<Int> = _maxLevel.asStateFlow()

    private val _guessHistory = MutableStateFlow<List<Int>>(emptyList())
    val guessHistory: StateFlow<List<Int>> = _guessHistory.asStateFlow()

    private var hintCount = 2

    private val _gameState = MutableStateFlow<GameState>(GameState.NotStarted)
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val isSoundEnabled: StateFlow<Boolean> = settingsRepository.isSoundEnabled
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val isHapticEnabled: StateFlow<Boolean> = settingsRepository.isHapticEnabled
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    fun getMaxAttemptsForCurrentLevel(): Int {
        // Decrease max attempts as level increases
        return when {
            _currentLevel.value <= 3 -> 7
            _currentLevel.value <= 6 -> 5
            else -> 3
        }
    }

    fun getMaxNumberForCurrentLevel(): Int {
        // Increase range as level increases
        return when {
            _currentLevel.value <= 3 -> 50
            _currentLevel.value <= 6 -> 100
            _currentLevel.value <= 9 -> 200
            else -> 500
        }
    }

    fun submitGuess(guess: Int) {
        _guessHistory.value = _guessHistory.value + guess
        _gameState.value = evaluateGuessUseCase.execute(guess)

        // If game is won, check if we should advance to next level
        if (_gameState.value is GameState.Won) {
            if (_currentLevel.value < _maxLevel.value) {
                _currentLevel.value++
            }
        }
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

    fun restartLevel() {
        resetGame()
    }

    fun advanceToNextLevel() {
        if (_currentLevel.value < _maxLevel.value) {
            _currentLevel.value++
            resetGame()
        }
    }

    private fun resetGame() {
        _guessHistory.value = emptyList()
        hintCount = 2
        _gameState.value = GameState.NotStarted
    }
}