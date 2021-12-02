package ru.er_log.stock.data.network

import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

object JsonHelpers {

    val moshiCleanInstance: Moshi = Moshi.Builder()
        .add(PlatformJsonHelpers.BigDecimalJsonAdapter)
        .add(PlatformJsonHelpers.BigIntegerJsonAdapter)
        .add(PlatformJsonHelpers.DateJsonAdapter)
        .add(KotlinJsonAdapterFactory())
        .build()

    class PlatformJsonHelpers {
        object BigDecimalJsonAdapter {
            @FromJson
            fun fromJson(string: String) = string.toBigDecimal()
            @ToJson
            fun toJson(value: BigDecimal) = value.toString()
        }

        object BigIntegerJsonAdapter {
            @FromJson fun fromJson(string: String) = string.toBigInteger()
            @ToJson fun toJson(value: BigInteger) = value.toString()
        }

        object DateJsonAdapter {
            @FromJson fun fromJson(date: Long) = Date(date)
            @ToJson fun toJson(date: Date) = date.time
        }
    }
}