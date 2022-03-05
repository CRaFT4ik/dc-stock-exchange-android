package ru.er_log.stock.android.features.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.er_log.stock.android.R
import ru.er_log.stock.android.compose.components.AppTextField
import ru.er_log.stock.android.compose.components.appMainBackground
import ru.er_log.stock.android.compose.theme.AppTheme
import ru.er_log.stock.android.compose.theme.darkColors

class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme(colors = darkColors()) { // FIXME: remove colors
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
                .padding(32.dp)
        ) {
            LogoBox(heightFraction = 0.55f)
            InputBox()
        }
    }

    @Composable
    private fun LogoBox(
        modifier: Modifier = Modifier,
        heightFraction: Float,
        @DrawableRes logoResId: Int = R.drawable.ic_launcher_foreground
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(heightFraction),
            contentAlignment = Alignment.Center
        ) {
            Image(painter = painterResource(logoResId), contentDescription = "Logo")
        }
    }

    @Composable
    private fun InputBox(
        modifier: Modifier = Modifier,
    ) {
        Box(
            modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Column {
                val localFocusManager = LocalFocusManager.current

                AppTextField(
                    label = R.string.prompt_username,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { localFocusManager.moveFocus(FocusDirection.Down) }
                    )
                ) {
                    loginViewModel.loginDataChanged(username = it)
                }

                Box(Modifier.height(16.dp))
                AppTextField(
                    label = R.string.prompt_password,
                    isPasswordField = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { localFocusManager.clearFocus() }
                    )
                ) {
                    loginViewModel.loginDataChanged(password = it)
                }

                Box(Modifier.height(16.dp))
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            Toast
                                .makeText(
                                    requireContext(),
                                    R.string.action_forgot_the_password_unsupported,
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        },
                    color = AppTheme.colors.textSecondary,
                    text = stringResource(R.string.action_forgot_the_password),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    @Preview
    @Composable
    private fun Preview() {
        AppTheme(colors = darkColors()) {
            ScreenLogin()
        }
    }
}