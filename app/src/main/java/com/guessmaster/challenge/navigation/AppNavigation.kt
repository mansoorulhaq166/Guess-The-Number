package com.guessmaster.challenge.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.guessmaster.challenge.ui.screen.levelscreen.GameByLevelScreen
import com.guessmaster.challenge.ui.screen.gamescreen.GameScreen
import com.guessmaster.challenge.ui.screen.homescreen.MainScreen
import com.guessmaster.challenge.ui.screen.language.LanguageSelectionScreen
import com.guessmaster.challenge.ui.screen.settingscreen.SettingsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) {
            MainScreen(navController)
        }
        composable(Screen.Game.route) {
            GameScreen(navController)
        }
        composable(Screen.GameByLevels.route) {
            GameByLevelScreen(navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
        composable(Screen.Languages.route) {
            LanguageSelectionScreen(navController)
        }
    }
}