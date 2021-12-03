package ru.er_log.stock.domain.boundaries.responses

import com.squareup.moshi.Json
import ru.er_log.stock.domain.boundaries.Mappable
import ru.er_log.stock.domain.boundaries.map
import ru.er_log.stock.domain.boundaries.payloads.LotPayload
import ru.er_log.stock.domain.models.ActiveLots

data class ActiveLotsResponse(
    @Json(name = "lotPurchases")
    val lotPurchases: List<LotPayload>,
    @Json(name = "lotSales")
    val lotSales: List<LotPayload>
) : Mappable<ActiveLots> {
    override fun map(): ActiveLots = ActiveLots(lotPurchases.map(), lotSales.map())
}