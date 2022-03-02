package ru.er_log.stock.data.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.er_log.stock.data.network.AuthInterceptor
import ru.er_log.stock.data.network.AuthService
import ru.er_log.stock.data.network.ExchangeService
import java.util.concurrent.TimeUnit

internal class NetworkComponent : KoinModuleComponent() {

    override fun Module.provide() {
        provideOkHttp()
        provideRetrofit()
        provideServices()
    }

    private fun Module.provideOkHttp() {
        factory<OkHttpClient> {
            val loggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)

            OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(AuthInterceptor(authDataStorage = get()))
                .addInterceptor(loggingInterceptor)
                .build()
        }
    }

    private fun Module.provideRetrofit() {
        factory<Retrofit> {
            val moshiConverterFactory = MoshiConverterFactory.create(get()).asLenient()

            Retrofit.Builder()
                .addConverterFactory(moshiConverterFactory)
                .baseUrl("https://er-log.ru:2053/api/")
                .client(get())
                .build()
        }
    }

    private fun Module.provideServices() {
        single<AuthService> {
            createService(retrofit = get())
        }

        single<ExchangeService> {
            createService(retrofit = get())
        }
    }

    private inline fun <reified T> createService(retrofit: Retrofit): T {
        return retrofit.create(T::class.java)
    }
}