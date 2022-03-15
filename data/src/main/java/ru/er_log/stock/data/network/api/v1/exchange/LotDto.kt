package ru.er_log.stock.data.network.api.v1.exchange

import com.squareup.moshi.Json
import ru.er_log.stock.domain.models.`in`.Lot
import java.math.BigDecimal

internal data class LotDto(
    @Json(name = "price")
    val price: BigDecimal,
    @Json(name = "amount")
    val amount: BigDecimal,
    @Json(name = "timestampCreated")
    val timestampCreated: Long,
    @Json(name = "owner")
    val owner: Owner
) {
    internal data class Owner(
        @Json(name = "name")
        val name: String
    )
}

internal fun LotDto.map(): Lot = Lot(price, amount)