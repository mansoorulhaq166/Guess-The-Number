package com.example.guessthenumber.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onPlayGame: () -> Unit,
    onHighScores: () -> Unit,
    onSettings: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Guess The Number") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Button to start the game
            Button(
                onClick = onPlayGame,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Play Game")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Button to view high scores
            Button(
                onClick = onHighScores,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("High Scores")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Button to access settings
            Button(
                onClick = onSettings,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Settings")
            }
        }
    }
}