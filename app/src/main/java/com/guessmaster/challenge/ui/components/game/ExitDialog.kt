package com.guessmaster.challenge.ui.components.game

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
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

// Custom easing curves
private val EaseOutCirc = CubicBezierEasing(0f, 0.55f, 0.45f, 1f)
private val EaseInCirc = CubicBezierEasing(0.55f, 0f, 1f, 0.45f)
private val EaseOutBack = CubicBezierEasing(0.34f, 1.56f, 0.64f, 1f)

@Composable
fun ExitDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmExit: () -> Unit
) {
    if (!showDialog) return

    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    // Animation states
    var showDialogContent by remember { mutableStateOf(false) }
    var exitAnimationPlayed by remember { mutableStateOf(false) }
    val dialogAnimator = remember { Animatable(0f) }
    val buttonScale = remember { Animatable(0.8f) }
    val rotationX = remember { Animatable(20f) }
    val rotationY = remember { Animatable(-15f) }

    // Handle entry animations
    LaunchedEffect(showDialog) {
        delay(100)
        dialogAnimator.animateTo(1f, tween(500, easing = EaseOutBack))
        showDialogContent = true

        // Animate rotation and button
        rotationX.animateTo(0f, tween(700, easing = EaseOutCirc))
        rotationY.animateTo(0f, tween(700, easing = EaseOutCirc))

        delay(200)
        buttonScale.animateTo(
            1f,
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
    }

    // Backdrop animation
    val backdropAlpha by animateFloatAsState(
        targetValue = if (showDialogContent) 0.9f else 0f,
        animationSpec = tween(500)
    )

    // Handle exit animation
    fun playExitAnimation(onComplete: () -> Unit) {
        coroutineScope.launch {
            exitAnimationPlayed = true
            rotationX.animateTo(15f, tween(300, easing = EaseInCirc))
            dialogAnimator.animateTo(0f, tween(300, easing = EaseInCirc))
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
        // Backdrop
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = backdropAlpha * 0.8f),
                            Color.Black.copy(alpha = backdropAlpha)
                        )
                    )
                )
                .blur(20.dp)
                .clickable { playExitAnimation(onDismissRequest) },
            contentAlignment = Alignment.Center
        ) {
            // Dialog card
            Card(
                modifier = Modifier
                    .width(320.dp)
                    .padding(16.dp)
                    .graphicsLayer {
                        scaleX = dialogAnimator.value
                        scaleY = dialogAnimator.value
                        alpha = dialogAnimator.value
                        transformOrigin = TransformOrigin(0.5f, 0.5f)
                        shadowElevation = 25f
                    }
                    .clickable(enabled = false) { },
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Warning icon
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        Color(0xFFFF5252).copy(alpha = 0.2f),
                                        Color.Transparent
                                    )
                                ),
                                CircleShape
                            )
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Warning",
                            tint = Color(0xFFFF5252),
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Dialog title
                    Text(
                        text = "Exit Game?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Dialog message
                    Text(
                        text = "Your progress will be lost. Are you sure you want to exit?",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .alpha(dialogAnimator.value)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Buttons
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
                            shape = RoundedCornerShape(12.dp),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = Brush.horizontalGradient(
                                    listOf(
                                        Color.White.copy(alpha = 0.3f),
                                        Color.White.copy(alpha = 0.1f)
                                    )
                                )
                            )
                        ) {
                            Text(
                                "Cancel",
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
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
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(0.dp),
                            elevation = ButtonDefaults.elevatedButtonElevation(
                                defaultElevation = 0.dp,
                                pressedElevation = 0.dp
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(
                                                Color(0xFFFF5252),
                                                Color(0xFFFF1744)
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Exit",
                                        color = Color.White,
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