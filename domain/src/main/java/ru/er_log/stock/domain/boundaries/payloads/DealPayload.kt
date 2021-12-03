package ru.er_log.stock.domain.boundaries.payloads

import com.squareup.moshi.Json
import ru.er_log.stock.domain.boundaries.Mappable
import ru.er_log.stock.domain.models.Deal

data class DealPayload(
    @Json(name = "lotPurchase")
    val lotPurchase: LotPayload,
    @Json(name = "lotSale")
    val lotSale: LotPayload,
    @Json(name = "timestampCreated")
    val timestampCreated: Long
) : Mappable<Deal> {
    override fun map() = Deal(lotPurchase.map(), lotSale.map(), timestampCreated)
}
