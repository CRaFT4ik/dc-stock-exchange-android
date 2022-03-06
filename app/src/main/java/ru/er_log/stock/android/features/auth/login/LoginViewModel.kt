package ru.er_log.stock.android.features.auth.login

import android.content.Context
import android.content.res.Resources
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.er_log.stock.android.R
import ru.er_log.stock.android.base.util.scoped
import ru.er_log.stock.android.compose.components.AppInputValidator
import ru.er_log.stock.android.compose.components.InputState
import ru.er_log.stock.android.features.auth.login.model.LoginFormState
import ru.er_log.stock.android.features.auth.login.model.LoginUIState
import ru.er_log.stock.data.di.inject
import ru.er_log.stock.domain.api.v1.auth.SignInRequest
import ru.er_log.stock.domain.usecases.AuthUseCases
import java.util.regex.Pattern

class LoginViewModel(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _loginUIState = MutableStateFlow<LoginUIState>(LoginUIState.Idle)
    val loginUIState = _loginUIState.asStateFlow()

    fun login(
        username: InputState, usernameValidator: AppInputValidator,
        password: InputState, passwordValidator: AppInputValidator
    ) {
        scoped {
            val context: Context = inject()
            val loginValidate = usernameValidator.validate(username, context)
            val passwordValidate = passwordValidator.validate(password, context)
            if (!loginValidate || !passwordValidate) return@scoped

            _loginUIState.value = LoginUIState.Loading
            val request = SignInRequest(username.input.value, password.input.value)

            authUseCases.signIn(request, viewModelScope) {
                it.onSuccess { v -> _loginUIState.value = LoginUIState.Result.Success(v) }
                it.onFailure { v ->
                    _loginUIState.value = LoginUIState.Result.Failure(v.message)
                }
            }
        }
    }

    fun setInitialUIState() {
        _loginUIState.value = LoginUIState.Idle
    }
}

internal class AppPasswordInputValidator : AppInputValidator() {

    // Minimum eight characters, at least one letter, one number and one special character.
    private val passwordRequiredPattern =
        "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$"

    override fun validateInput(input: String): Boolean {
        return true // Pattern.matches(passwordRequiredPattern, input)
    }

    override fun formErrorMessage(resources: Resources, input: String): String {
        return resources.getString(R.string.auth_register_error_wrong_password)
    }
}

internal class AppLoginInputValidator : AppInputValidator() {

    override fun validateInput(input: String): Boolean {
        return input.length > 3
    }

    override fun formErrorMessage(resources: Resources, input: String): String {
        return resources.getString(R.string.auth_register_error_wrong_login)
    }
}
