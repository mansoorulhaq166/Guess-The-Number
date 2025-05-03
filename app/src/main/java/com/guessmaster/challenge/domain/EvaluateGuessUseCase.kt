package com.guessmaster.challenge.domain

import android.content.Context
import com.guessmaster.challenge.R
import com.guessmaster.challenge.data.models.GameState
import com.guessmaster.challenge.data.repository.GameRepository
import javax.inject.Inject
import kotlin.math.abs

class EvaluateGuessUseCase @Inject constructor(
    private val gameRepository: GameRepository,
    private val context: Context
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
        val baseMessage = if (guess < gameRepository.getSecretNumber())
            context.getString(R.string.hint_too_low)
        else
            context.getString(R.string.hint_too_high)

        return when {
            diff > 30 -> "$baseMessage ${context.getString(R.string.hint_ice_cold)}"
            diff in 21..30 -> "$baseMessage ${context.getString(R.string.hint_far)}"
            diff in 11..20 -> "$baseMessage ${context.getString(R.string.hint_closer)}"
            diff in 6..10 -> "$baseMessage ${context.getString(R.string.hint_very_close)}"
            else -> "$baseMessage ${context.getString(R.string.hint_almost_there)}"
        }
    }
}