package com.guessmaster.challenge.ui.components.main

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guessmaster.challenge.ui.theme.bungee

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    gradient: List<Color>,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(36.dp),
    elevation: Dp = 12.dp,
    pressedElevation: Dp = 4.dp,
    scaleDownFactor: Float = 0.95f,
    textStyle: TextStyle = TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = bungee
    )
) {
    var isPressed by remember { mutableStateOf(false) }

    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) scaleDownFactor else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "button_scale_animation"
    )

    val animatedElevation by animateDpAsState(
        targetValue = if (isPressed) pressedElevation else elevation,
        animationSpec = tween(durationMillis = 150),
        label = "button_elevation_animation"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth(0.7f)
            .height(64.dp)
            .shadow(
                elevation = animatedElevation,
                shape = shape,
                ambientColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                spotColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                clip = true
            )
            .clip(shape)
            .background(
                brush = Brush.linearGradient(
                    colors = gradient,
                    start = Offset(0f, 0f),
                    end = Offset(100f, 100f), // Fixed values work well for button size
                    tileMode = TileMode.Mirror
                )
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                        onClick()
                    }
                )
            }
            .scale(animatedScale)
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = text,
            style = textStyle,
            color = Color.White,
            modifier = Modifier.padding(vertical = 8.dp),
            maxLines = 1
        )
    }
}