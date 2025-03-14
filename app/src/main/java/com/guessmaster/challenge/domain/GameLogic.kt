package com.guessmaster.challenge.domain

import javax.inject.Inject
import kotlin.math.abs

class GameLogic @Inject constructor() {

    private var maxAttempts: Int = 10
    private var numberRange: IntRange = 1..100
    private var secretNumber: Int = generateSecretNumber()
    private var attempts = 0
    private val guessDiffs = mutableListOf<Int>()
    private var closestGuess: Int? = null

    fun updateGameSettings(maxAttempts: Int, numberRange: IntRange) {
        this.maxAttempts = maxAttempts
        this.numberRange = numberRange
        resetGame()  // Restart the game with new settings
    }

    fun evaluateGuess(guess: Int): GameState {
        attempts++
        val diff = abs(guess - secretNumber)
        guessDiffs.add(diff)

        // Update closest guess
        if (closestGuess == null || abs(closestGuess!! - secretNumber) > diff) {
            closestGuess = guess
        }

        return when {
            guess == secretNumber -> GameState.Won(attempts)
            attempts >= maxAttempts -> GameState.Lost(attempts, secretNumber)
            else -> GameState.InProgress(attempts, generateHintMessage(guess, diff))
        }
    }

    private fun generateHintMessage(guess: Int, diff: Int): String {
        val baseMessage = if (guess < secretNumber) "Too low!" else "Too high!"

        if (attempts < 3) return baseMessage

        val dynamicHint = when {
            diff > 30 -> " ❄️ Ice cold! You're way off! Consider a major adjustment."
            diff in 21..30 -> " You're far from the target. Try shifting significantly."
            diff in 11..20 -> " Getting closer! Adjust by about 10."
            diff in 6..10 -> " 🔥 Very close! Fine-tune your guess."
            else -> " 🔥 Almost there!"
        }

        val averageDiff = guessDiffs.average()
        val trendHint = if (averageDiff > 25 && diff > averageDiff) {
            " It seems your guesses are drifting away—try reversing your trend."
        } else ""

        val closestHint = closestGuess?.let {
            if (it != guess) " Your closest guess so far was $it. Try near that!"
            else ""
        } ?: ""

        return baseMessage + dynamicHint + trendHint + closestHint
    }

    fun provideHint(hintLevel: Int): String {
        val hints = listOf(
            "The number is ${if (secretNumber % 2 == 0) "even" else "odd"}.",
            "It is ${
                when {
                    secretNumber % 10 == 0 -> "a multiple of 10"
                    secretNumber % 5 == 0 -> "a multiple of 5"
                    secretNumber % 3 == 0 -> "a multiple of 3"
                    else -> "not a multiple of 3, 5, or 10"
                }
            }.",
            "It falls in the range ${
                when {
                    secretNumber <= 50 -> "between 1 and 50"
                    secretNumber in 51..100 -> "between 51 and 100"
                    secretNumber in 101..150 -> "between 101 and 150"
                    else -> "between 151 and 200"
                }
            }.",
            "The first digit is ${
                secretNumber.toString().first()
            }, and the last digit is ${secretNumber.toString().last()}."
        )

        return hints.take(hintLevel + 1).joinToString("\n")
    }

    fun resetGame() {
        secretNumber = generateSecretNumber()
        attempts = 0
        guessDiffs.clear()
        closestGuess = null
    }

    private fun generateSecretNumber(): Int = numberRange.random()
}