package com.guessmaster.challenge.data.repository

import javax.inject.Inject
import kotlin.math.abs

class GameRepository @Inject constructor() {
    private var numberRange: IntRange = 1..100
    private var secretNumber: Int = generateSecretNumber()
    private var attempts = 0
    private var closestGuess: Int? = null

    private fun generateSecretNumber(): Int = numberRange.random()

    fun resetGame() {
        attempts = 0
        secretNumber = generateSecretNumber()
        closestGuess = null
    }

    fun getSecretNumber(): Int = secretNumber
    fun getAttempts(): Int = attempts
    fun incrementAttempts() { attempts++ }
    fun getClosestGuess(): Int? = closestGuess

    fun updateClosestGuess(guess: Int) {
        val diff = abs(guess - secretNumber)
        if (closestGuess == null || abs(closestGuess!! - secretNumber) > diff) {
            closestGuess = guess
        }
    }
}