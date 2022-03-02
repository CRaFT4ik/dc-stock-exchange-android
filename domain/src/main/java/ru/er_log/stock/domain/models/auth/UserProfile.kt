package ru.er_log.stock.domain.models.auth

data class UserProfile(
    val userName: String,
    val userEmail: String,
    val authData: AuthData
) {
    data class AuthData(
        val token: String,
        val userId: Int,
        val roles: List<String>
    )
}