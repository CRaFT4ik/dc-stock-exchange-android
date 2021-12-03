package ru.er_log.stock.domain.boundaries.responses

import com.squareup.moshi.Json
import ru.er_log.stock.domain.boundaries.Mappable
import ru.er_log.stock.domain.boundaries.map
import ru.er_log.stock.domain.boundaries.payloads.DealPayload
import ru.er_log.stock.domain.models.Deal

data class DealsResponse(
    @Json(name = "deals")
    val lotPurchases: List<DealPayload>
) : Mappable<List<Deal>> {
    override fun map(): List<Deal> = lotPurchases.map()
}