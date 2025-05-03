package com.guessmaster.challenge.domain

import android.content.res.Resources
import com.guessmaster.challenge.R
import com.guessmaster.challenge.data.repository.GameRepository
import javax.inject.Inject

class ProvideHintUseCase @Inject constructor(
    private val gameRepository: GameRepository,
    private val resources: Resources
) {
    fun execute(hintLevel: Int): String {
        val secretNumber = gameRepository.getSecretNumber()

        val hints = listOf(
            // Even/Odd hint
            resources.getString(
                R.string.provide_hint_even_odd,
                if (secretNumber % 2 == 0) {
                    resources.getString(R.string.provide_hint_even)
                } else {
                    resources.getString(R.string.provide_hint_odd)
                }
            ),

            // Multiple hint
            resources.getString(
                R.string.provide_hint_multiple,
                when {
                    secretNumber % 10 == 0 -> resources.getString(R.string.provide_hint_multiple_10)
                    secretNumber % 5 == 0 -> resources.getString(R.string.provide_hint_multiple_5)
                    secretNumber % 3 == 0 -> resources.getString(R.string.provide_hint_multiple_3)
                    else -> resources.getString(R.string.provide_hint_not_multiple)
                }
            ),

            // Range hint
            resources.getString(
                R.string.provide_hint_range,
                when {
                    secretNumber <= 50 -> resources.getString(R.string.provide_hint_range_1_50)
                    secretNumber in 51..100 -> resources.getString(R.string.provide_hint_range_51_100)
                    secretNumber in 101..150 -> resources.getString(R.string.provide_hint_range_101_150)
                    else -> resources.getString(R.string.provide_hint_range_151_200)
                }
            ),

            // Digits hint
            resources.getString(
                R.string.provide_hint_digits,
                secretNumber.toString().first().toString(),
                secretNumber.toString().last().toString()
            )
        )

        return hints.take(hintLevel + 1).joinToString("\n")
    }
}