package ru.er_log.stock.data.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.core.module.Module
import ru.er_log.stock.data.network.JsonHelpers

internal class SerializerComponent : KoinModuleComponent() {

    override fun Module.provide() {
        single { provideMoshiCleanInstance() }
    }

    private fun provideMoshiCleanInstance(): Moshi {
        return Moshi.Builder()
            .add(JsonHelpers.PlatformJsonHelpers.BigDecimalJsonAdapter)
            .add(JsonHelpers.PlatformJsonHelpers.BigIntegerJsonAdapter)
            .add(JsonHelpers.PlatformJsonHelpers.DateJsonAdapter)
            .add(KotlinJsonAdapterFactory())
            .build()
    }
}
