package ru.er_log.stock.android.features.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.er_log.stock.android.compose.theme.AppTheme
import ru.er_log.stock.android.compose.theme.darkColors

class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModel()
    private val isDarkTheme = true // todo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    ScreenLogin()
                }
            }
        }
    }

    @Composable
    private fun ScreenLogin() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .appMainBackground()
        ) {
            Text(text = "Hello")
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Hello button")
            }
        }
    }

    private fun Modifier.appMainBackground() = composed {
        this.background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    AppTheme.colors.background,
                    AppTheme.colors.backgroundSecondary
                )
            )
        )
    }

    @Preview
    @Composable
    private fun Preview() {
        AppTheme(colors = darkColors()) {
            ScreenLogin()
        }
    }
}