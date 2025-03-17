package com.guessmaster.challenge.ui.components.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guessmaster.challenge.ui.theme.DropdownBackgroundColor
import com.guessmaster.challenge.ui.theme.DropdownItemColor
import com.guessmaster.challenge.ui.theme.PrimaryTextColor
import com.guessmaster.challenge.ui.theme.TextHighlightColor
import com.guessmaster.challenge.ui.theme.montserrat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DifficultyDropdown(
    selectedDifficulty: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onSelect: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        Text(
            text = "Select Difficulty",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = montserrat,
            color = PrimaryTextColor, // ✅ Custom color
            modifier = Modifier.padding(bottom = 8.dp)
        )
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = onExpandedChange) {
            OutlinedTextField(
                value = selectedDifficulty,
                onValueChange = {},
                readOnly = true,
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp, color = TextHighlightColor), // ✅ Highlight color
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .background(DropdownBackgroundColor) // ✅ Custom background
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) },
                modifier = Modifier.background(DropdownBackgroundColor) // ✅ Custom background
            ) {
                listOf("Easy", "Medium", "Hard").forEach { difficulty ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                difficulty,
                                fontFamily = montserrat,
                                color = DropdownItemColor, // ✅ Custom text color
                                fontSize = 16.sp
                            )
                        },
                        onClick = { onSelect(difficulty) },
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}