package ru.er_log.stock.android.features.auth.login

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.er_log.stock.android.R
import ru.er_log.stock.android.base.utils.scoped
import ru.er_log.stock.android.compose.components.InputState
import ru.er_log.stock.android.compose.components.StockInputValidator
import ru.er_log.stock.domain.models.`in`.UserInfo
import ru.er_log.stock.domain.models.out.SignInRequest
import ru.er_log.stock.domain.usecases.AuthUseCases

class LoginViewModel(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _loginUIState = MutableStateFlow<LoginUIState>(LoginUIState.Loading)
    val loginUIState = _loginUIState.observeLoginState().asStateFlow()

    fun login(usernameState: InputState, passwordState: InputState) {
        val username = usernameState.input.value
        val password = passwordState.input.value

        scoped {
            _loginUIState.value = LoginUIState.Loading
            val request = SignInRequest(username, password)

            authUseCases.signIn(request, viewModelScope) {
                it.onSuccess { v -> _loginUIState.value = LoginUIState.Result.Success(v) }
                it.onFailure { v -> _loginUIState.value = LoginUIState.Result.Failure(v.message) }
            }
        }
    }

    fun setInitialUIState() {
        _loginUIState.value = LoginUIState.Idle
    }

    override fun onCleared() {
        Log.d("CRTag", "onCleared: " + this::class.simpleName)
        super.onCleared()
    }

    /**
     * Checks the user login status and operates the UI state based on this.
     */
    private fun MutableStateFlow<LoginUIState>.observeLoginState() = apply {
        authUseCases.observeLoginState(Unit, viewModelScope) { result ->
            result.onSuccess { userInfo ->
                value = when (userInfo) {
                    null -> LoginUIState.Idle
                    else -> LoginUIState.Result.Success(userInfo)
                }
            }
            result.onFailure {
                value = LoginUIState.Idle
            }
        }
    }
}

sealed class LoginUIState {
    object Idle : LoginUIState()
    object Loading : LoginUIState()
    sealed class Result : LoginUIState() {
        class Failure(val message: String?) : Result()
        class Success(val userInfo: UserInfo) : Result()
    }
}

internal class StockPasswordInputValidator : StockInputValidator() {

    // Minimum eight characters, at least one letter, one number and one special character.
    private val passwordRequiredPattern =
        "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$"

    override fun validateInput(input: String, resources: Resources): String? {
        // resources.getString(R.string.auth_register_error_wrong_password)
        return null // Pattern.matches(passwordRequiredPattern, input)
    }
}

internal class StockLoginInputValidator : StockInputValidator() {

    override fun validateInput(input: String, resources: Resources): String? {
        if (input.length <= 3) {
            return resources.getString(R.string.auth_register_error_wrong_login)
        }
        return null
    }
}
