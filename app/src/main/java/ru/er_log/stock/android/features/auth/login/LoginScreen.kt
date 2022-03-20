package ru.er_log.stock.android.features.auth.login

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import ru.er_log.stock.android.R
import ru.er_log.stock.android.base.utils.onlyFalse
import ru.er_log.stock.android.compose.components.*
import ru.er_log.stock.android.compose.theme.StockTheme
import ru.er_log.stock.android.compose.theme.darkColors

@Preview
@Composable
private fun Preview() {
    StockTheme(colors = darkColors()) {
        LoginScreenImpl(
            actionLogin = { _, _ -> },
            actionLoginSuccess = {},
            loginUIState = MutableStateFlow(LoginUIState.Idle),
            actionBackToLogin = {},
            actionDemo = {}
        )
    }
}

@Composable
fun LoginScreen(
    actionLoginSuccess: () -> Unit,
    loginViewModel: LoginViewModel = getViewModel()
) {
    LoginScreenImpl(
        actionLogin = loginViewModel::login,
        actionLoginSuccess = actionLoginSuccess,
        loginUIState = loginViewModel.loginUIState,
        actionBackToLogin = loginViewModel::setInitialUIState,
        actionDemo = loginViewModel::loginDemo
    )
}

@Composable
fun LoginScreenImpl(
    actionLogin: (InputState, InputState) -> Unit,
    actionLoginSuccess: () -> Unit,
    loginUIState: StateFlow<LoginUIState>,
    actionBackToLogin: () -> Unit,
    actionDemo: () -> Unit
) {
    val loginState = rememberSaveable { InputState() }
    val passwordState = rememberSaveable { InputState() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .stockMainBackground()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        LogoBox(Modifier.weight(0.5f))

        Crossfade(
            modifier = Modifier.weight(0.5f),
            targetState = loginUIState.collectAsState().value,
            animationSpec = tween(800)
        ) { state ->
            when (state) {
                LoginUIState.Idle -> InputBox(
                    loginState = loginState,
                    passwordState = passwordState,
                    actionLogin = actionLogin,
                    actionDemo = actionDemo
                )
                LoginUIState.Loading -> Progress()
                is LoginUIState.Result -> Result(
                    result = state,
                    actionBackToLogin = actionBackToLogin,
                    actionLoginSuccess = actionLoginSuccess
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
                                text = stringResource(R.string.auth_login_error_login_failed),
                                fontWeight = FontWeight.Bold
                            )
                        },
                        text = {
                            Text(result.message ?: "")
                        },
                        confirmButton = {
                            StockOutlinedButton(
                                onClick = {
                                    showDialog.value = false
                                    actionBackToLogin()
                                }
                            ) {
                                Text(stringResource(R.string.ok))
                            }
                        },
                        contentColor = StockTheme.colors.textPrimary,
                        backgroundColor = StockTheme.colors.background
                    )
                }
            }
            is LoginUIState.Result.Success -> {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(
                        R.string.auth_login_greetings,
                        result.userInfo.userName
                    ),
                    color = StockTheme.colors.textPrimary,
                    fontSize = StockTheme.typography.h6.fontSize
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
            color = StockTheme.colors.primary,
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
        Image(
            modifier = Modifier.size(180.dp),
            painter = painterResource(logoResId),
            contentDescription = "Logo"
        )
    }
}

@Composable
private fun InputBox(
    modifier: Modifier = Modifier,
    loginState: InputState,
    passwordState: InputState,
    actionLogin: (InputState, InputState) -> Unit,
    actionDemo: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val localFocusManager = LocalFocusManager.current
        val localTextInputService = LocalTextInputService.current
        val localContext = LocalContext.current

        val loginValidator = remember { StockLoginInputValidator() }
        val passwordValidator = remember { StockPasswordInputValidator() }

        val isFieldsOkay = derivedStateOf {
            loginState.hasError.onlyFalse() && passwordState.hasError.onlyFalse()
        }

        StockTextField(
            inputState = loginState,
            inputValidator = loginValidator,
            label = R.string.auth_prompt_username,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { localFocusManager.moveFocus(FocusDirection.Next) }
            )
        )

        Spacer(Modifier.size(16.dp))
        StockTextField(
            inputState = passwordState,
            inputValidator = passwordValidator,
            label = R.string.auth_prompt_password,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onDone = { localFocusManager.clearFocus() }
            )
        )

        Spacer(Modifier.size(32.dp))
        StockButton(
            enabled = isFieldsOkay.value,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                localTextInputService?.hideSoftwareKeyboard()
                actionLogin(loginState, passwordState)
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
            color = StockTheme.colors.textSecondary,
            text = stringResource(R.string.auth_action_forgot_the_password),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.weight(100f, true))
        StockTextButton(
            onClick = actionDemo,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(R.string.auth_action_demo_access))
        }
//        StockOutlinedButton(
//            modifier = Modifier.align(Alignment.End),
//            onClick = actionDemo
//        ) {
//            Text(stringResource(R.string.auth_action_demo_access))
//        }
    }
}
