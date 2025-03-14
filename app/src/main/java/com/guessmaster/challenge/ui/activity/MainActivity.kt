package com.guessmaster.challenge.ui.activity

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.guessmaster.challenge.navigation.Destinations
import com.guessmaster.challenge.ui.screen.GameScreen
import com.guessmaster.challenge.ui.screen.MainScreen
import com.guessmaster.challenge.ui.screen.SettingsScreen
import com.guessmaster.challenge.ui.theme.GuessTheNumberTheme
import com.guessmaster.challenge.viewmodel.GameViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            GuessTheNumberTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Create the NavController for destination navigation
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Destinations.MAIN_SCREEN
                    ) {
                        composable(Destinations.MAIN_SCREEN) {
                            MainScreen(
                                onPlayGame = {
                                    navController.navigate(Destinations.GAME_SCREEN)
                                },
                                onLeaderboard = {
                                    // TODO: Navigate to High Scores screen when implemented
                                },
                                onSettings = {
                                    navController.navigate(Destinations.SETTINGS_SCREEN)
                                }
                            )
                        }
                        composable(Destinations.GAME_SCREEN) {
                            GameScreen(
                                navController,
                                viewModel = viewModel
                            )
                        }

                        composable(Destinations.SETTINGS_SCREEN) {
                            SettingsScreen(
                                navController)
                        }
                    }
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val listener = View.OnApplyWindowInsetsListener { _, insets ->
                val isSystemBarsVisible = insets.isVisible(WindowInsets.Type.systemBars())
                if (isSystemBarsVisible) {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(3000)
                        hideSystemUI()
                    }
                }
                insets
            }
            window.decorView.setOnApplyWindowInsetsListener(listener)
        } else {
            val listener = View.OnSystemUiVisibilityChangeListener { visibility ->
                if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(3000)
                        hideSystemUI()
                    }
                }
            }
            window.decorView.setOnSystemUiVisibilityChangeListener(listener)
        }
        hideSystemUI()
    }

    private fun Activity.hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let {
                it.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                it.hide(WindowInsets.Type.systemBars())
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    // Do not let system steal touches for showing the navigation bar
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            // Hide the nav bar and status bar
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            // Keep the app content behind the bars even if user swipes them up
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            // make navbar translucent - do this already in hideSystemUI() so that the bar
            // is translucent if user swipes it up
            @Suppress("DEPRECATION")
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }
}