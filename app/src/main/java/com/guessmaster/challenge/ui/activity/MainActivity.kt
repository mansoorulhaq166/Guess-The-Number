package com.guessmaster.challenge.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.guessmaster.challenge.navigation.Destinations
import com.guessmaster.challenge.ui.screen.GameScreen
import com.guessmaster.challenge.ui.screen.MainScreen
import com.guessmaster.challenge.ui.theme.GuessTheNumberTheme
import com.guessmaster.challenge.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    // Obtain an instance of GameViewModel
    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
                                    // TODO: Navigate to Settings screen when implemented
                                }
                            )
                        }
                        composable(Destinations.GAME_SCREEN) {
                            GameScreen(
                                navController,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}