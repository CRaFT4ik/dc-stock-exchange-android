package ru.er_log.stock.data.network.api.v1.auth

import com.squareup.moshi.Json
import ru.er_log.stock.domain.models.`in`.AuthData
import ru.er_log.stock.domain.models.`in`.UserInfo

internal data class SignInResponseDto(
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
)

internal fun SignInResponseDto.toUserInfo(): UserInfo {
    return UserInfo(userName, userEmail)
}

internal fun SignInResponseDto.toAuthData(): AuthData {
    return AuthData(token, userId, roles)
}
