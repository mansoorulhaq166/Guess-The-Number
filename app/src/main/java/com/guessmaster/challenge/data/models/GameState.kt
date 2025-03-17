package com.guessmaster.challenge.data.models

sealed class GameState {
    data object NotStarted : GameState()
    data class InProgress(val attempts: Int, val message: String?) : GameState()
    data class Won(val attempts: Int) : GameState()
    data class Lost(val attempts: Int, val secretNumber: Int, val reason: String? = null) : GameState()
}