package com.guessmaster.challenge.data.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guessmaster.challenge.data.repository.GameRepository
import com.guessmaster.challenge.data.repository.SettingsRepository
import com.guessmaster.challenge.domain.GameState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private var selectedDifficulty: String = "Medium"
    val guessHistory = mutableStateListOf<Int>()
    private var hintCount = 2

    private val _gameState = MutableStateFlow<GameState>(GameState.NotStarted)
    val gameState = _gameState.asStateFlow()

    // Expose settings as StateFlows
    private val isSoundEnabled: StateFlow<Boolean> = settingsRepository.isSoundEnabled
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val isHapticEnabled: StateFlow<Boolean> = settingsRepository.isHapticEnabled
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    fun getMaxAttemptsForDifficulty(difficulty: String): Int = when (difficulty) {
        "Easy" -> 15
        "Hard" -> 5
        else -> 10
    }

    private fun getNumberRangeForDifficulty(difficulty: String): IntRange = when (difficulty) {
        "Easy" -> 1..50
        "Hard" -> 1..200
        else -> 1..100
    }

    /**
     * Update the selected difficulty.
     * This reinitializes the game logic with new parameters,
     * clears the guess history, and resets the game state.
     */
    fun updateDifficulty(newDifficulty: String) {
        selectedDifficulty = newDifficulty
        gameRepository.updateGameSettings(
            maxAttempts = getMaxAttemptsForDifficulty(selectedDifficulty),
            numberRange = getNumberRangeForDifficulty(selectedDifficulty)
        )
        resetGame()
    }

    /**
     * Submit a guess.
     * The guess is added to the history, and the game state is updated based on the evaluation.
     */
    fun submitGuess(guess: Int) {
        guessHistory.add(guess)
        _gameState.value = gameRepository.evaluateGuess(guess)
    }

    /**
     * Enhanced hint system that provides more detailed hints based on game progress
     */
    fun requestHint(): String {
        if (hintCount <= 0) return "No more hints available!"
        
        val currentState = gameState.value
        if (currentState !is GameState.InProgress) {
            return "Start the game first!"
        }

        hintCount--
        val hintLevel = 2 - hintCount // 0 for first hint, 1 for second
        val hint = gameRepository.provideHint(hintLevel)
        
        // Play hint sound if enabled
        if (isSoundEnabled.value) {
            playHintSound()
        }
        
        return hint
    }

    private fun playHintSound() {
        // TODO: Implement sound playing logic
    }

    /**
     * Restart the game.
     * This resets the game logic and clears the guess history.
     */
    fun restartGame() {
        resetGame()
    }

    fun resetGame() {
        guessHistory.clear()
        hintCount = 2
        _gameState.value = GameState.NotStarted
        gameRepository.resetGame()
    }

    fun getCurrentDifficulty() = selectedDifficulty
    fun getRemainingHints() = hintCount
}