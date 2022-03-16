package ru.er_log.stock.domain.models.`in`

data class SignInResponse(
    val authData: AuthData,
    val userInfo: UserInfo
)