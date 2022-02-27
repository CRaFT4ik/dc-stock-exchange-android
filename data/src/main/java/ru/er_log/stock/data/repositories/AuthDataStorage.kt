package ru.er_log.stock.data.repositories

import ru.er_log.stock.domain.models.auth.LoggedInUser

interface AuthDataStorage {

    companion object {
        const val KEY_AUTH_TOKEN = "KEY_AUTH_TOKEN"
        const val KEY_USER_PROFILE = "KEY_USER_PROFILE"
    }

    fun saveAuthToken(token: String)

    fun fetchAuthToken(): String?

    fun saveUserProfile(userInfo: LoggedInUser)

    fun fetchUserProfile(): LoggedInUser?
}