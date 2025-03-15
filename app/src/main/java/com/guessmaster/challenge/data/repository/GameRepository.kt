package com.guessmaster.challenge.data.repository

import com.guessmaster.challenge.domain.GameState
import javax.inject.Inject
import kotlin.math.abs

class GameRepository @Inject constructor() {
    private var maxAttempts: Int = 10
    private var numberRange: IntRange = 1..100
    private var secretNumber: Int = generateSecretNumber()
    private var attempts = 0
    private val guessDiffs = mutableListOf<Int>()
    private var closestGuess: Int? = null

    private fun generateSecretNumber(): Int = numberRange.random()

    fun updateGameSettings(maxAttempts: Int, numberRange: IntRange) {
        this.maxAttempts = maxAttempts
        this.numberRange = numberRange
        resetGame()
    }

    fun resetGame() {
        attempts = 0
        secretNumber = generateSecretNumber()
        guessDiffs.clear()
        closestGuess = null
    }

    fun evaluateGuess(guess: Int): GameState {
        attempts++
        val diff = abs(guess - secretNumber)
        guessDiffs.add(diff)

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

        return baseMessage + when {
            diff > 30 -> " ❄️ Ice cold! You're way off! Consider a major adjustment."
            diff in 21..30 -> " You're far from the target. Try shifting significantly."
            diff in 11..20 -> " Getting closer! Adjust by about 10."
            diff in 6..10 -> " 🔥 Very close! Fine-tune your guess."
            else -> " 🔥 Almost there!"
        }
    }

    fun getCurrentRange() = numberRange
    fun getCurrentMaxAttempts() = maxAttempts

    fun provideHint(hintLevel: Int): String {
        val diff = abs(closestGuess?.minus(secretNumber) ?: return "Make your first guess!")
        
        return when (hintLevel) {
            0 -> generateBasicHint(diff)
            1 -> generateDetailedHint(diff)
            else -> generatePreciseHint(diff)
        }
    }

    private fun generateBasicHint(diff: Int): String = when {
        diff > 50 -> "You're very far from the number"
        diff > 25 -> "You're somewhat far from the number"
        diff > 10 -> "You're getting closer"
        else -> "You're in the right neighborhood"
    }

    private fun generateDetailedHint(diff: Int): String {
        val direction = if (closestGuess!! < secretNumber) "higher" else "lower"
        val magnitude = when {
            diff > 50 -> "much"
            diff > 25 -> "quite a bit"
            diff > 10 -> "somewhat"
            else -> "slightly"
        }
        return "Try going $magnitude $direction than $closestGuess"
    }

    private fun generatePreciseHint(diff: Int): String {
        val closeRange = secretNumber - 5..secretNumber + 5
        return if (closestGuess!! in closeRange) {
            "Very close! The number is within 5 of your last guess"
        } else {
            val rangeHint = if (closestGuess!! < secretNumber) {
                "between ${closestGuess!! + 1} and ${closestGuess!! + 20}"
            } else {
                "between ${closestGuess!! - 20} and ${closestGuess!! - 1}"
            }
            "The number is $rangeHint"
        }
    }
} 