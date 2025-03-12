package com.guessmaster.challenge.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.guessmaster.challenge.domain.GameLogic
import com.guessmaster.challenge.domain.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {

    // Difficulty state: "Easy", "Medium", or "Hard"
    private var selectedDifficulty: String = "Medium"

    // Guess history: stores all the guesses made by the user.
    val guessHistory = mutableStateListOf<Int>()
    private var hintCount = 2

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

    // Initialize gameLogic with difficulty-based settings
    private var gameLogic: GameLogic = GameLogic(
        maxAttempts = getMaxAttemptsForDifficulty(selectedDifficulty),
        numberRange = getNumberRangeForDifficulty(selectedDifficulty)
    )

    private val _gameState = MutableStateFlow<GameState>(GameState.NotStarted)
    val gameState = _gameState.asStateFlow()

    /**
     * Update the selected difficulty.
     * This reinitializes the game logic with new parameters,
     * clears the guess history, and resets the game state.
     */
    fun updateDifficulty(newDifficulty: String) {
        selectedDifficulty = newDifficulty
        gameLogic = GameLogic(
            maxAttempts = getMaxAttemptsForDifficulty(selectedDifficulty),
            numberRange = getNumberRangeForDifficulty(selectedDifficulty)
        )
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