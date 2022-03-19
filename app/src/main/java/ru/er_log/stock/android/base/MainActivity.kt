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

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            splashViewModel.isLoading.value
        }

        super.onCreate(savedInstanceState)

        setContent {
            StartScreen(
                isLightTheme = splashViewModel.isLightTheme.value
            )
        }
//
//            splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
//                // Get icon instance and start a fade out animation
//                splashScreenViewProvider.iconView
//                    .animate()
//                    .setDuration(300L)
//                    .alpha(0f)
//                    .withEndAction {
//                        // After the fade out, remove the splash and set content view
//                        splashScreenViewProvider.remove()
    }
}