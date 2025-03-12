package com.guessmaster.challenge.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guessmaster.challenge.domain.GameState
import com.guessmaster.challenge.ui.theme.montserrat
import com.guessmaster.challenge.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DifficultyDropdown(
    selectedDifficulty: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onSelect: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        Text(
            text = "Select Difficulty",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = montserrat,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = onExpandedChange) {
            OutlinedTextField(
                value = selectedDifficulty,
                onValueChange = {},
                readOnly = true,
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
                listOf("Easy", "Medium", "Hard").forEach { difficulty ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                difficulty,
                                fontFamily = montserrat,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        },
                        onClick = { onSelect(difficulty) },
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun GameStatus(
    gameState: GameState,
    viewModel: GameViewModel,
    maxNumber: Int,
    selectedDifficulty: String
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (gameState) {
            is GameState.NotStarted -> Text(
                "Start guessing a number between 1 and $maxNumber",
                fontFamily = montserrat,
                fontSize = 18.sp,
                color = Color.White
            )
            is GameState.InProgress -> {
                val maxAttempts = viewModel.getMaxAttemptsForDifficulty(selectedDifficulty)
                Text(
                    "Attempts: ${gameState.attempts} / $maxAttempts",
                    fontFamily = montserrat,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Cyan
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Remaining: ${maxAttempts - gameState.attempts}",
                    fontFamily = montserrat,
                    fontSize = 16.sp,
                    color = Color.Yellow
                )
                gameState.message?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it, fontFamily = montserrat, fontSize = 18.sp, color = Color.Green)
                }
            }
            is GameState.Won -> Text(
                "🎉 Congrats! You guessed it in ${gameState.attempts} attempts!",
                fontFamily = montserrat,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Green
            )
            is GameState.Lost -> Text(
                "❌ Game Over! The number was ${gameState.secretNumber}.",
                fontFamily = montserrat,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
        }
    }
}