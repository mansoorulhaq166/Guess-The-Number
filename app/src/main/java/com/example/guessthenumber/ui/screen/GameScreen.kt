package com.example.guessthenumber.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.guessthenumber.domain.GameState
import com.example.guessthenumber.ui.components.NumberEntry
import com.example.guessthenumber.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(modifier: Modifier = Modifier, viewModel: GameViewModel) {
    val gameState by viewModel.gameState.collectAsState()
    var guessInput by remember { mutableStateOf("") }
    var hintMessage by remember { mutableStateOf<String?>(null) }

    // Difficulty selection state
    var selectedDifficulty by remember { mutableStateOf("Medium") }
    var difficultyExpanded by remember { mutableStateOf(false) }
    val difficultyOptions = listOf("Easy", "Medium", "Hard")

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Guess The Number!",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Difficulty selection (only before game starts)
        if (gameState is GameState.NotStarted) {
            ExposedDropdownMenuBox(
                expanded = difficultyExpanded,
                onExpandedChange = { difficultyExpanded = !difficultyExpanded }
            ) {
                OutlinedTextField(
                    value = selectedDifficulty,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Difficulty") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = difficultyExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = difficultyExpanded,
                    onDismissRequest = { difficultyExpanded = false }
                ) {
                    difficultyOptions.forEach { difficulty ->
                        DropdownMenuItem(
                            text = { Text(difficulty) },
                            onClick = {
                                selectedDifficulty = difficulty
                                difficultyExpanded = false
                                viewModel.updateDifficulty(difficulty)
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Display Game State messages
        when (val state = gameState) {
            is GameState.NotStarted -> Text("Start guessing a number between 1 and ${viewModel.getNumberRangeForDifficulty(selectedDifficulty)}")
            is GameState.InProgress -> {
                val maxAttempts = viewModel.getMaxAttemptsForDifficulty(selectedDifficulty)
                Text("Attempts: ${state.attempts} / $maxAttempts")
                Text("Remaining attempts: ${maxAttempts - state.attempts}")
                state.message?.let { Text(it) }
            }
            is GameState.Won -> Text("🎉 Congratulations! You guessed correctly in ${state.attempts} attempts.")
            is GameState.Lost -> Text("❌ Game Over! The correct number was ${state.secretNumber}.")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display guess history if available
        val guessHistory = viewModel.guessHistory
        if (guessHistory.isNotEmpty()) {
            Text("Guess History:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            guessHistory.forEach { guess -> Text("- $guess", style = MaterialTheme.typography.bodyLarge) }
            Spacer(modifier = Modifier.height(16.dp))
        }

        var enteredNumber by remember { mutableStateOf("") }

        // Guess input field (Only during an active game)
        if (gameState is GameState.NotStarted || gameState is GameState.InProgress) {
            NumberEntry(
                enteredNumber = enteredNumber,
                onNumberClick = { number ->
                    if (enteredNumber.length < 3) { // Limit input length
                        enteredNumber += number
                    }
                },
                onDelete = {
                    enteredNumber = enteredNumber.dropLast(1)
                },
                onSubmit = {
                    if (enteredNumber.isNotEmpty()) {
                        viewModel.submitGuess(enteredNumber.toInt())
                        enteredNumber = "" // Clear after submitting
                    }
                }
            )
        }

        // Restart button (Only after game ends)
        if (gameState is GameState.Won || gameState is GameState.Lost) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.restartGame()
                    guessInput = ""
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Restart Game")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hint button (Enabled only during an active game)
        Button(
            onClick = { hintMessage = viewModel.requestHint() },
            modifier = Modifier.fillMaxWidth(),
            enabled = gameState is GameState.InProgress
        ) {
            Text("Get a Hint")
        }

        // Display hint message if available
        hintMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
        }
    }
}