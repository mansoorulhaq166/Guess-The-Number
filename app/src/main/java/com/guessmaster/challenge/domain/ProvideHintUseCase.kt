package com.guessmaster.challenge.domain

import com.guessmaster.challenge.data.repository.GameRepository
import javax.inject.Inject

class ProvideHintUseCase @Inject constructor(
    private val gameRepository: GameRepository
) {
    fun execute(hintLevel: Int): String {
        val secretNumber = gameRepository.getSecretNumber()

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
            "The first digit is ${secretNumber.toString().first()}, and the last digit is ${secretNumber.toString().last()}."
        )

        return hints.take(hintLevel + 1).joinToString("\n")
    }
}