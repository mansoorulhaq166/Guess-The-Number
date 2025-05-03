package com.guessmaster.challenge.ui.screen.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.guessmaster.challenge.R
import com.guessmaster.challenge.ui.components.main.GradientButton
import com.guessmaster.challenge.ui.theme.buttonSpacing
import com.guessmaster.challenge.ui.theme.luckiestGuy
import com.guessmaster.challenge.ui.theme.screenPadding

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val showLeaderboardButton by viewModel.showLeaderboardButton.collectAsState()

    MainScreenContent(
        showLeaderboardButton = showLeaderboardButton,
        onPlayGame = { viewModel.onPlayGame(navController) },
        onPlayGameByLevels = { viewModel.onPlayGameByLevels(navController) },
        onSettings = { viewModel.onSettings(navController) },
        onLeaderboard = { viewModel.onLeaderboard(navController) }
    )
}

@Composable
private fun MainScreenContent(
    showLeaderboardButton: Boolean,
    onPlayGame: () -> Unit,
    onPlayGameByLevels: () -> Unit,
    onSettings: () -> Unit,
    onLeaderboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Modern gradient background overlay
        Image(
            painter = painterResource(id = R.drawable.main_bg),
            contentDescription = stringResource(R.string.background_image_desc),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x80111111),
                            Color(0xCC111111)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(screenPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GameTitle()

            Spacer(modifier = Modifier.height(48.dp))

            ButtonsSection(
                showLeaderboardButton = showLeaderboardButton,
                onPlayGame = onPlayGame,
                onPlayGameByLevels = onPlayGameByLevels,
                onSettings = onSettings,
                onLeaderboard = onLeaderboard
            )
        }
    }
}

@Composable
private fun GameTitle() {
    Text(
        text = stringResource(R.string.game_title),
        fontSize = 48.sp,
        color = Color.White,
        textAlign = TextAlign.Center,
        fontFamily = luckiestGuy,
        fontWeight = FontWeight.ExtraBold,
        style = MaterialTheme.typography.displayLarge.copy(
            shadow = Shadow(
                color = Color.Black.copy(alpha = 0.5f),
                offset = Offset(4f, 4f),
                blurRadius = 8f
            ),
            lineHeight = 52.sp
        )
    )
}

@Composable
private fun ButtonsSection(
    showLeaderboardButton: Boolean,
    onPlayGame: () -> Unit,
    onPlayGameByLevels: () -> Unit,
    onSettings: () -> Unit,
    onLeaderboard: () -> Unit
) {
    // Play buttons with vibrant gradients
    GradientButton(
        text = stringResource(R.string.quick_play),
        onClick = onPlayGame,
        gradient = listOf(
            Color(0xFF6A11CB),  // Deep purple
            Color(0xFF2575FC)   // Bright blue
        ),
        modifier = Modifier.fillMaxWidth(0.8f)
    )
    Spacer(modifier = Modifier.height(buttonSpacing))

    GradientButton(
        text = stringResource(R.string.level_challenge),
        onClick = onPlayGameByLevels,
        gradient = listOf(
            Color(0xFF11998E),  // Teal
            Color(0xFF38EF7D)   // Light green
        ),
        modifier = Modifier.fillMaxWidth(0.8f)
    )
    Spacer(modifier = Modifier.height(buttonSpacing))

    // Settings button with warm gradient
    GradientButton(
        text = stringResource(R.string.game_settings),
        onClick = onSettings,
        gradient = listOf(
            Color(0xFFFF512F),  // Orange
            Color(0xFFDD2476)    // Deep pink
        ),
        modifier = Modifier.fillMaxWidth(0.8f)
    )
    Spacer(modifier = Modifier.height(buttonSpacing))

    // Leaderboard button with cool gradient
    if (showLeaderboardButton) {
        GradientButton(
            text = stringResource(R.string.leaderboard),
            onClick = onLeaderboard,
            gradient = listOf(
                Color(0xFF4776E6),  // Blue
                Color(0xFF8E54E9)    // Purple
            ),
            modifier = Modifier.fillMaxWidth(0.8f)
        )
    }
}