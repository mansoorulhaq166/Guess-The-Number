package com.guessmaster.challenge.ui.components.level

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.guessmaster.challenge.R
import com.guessmaster.challenge.data.models.GameState

@Composable
fun GameByLevelActions(
    gameState: GameState,
    onRetryLevel: () -> Unit,
    onNewGame: () -> Unit,
    onShowHint: () -> Unit,
    onShowHistory: () -> Unit,
    buttonScale: Float
) {
    when (gameState) {
        is GameState.Won, is GameState.Lost -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onRetryLevel,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A2151)),
                    modifier = Modifier.scale(buttonScale)
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = stringResource(R.string.retry_level_description),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(stringResource(R.string.retry_level_button))
                }

                Button(
                    onClick = onNewGame,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A6CFF))
                ) {
                    Text(stringResource(R.string.new_game_button))
                }
            }
        }

        is GameState.InProgress -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilledTonalButton(
                    onClick = onShowHint,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Color(0xFF4A6CFF).copy(alpha = 0.7f)
                    )
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = stringResource(R.string.hint_description),
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(stringResource(R.string.hint_button))
                }

                FilledTonalButton(
                    onClick = onShowHistory,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Color(0xFF1A2151)
                    )
                ) {
                    Icon(
                        Icons.AutoMirrored.Default.List,
                        contentDescription = stringResource(R.string.history_description),
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(stringResource(R.string.history_button))
                }
            }
        }

        else -> Unit
    }
}