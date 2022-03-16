package ru.er_log.stock.domain.models.out

data class SignInRequest(
    val username: String,
    val password: String
)