package com.guessmaster.challenge.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.guessmaster.challenge.domain.GameLogic
import com.guessmaster.challenge.domain.GameState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameLogic: GameLogic
) : ViewModel() {

    private var selectedDifficulty: String = "Medium"

    val guessHistory = mutableStateListOf<Int>()
    private var hintCount = 2

    private val _gameState = MutableStateFlow<GameState>(GameState.NotStarted)
    val gameState = _gameState.asStateFlow()

    // Map difficulty to maximum attempts and number range
    fun getMaxAttemptsForDifficulty(difficulty: String): Int {
        return when (difficulty) {
            "Easy" -> 15
            "Hard" -> 5
            else -> 10
        }
    }

    private fun getNumberRangeForDifficulty(difficulty: String): IntRange {
        return when (difficulty) {
            "Easy" -> 1..50
            "Hard" -> 1..200
            else -> 1..100
        }
    }

    /**
     * Update the selected difficulty.
     * This reinitializes the game logic with new parameters,
     * clears the guess history, and resets the game state.
     */
    fun updateDifficulty(newDifficulty: String) {
        selectedDifficulty = newDifficulty
        gameLogic.updateGameSettings(
            maxAttempts = getMaxAttemptsForDifficulty(selectedDifficulty),
            numberRange = getNumberRangeForDifficulty(selectedDifficulty)
        )
//        gameLogic = GameLogic(
//            maxAttempts = getMaxAttemptsForDifficulty(selectedDifficulty),
//            numberRange = getNumberRangeForDifficulty(selectedDifficulty)
//        )
        guessHistory.clear()
        hintCount = 3
        _gameState.value = GameState.NotStarted
    }

    /**
     * Submit a guess.
     * The guess is added to the history, and the game state is updated based on the evaluation.
     */
    fun submitGuess(guess: Int) {
        guessHistory.add(guess)
        val newState = gameLogic.evaluateGuess(guess)
        _gameState.value = newState
    }

    /**
     * Restart the game.
     * This resets the game logic and clears the guess history.
     */
    fun restartGame() {
        gameLogic.resetGame()
        guessHistory.clear()
        hintCount = 2
        _gameState.value = GameState.NotStarted
    }

    /**
     * Request a hint.
     * Decreases the hint count and returns a hint from the game logic.
     */
    fun requestHint(): String {
        return if (hintCount > 0) {
            hintCount--
            gameLogic.provideHint(2 - hintCount)  // Pass the hint level (0 for first hint, 1 for second, etc.)
        } else {
            "No more hints available!"
        }
    }
}