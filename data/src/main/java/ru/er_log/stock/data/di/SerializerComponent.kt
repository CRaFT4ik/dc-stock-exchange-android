package ru.er_log.stock.data.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.core.module.Module
import ru.er_log.stock.data.network.JsonHelpers
import ru.er_log.stock.data.network.api.v1.account.TransactionDto

internal class SerializerComponent : KoinModuleComponent() {

    override fun Module.provide() {
        single { provideMoshiAPIv1Instance() }
    }

    private fun provideMoshiCleanInstance(): Moshi {
        return Moshi.Builder()
            .add(JsonHelpers.PlatformJsonHelpers.BigDecimalJsonAdapter())
            .add(JsonHelpers.PlatformJsonHelpers.BigIntegerJsonAdapter())
            .add(JsonHelpers.PlatformJsonHelpers.DateJsonAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    private fun provideMoshiAPIv1Instance(): Moshi {
        return provideMoshiCleanInstance().newBuilder()
            .add(TransactionDto.Type.JsonAdapter())
            .build()
    }
}
