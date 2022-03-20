package ru.er_log.stock.android.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import ru.er_log.stock.android.features.splash.SplashViewModel
import ru.er_log.stock.data.di.inject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashViewModel: SplashViewModel = inject()

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                splashViewModel.isLoading.value
            }
            setOnExitAnimationListener { splashScreenViewProvider ->
                // Get icon instance and start a fade out animation
                splashScreenViewProvider.iconView
                    .animate()
                    .setDuration(350L)
                    .alpha(0f)
                    .withEndAction {
                        splashScreenViewProvider.remove()
                    }
            }
        }

        super.onCreate(savedInstanceState)

        setContent {
            StartScreen(
                isLightTheme = splashViewModel.isLightTheme.value
            )
        }
    }
}