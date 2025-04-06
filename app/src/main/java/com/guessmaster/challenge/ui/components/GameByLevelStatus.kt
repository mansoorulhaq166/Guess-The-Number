package com.guessmaster.challenge.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.guessmaster.challenge.data.models.GameState
import com.guessmaster.challenge.ui.screen.levelscreen.LevelsViewModel
import com.guessmaster.challenge.ui.theme.montserrat

@Composable
fun GameByLevelStatus(
    gameState: GameState,
    viewModel: LevelsViewModel = hiltViewModel(),
    maxNumber: Int,
    currentLevel: Int
) {
    val backgroundColor = when (gameState) {
        is GameState.Won -> Color(0xFF4CAF50)
        is GameState.Lost -> Color(0xFFF44336)
        else -> Color(0xFF0B0D2E)
    }

    val elevation by animateFloatAsState(
        targetValue = if (gameState is GameState.InProgress) 8f else 4f
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(elevation.dp, RoundedCornerShape(8.dp)),
        color = backgroundColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (gameState) {
                is GameState.NotStarted -> Text(
                    "Level $currentLevel: Guess between 1 and $maxNumber",
                    fontFamily = montserrat,
                    fontSize = 18.sp,
                    color = Color.White
                )
                is GameState.InProgress -> {
                    val maxAttempts = viewModel.getMaxAttemptsForCurrentLevel()
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
                is GameState.Won -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Level Complete",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        "🎉 Level $currentLevel Complete!",
                        fontFamily = montserrat,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        "Guessed in ${gameState.attempts} attempts",
                        fontFamily = montserrat,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
                is GameState.Lost -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Level Failed",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        "❌ Level $currentLevel Failed",
                        fontFamily = montserrat,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        "The number was ${gameState.secretNumber}",
                        fontFamily = montserrat,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}