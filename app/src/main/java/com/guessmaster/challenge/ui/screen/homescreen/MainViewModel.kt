package com.guessmaster.challenge.ui.screen.homescreen

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _showLeaderboardButton = MutableStateFlow(false)
    val showLeaderboardButton: StateFlow<Boolean> = _showLeaderboardButton

    fun onPlayGame(navController: NavController) {
        navController.navigate("game")
    }

    fun onPlayGameByLevels(navController: NavController) {
        navController.navigate("gameByLevels")
    }

    fun onSettings(navController: NavController) {
        navController.navigate("settings")
    }

    fun onLeaderboard(navController: NavController) {
//        navController.navigate("leaderboard")
    }
}