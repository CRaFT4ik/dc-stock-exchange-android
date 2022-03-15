package ru.er_log.stock.data.network.api.v1.auth

import com.squareup.moshi.Json

internal data class SignInRequestDto(
    @Json(name = "username")
    val username: String,
    @Json(name = "password")
    val password: String
)
