package ru.er_log.stock.domain.models

data class LoggedInUser(
    val token: String,
    val userId: Int,
    val userName: String,
    val userEmail: String,
    val roles: List<String>
)