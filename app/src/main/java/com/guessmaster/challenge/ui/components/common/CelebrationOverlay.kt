package com.guessmaster.challenge.ui.components.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun ModernCelebrationOverlay(
    modifier: Modifier = Modifier,
    particleCount: Int = 150,
    duration: Int = 4000,
    isActive: Boolean = true
) {
    val confettiColors = remember {
        listOf(
            Color(0xFFFFA726), // Orange
            Color(0xFF42A5F5), // Blue
            Color(0xFFFF7043), // Deep Orange
            Color(0xFF66BB6A), // Green
            Color(0xFFAB47BC), // Purple
            Color(0xFFFFEE58), // Yellow
            Color(0xFF26C6DA), // Cyan
            Color(0xFFEC407A)  // Pink
        )
    }

    val glowColors = remember {
        listOf(
            Color(0xFFFFD700), // Gold
            Color(0xFFE91E63), // Pink
            Color(0xFF2196F3), // Blue
            Color(0xFF00BCD4), // Cyan
            Color(0xFF4CAF50), // Green
            Color(0xFFFF9800)  // Orange
        )
    }

    // Main animation state
    val animationProgress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    // Particles state
    val confetti = remember {
        List(particleCount) {
            Confetti(
                colorIndex = Random.nextInt(confettiColors.size),
                startPosition = Offset(
                    x = Random.nextFloat() * 1000,
                    y = -Random.nextFloat() * 500 - 100
                )
            )
        }
    }

    val streamers = remember {
        List(20) {
            Streamer(
                color = confettiColors[Random.nextInt(confettiColors.size)],
                startAngle = Random.nextFloat() * 360f
            )
        }
    }

    val sparkles = remember {
        List(40) {
            Sparkle(
                color = glowColors[Random.nextInt(glowColors.size)],
                startPosition = Offset(
                    x = Random.nextFloat() * 1200 - 100,
                    y = Random.nextFloat() * 1200 - 100
                ),
                delay = Random.nextInt(duration / 2)
            )
        }
    }

    val fireworks = remember {
        List(8) {
            Firework(
                color = glowColors[Random.nextInt(glowColors.size)],
                particlesCount = Random.nextInt(15, 30),
                startPosition = Offset(
                    x = Random.nextFloat() * 1000,
                    y = Random.nextFloat() * 600 + 400
                ),
                delay = Random.nextInt(duration - 500)
            )
        }
    }

    // Reset and start animation when isActive changes
    LaunchedEffect(isActive) {
        if (isActive) {
            animationProgress.snapTo(0f)
            scope.launch {
                animationProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = duration, easing = LinearEasing)
                )
            }
        }
    }

    if (isActive) {
        Box(modifier = modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val currentProgress = animationProgress.value

                // Draw fireworks
                fireworks.forEach { firework ->
                    if (currentProgress * duration > firework.delay) {
                        val fireworkProgress = ((currentProgress * duration) - firework.delay) / 1000f
                        if (fireworkProgress <= 1.5f) {
                            drawFirework(firework, fireworkProgress)
                        }
                    }
                }

                // Draw confetti
                confetti.forEach { particle ->
                    drawConfetti(
                        particle = particle,
                        progress = currentProgress,
                        duration = duration,
                        colors = confettiColors
                    )
                }

                // Draw streamers
                streamers.forEach { streamer ->
                    drawStreamer(
                        streamer = streamer,
                        progress = currentProgress
                    )
                }

                // Draw sparkles
                sparkles.forEach { sparkle ->
                    if (currentProgress * duration > sparkle.delay) {
                        val sparkleProgress = ((currentProgress * duration) - sparkle.delay) / (duration - sparkle.delay).toFloat()
                        drawSparkle(sparkle, sparkleProgress)
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawConfetti(
    particle: Confetti,
    progress: Float,
    duration: Int,
    colors: List<Color>
) {
    val gravity = 600f  // pixels per second²
    val windFactor = sin(progress * 10f) * 20f

    val time = progress * (duration / 1000f)
    val x = particle.startPosition.x + (particle.velocity.x * time) + (windFactor * time)
    val y = particle.startPosition.y + (particle.velocity.y * time) + (0.5f * gravity * time * time)

    val rotation = particle.rotation + (particle.rotationSpeed * time * 360f)
    val alpha = 1f - min(1f, max(0f, progress * 1.5f - 0.5f))

    translate(x, y) {
        rotate(rotation) {
            scale(particle.scale) {
                when (particle.shape) {
                    ConfettiShape.RECTANGLE -> {
                        drawRect(
                            color = colors[particle.colorIndex],
                            topLeft = Offset(-5f, -2f),
                            size = particle.size,
                            alpha = alpha
                        )
                    }
                    ConfettiShape.CIRCLE -> {
                        drawCircle(
                            color = colors[particle.colorIndex],
                            radius = particle.size.width / 2,
                            alpha = alpha
                        )
                    }
                    ConfettiShape.STAR -> {
                        drawStar(
                            color = colors[particle.colorIndex],
                            radius = particle.size.width / 2,
                            alpha = alpha
                        )
                    }
                    ConfettiShape.RIBBON -> {
                        drawPath(
                            path = createRibbonPath(particle.size.width, particle.size.height),
                            color = colors[particle.colorIndex],
                            alpha = alpha
                        )
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawStreamer(streamer: Streamer, progress: Float) {
    val segments = 15
    val finalAlpha = 1f - progress * 2f

    if (finalAlpha <= 0) return

    val pathEffect = PathEffect.dashPathEffect(
        intervals = floatArrayOf(8f, 4f),
        phase = progress * 40f
    )

    val path = Path().apply {
        moveTo(center.x, center.y)
        for (i in 1..segments) {
            val segmentProgress = i.toFloat() / segments
            val length = 400f * segmentProgress * (1f - progress * 0.6f)
            val angle = streamer.startAngle + sin(progress * 8f + segmentProgress * 10f) * 30f
            val x = center.x + cos(Math.toRadians(angle.toDouble()).toFloat()) * length
            val y = center.y + sin(Math.toRadians(angle.toDouble()).toFloat()) * length
            lineTo(x, y)
        }
    }

    drawPath(
        path = path,
        color = streamer.color,
        alpha = finalAlpha * 0.7f,
        style = Stroke(
            width = 3f,
            pathEffect = pathEffect
        )
    )
}

private fun DrawScope.drawSparkle(sparkle: Sparkle, progress: Float) {
    val pulseScale = 0.7f + sin(progress * Math.PI.toFloat() * 4) * 0.3f
    val alpha = sin(progress * Math.PI.toFloat())

    translate(sparkle.startPosition.x, sparkle.startPosition.y) {
        scale(pulseScale) {
            for (i in 0 until 4) {
                rotate(45f * i) {
                    drawLine(
                        color = sparkle.color,
                        start = Offset(-10f, 0f),
                        end = Offset(10f, 0f),
                        strokeWidth = 2f,
                        alpha = alpha
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawFirework(firework: Firework, progress: Float) {
    if (progress < 0.2f) {
        // Draw launch trail
        val trailPath = Path().apply {
            moveTo(firework.startPosition.x, size.height)
            val controlPoint1 = Offset(
                firework.startPosition.x - 20f,
                firework.startPosition.y + size.height / 2
            )
            val controlPoint2 = Offset(
                firework.startPosition.x + 20f,
                firework.startPosition.y
            )
            cubicTo(
                controlPoint1.x, controlPoint1.y,
                controlPoint2.x, controlPoint2.y,
                firework.startPosition.x, firework.startPosition.y
            )
        }

        drawPath(
            path = trailPath,
            color = firework.color.copy(alpha = 0.6f),
            style = Stroke(
                width = 2f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f))
            )
        )

        // Draw rocket
        drawCircle(
            color = firework.color,
            radius = 4f,
            center = firework.startPosition,
            alpha = (0.2f - progress) / 0.2f
        )
    } else {
        // Explosion phase
        val explosionProgress = (progress - 0.2f) / 0.8f
        val explosionRadius = 150f * min(1f, explosionProgress * 2f)
        val fadeOut = 1f - max(0f, (explosionProgress - 0.5f) * 2f)

        // Central glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    firework.color.copy(alpha = 0.4f * fadeOut),
                    firework.color.copy(alpha = 0f)
                ),
                center = Offset.Zero,
                radius = explosionRadius * 1.2f
            ),
            radius = explosionRadius,
            center = firework.startPosition
        )

        // Particles
        for (i in 0 until firework.particlesCount) {
            val angle = (i.toFloat() / firework.particlesCount) * 360f
            val particleDistance = explosionRadius * (0.3f + Random.nextFloat() * 0.7f)
            val x = firework.startPosition.x + cos(Math.toRadians(angle.toDouble()).toFloat()) * particleDistance
            val y = firework.startPosition.y + sin(Math.toRadians(angle.toDouble()).toFloat()) * particleDistance

            val particleSize = 4f * (1f - explosionProgress * 0.7f)

            drawCircle(
                color = firework.color,
                radius = particleSize,
                center = Offset(x, y),
                alpha = fadeOut
            )

            // Trailing effect
            val trailLength = 10f * (1f - explosionProgress)
            val trailPath = Path().apply {
                moveTo(x, y)
                val trailX = x - cos(Math.toRadians(angle.toDouble()).toFloat()) * trailLength
                val trailY = y - sin(Math.toRadians(angle.toDouble()).toFloat()) * trailLength
                lineTo(trailX, trailY)
            }

            drawPath(
                path = trailPath,
                color = firework.color,
                style = Stroke(width = particleSize * 0.8f),
                alpha = fadeOut * 0.5f
            )
        }
    }
}

private fun DrawScope.drawStar(color: Color, radius: Float, alpha: Float) {
    val path = Path()
    val outerRadius = radius
    val innerRadius = radius * 0.4f
    val numPoints = 5

    for (i in 0 until numPoints * 2) {
        val r = if (i % 2 == 0) outerRadius else innerRadius
        val angle = Math.PI * i / numPoints
        val x = cos(angle).toFloat() * r
        val y = sin(angle).toFloat() * r

        if (i == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }
    path.close()

    drawPath(
        path = path,
        color = color,
        alpha = alpha
    )
}

private fun createRibbonPath(width: Float, height: Float): Path {
    return Path().apply {
        val halfWidth = width / 2
        val halfHeight = height / 2

        moveTo(-halfWidth, -halfHeight)
        quadraticBezierTo(0f, 0f, halfWidth, -halfHeight)
        lineTo(halfWidth, halfHeight)
        quadraticBezierTo(0f, 0f, -halfWidth, halfHeight)
        close()
    }
}

private enum class ConfettiShape {
    RECTANGLE, CIRCLE, STAR, RIBBON
}

private class Confetti(
    val colorIndex: Int,
    val startPosition: Offset,
    val shape: ConfettiShape = ConfettiShape.values()[Random.nextInt(ConfettiShape.values().size)]
) {
    val size = androidx.compose.ui.geometry.Size(
        width = Random.nextFloat() * 10f + 5f,
        height = Random.nextFloat() * 5f + 3f
    )
    val velocity = Offset(
        x = (Random.nextFloat() - 0.5f) * 300f,
        y = Random.nextFloat() * -200f - 100f
    )
    val rotation = Random.nextFloat() * 360f
    val rotationSpeed = Random.nextFloat() * 2f - 1f
    val scale = Random.nextFloat() * 0.5f + 0.8f
}

private class Streamer(
    val color: Color,
    val startAngle: Float
)

private class Sparkle(
    val color: Color,
    val startPosition: Offset,
    val delay: Int
)

private class Firework(
    val color: Color,
    val particlesCount: Int,
    val startPosition: Offset,
    val delay: Int
)