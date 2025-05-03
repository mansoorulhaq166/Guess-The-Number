package com.guessmaster.challenge.data.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

interface GameByLevelUiState {
    val gameState: GameState
    val currentLevel: Int
    val maxLevel: Int
    val isHapticEnabled: Boolean
    val guessHistory: List<Int>
    val enteredNumber: String
    val showExitConfirmation: Boolean
    val showHintDialog: Boolean
    val showGuessHistory: Boolean
    val showCelebration: Boolean
    val attemptsLeft: Int
}

class MutableGameByLevelState : GameByLevelUiState {
    override var gameState: GameState by mutableStateOf(GameState.NotStarted)
    override var currentLevel: Int by mutableIntStateOf(1)
    override var maxLevel: Int by mutableIntStateOf(10)
    override var isHapticEnabled: Boolean by mutableStateOf(true)
    override var guessHistory: List<Int> by mutableStateOf(emptyList())
    override var enteredNumber: String by mutableStateOf("")
    override var showExitConfirmation: Boolean by mutableStateOf(false)
    override var showHintDialog: Boolean by mutableStateOf(false)
    override var showGuessHistory: Boolean by mutableStateOf(false)
    override var showCelebration: Boolean by mutableStateOf(false)
    override var attemptsLeft: Int by mutableIntStateOf(10)
}