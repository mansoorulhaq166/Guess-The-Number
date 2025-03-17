package com.guessmaster.challenge.domain

import javax.inject.Inject

class GetGameSettingsUseCase @Inject constructor() {
    fun getMaxAttempts(difficulty: String): Int = when (difficulty) {
        "Easy" -> 15
        "Hard" -> 5
        else -> 10
    }

    fun getNumberRange(difficulty: String): IntRange = when (difficulty) {
        "Easy" -> 1..50
        "Hard" -> 1..200
        else -> 1..100
    }
}