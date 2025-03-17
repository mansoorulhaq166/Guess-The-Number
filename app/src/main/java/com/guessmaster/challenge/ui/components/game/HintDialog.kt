package com.guessmaster.challenge.ui.components.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.guessmaster.challenge.ui.theme.montserrat

@Composable
fun HintDialog(hint: String, onDismiss: () -> Unit) {
    var isVisible by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 })
    ) {
        AlertDialog(
            onDismissRequest = {
                isVisible = false
                onDismiss()
            },
            title = { Text("Hint", fontSize = 20.sp, fontFamily = montserrat) },
            text = { Text(hint, fontSize = 16.sp, fontFamily = montserrat) },
            confirmButton = {
                Button(
                    onClick = {
                        isVisible = false
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047))
                ) {
                    Text("Got it", color = Color.White)
                }
            },
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}