package ru.er_log.stock.android.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.er_log.stock.domain.usecases.AuthUseCases

class AuthViewModel(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.apply {
        authUseCases.observeUser(Unit, viewModelScope) { result ->
            result.onSuccess { this.value = it != null }
        }
    }
}
