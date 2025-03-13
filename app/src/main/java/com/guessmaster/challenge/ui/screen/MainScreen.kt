package com.guessmaster.challenge.ui.screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guessmaster.challenge.R
import com.guessmaster.challenge.ui.theme.bungee
import com.guessmaster.challenge.ui.theme.luckiestGuy

@Composable
fun MainScreen(
    onPlayGame: () -> Unit,
    onSettings: () -> Unit,
    onLeaderboard: () -> Unit
) {
    val showSettingsButton by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
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
                onClick = onPlayGame,
                gradient = listOf(Color(0xFF4A90E2), Color(0xFF7B1FA2))
            )
            Spacer(modifier = Modifier.height(16.dp))
            GradientButton(
                text = "Settings",
                onClick = onSettings,
                gradient = listOf(Color(0xFFFFA726), Color(0xFFFF7043))
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (showSettingsButton) {
                GradientButton(
                    text = "Leaderboard",
                    onClick = onLeaderboard,
                    gradient = listOf(Color(0xFF42A5F5), Color(0xFF1E88E5))
                )
            }
        }
    }
}

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    gradient: List<Color>
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "button_press_animation"
    )

    val elevation by animateDpAsState(
        targetValue = if (isPressed) 4.dp else 12.dp, // Changes depth on press
        animationSpec = tween(durationMillis = 100),
        label = "button_elevation_animation"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .height(60.dp)
            .scale(scale) // Press effect
            .clip(RoundedCornerShape(36.dp))
            .shadow(
                elevation = elevation, // Dynamic shadow effect
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = 0.5f),
                spotColor = Color.Black.copy(alpha = 0.3f)
            )
            .background(
                brush = Brush.verticalGradient(gradient), // Glossy gradient
                shape = RoundedCornerShape(36.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                        onClick() // Ensure navigation works
                    }
                )
            }
    ) {
        Text(
            text = text,
            fontSize = 22.sp, // Bigger for emphasis
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontFamily = bungee,
            style = TextStyle( // Correct way to apply shadow
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.3f),
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            )
        )
    }
}