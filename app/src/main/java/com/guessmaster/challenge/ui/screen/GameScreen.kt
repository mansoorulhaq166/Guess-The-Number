package com.guessmaster.challenge.ui.screen

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.guessmaster.challenge.R
import com.guessmaster.challenge.domain.GameState
import com.guessmaster.challenge.ui.components.DifficultyDropdown
import com.guessmaster.challenge.ui.components.GameStatus
import com.guessmaster.challenge.ui.components.GuessHistoryDialog
import com.guessmaster.challenge.ui.components.HintDialog
import com.guessmaster.challenge.ui.components.NumberEntry
import com.guessmaster.challenge.ui.components.RestartButton
import com.guessmaster.challenge.ui.theme.montserrat
import com.guessmaster.challenge.viewmodel.GameViewModel

@Composable
fun GameScreen(navController: NavController, viewModel: GameViewModel) {
    val gameState by viewModel.gameState.collectAsState()
    var showGuessHistory by remember { mutableStateOf(false) }
    var showHintDialog by remember { mutableStateOf(false) }
    var enteredNumber by remember { mutableStateOf("") }
    var difficultyExpanded by remember { mutableStateOf(false) }
    var showExitConfirmation by remember { mutableStateOf(false) } // Back press confirmation dialog

    var selectedDifficulty by remember { mutableStateOf("Medium") }

    // Handle back press only if the game is in progress
    BackHandler(enabled = gameState is GameState.InProgress) {
        showExitConfirmation = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0D2E)) // Dark-themed background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Game Title
            Text(
                "Guess The Number!",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontFamily = montserrat
            )

            // Difficulty Selection (Only visible before the game starts)
            if (gameState is GameState.NotStarted) {
                DifficultyDropdown(selectedDifficulty, difficultyExpanded, {
                    difficultyExpanded = it
                }) { difficulty ->
                    selectedDifficulty = difficulty
                    difficultyExpanded = false
                    viewModel.updateDifficulty(difficulty)
                }
            }

            // Determine the number range based on selected difficulty
            val maxNumber = when (selectedDifficulty) {
                "Easy" -> 50
                "Medium" -> 100
                "Hard" -> 200
                else -> 100
            }

            // Display Game Status (Attempts, Messages, Win/Loss)
            GameStatus(gameState, viewModel, maxNumber, selectedDifficulty)

            // Hint & History Icons
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
                            .clickable { showHintDialog = true }
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.baseline_history_24),
                        contentDescription = "Guess History",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { showGuessHistory = true }
                    )
                }

                // Guess History Dialog
                if (showGuessHistory) {
                    GuessHistoryDialog(viewModel.guessHistory) { showGuessHistory = false }
                }

                // Hint Dialog
                if (showHintDialog) {
                    HintDialog(viewModel.requestHint()) { showHintDialog = false }
                }
            }

            // Restart Button (Only visible after a win or loss)
            if (gameState is GameState.Won || gameState is GameState.Lost) {
                RestartButton {
                    viewModel.restartGame()
                    enteredNumber = ""
                }
            }
        }

        // Numeric Input Pad (Only visible when the game is active)
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
                    onDelete = { enteredNumber = enteredNumber.dropLast(1) },
                    onSubmit = {
                        enteredNumber.toIntOrNull()?.let {
                            viewModel.submitGuess(it)
                            enteredNumber = ""
                        }
                    }
                )
            }
        }
    }

    if (showExitConfirmation) {
        AlertDialog(
            onDismissRequest = { showExitConfirmation = false },
            title = { Text("Exit Game?") },
            text = { Text("Your progress will be lost. Are you sure you want to exit?") },
            confirmButton = {
                TextButton(onClick = {
                    showExitConfirmation = false
                    navController.popBackStack()
                    viewModel.restartGame()
                }) {
                    Text("Exit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}