package com.guessmaster.challenge.ui.components.game

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.guessmaster.challenge.R
import com.guessmaster.challenge.data.models.GameState
import com.guessmaster.challenge.ui.screen.gamescreen.GameViewModel
import com.guessmaster.challenge.ui.theme.montserrat

@Composable
fun GameStatus(
    gameState: GameState,
    viewModel: GameViewModel = hiltViewModel(),
    maxNumber: Int,
    selectedDifficulty: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (gameState) {
            is GameState.NotStarted -> {
                Text(
                    text = stringResource(R.string.game_not_started, maxNumber),
                    fontFamily = montserrat,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }

            is GameState.InProgress -> {
                val maxAttempts = viewModel.getMaxAttemptsForDifficulty(selectedDifficulty)
                val remaining = maxAttempts - gameState.attempts

                Text(
                    text = stringResource(R.string.game_attempts, gameState.attempts, maxAttempts),
                    fontFamily = montserrat,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Cyan
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.game_remaining, remaining),
                    fontFamily = montserrat,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Yellow
                )

                gameState.message?.takeIf { it.isNotBlank() }?.let { message ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = message,
                        fontFamily = montserrat,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Green
                    )
                }
            }

            is GameState.Won -> {
                Text(
                    text = stringResource(R.string.game_won, gameState.attempts),
                    fontFamily = montserrat,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            }

            is GameState.Lost -> {
                Text(
                    text = stringResource(R.string.game_lost, gameState.secretNumber),
                    fontFamily = montserrat,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF44336)
                )
            }
        }
    }
}