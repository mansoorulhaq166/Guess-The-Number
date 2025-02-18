package com.example.guessthenumber.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.guessthenumber.domain.GameState
import com.example.guessthenumber.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(modifier: Modifier = Modifier, viewModel: GameViewModel) {
    // Observe the game state from our ViewModel
    val gameState by viewModel.gameState.collectAsState()
    var guessInput by remember { mutableStateOf("") }
    // New: Guess history (ensure your GameViewModel exposes this list)
    val guessHistory = viewModel.guessHistory

    // New: Difficulty selection state (only available when the game hasn't started)
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

        // Difficulty selector shown only before the game begins
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
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = difficultyExpanded)
                    },
                    modifier = Modifier.fillMaxWidth()
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
                                // Optionally, update your ViewModel with the selected difficulty here
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Display instructions or current game messages
        when (gameState) {
            is GameState.NotStarted -> {
                Text("Start guessing a number between 1 and 100")
            }
            is GameState.InProgress -> {
                val state = gameState as GameState.InProgress
                Text("Attempts: ${state.attempts} / 10")
                Text("Remaining attempts: ${10 - state.attempts}")
                state.message?.let { Text(it) }
            }
            is GameState.Won -> {
                val state = gameState as GameState.Won
                Text("Congratulations! You guessed correctly in ${state.attempts} attempts.")
            }
            is GameState.Lost -> {
                val state = gameState as GameState.Lost
                Text("Game Over! The correct number was ${state.secretNumber}.")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // New: Display the guess history if any guesses have been made
        if (guessHistory.isNotEmpty()) {
            Text("Guess History:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            for (guess in guessHistory) {
                Text("- $guess", style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Show the input field and submit button if the game is active
        if (gameState is GameState.NotStarted || gameState is GameState.InProgress) {
            OutlinedTextField(
                value = guessInput,
                onValueChange = { guessInput = it },
                label = { Text("Enter your guess") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Validate input and submit guess
                    guessInput.toIntOrNull()?.let { guess ->
                        viewModel.submitGuess(guess)
                        guessInput = ""  // Clear input after submission
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Guess")
            }
        }

        // When the game concludes, show a restart button
        if (gameState is GameState.Won || gameState is GameState.Lost) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.restartGame()
                    guessInput = ""
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Restart Game")
            }
        }
    }
}