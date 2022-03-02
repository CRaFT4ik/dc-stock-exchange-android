package ru.er_log.stock.domain.api.v1.auth

import com.squareup.moshi.Json
import ru.er_log.stock.domain.api.Mappable
import ru.er_log.stock.domain.models.auth.UserProfile

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
) : Mappable<UserProfile> {
    override fun map(): UserProfile {
        val authData = UserProfile.AuthData(token, userId, roles)
        return UserProfile(userName, userEmail, authData)
    }
}
