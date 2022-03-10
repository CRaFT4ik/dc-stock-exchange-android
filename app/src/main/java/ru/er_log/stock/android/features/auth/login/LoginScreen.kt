package ru.er_log.stock.android.features.auth.login

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import ru.er_log.stock.android.R
import ru.er_log.stock.android.base.utils.Navigator
import ru.er_log.stock.android.compose.components.*
import ru.er_log.stock.android.compose.theme.AppTheme
import ru.er_log.stock.android.compose.theme.darkColors
import ru.er_log.stock.android.features.auth.login.model.LoginUIState

@Preview
@Composable
private fun Preview() {
    AppTheme(colors = darkColors()) {
        ScreenLogin(navigator = Navigator())
    }
}

@Composable
fun ScreenLogin(
    navigator: Navigator,
    loginViewModel: LoginViewModel = getViewModel()
) {
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
                LoginUIState.Idle -> InputBox(
                    loginState = loginState,
                    passwordState = passwordState,
                    actionLogin = loginViewModel::login
                )
                LoginUIState.Loading -> Progress()
                is LoginUIState.Result -> Result(
                    result = state,
                    actionBackToLogin = loginViewModel::setInitialUIState,
                    actionLoginSuccess = { navigator.navigateTo(Navigator.NavTarget.HomeFeed) }
                )
            }
        }
    }
}

@Composable
private fun Result(
    modifier: Modifier = Modifier,
    result: LoginUIState.Result,
    actionBackToLogin: () -> Unit,
    actionLoginSuccess: () -> Unit
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
                                    actionBackToLogin()
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
                LaunchedEffect("login success") {
                    launch {
                        delay(1500)
                        actionLoginSuccess()
                    }
                }
            }
        }
    }
}

@Composable
private fun Progress(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        CircularProgressIndicator(
            modifier = modifier.align(Alignment.Center),
            color = AppTheme.colors.primary,
        )
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
    modifier: Modifier = Modifier,
    loginState: InputState,
    passwordState: InputState,
    actionLogin: (
        Pair<InputState, AppInputValidator>,
        Pair<InputState, AppInputValidator>
    ) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val localFocusManager = LocalFocusManager.current
        val localTextInputService = LocalTextInputService.current
        val localContext = LocalContext.current

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
                actionLogin(loginState to loginValidator, passwordState to passwordValidator)
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
                            localContext,
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
