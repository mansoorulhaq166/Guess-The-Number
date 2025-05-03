package com.guessmaster.challenge.ui.components.level

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.guessmaster.challenge.R
import com.guessmaster.challenge.ui.theme.montserrat

@Composable
fun GameByLevelHeader(
    currentLevel: Int,
    maxLevel: Int,
    attemptsLeft: Int,
    showNextLevelButton: Boolean,
    onNextLevel: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFF1A2151))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.level_indicator, currentLevel, maxLevel),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontFamily = montserrat,
                fontWeight = FontWeight.Bold
            )

            AttemptsIndicator(attemptsLeft = attemptsLeft)

            if (showNextLevelButton) {
                NextLevelButton(onClick = onNextLevel)
            }
        }
    }
}

@Composable
private fun AttemptsIndicator(attemptsLeft: Int) {
    val containerColor = when {
        attemptsLeft > 5 -> Color(0xFF4A6CFF)
        attemptsLeft > 2 -> Color(0xFFFFA726)
        else -> Color.Red.copy(alpha = 0.8f)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = stringResource(R.string.attempts_indicator, attemptsLeft),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            fontFamily = montserrat,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun NextLevelButton(onClick: () -> Unit) {
    FilledTonalButton(
        onClick = onClick,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = Color(0xFF4CAF50)
        )
    ) {
        Text(
            stringResource(R.string.next_level_button),
            style = MaterialTheme.typography.labelLarge,
            color = Color.White,
            fontFamily = montserrat
        )
        Icon(
            Icons.AutoMirrored.Default.ArrowForward,
            contentDescription = stringResource(R.string.next_level_description),
            tint = Color.White,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}