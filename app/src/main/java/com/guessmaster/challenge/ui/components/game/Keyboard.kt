package com.guessmaster.challenge.ui.components.game

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guessmaster.challenge.R
import com.guessmaster.challenge.ui.theme.montserrat

@Composable
fun NumberEntry(
    enteredNumber: String,
    onNumberClick: (String) -> Unit,
    onDelete: () -> Unit,
    onSubmit: () -> Unit
) {
    val backgroundColor = Color(0xFF121212) // Deep Space Gray
    val buttonBackground = Color(0xFF1E88E5) // Vibrant Blue
    val deleteBackground = Color(0xFFD32F2F) // Elegant Red
    val submitBackground = Color(0xFF43A047) // Soft Emerald Green
    val inputBackground = Color(0xFF212121) // Muted Graphite

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(4.dp)
    ) {
        // Input field (non-editable)
        OutlinedTextField(
            value = enteredNumber.ifEmpty { stringResource(R.string.number_placeholder) },
            onValueChange = {},
            readOnly = true,
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 26.sp, textAlign = TextAlign.Center,
                fontFamily = montserrat,
                color = Color.White
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = buttonBackground,
                unfocusedBorderColor = Color.Gray,
                focusedContainerColor = inputBackground,
                unfocusedContainerColor = inputBackground
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .padding(start = 10.dp, end = 10.dp, bottom = 8.dp)
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
                    Button(
                        onClick = { onNumberClick(number.toString()) },
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape),
                        colors = ButtonDefaults.buttonColors(containerColor = buttonBackground)
                    ) {
                        Text(
                            text = number.toString(),
                            fontSize = 20.sp,
                            fontFamily = montserrat,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Last row with 0, Delete, and Submit buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { onNumberClick("0") },
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                colors = ButtonDefaults.buttonColors(containerColor = buttonBackground)
            ) {
                Text(text = "0", fontSize = 20.sp, fontFamily = montserrat, color = Color.White)
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(deleteBackground)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_backspace_24),
                    contentDescription = stringResource(R.string.action_delete),
                    tint = Color.White
                )
            }

            IconButton(
                onClick = onSubmit,
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(submitBackground)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_check_24),
                    contentDescription = stringResource(R.string.action_submit),
                    tint = Color.White
                )
            }
        }
    }
}