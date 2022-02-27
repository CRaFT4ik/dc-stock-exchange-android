package ru.er_log.stock.domain.api.v1.exchange

import com.squareup.moshi.Json
import ru.er_log.stock.domain.api.Mappable
import ru.er_log.stock.domain.api.map
import ru.er_log.stock.domain.models.exchange.Deal

data class DealsResponse(
    @Json(name = "deals")
    val lotPurchases: List<DealPayload>
) : Mappable<List<Deal>> {
    override fun map(): List<Deal> = lotPurchases.map()
}