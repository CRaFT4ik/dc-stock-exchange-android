package ru.er_log.stock.android.ui.auth

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.er_log.stock.android.R
import ru.er_log.stock.android.UseCaseLocator
import ru.er_log.stock.android.ui.auth.login.LoginFormState
import ru.er_log.stock.android.ui.auth.login.LoginResult
import ru.er_log.stock.domain.boundaries.SignInRequest

class AuthViewModel : ViewModel() {

    private val authUseCase = UseCaseLocator.authUseCase()

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        val request = SignInRequest(username, password)
        authUseCase.signIn(request)
            .catch { _loginResult.value = LoginResult(failure = it.localizedMessage) }
            .onEach { _loginResult.value = LoginResult(success = it) }
            .launchIn(viewModelScope)
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