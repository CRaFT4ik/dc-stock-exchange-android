package ru.er_log.stock.domain.boundaries.payloads

import com.squareup.moshi.Json
import ru.er_log.stock.domain.boundaries.Mappable
import ru.er_log.stock.domain.models.Lot
import java.math.BigDecimal

data class LotPayload(
    @Json(name = "price")
    val price: BigDecimal,
    @Json(name = "timestampCreated")
    val timestampCreated: Long,
    @Json(name = "owner")
    val owner: Owner
) : Mappable<Lot> {
    override fun map() = Lot(price, timestampCreated, owner.map())

    data class Owner(
        @Json(name = "name")
        val name: String
    ) : Mappable<Lot.Owner> {
        override fun map() = Lot.Owner(name)
    }
}

