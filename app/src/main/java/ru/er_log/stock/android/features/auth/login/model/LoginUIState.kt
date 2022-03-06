package ru.er_log.stock.android.features.auth.login.model

import ru.er_log.stock.domain.models.auth.UserProfile

sealed class LoginUIState {
    object Idle : LoginUIState()
    object Loading : LoginUIState()
    sealed class Result : LoginUIState() {
        class Failure(val message: String?) : Result()
        class Success(val userProfile: UserProfile) : Result()
    }
}