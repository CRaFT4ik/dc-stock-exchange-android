package ru.er_log.stock.android.features.auth.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.er_log.stock.android.R
import ru.er_log.stock.android.base.util.scoped
import ru.er_log.stock.android.features.auth.login.model.LoginFormState
import ru.er_log.stock.android.features.auth.login.model.LoginResult
import ru.er_log.stock.domain.api.v1.auth.SignInRequest
import ru.er_log.stock.domain.usecases.AuthUseCases

class LoginViewModel(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String): LiveData<LoginResult> {
        scoped {
            val request = SignInRequest(username, password)

            authUseCases.signIn(request, viewModelScope) {
                it.onSuccess { v -> _loginResult.value = LoginResult(success = v) }
                it.onFailure { v -> _loginResult.value = LoginResult(failure = v.localizedMessage) }
            }
        }
        return loginResult
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }
}
