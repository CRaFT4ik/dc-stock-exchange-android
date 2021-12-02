package ru.er_log.stock.data.network

import retrofit2.http.Body
import retrofit2.http.POST
import ru.er_log.stock.domain.boundaries.SignInRequest
import ru.er_log.stock.domain.boundaries.SignInResponse

internal interface AuthService {

    @POST("auth/signin")
    suspend fun signIn(@Body body: SignInRequest): SignInResponse
}
