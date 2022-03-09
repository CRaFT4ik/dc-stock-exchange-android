package ru.er_log.stock.domain.api.v1.exchange

import com.squareup.moshi.Json
import ru.er_log.stock.domain.api.Mappable
import ru.er_log.stock.domain.models.exchange.Lot
import java.math.BigDecimal

data class LotPayload(
    @Json(name = "price")
    val price: BigDecimal,
    @Json(name = "amount")
    val amount: BigDecimal,
    @Json(name = "timestampCreated")
    val timestampCreated: Long,
    @Json(name = "owner")
    val owner: Owner
) : Mappable<Lot> {
    override fun map() = Lot(price, amount, timestampCreated, owner.map())

    data class Owner(
        @Json(name = "name")
        val name: String
    ) : Mappable<Lot.Owner> {
        override fun map() = Lot.Owner(name)
    }
}

