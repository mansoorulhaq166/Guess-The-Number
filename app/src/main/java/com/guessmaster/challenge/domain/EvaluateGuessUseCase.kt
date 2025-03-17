package com.guessmaster.challenge.domain

import com.guessmaster.challenge.data.models.GameState
import com.guessmaster.challenge.data.repository.GameRepository
import javax.inject.Inject
import kotlin.math.abs

class EvaluateGuessUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {
    fun execute(guess: Int): GameState {
        gameRepository.incrementAttempts()
        val attempts = gameRepository.getAttempts()
        val secretNumber = gameRepository.getSecretNumber()
        val diff = abs(guess - secretNumber)

        gameRepository.updateClosestGuess(guess)

        return when {
            guess == secretNumber -> {
                gameRepository.resetGame()
                GameState.Won(attempts)
            }
            attempts >= 10 -> {
                gameRepository.resetGame()
                GameState.Lost(attempts, secretNumber)
            }
            else -> GameState.InProgress(attempts, generateHintMessage(guess, diff))
        }
    }

    private fun generateHintMessage(guess: Int, diff: Int): String {
        val baseMessage = if (guess < gameRepository.getSecretNumber()) "Too low!" else "Too high!"

        return when {
            diff > 30 -> "$baseMessage ❄️ Ice cold! You're way off! Consider a major adjustment."
            diff in 21..30 -> "$baseMessage You're far from the target. Try shifting significantly."
            diff in 11..20 -> "$baseMessage Getting closer! Adjust by about 10."
            diff in 6..10 -> "$baseMessage 🔥 Very close! Fine-tune your guess."
            else -> "$baseMessage 🔥 Almost there!"
        }
    }
}