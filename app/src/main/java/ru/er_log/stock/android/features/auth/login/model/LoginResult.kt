package ru.er_log.stock.android.features.auth.login.model

import ru.er_log.stock.domain.models.auth.UserProfile

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: UserProfile? = null,
    val failure: String? = null
)