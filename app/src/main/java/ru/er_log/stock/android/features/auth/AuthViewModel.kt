package ru.er_log.stock.android.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.er_log.stock.domain.usecases.AuthUseCases

class AuthViewModel(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.apply {
        authUseCases.observeLoginState(Unit, viewModelScope) { result ->
            result.onSuccess { v -> emit(v) }
        }
    }.asStateFlow()
}
