package ru.er_log.stock.android.features.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.er_log.stock.android.AppGlobalNavigationDirections
import ru.er_log.stock.android.R
import ru.er_log.stock.android.compose.components.*
import ru.er_log.stock.android.compose.theme.AppTheme
import ru.er_log.stock.android.compose.theme.darkColors
import ru.er_log.stock.android.features.auth.login.model.LoginUIState

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel.setInitialUIState()
    }

    @Preview
    @Composable
    private fun Preview() {
        AppTheme(colors = darkColors()) {
            ScreenLogin()
        }
    }

    @Composable
    private fun ScreenLogin() {
        val loginState = rememberSaveable { InputState() }
        val passwordState = rememberSaveable { InputState() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .appMainBackground()
                .padding(24.dp)

        ) {
            LogoBox(Modifier.weight(0.5f))

            Crossfade(
                modifier = Modifier.weight(0.5f),
                targetState = loginViewModel.loginUIState.collectAsState().value,
                animationSpec = tween(800)
            ) { state ->
                when (state) {
                    LoginUIState.Idle -> InputBox(loginState, passwordState)
                    LoginUIState.Loading -> Progress(result = null)
                    is LoginUIState.Result -> Progress(result = state)
                }
            }
        }
    }

    @Composable
    private fun Progress(
        modifier: Modifier = Modifier,
        result: LoginUIState.Result? = null
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            when (result) {
                is LoginUIState.Result.Failure -> {
                    val showDialog = remember { mutableStateOf(true) }
                    if (showDialog.value) {
                        AlertDialog(
                            onDismissRequest = {},
                            title = {
                                Text(
                                    text = stringResource(R.string.login_failed),
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            text = {
                                Text(result.message ?: "")
                            },
                            confirmButton = {
                                AppOutlinedButton(
                                    onClick = {
                                        showDialog.value = false
                                        loginViewModel.setInitialUIState()
                                    }
                                ) {
                                    Text("Ok")
                                }
                            },
                            contentColor = AppTheme.colors.textPrimary,
                            backgroundColor = AppTheme.colors.background
                        )
                    }
                }
                is LoginUIState.Result.Success -> {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(
                            R.string.auth_login_greetings,
                            result.userProfile.userName
                        ),
                        color = AppTheme.colors.textPrimary,
                        fontSize = AppTheme.typography.h6.fontSize
                    )
                    LaunchedEffect(key1 = loginViewModel) {
                        launch {
                            delay(1500)
                            loginViewModel.successLoginNavigate(findNavController())
                        }
                    }
                }
                null -> {
                    CircularProgressIndicator(
                        modifier = modifier.align(Alignment.Center),
                        color = AppTheme.colors.primary,
                    )
                }
            }
        }
    }

    @Composable
    private fun LogoBox(
        modifier: Modifier = Modifier,
        @DrawableRes logoResId: Int = R.drawable.ic_launcher_foreground
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Image(painter = painterResource(logoResId), contentDescription = "Logo")
        }
    }

    @Composable
    private fun InputBox(
        loginState: InputState,
        passwordState: InputState,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            val localFocusManager = LocalFocusManager.current
            val localTextInputService = LocalTextInputService.current

            val loginValidator = remember { AppLoginInputValidator() }
            val passwordValidator = remember { AppPasswordInputValidator() }

            AppTextField(
                inputState = loginState,
                inputValidator = loginValidator,
                label = R.string.auth_prompt_username,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { localFocusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(Modifier.size(16.dp))
            AppTextField(
                inputState = passwordState,
                inputValidator = passwordValidator,
                label = R.string.auth_prompt_password,
                isPasswordField = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { localFocusManager.clearFocus() }
                )
            )

            Spacer(Modifier.size(32.dp))
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    localTextInputService?.hideSoftwareKeyboard()
                    loginViewModel.login(
                        loginState,
                        loginValidator,
                        passwordState,
                        passwordValidator
                    )
                }
            ) {
                Text(stringResource(R.string.auth_action_sign_in))
            }

            Spacer(Modifier.size(16.dp))
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        Toast
                            .makeText(
                                requireContext(),
                                R.string.auth_action_forgot_the_password_unsupported,
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    },
                color = AppTheme.colors.textSecondary,
                text = stringResource(R.string.auth_action_forgot_the_password),
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    private fun ProgressIndicatorDialog() {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(4.dp))
                    .background(AppTheme.colors.background)
                    .padding(10.dp)
            ) {
                CircularProgressIndicator(color = AppTheme.colors.primary)
            }
        }
    }
}