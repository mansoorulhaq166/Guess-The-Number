package com.guessmaster.challenge.ui.screen.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.guessmaster.challenge.R
import com.guessmaster.challenge.ui.components.main.GradientButton
import com.guessmaster.challenge.ui.theme.luckiestGuy

@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val showLeaderboardButton by viewModel.showLeaderboardButton.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.main_bg),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Guess the \nNumber",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontFamily = luckiestGuy
            )
            Spacer(modifier = Modifier.height(32.dp))

            GradientButton(
                text = "Play",
                onClick = { viewModel.onPlayGame(navController) },
                gradient = listOf(Color(0xFF4A90E2), Color(0xFF7B1FA2))
            )
            Spacer(modifier = Modifier.height(16.dp))

            GradientButton(
                text = "Settings",
                onClick = { viewModel.onSettings(navController) },
                gradient = listOf(Color(0xFFFFA726), Color(0xFFFF7043))
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (showLeaderboardButton) {
                GradientButton(
                    text = "Leaderboard",
                    onClick = { viewModel.onLeaderboard(navController) },
                    gradient = listOf(Color(0xFF42A5F5), Color(0xFF1E88E5))
                )
            }
        }
    }
}