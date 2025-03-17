package com.guessmaster.challenge

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
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
import com.guessmaster.challenge.ui.screen.gamescreen.GameViewModel
import com.guessmaster.challenge.navigation.AppNavigation
import com.guessmaster.challenge.ui.screen.gamescreen.GameScreen
import com.guessmaster.challenge.ui.screen.homescreen.MainScreen
import com.guessmaster.challenge.ui.screen.settingscreen.SettingsScreen
import com.guessmaster.challenge.ui.theme.GuessTheNumberTheme
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
                    AppNavigation()
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
        }
//        else {
//            val listener = View.OnSystemUiVisibilityChangeListener { visibility ->
//                if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
//                    CoroutineScope(Dispatchers.Main).launch {
//                        delay(3000)
//                        hideSystemUI()
//                    }
//                }
//            }
//            window.decorView.setOnSystemUiVisibilityChangeListener(listener)
//        }
        hideSystemUI()
    }

    private fun Activity.hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let {
                it.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                it.hide(WindowInsets.Type.systemBars())
            }
        }
//        else {
//            window.decorView.systemUiVisibility = (
//                    View.SYSTEM_UI_FLAG_IMMERSIVE or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            or View.SYSTEM_UI_FLAG_FULLSCREEN
//                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
//            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        }
    }
}