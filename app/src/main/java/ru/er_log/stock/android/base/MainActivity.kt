package ru.er_log.stock.android.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            val splashScreen = installSplashScreen()

            splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
                // Get icon instance and start a fade out animation
                splashScreenViewProvider.iconView
                    .animate()
                    .setDuration(300L)
                    .alpha(0f)
                    .withEndAction {
                        // After the fade out, remove the splash and set content view
                        splashScreenViewProvider.remove()
                        setContent{
                            StartScreen()
                        }
                    }.start()
            }
        } else {
            setContent { StartScreen() }
        }
    }
}