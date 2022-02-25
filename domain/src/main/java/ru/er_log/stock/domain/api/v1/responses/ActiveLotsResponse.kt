package ru.er_log.stock.domain.api.v1.responses

import com.squareup.moshi.Json
import ru.er_log.stock.domain.api.Mappable
import ru.er_log.stock.domain.api.map
import ru.er_log.stock.domain.api.v1.payloads.LotPayload
import ru.er_log.stock.domain.models.ActiveLots

data class ActiveLotsResponse(
    @Json(name = "lotPurchases")
    val lotPurchases: List<LotPayload>,
    @Json(name = "lotSales")
    val lotSales: List<LotPayload>
) : Mappable<ActiveLots> {
    override fun map(): ActiveLots = ActiveLots(lotPurchases.map(), lotSales.map())
}