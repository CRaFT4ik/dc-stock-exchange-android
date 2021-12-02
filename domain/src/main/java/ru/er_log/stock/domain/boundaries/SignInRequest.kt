package ru.er_log.stock.domain.boundaries

import com.squareup.moshi.Json

data class SignInRequest(
    @Json(name = "username")
    val username: String,
    @Json(name = "password")
    val password: String
)
