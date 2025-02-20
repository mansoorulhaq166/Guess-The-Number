package com.example.guessthenumber.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.guessthenumber.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberEntry(
    enteredNumber: String,
    onNumberClick: (String) -> Unit,
    onDelete: () -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        // Input field (non-editable)
        OutlinedTextField(
            value = enteredNumber.ifEmpty { "Enter Your Guess" },
            onValueChange = {},
            readOnly = true,
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 22.sp, textAlign = TextAlign.Center),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.surface,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(bottom = 12.dp)
        )

        // Number Buttons (1-9, 0)
        val numberButtons = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.padding(4.dp)
        ) {
            items(numberButtons) { number ->
                Button(
                    onClick = { onNumberClick(number.toString()) },
                    modifier = Modifier
                        .padding(6.dp)
                        .size(56.dp)
                        .clip(CircleShape),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = number.toString(), fontSize = 18.sp)
                }
            }
        }

        // Delete & Submit Buttons
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(top = 12.dp)
        ) {
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(52.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_backspace_24),
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(40.dp)
                )
            }

            IconButton(
                onClick = onSubmit,
                modifier = Modifier.size(52.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_check_24),
                    contentDescription = "Submit",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NumberEntryPreview() {
    NumberEntry(
        enteredNumber = "23",
        onNumberClick = {},
        onDelete = {},
        onSubmit = {}
    )
}