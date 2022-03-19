package ru.er_log.stock.data.network.api.v1.auth

import com.squareup.moshi.Json
import ru.er_log.stock.domain.models.`in`.AuthData
import ru.er_log.stock.domain.models.`in`.SignInResponse
import ru.er_log.stock.domain.models.`in`.UserInfo

internal data class SignInResponseDto(
    @Json(name = "token")
    val token: String,
    @Json(name = "id")
    val userId: Long,
    @Json(name = "roles")
    val roles: List<String>,
    @Json(name = "username")
    val userName: String,
    @Json(name = "email")
    val userEmail: String
)

internal fun SignInResponseDto.map() = SignInResponse(
    authData = AuthData(token, userId, roles),
    userInfo = UserInfo(userName, userEmail)
)