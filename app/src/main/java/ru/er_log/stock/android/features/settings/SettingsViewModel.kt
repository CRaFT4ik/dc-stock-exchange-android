package ru.er_log.stock.android.features.settings

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.er_log.stock.android.base.utils.UserPrefsStore
import ru.er_log.stock.domain.usecases.AuthUseCases

class SettingsViewModel(
    private val authUseCases: AuthUseCases,
    private val userPrefsStore: UserPrefsStore
) : ViewModel() {

    private val _isLightTheme: MutableState<Boolean> = mutableStateOf(false)
    val isLightTheme: State<Boolean> = _isLightTheme

    init {
        viewModelScope.launch {
            userPrefsStore.getIsLightTheme().collect { isLight ->
                isLight?.let { _isLightTheme.value = it }
            }
        }
    }

    fun logout() {
        authUseCases.signOut(Unit, viewModelScope)
    }

    fun setAppTheme(isLight: Boolean) {
        viewModelScope.launch {
            userPrefsStore.setIsLightTheme(isLight)
        }
    }
}