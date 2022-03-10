package ru.er_log.stock.android.features.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.er_log.stock.domain.models.auth.UserProfile
import ru.er_log.stock.domain.usecases.AuthUseCases

class ProfileViewModel(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _profile = MutableLiveData<UserProfile?>()
    val profile: LiveData<UserProfile?> = _profile.apply {
        authUseCases.observeUser(Unit, viewModelScope) { result ->
            result.onSuccess { this.value = it }
            result.onFailure { this.value = null }
        }
    }
}
