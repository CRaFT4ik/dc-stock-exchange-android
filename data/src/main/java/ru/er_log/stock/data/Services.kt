package ru.er_log.stock.data

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.er_log.stock.data.network.AuthService
import ru.er_log.stock.data.network.JsonHelpers

object Services {

    private const val apiDomain = "http://192.168.0.100:8080/api/"

    private val retrofitInstance: Retrofit by lazy {
        val moshi = MoshiConverterFactory.create(JsonHelpers.moshiCleanInstance)
            .asLenient()

        return@lazy Retrofit.Builder()
            .addConverterFactory(moshi)
            .baseUrl(apiDomain)
            .build()
    }

    internal val authService = retrofitInstance.create(AuthService::class.java)
}