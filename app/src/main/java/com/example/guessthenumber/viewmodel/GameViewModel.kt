package com.example.guessthenumber.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.guessthenumber.domain.GameLogic
import com.example.guessthenumber.domain.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {

    // Difficulty state: "Easy", "Medium", or "Hard"
    var selectedDifficulty: String = "Medium"
        private set

    // Guess history: stores all the guesses made by the user.
    val guessHistory = mutableStateListOf<Int>()

    // Map difficulty to maximum attempts
    private fun getMaxAttemptsForDifficulty(difficulty: String): Int {
        return when (difficulty) {
            "Easy" -> 15
            "Hard" -> 5
            else -> 10
        }
    }

    // Initialize gameLogic with the max attempts based on the current difficulty.
    private var gameLogic: GameLogic = GameLogic(maxAttempts = getMaxAttemptsForDifficulty(selectedDifficulty))

    private val _gameState = MutableStateFlow<GameState>(GameState.NotStarted)
    val gameState = _gameState.asStateFlow()

    /**
     * Update the selected difficulty.
     * This reinitializes the game logic with new parameters,
     * clears the guess history, and resets the game state.
     */
    fun updateDifficulty(newDifficulty: String) {
        selectedDifficulty = newDifficulty
        gameLogic = GameLogic(maxAttempts = getMaxAttemptsForDifficulty(selectedDifficulty))
        guessHistory.clear()
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
        _gameState.value = GameState.NotStarted
    }
}