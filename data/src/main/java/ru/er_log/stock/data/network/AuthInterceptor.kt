package ru.er_log.stock.data.network

import okhttp3.Interceptor
import okhttp3.Response
import ru.er_log.stock.data.repositories.AuthDataStorage

class AuthInterceptor(private val authDataStorage: AuthDataStorage) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        authDataStorage.fetchAuthToken()?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}