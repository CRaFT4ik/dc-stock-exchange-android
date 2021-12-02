package ru.er_log.stock.domain.boundaries

data class SignInResponse(
    val token: String,
    val userId: Int,
    val userName: String,
    val userEmail: String,
    val roles: List<String>
)
