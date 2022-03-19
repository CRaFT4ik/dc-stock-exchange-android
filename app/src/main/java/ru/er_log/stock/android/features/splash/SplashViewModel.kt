package ru.er_log.stock.android.features.splash

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.er_log.stock.android.base.utils.UserPrefsStore

class SplashViewModel(private val userPrefsStore: UserPrefsStore) : ViewModel() {

    private val _isLoading: MutableState<Boolean> = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _isLightTheme: MutableState<Boolean> = mutableStateOf(false)
    val isLightTheme: State<Boolean> = _isLightTheme

    init {
        viewModelScope.launch {
            userPrefsStore.getIsLightTheme().collect { isLight ->
                isLight?.let { _isLightTheme.value = it }
                _isLoading.value = false
            }
        }
    }
}