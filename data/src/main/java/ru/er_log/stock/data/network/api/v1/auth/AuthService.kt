package ru.er_log.stock.data.network.api.v1.auth

import retrofit2.http.Body
import retrofit2.http.POST
import ru.er_log.stock.data.network.api.v1.auth.SignInRequestDto
import ru.er_log.stock.data.network.api.v1.auth.SignInResponseDto

internal interface AuthService {

    @POST("auth/signin")
    suspend fun signIn(@Body body: SignInRequestDto): SignInResponseDto
}
