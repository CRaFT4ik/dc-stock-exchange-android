package ru.er_log.stock.data.network.api.v1.auth

import retrofit2.http.Body
import retrofit2.http.POST

internal interface AuthService {

    @POST("auth/signin")
    suspend fun signIn(@Body body: SignInRequestDto): SignInResponseDto

    @POST("auth/demo")
    suspend fun signInDemo(): SignInResponseDto
}
