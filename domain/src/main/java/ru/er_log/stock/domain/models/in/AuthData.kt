package ru.er_log.stock.domain.models.`in`

data class AuthData(
    val token: String,
    val userId: Long,
    val roles: List<String>
)