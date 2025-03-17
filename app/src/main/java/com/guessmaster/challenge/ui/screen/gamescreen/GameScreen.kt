package com.guessmaster.challenge.ui.screen.gamescreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.guessmaster.challenge.R
import com.guessmaster.challenge.data.models.GameState
import com.guessmaster.challenge.ui.components.game.GameStatus
import com.guessmaster.challenge.ui.components.game.GuessHistoryDialog
import com.guessmaster.challenge.ui.components.game.HintDialog
import com.guessmaster.challenge.ui.components.game.ExitDialog
import com.guessmaster.challenge.ui.components.game.NumberEntry
import com.guessmaster.challenge.ui.components.game.RestartButton
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
    var enteredNumber by remember { mutableStateOf("") }
    var difficultyExpanded by remember { mutableStateOf(false) }
    var showExitConfirmation by remember { mutableStateOf(false) }
    var selectedDifficulty by remember { mutableStateOf("Medium") }

    val isHapticEnabled by settingsViewModel.isHapticEnabled.collectAsState(initial = true)
    val hapticFeedback = LocalHapticFeedback.current

    var showHintDialog by remember { mutableStateOf(false) }
    var showGuessHistory by remember { mutableStateOf(false) }

    fun triggerHaptic(type: HapticFeedbackType) {
        if (isHapticEnabled) {
            hapticFeedback.performHapticFeedback(type)
        }
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
    }

    if (showHintDialog) {
        HintDialog(viewModel.requestHint()) { showHintDialog = false }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Guess The Number!",
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

        val maxNumber = when (selectedDifficulty) {
            "Easy" -> 50
            "Medium" -> 100
            "Hard" -> 200
            else -> 100
        }

        GameStatus(gameState, viewModel, maxNumber, selectedDifficulty)

        if (gameState is GameState.Won || gameState is GameState.Lost) {
            RestartButton {
                triggerHaptic(HapticFeedbackType.LongPress)
                viewModel.restartGame()
                enteredNumber = ""
            }
        }

        if (gameState is GameState.InProgress) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_hint),
                    contentDescription = "Hint",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            triggerHaptic(HapticFeedbackType.TextHandleMove); showHintDialog =
                            true
                        },
                    tint = Color.White
                )
                Icon(
                    painter = painterResource(id = R.drawable.baseline_history_24),
                    contentDescription = "Guess History",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            triggerHaptic(HapticFeedbackType.TextHandleMove); showGuessHistory =
                            true
                        },
                    tint = Color.White
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 24.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        if (gameState is GameState.NotStarted || gameState is GameState.InProgress) {
            NumberEntry(
                enteredNumber = enteredNumber,
                onNumberClick = { if (enteredNumber.length < 3) enteredNumber += it },
                onDelete = {
                    triggerHaptic(HapticFeedbackType.TextHandleMove); enteredNumber =
                    enteredNumber.dropLast(1)
                },
                onSubmit = {
                    enteredNumber.toIntOrNull()?.let {
                        viewModel.submitGuess(it)
                        enteredNumber = ""
                        triggerHaptic(if (viewModel.gameState.value is GameState.Won || viewModel.gameState.value is GameState.Lost) HapticFeedbackType.LongPress else HapticFeedbackType.TextHandleMove)
                    }
                }
            )
        }
    }

    if (showExitConfirmation) {
        ExitDialog(
            showDialog = showExitConfirmation,
            onDismissRequest = { showExitConfirmation = false },
            onConfirmExit = {
                showExitConfirmation = false
                navController.popBackStack()
                viewModel.restartGame()
            }
        )
    }
}