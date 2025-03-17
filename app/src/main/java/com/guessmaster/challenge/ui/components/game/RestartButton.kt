package com.guessmaster.challenge.ui.components.game

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guessmaster.challenge.ui.theme.montserrat

@Composable
fun RestartButton(onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val isHovered by remember { mutableStateOf(false) }

    // Animation values
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else if (isHovered) 1.03f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "buttonScale"
    )

    val shadowElevation by animateFloatAsState(
        targetValue = if (isPressed) 2f else if (isHovered) 16f else 8f,
        animationSpec = tween(durationMillis = 200),
        label = "shadowElevation"
    )

    val buttonRotation by animateFloatAsState(
        targetValue = if (isPressed) -5f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "buttonRotation"
    )

    val iconRotation by animateFloatAsState(
        targetValue = if (isHovered) 360f else 0f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "iconRotation"
    )

    // Color gradient for modern look
    val gradientColors = if (isPressed) {
        listOf(Color(0xFF1565C0), Color(0xFF0D47A1))
    } else {
        listOf(Color(0xFF2979FF), Color(0xFF2196F3), Color(0xFF1976D2))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(horizontal = 24.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        // 3D Shadow Layer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .offset(y = 6.dp)
                .shadow(
                    elevation = 0.dp,
                    shape = RoundedCornerShape(20.dp),
                    clip = false
                )
                .background(
                    color = Color(0xFF0D47A1).copy(alpha = 0.3f),
                    shape = RoundedCornerShape(20.dp)
                )
        )

        // Main Button with 3D effect
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .scale(scale)
                .shadow(
                    elevation = shadowElevation.dp,
                    shape = RoundedCornerShape(20.dp),
                    spotColor = Color(0xFF0D47A1),
                    ambientColor = Color(0xFF42A5F5)
                )
                .clip(RoundedCornerShape(20.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = gradientColors,
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF64B5F6).copy(alpha = 0.8f),
                            Color(0xFF1565C0).copy(alpha = 0.5f)
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .clickable {
                    isPressed = true
                    onClick()
                },
//                .hoverable(
//                    onEnter = { isHovered = true; false },
//                    onExit = { isHovered = false; false }
//                ),
            contentAlignment = Alignment.Center
        ) {
            // Inner highlight for 3D effect
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp)
                    .align(Alignment.TopCenter)
                    .clip(
                        RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp,
                            bottomStart = 40.dp,
                            bottomEnd = 40.dp
                        )
                    )
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.4f),
                                Color.White.copy(alpha = 0.0f)
                            )
                        )
                    )
            )

            // Button content
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = CenterVertically
            ) {
                // Rotating icon
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .graphicsLayer {
                            rotationZ = iconRotation
                        }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = "Restart",
                        tint = Color.White,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Text with 3D effect
                Text(
                    text = "Restart Game",
                    style = TextStyle(
                        fontFamily = montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        letterSpacing = 0.5.sp,
                        shadow = Shadow(
                            color = Color(0xFF0D47A1),
                            offset = Offset(1f, 1f),
                            blurRadius = 2f
                        )
                    ),
                    color = Color.White
                )
            }
        }

        // Button press animation effect
        if (isPressed) {
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(200)
                isPressed = false
            }
        }
    }
}