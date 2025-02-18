package com.example.guessthenumber.domain

class GameLogic(private val maxAttempts: Int = 10) {
    private var secretNumber: Int = generateSecretNumber()
    var attempts = 0
        private set

    fun evaluateGuess(guess: Int): GameState {
        attempts++
        return when {
            guess == secretNumber -> GameState.Won(attempts)
            attempts >= maxAttempts -> GameState.Lost(attempts, secretNumber)
            guess < secretNumber -> GameState.InProgress(attempts, "Too low!")
            else -> GameState.InProgress(attempts, "Too high!")
        }
    }

    fun resetGame() {
        secretNumber = generateSecretNumber()
        attempts = 0
    }

    private fun generateSecretNumber(): Int = (1..100).random()
}