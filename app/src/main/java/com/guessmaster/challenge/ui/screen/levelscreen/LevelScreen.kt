package com.guessmaster.challenge.ui.screen.levelscreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.guessmaster.challenge.data.models.GameState
import com.guessmaster.challenge.ui.components.GameByLevelStatus
import com.guessmaster.challenge.ui.components.game.BottomNumberEntry
import com.guessmaster.challenge.ui.components.game.ExitDialog
import com.guessmaster.challenge.ui.components.game.GuessHistoryDialog
import com.guessmaster.challenge.ui.components.game.HintDialog
import com.guessmaster.challenge.ui.screen.settingscreen.SettingsViewModel
import com.guessmaster.challenge.ui.theme.montserrat
import kotlinx.coroutines.delay

@Composable
fun GameByLevelScreen(
    navController: NavController,
    viewModel: LevelsViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    // State collection
    val gameState by viewModel.gameState.collectAsState()
    val currentLevel by viewModel.currentLevel.collectAsState()
    val maxLevel by viewModel.maxLevel.collectAsState()
    val isHapticEnabled by settingsViewModel.isHapticEnabled.collectAsState(initial = true)
    // Calculate attempts based on guess history
    val attemptsLeft = remember(viewModel.guessHistory) {
        viewModel.getMaxAttemptsForCurrentLevel() - viewModel.guessHistory.size
    }
    val hapticFeedback = LocalHapticFeedback.current

    // UI state
    var enteredNumber by remember { mutableStateOf("") }
    var showExitConfirmation by remember { mutableStateOf(false) }
    var showHintDialog by remember { mutableStateOf(false) }
    var showGuessHistory by remember { mutableStateOf(false) }
    var showCelebration by remember { mutableStateOf(false) }

    // Animation states
    val buttonScale by animateFloatAsState(
        targetValue = if (enteredNumber.isNotEmpty()) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    // Color theme
    val backgroundColor = Color(0xFF0C1445)
    val cardBackground = Color(0xFF1A2151)
    val accentColor = Color(0xFF4A6CFF)
    val successColor = Color(0xFF4CAF50)
    val warningColor = Color(0xFFFFA726)

    // Haptic feedback helper
    fun triggerHaptic(type: HapticFeedbackType) {
        if (isHapticEnabled) hapticFeedback.performHapticFeedback(type)
    }

    // Show celebration animation on win
    LaunchedEffect(gameState) {
        if (gameState is GameState.Won) {
            showCelebration = true
            delay(2000)
            showCelebration = false
        }
    }

    // Back button handling
    BackHandler(enabled = gameState is GameState.InProgress) {
        triggerHaptic(HapticFeedbackType.LongPress)
        showExitConfirmation = true
    }

    // Main UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // Content column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header card with level info
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.elevatedCardElevation(4.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = cardBackground)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Level: $currentLevel/$maxLevel",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontFamily = montserrat,
                        fontWeight = FontWeight.Bold
                    )

                    // Attempts indicator
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                attemptsLeft > 5 -> accentColor
                                attemptsLeft > 2 -> warningColor
                                else -> Color.Red.copy(alpha = 0.8f)
                            }
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Attempts: $attemptsLeft",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            fontFamily = montserrat,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    // Next level button
                    AnimatedVisibility(
                        visible = gameState is GameState.Won && currentLevel < maxLevel,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        FilledTonalButton(
                            onClick = {
                                triggerHaptic(HapticFeedbackType.LongPress)
                                viewModel.advanceToNextLevel()
                                enteredNumber = ""
                            },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = successColor
                            )
                        ) {
                            Text(
                                "Next",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White,
                                fontFamily = montserrat
                            )
                            Icon(
                                Icons.Default.ArrowForward,
                                contentDescription = "Next level",
                                tint = Color.White,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Game title
            Text(
                "Guess The Number!",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontFamily = montserrat,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Game status display
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = CardDefaults.cardColors(containerColor = cardBackground),
                shape = RoundedCornerShape(16.dp)
            ) {
                GameByLevelStatus(
                    gameState = gameState,
                    viewModel = viewModel,
                    maxNumber = viewModel.getMaxNumberForCurrentLevel(),
                    currentLevel = currentLevel
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Game actions section
            when (gameState) {
                is GameState.Won, is GameState.Lost -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                triggerHaptic(HapticFeedbackType.LongPress)
                                viewModel.restartLevel()
                                enteredNumber = ""
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = cardBackground),
                            modifier = Modifier.scale(buttonScale)
                        ) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Retry level",
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text("Retry Level")
                        }

                        Button(
                            onClick = {
                                triggerHaptic(HapticFeedbackType.LongPress)
                                viewModel.restartGame()
                                enteredNumber = ""
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                        ) {
                            Text("New Game")
                        }
                    }
                }

                is GameState.InProgress -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        FilledTonalButton(
                            onClick = {
                                triggerHaptic(HapticFeedbackType.TextHandleMove)
                                showHintDialog = true
                            },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = accentColor.copy(alpha = 0.7f)
                            )
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = "Get hint",
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text("Hint")
                        }

                        FilledTonalButton(
                            onClick = {
                                triggerHaptic(HapticFeedbackType.TextHandleMove)
                                showGuessHistory = true
                            },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = cardBackground
                            )
                        ) {
                            Icon(
                                Icons.AutoMirrored.Default.List,
                                contentDescription = "View history",
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text("History")
                        }
                    }
                }
                else -> Unit
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom number entry
            BottomNumberEntry(
                gameState = gameState,
                enteredNumber = enteredNumber,
                onNumberClick = { if (enteredNumber.length < 4) enteredNumber += it },
                onDelete = {
                    triggerHaptic(HapticFeedbackType.TextHandleMove)
                    enteredNumber = enteredNumber.dropLast(1)
                },
                onSubmit = {
                    enteredNumber.toIntOrNull()?.let {
                        viewModel.submitGuess(it)
                        enteredNumber = ""
                        triggerHaptic(
                            if (gameState is GameState.Won || gameState is GameState.Lost)
                                HapticFeedbackType.LongPress
                            else
                                HapticFeedbackType.TextHandleMove
                        )
                    }
                }
            )
        }

        // Dialogs
        if (showGuessHistory) {
            GuessHistoryDialog(
                guessHistory = viewModel.guessHistory,
                onDismiss = { showGuessHistory = false }
            )
        }

        if (showHintDialog) {
            HintDialog(
                hint = viewModel.requestHint(),
                onDismiss = { showHintDialog = false }
            )
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

        // Win celebration overlay
        if (showCelebration) {
            CelebrationOverlay()
        }
    }
}

@Composable
fun CelebrationOverlay() {
    // Implementation of confetti animation
    // Could use a library like nl.dionsegijn:konfetti-compose
    // For now just a placeholder
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x33FFFFFF)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Level Complete!",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}