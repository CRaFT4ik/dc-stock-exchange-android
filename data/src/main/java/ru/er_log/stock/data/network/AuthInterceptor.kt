package ru.er_log.stock.data.network

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ru.er_log.stock.data.di.inject
import ru.er_log.stock.domain.repositories.AuthRepository

class AuthInterceptor : Interceptor {

    private val authRepository: AuthRepository by lazy { inject() }

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        runBlocking {
            try {
                authRepository.getAuthToken()?.let { token ->
                    requestBuilder.addAuthToken(token)
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                throw t
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}

fun Request.Builder.addAuthToken(token: String) = apply {
    addHeader("Authorization", "Bearer $token")
}