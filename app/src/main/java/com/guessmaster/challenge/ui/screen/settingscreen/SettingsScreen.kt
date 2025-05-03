package com.guessmaster.challenge.ui.screen.settingscreen

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.guessmaster.challenge.R
import com.guessmaster.challenge.navigation.Screen
import com.guessmaster.challenge.ui.components.settings.SettingOption
import com.guessmaster.challenge.ui.components.settings.SettingToggle
import com.guessmaster.challenge.ui.theme.montserrat

@Composable
fun SettingsScreen(navController: NavController, viewModel: SettingsViewModel = hiltViewModel()) {
    val isSoundEnabled by viewModel.isSoundEnabled.collectAsState(initial = true)
    val isHapticEnabled by viewModel.isHapticEnabled.collectAsState(initial = true)
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.main_bg),
            contentDescription = stringResource(R.string.background_image_description),
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 16.dp,
                    vertical = 48.dp
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Back Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button_description),
                        tint = Color.White
                    )
                }
                Text(
                    text = stringResource(R.string.settings_title),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontFamily = montserrat,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Settings Container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // Sound Effects Toggle
                SettingToggle(
                    label = stringResource(R.string.sound_effects_label),
                    checked = isSoundEnabled,
                    onCheckedChange = {
                        viewModel.toggleSound(it)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Haptic Feedback Toggle
                SettingToggle(
                    label = stringResource(R.string.haptic_feedback_label),
                    checked = isHapticEnabled,
                    onCheckedChange = {
                        viewModel.toggleHaptic(it)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Change Language Button
                SettingOption(
                    label = stringResource(R.string.change_language_label),
                    icon = Icons.Default.Star,
                    onClick = {
                        navController.navigate(Screen.Languages.route)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Rate App Button
                SettingOption(
                    label = stringResource(R.string.rate_app_label),
                    icon = Icons.Default.Star,
                    onClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            "https://play.google.com/store/apps/details?id=com.guessmaster.challenge".toUri()
                        )
                        context.startActivity(intent)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                val shareMessage = stringResource(R.string.share_app_message)
                val shareVia = stringResource(R.string.share_via)

                // Share App Button
                SettingOption(
                    label = stringResource(R.string.share_app_label),
                    icon = Icons.Default.Share,
                    onClick = {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                shareMessage
                            )
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, shareVia))
                    }
                )
            }
        }
    }
}