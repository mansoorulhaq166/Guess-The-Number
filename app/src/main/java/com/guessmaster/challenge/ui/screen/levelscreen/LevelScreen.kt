package com.guessmaster.challenge.ui.screen.levelscreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.guessmaster.challenge.R
import com.guessmaster.challenge.data.models.GameState
import com.guessmaster.challenge.data.models.MutableGameByLevelState
import com.guessmaster.challenge.ui.components.common.ModernCelebrationOverlay
import com.guessmaster.challenge.ui.components.game.BottomNumberEntry
import com.guessmaster.challenge.ui.components.game.ExitDialog
import com.guessmaster.challenge.ui.components.game.GuessHistoryDialog
import com.guessmaster.challenge.ui.components.game.HintDialog
import com.guessmaster.challenge.ui.components.level.GameByLevelActions
import com.guessmaster.challenge.ui.components.level.GameByLevelHeader
import com.guessmaster.challenge.ui.components.level.GameStatusCard
import com.guessmaster.challenge.ui.components.level.rememberGameByLevelState
import com.guessmaster.challenge.ui.screen.settingscreen.SettingsViewModel
import com.guessmaster.challenge.ui.theme.montserrat
import kotlinx.coroutines.delay

// GameByLevelScreen.kt
@Composable
fun GameByLevelScreen(
    navController: NavController,
    viewModel: LevelsViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val state = rememberGameByLevelState(viewModel, settingsViewModel)
    val hapticFeedback = LocalHapticFeedback.current

    // Handle back press
    BackHandler(enabled = state.gameState is GameState.InProgress) {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        state.showExitConfirmation = true
    }

    // Show celebration animation on win
    LaunchedEffect(state.gameState) {
        if (state.gameState is GameState.Won) {
            state.showCelebration = true
            delay(2000)
            state.showCelebration = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C1445))
    ) {
        GameContent(
            state = state,
            viewModel = viewModel,
            onNumberClick = { digit ->
                if (state.enteredNumber.length < 4) {
                    state.enteredNumber += digit
                }
            },
            onDelete = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                if (state.enteredNumber.isNotEmpty()) {
                    state.enteredNumber = state.enteredNumber.dropLast(1)
                }
            },
            onSubmit = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                state.enteredNumber.toIntOrNull()?.let { number ->
                    viewModel.submitGuess(number)
                    state.enteredNumber = ""
                }
            },
            onShowHint = { state.showHintDialog = true },
            onShowHistory = { state.showGuessHistory = true }
        )

        // Dialogs
        if (state.showGuessHistory) {
            GuessHistoryDialog(
                guessHistory = state.guessHistory,
                onDismiss = { state.showGuessHistory = false }
            )
        }

        if (state.showHintDialog) {
            HintDialog(
                hint = viewModel.requestHint(),
                onDismiss = { state.showHintDialog = false }
            )
        }

        if (state.showExitConfirmation) {
            ExitDialog(
                showDialog = true,
                onDismissRequest = { state.showExitConfirmation = false },
                onConfirmExit = {
                    state.showExitConfirmation = false
                    navController.popBackStack()
                    viewModel.restartGame()
                }
            )
        }

        if (state.showCelebration) {
            ModernCelebrationOverlay()
        }
    }
}

@Composable
private fun GameContent(
    state: MutableGameByLevelState,
    viewModel: LevelsViewModel,
    onNumberClick: (String) -> Unit,
    onDelete: () -> Unit,
    onSubmit: () -> Unit,
    onShowHint: () -> Unit,
    onShowHistory: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameByLevelHeader(
            currentLevel = state.currentLevel,
            maxLevel = state.maxLevel,
            attemptsLeft = state.attemptsLeft,
            showNextLevelButton = state.gameState is GameState.Won && state.currentLevel < state.maxLevel,
            onNextLevel = {
                viewModel.advanceToNextLevel()
                state.enteredNumber = ""
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            stringResource(R.string.guess_the_number_title),
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            fontFamily = montserrat,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        GameStatusCard(
            gameState = state.gameState,
            viewModel = viewModel,
            currentLevel = state.currentLevel
        )

        Spacer(modifier = Modifier.height(16.dp))

        val buttonScale by animateFloatAsState(
            targetValue = if (state.enteredNumber.isNotEmpty()) 1.05f else 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )

        GameByLevelActions(
            gameState = state.gameState,
            onRetryLevel = {
                viewModel.restartLevel()
                state.enteredNumber = ""
            },
            onNewGame = {
                viewModel.restartGame()
                state.enteredNumber = ""
            },
            onShowHint = onShowHint,
            onShowHistory = onShowHistory,
            buttonScale = buttonScale
        )

        Spacer(modifier = Modifier.weight(1f))

        BottomNumberEntry(
            gameState = state.gameState,
            enteredNumber = state.enteredNumber,
            onNumberClick = onNumberClick,
            onDelete = onDelete,
            onSubmit = onSubmit
        )
    }
}