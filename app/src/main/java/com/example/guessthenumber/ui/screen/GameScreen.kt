package com.example.guessthenumber.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.guessthenumber.domain.GameState
import com.example.guessthenumber.ui.components.NumberEntry
import com.example.guessthenumber.ui.theme.montserrat
import com.example.guessthenumber.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(modifier: Modifier = Modifier, viewModel: GameViewModel) {
    val gameState by viewModel.gameState.collectAsState()
    var guessInput by remember { mutableStateOf("") }
    var hintMessage by remember { mutableStateOf<String?>(null) }

    var selectedDifficulty by remember { mutableStateOf("Medium") }
    var difficultyExpanded by remember { mutableStateOf(false) }
    val difficultyOptions = listOf("Easy", "Medium", "Hard")

    var enteredNumber by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0D2E)) // Dark background
    ) {
        // Background Image
//        Image(
//            painter = painterResource(id = R.drawable.main_bg),
//            contentDescription = "Background",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.matchParentSize()
//        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Guess The Number!",
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = montserrat,
                modifier = Modifier.padding(bottom = 16.dp)
            )

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
                            .menuAnchor() // ✅ FIXED: No parentheses
                    )
                    ExposedDropdownMenu(
                        expanded = difficultyExpanded,
                        onDismissRequest = { difficultyExpanded = false }
                    ) {
                        difficultyOptions.forEach { difficulty ->
                            DropdownMenuItem(
                                text = { Text(difficulty, fontFamily = montserrat) },
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
            val setDifficulty: Int =
                if (viewModel.getNumberRangeForDifficulty(selectedDifficulty) == 1..100) {
                    100
                } else if (viewModel.getNumberRangeForDifficulty(selectedDifficulty) == 1..200) {
                    200
                } else {
                    50
                }

            when (val state = gameState) {
                is GameState.NotStarted -> Text(
                    "Start guessing a number between 1 and $setDifficulty"
                )

                is GameState.InProgress -> {
                    val maxAttempts = viewModel.getMaxAttemptsForDifficulty(selectedDifficulty)
                    Text("Attempts: ${state.attempts} / $maxAttempts", fontFamily = montserrat)
                    Text(
                        "Remaining attempts: ${maxAttempts - state.attempts}",
                        fontFamily = montserrat
                    )
                    state.message?.let { Text(it, fontFamily = montserrat) }
                }

                is GameState.Won -> Text(
                    "🎉 Congratulations! You guessed correctly in ${state.attempts} attempts.",
                    fontFamily = montserrat
                )

                is GameState.Lost -> Text(
                    "❌ Game Over! The correct number was ${state.secretNumber}.",
                    fontFamily = montserrat
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display guess history if available
            val guessHistory = viewModel.guessHistory
            if (guessHistory.isNotEmpty()) {
                Text(
                    "Guess History:",
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = montserrat
                )
                Spacer(modifier = Modifier.height(8.dp))
                guessHistory.forEach { guess ->
                    Text(
                        "- $guess",
                        style = MaterialTheme.typography.bodyLarge, fontFamily = montserrat
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (gameState is GameState.Won || gameState is GameState.Lost) {
                Button(
                    onClick = {
                        viewModel.restartGame()
                        guessInput = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Restart Game", fontFamily = montserrat)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = { hintMessage = viewModel.requestHint() },
                modifier = Modifier.fillMaxWidth(),
                enabled = gameState is GameState.InProgress
            ) {
                Text("Get a Hint", fontFamily = montserrat)
            }

            hintMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    it,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // ✅ Move Number Entry to Bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            if (gameState is GameState.NotStarted || gameState is GameState.InProgress) {
                NumberEntry(
                    enteredNumber = enteredNumber,
                    onNumberClick = { number ->
                        if (enteredNumber.length < 3) {
                            enteredNumber += number
                        }
                    },
                    onDelete = {
                        enteredNumber = enteredNumber.dropLast(1)
                    },
                    onSubmit = {
                        if (enteredNumber.isNotEmpty()) {
                            viewModel.submitGuess(enteredNumber.toInt())
                            enteredNumber = ""
                        }
                    }
                )
            }
        }
    }
}