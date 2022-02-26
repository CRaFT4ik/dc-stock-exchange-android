package ru.er_log.stock.android.presentation.auth.login

import ru.er_log.stock.domain.models.LoggedInUser

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: LoggedInUser? = null,
    val failure: String? = null
)