package com.guessmaster.challenge.domain

import android.content.Context
import com.guessmaster.challenge.R
import javax.inject.Inject

class GetGameSettingsUseCase @Inject constructor(
    private val context: Context
) {
    fun getMaxAttempts(difficulty: String): Int = when (difficulty) {
        context.getString(R.string.difficulty_easy) -> 15
        context.getString(R.string.difficulty_hard) -> 5
        else -> 10
    }

    fun getNumberRange(difficulty: String): IntRange = when (difficulty) {
        context.getString(R.string.difficulty_easy) -> 1..50
        context.getString(R.string.difficulty_hard) -> 1..200
        else -> 1..100
    }
}