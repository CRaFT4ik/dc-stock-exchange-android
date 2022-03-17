package ru.er_log.stock.data.di

import okhttp3.OkHttpClient
import okhttp3.internal.tls.OkHostnameVerifier
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.tls.HandshakeCertificates
import org.koin.core.module.Module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.er_log.stock.data.network.AuthInterceptor
import ru.er_log.stock.data.network.Authenticator
import ru.er_log.stock.data.network.api.v1.account.AccountService
import ru.er_log.stock.data.network.api.v1.auth.AuthService
import ru.er_log.stock.data.network.api.v1.exchange.ExchangeService
import ru.er_log.stock.data.storages.database.daos.UserDao
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier

internal class NetworkComponent : KoinModuleComponent() {

    companion object {
        const val devHost = "192.168.0.100"
        const val remoteHost = "192.168.0.100"
        const val remotePort = "2053"
    }

    override fun Module.provide() {
        provideOkHttp()
        provideRetrofit()
        provideServices()
    }

    private fun Module.provideOkHttp() {
        factory<OkHttpClient> {
            val loggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)

            val certificates: HandshakeCertificates = HandshakeCertificates.Builder()
                .addPlatformTrustedCertificates()
                .addInsecureHost(devHost)
                .build()

            val hostnameVerifier = HostnameVerifier { hostname, session ->
                hostname == devHost || OkHostnameVerifier.verify(hostname, session)
            }

            OkHttpClient.Builder()
                .sslSocketFactory(certificates.sslSocketFactory(), certificates.trustManager)
                .hostnameVerifier(hostnameVerifier)
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .authenticator(Authenticator(get()))
                .addInterceptor(AuthInterceptor(get()))
                .addInterceptor(loggingInterceptor)
                .build()
        }
    }

    private fun Module.provideRetrofit() {
        single<Retrofit> {
            val moshiConverterFactory = MoshiConverterFactory.create(get()).asLenient()

            Retrofit.Builder()
                .addConverterFactory(moshiConverterFactory)
                .baseUrl("https://$remoteHost:$remotePort/api/v1/")
                .client(get())
                .build()
        }
    }

    private fun Module.provideServices() {
        single<AuthService> {
            createService(retrofit = get())
        }

        single<AccountService> {
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