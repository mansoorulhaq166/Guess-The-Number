package com.guessmaster.challenge.ui.screen.gamescreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.guessmaster.challenge.R
import com.guessmaster.challenge.data.models.GameState
import com.guessmaster.challenge.ui.components.game.BottomNumberEntry
import com.guessmaster.challenge.ui.components.game.ExitDialog
import com.guessmaster.challenge.ui.components.game.GameActions
import com.guessmaster.challenge.ui.components.game.GameStatus
import com.guessmaster.challenge.ui.components.game.GuessHistoryDialog
import com.guessmaster.challenge.ui.components.game.HintDialog
import com.guessmaster.challenge.ui.components.game.RestartButton
import com.guessmaster.challenge.ui.components.game.getMaxNumber
import com.guessmaster.challenge.ui.components.main.DifficultyDropdown
import com.guessmaster.challenge.ui.screen.settingscreen.SettingsViewModel
import com.guessmaster.challenge.ui.theme.montserrat

@Composable
fun GameScreen(
    navController: NavController,
    viewModel: GameViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val gameState by viewModel.gameState.collectAsState()
    val isHapticEnabled by settingsViewModel.isHapticEnabled.collectAsState(initial = true)
    val hapticFeedback = LocalHapticFeedback.current

    var enteredNumber by remember { mutableStateOf("") }
    var showExitConfirmation by remember { mutableStateOf(false) }
    var showHintDialog by remember { mutableStateOf(false) }
    var showGuessHistory by remember { mutableStateOf(false) }
    var difficultyExpanded by remember { mutableStateOf(false) }
    var selectedDifficulty by remember { mutableStateOf("Medium") }

    fun triggerHaptic(type: HapticFeedbackType) {
        if (isHapticEnabled) hapticFeedback.performHapticFeedback(type)
    }

    BackHandler(enabled = gameState is GameState.InProgress) {
        triggerHaptic(HapticFeedbackType.LongPress)
        showExitConfirmation = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0D2E))
    ) {
        if (showGuessHistory) {
            GuessHistoryDialog(viewModel.guessHistory) { showGuessHistory = false }
        }
        if (showHintDialog) {
            HintDialog(viewModel.requestHint()) { showHintDialog = false }
        }

        if (showExitConfirmation) {
            ExitDialog(
                showDialog = true,
                onDismissRequest = { showExitConfirmation = false },
                onConfirmExit = {
                    showExitConfirmation = false
                    navController.popBackStack()
                    viewModel.restartGame()
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.game_title),
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            fontFamily = montserrat
        )

        if (gameState is GameState.NotStarted) {
            DifficultyDropdown(
                selectedDifficulty,
                difficultyExpanded,
                { difficultyExpanded = it }) { difficulty ->
                selectedDifficulty = difficulty
                difficultyExpanded = false
                viewModel.updateDifficulty(difficulty)
            }
        }

        GameStatus(gameState, viewModel, getMaxNumber(selectedDifficulty), selectedDifficulty)

        when (gameState) {
            is GameState.Won, is GameState.Lost -> RestartButton {
                triggerHaptic(HapticFeedbackType.LongPress)
                viewModel.restartGame()
                enteredNumber = ""
            }

            is GameState.InProgress -> GameActions(
                onHintClick = {
                    triggerHaptic(HapticFeedbackType.TextHandleMove)
                    showHintDialog = true
                },
                onHistoryClick = {
                    triggerHaptic(HapticFeedbackType.TextHandleMove)
                    showGuessHistory = true
                }
            )

            else -> Unit
        }
    }

    BottomNumberEntry(
        gameState = gameState,
        enteredNumber = enteredNumber,
        onNumberClick = { if (enteredNumber.length < 3) enteredNumber += it },
        onDelete = {
            triggerHaptic(HapticFeedbackType.TextHandleMove)
            enteredNumber = enteredNumber.dropLast(1)
        },
        onSubmit = {
            enteredNumber.toIntOrNull()?.let {
                viewModel.submitGuess(it)
                enteredNumber = ""
                triggerHaptic(
                    if (gameState is GameState.Won || gameState is GameState.Lost) HapticFeedbackType.LongPress
                    else HapticFeedbackType.TextHandleMove
                )
            }
        }
    )
}