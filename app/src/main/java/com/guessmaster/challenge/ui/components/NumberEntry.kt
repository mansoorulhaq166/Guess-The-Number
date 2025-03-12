package com.guessmaster.challenge.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guessmaster.challenge.R
import com.guessmaster.challenge.ui.theme.bungee
import com.guessmaster.challenge.ui.theme.luckiestGuy

@Composable
fun NumberEntry(
    enteredNumber: String,
    onNumberClick: (String) -> Unit,
    onDelete: () -> Unit,
    onSubmit: () -> Unit
) {

    val backgroundColor = Color(0xFF0B0D2E) // Main Screen Dark Background
    val buttonBackground = Color(0xFF1A1C48) // Dark Blue Gray
    val deleteBackground = Color(0xFFFF8000) // Soft Dark Red
    val inputBackground = Color(0xFF14163A) // Slightly Lighter Deep Blue

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(top = 4.dp, start = 16.dp, end = 16.dp, bottom = 4.dp)
    ) {
        // Input field (non-editable)
        OutlinedTextField(
            value = enteredNumber.ifEmpty { "?" },
            onValueChange = {},
            readOnly = true,
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 26.sp, textAlign = TextAlign.Center,
                fontFamily = bungee
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(inputBackground, shape = MaterialTheme.shapes.medium)
                .padding(bottom = 8.dp)
        )

        val numberButtons = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)

        // Create 3x3 grid with numbers (1-9)
        for (i in numberButtons.chunked(3)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                i.forEach { number ->
                    TextButton(
                        onClick = { onNumberClick(number.toString()) },
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(buttonBackground),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = number.toString(), fontSize = 22.sp, fontFamily = luckiestGuy)
                    }
                }
            }
        }

        // Last row with 0, Delete, and Submit buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(
                onClick = { onNumberClick("0") },
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(buttonBackground),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.White
                )
            ) {
                Text(text = "0", fontSize = 22.sp, fontFamily = luckiestGuy)
            }

            FilledIconButton(
                onClick = onDelete,
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = deleteBackground),
                modifier = Modifier.size(54.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_backspace_24),
                    contentDescription = "Delete",
                    tint = Color.Black
                )
            }

            FilledIconButton(
                onClick = onSubmit,
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color(0xFF7CFC00)), // Light green
                modifier = Modifier.size(54.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_check_24),
                    contentDescription = "Submit",
                    tint = Color.Black
                )
            }
        }
    }
}