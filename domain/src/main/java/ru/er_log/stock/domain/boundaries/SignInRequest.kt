package ru.er_log.stock.domain.boundaries

data class SignInRequest(
    val username: String,
    val password: String
)
