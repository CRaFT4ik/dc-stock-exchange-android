package ru.er_log.stock.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.er_log.stock.data.network.AuthInterceptor
import ru.er_log.stock.data.network.AuthService
import ru.er_log.stock.data.network.ExchangeService
import ru.er_log.stock.data.network.JsonHelpers
import ru.er_log.stock.data.repositories.AuthTokenStorage
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit

class NetServices(storage: AuthTokenStorage) {

    private val apiDomain = "http://192.168.0.100:8080/api/"

    private val loggingInterceptor: HttpLoggingInterceptor
        get() = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClientCreator: OkHttpClient by lazy {
        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        return@lazy OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.SECONDS)
            .connectTimeout(0, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(storage))
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val retrofitInstance: Retrofit by lazy {
        val moshi = MoshiConverterFactory.create(JsonHelpers.moshiCleanInstance)
            .asLenient()

        return@lazy Retrofit.Builder()
            .addConverterFactory(moshi)
            .baseUrl(apiDomain)
            .client(okHttpClientCreator)
            .build()
    }

    internal val authService = retrofitInstance.create(AuthService::class.java)

    internal val exchangeService = retrofitInstance.create(ExchangeService::class.java)
}