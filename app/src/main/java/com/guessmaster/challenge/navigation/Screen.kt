package com.guessmaster.challenge.navigation

sealed class Screen(val route: String) {
    data object Main : Screen("main")
    data object Game : Screen("game")
    data object Settings : Screen("settings")
    data object Leaderboard : Screen("leaderboard")
}