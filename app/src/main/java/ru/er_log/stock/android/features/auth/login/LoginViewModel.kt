package ru.er_log.stock.android.features.auth.login

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.er_log.stock.android.R
import ru.er_log.stock.android.base.utils.scoped
import ru.er_log.stock.android.compose.components.AppInputValidator
import ru.er_log.stock.android.compose.components.InputState
import ru.er_log.stock.android.features.auth.login.model.LoginUIState
import ru.er_log.stock.data.di.inject
import ru.er_log.stock.domain.api.v1.auth.SignInRequest
import ru.er_log.stock.domain.usecases.AuthUseCases

class LoginViewModel(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _loginUIState = MutableStateFlow<LoginUIState>(LoginUIState.Idle)
    val loginUIState = _loginUIState.asStateFlow()

    fun login(usernameState: InputState, passwordState: InputState) {
        val username = usernameState.input.value
        val password = passwordState.input.value

        scoped {
            _loginUIState.value = LoginUIState.Loading
            val request = SignInRequest(username, password)

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

    override fun validateInput(input: String, resources: Resources): String? {
        // resources.getString(R.string.auth_register_error_wrong_password)
        return null // Pattern.matches(passwordRequiredPattern, input)
    }
}

internal class AppLoginInputValidator : AppInputValidator() {

    override fun validateInput(input: String, resources: Resources): String? {
        if (input.length <= 3) {
            return resources.getString(R.string.auth_register_error_wrong_login)
        }
        return null
    }
}
