package com.example.guessthenumber.domain

import kotlin.math.abs

class GameLogic(private var maxAttempts: Int = 10, private var numberRange: IntRange = 1..100) {
    private var secretNumber: Int = generateSecretNumber()
    private var attempts = 0
    private val guessDiffs = mutableListOf<Int>()

    fun evaluateGuess(guess: Int): GameState {
        attempts++
        val diff = abs(guess - secretNumber)
        guessDiffs.add(diff)

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
            diff > 30 -> " You're way off! Consider a major adjustment."
            diff in 21..30 -> " You're far from the target. Try shifting significantly."
            diff in 11..20 -> " Getting closer! Adjust by about 10."
            diff in 6..10 -> " Very close! Fine-tune your guess."
            diff <= 5 -> " Almost there!"
            else -> ""
        }

        val averageDiff = guessDiffs.average()
        val trendHint = if (averageDiff > 25 && diff > averageDiff) {
            " It seems your guesses are drifting away—try reversing your trend."
        } else ""

        return baseMessage + dynamicHint + trendHint
    }

    fun provideHint(): String =
        "Hint: The secret number is ${if (secretNumber % 2 == 0) "even" else "odd"}."

    fun resetGame() {
        secretNumber = generateSecretNumber()
        attempts = 0
        guessDiffs.clear()
    }

    private fun generateSecretNumber(): Int = numberRange.random()
}