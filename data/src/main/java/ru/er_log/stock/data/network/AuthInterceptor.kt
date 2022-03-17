package ru.er_log.stock.data.network

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ru.er_log.stock.domain.repositories.AuthRepository

class AuthInterceptor(private val authRepository: AuthRepository) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        runBlocking {
            authRepository.fetchAuthToken()?.let { token ->
                requestBuilder.addAuthToken(token)
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}

fun Request.Builder.addAuthToken(token: String) = apply {
    addHeader("Authorization", "Bearer $token")
}