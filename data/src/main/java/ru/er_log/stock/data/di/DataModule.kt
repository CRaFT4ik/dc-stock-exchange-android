package ru.er_log.stock.data.di

import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.er_log.stock.data.network.AuthInterceptor
import ru.er_log.stock.data.network.AuthService
import ru.er_log.stock.data.network.ExchangeService
import ru.er_log.stock.data.network.JsonHelpers
import ru.er_log.stock.data.repositories.AuthRepositoryImpl
import ru.er_log.stock.data.repositories.ExchangeRepositoryImpl
import ru.er_log.stock.domain.repositories.AuthRepository
import ru.er_log.stock.domain.repositories.ExchangeRepository
import java.util.concurrent.TimeUnit

private val reposModule = module {

    val reposDispatcher = Dispatchers.IO

    single<AuthRepository> {
        AuthRepositoryImpl(authService = get(), reposDispatcher)
    }

    single<ExchangeRepository> {
        ExchangeRepositoryImpl(exchangeService = get(), reposDispatcher)
    }
}

private val networkModule = module {

    val apiDomain = "http://192.168.0.100:8080/api/"

    single<AuthService> {
        createService(retrofit = get())
    }

    single<ExchangeService> {
        createService(retrofit = get())
    }

    factory<Retrofit> {
        val moshi = MoshiConverterFactory.create(JsonHelpers.moshiCleanInstance)
            .asLenient()

        Retrofit.Builder()
            .addConverterFactory(moshi)
            .baseUrl(apiDomain)
            .client(get())
            .build()
    }

    factory<OkHttpClient> {
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.SECONDS)
            .connectTimeout(0, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(authDataStorage = get()))
            .addInterceptor(loggingInterceptor)
            .build()
    }
}

val dataModule = reposModule + networkModule

private inline fun <reified T> createService(retrofit: Retrofit): T {
    return retrofit.create(T::class.java)
}