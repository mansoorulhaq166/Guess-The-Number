package com.guessmaster.challenge.navigation

sealed class Screen(val route: String) {
    data object Main : Screen("main")
    data object Game : Screen("game")
    data object GameByLevels : Screen("gameByLevels")
    data object Settings : Screen("settings")
    data object Languages : Screen("languages")
    data object Leaderboard : Screen("leaderboard")
}