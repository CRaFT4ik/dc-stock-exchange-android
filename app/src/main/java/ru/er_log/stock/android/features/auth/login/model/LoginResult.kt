package ru.er_log.stock.android.features.auth.login.model

import ru.er_log.stock.domain.models.auth.LoggedInUser

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: LoggedInUser? = null,
    val failure: String? = null
)