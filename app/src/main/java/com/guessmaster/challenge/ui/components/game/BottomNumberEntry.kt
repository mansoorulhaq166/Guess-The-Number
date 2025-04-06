package com.guessmaster.challenge.ui.components.game

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.guessmaster.challenge.R
import com.guessmaster.challenge.data.models.GameState

@Composable
fun BottomNumberEntry(
    gameState: GameState,
    enteredNumber: String,
    onNumberClick: (String) -> Unit,
    onDelete: () -> Unit,
    onSubmit: () -> Unit
) {
    if (gameState is GameState.NotStarted || gameState is GameState.InProgress) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            NumberEntry(
                enteredNumber = enteredNumber,
                onNumberClick = onNumberClick,
                onDelete = onDelete,
                onSubmit = onSubmit
            )
        }
    }
}


@Composable
fun GameActions(onHintClick: () -> Unit, onHistoryClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ActionIcon(R.drawable.baseline_hint, "Hint", onHintClick)
        ActionIcon(R.drawable.baseline_history_24, "Guess History", onHistoryClick)
    }
}

@Composable
fun ActionIcon(iconId: Int, description: String, onClick: () -> Unit) {
    Icon(
        painter = painterResource(id = iconId),
        contentDescription = description,
        modifier = Modifier
            .size(40.dp)
            .clickable { onClick() },
        tint = Color.White
    )
}

fun getMaxNumber(difficulty: String): Int {
    return when (difficulty) {
        "Easy" -> 50
        "Medium" -> 100
        "Hard" -> 200
        else -> 100
    }
}