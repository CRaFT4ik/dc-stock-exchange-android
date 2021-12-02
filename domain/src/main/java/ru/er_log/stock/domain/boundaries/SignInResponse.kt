package ru.er_log.stock.domain.boundaries

import com.squareup.moshi.Json
import ru.er_log.stock.domain.models.LoggedInUser

data class SignInResponse(
    @Json(name = "token")
    val token: String,
    @Json(name = "id")
    val userId: Int,
    @Json(name = "username")
    val userName: String,
    @Json(name = "email")
    val userEmail: String,
    @Json(name = "roles")
    val roles: List<String>
) {
    fun map(): LoggedInUser {
        return LoggedInUser(token, userId, userName, userEmail, roles)
    }
}
