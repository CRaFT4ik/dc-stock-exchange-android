package ru.er_log.stock.data.network.api.v1.auth

import com.squareup.moshi.Json
import ru.er_log.stock.domain.models.out.SignInRequest

internal data class SignInRequestDto(
    @Json(name = "username")
    val username: String,
    @Json(name = "password")
    val password: String
)

internal fun SignInRequest.map() = SignInRequestDto(username, password)
