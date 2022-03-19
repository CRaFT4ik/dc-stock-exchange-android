package ru.er_log.stock.data.network.api.v1.exchange

import com.squareup.moshi.Json
import ru.er_log.stock.domain.models.`in`.Lot
import java.math.BigDecimal

internal data class LotDto(
    @Json(name = "price")
    val price: BigDecimal,
    @Json(name = "amount")
    val amount: BigDecimal
)

internal fun LotDto.map(): Lot = Lot(price, amount)

internal fun Lot.map(): LotDto = LotDto(price, amount)