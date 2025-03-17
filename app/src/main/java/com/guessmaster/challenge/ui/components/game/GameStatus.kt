package com.guessmaster.challenge.ui.components.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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