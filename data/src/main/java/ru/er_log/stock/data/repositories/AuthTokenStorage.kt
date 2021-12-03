package ru.er_log.stock.data.repositories

interface AuthTokenStorage {

    companion object {
        const val KEY_AUTH_TOKEN = "KEY_AUTH_TOKEN"
    }

    fun saveAuthToken(token: String)

    fun fetchAuthToken(): String?
}