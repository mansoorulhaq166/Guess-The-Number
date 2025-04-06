package com.guessmaster.challenge.ui.components.game

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ExitDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmExit: () -> Unit
) {
    if (!showDialog) return

    // Color palette
    val primaryColor = Color(0xFF6200EA)    // Deep purple
    val warningColor = Color(0xFFFF3D00)    // Bright red-orange
    val lightPurple = Color(0xFFB388FF)     // Light purple
    val darkPurple = Color(0xFF4A148C)      // Dark purple
    val textPrimary = Color.White
    val textSecondary = Color.White.copy(alpha = 0.7f)

    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    // Animation states
    val dialogAnimator = remember { Animatable(0f) }
    val buttonScale = remember { Animatable(0.8f) }
    val rotationX = remember { Animatable(20f) }
    val rotationY = remember { Animatable(-15f) }

    // Handle animations
    LaunchedEffect(true) {
        delay(50)
        dialogAnimator.animateTo(
            1f,
            tween(500, easing = CubicBezierEasing(0.34f, 1.56f, 0.64f, 1f))
        )
        rotationX.animateTo(0f, tween(700, easing = CubicBezierEasing(0f, 0.55f, 0.45f, 1f)))
        rotationY.animateTo(0f, tween(700, easing = CubicBezierEasing(0f, 0.55f, 0.45f, 1f)))
        delay(100)
        buttonScale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
    }

    fun playExitAnimation(onComplete: () -> Unit) {
        coroutineScope.launch {
            rotationX.animateTo(15f, tween(300))
            dialogAnimator.animateTo(0f, tween(300))
            delay(200)
            onComplete()
        }
    }

    Dialog(
        onDismissRequest = { playExitAnimation(onDismissRequest) },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .width(320.dp)
                    .padding(16.dp)
                    .graphicsLayer {
                        scaleX = dialogAnimator.value
                        scaleY = dialogAnimator.value
                        alpha = dialogAnimator.value
                        shadowElevation = 25f
                    }
                    .clickable(enabled = false) { },
                shape = RoundedCornerShape(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(primaryColor, darkPurple)
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(80.dp)
                                .background(
                                    Brush.radialGradient(
                                        listOf(
                                            warningColor.copy(alpha = 0.2f),
                                            Color.Transparent
                                        )
                                    ), CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Warning",
                                modifier = Modifier.size(48.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Exit Game?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = textPrimary,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Your progress will be lost. Are you sure you want to exit?",
                            color = textSecondary,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .alpha(dialogAnimator.value)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Cancel button
                            OutlinedButton(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    playExitAnimation(onDismissRequest)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                                    .graphicsLayer {
                                        scaleX = buttonScale.value
                                        scaleY = buttonScale.value
                                    },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    "Cancel",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp,
                                    color = lightPurple
                                )
                            }

                            // Exit button
                            Button(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    playExitAnimation(onConfirmExit)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                                    .graphicsLayer {
                                        scaleX = buttonScale.value
                                        scaleY = buttonScale.value
                                    },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                            contentDescription = null,
                                            tint = textPrimary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            "Exit",
                                            color = textPrimary,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}