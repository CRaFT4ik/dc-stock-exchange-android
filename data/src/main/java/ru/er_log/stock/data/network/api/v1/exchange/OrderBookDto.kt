package ru.er_log.stock.data.network.api.v1.exchange

import com.squareup.moshi.Json
import ru.er_log.stock.data.network.api.Mappable
import ru.er_log.stock.domain.models.`in`.OrderBook

internal data class OrderBookDto(
    @Json(name = "orders")
    val orders: List<LotDto>,
    @Json(name = "offers")
    val offers: List<LotDto>
) : Mappable<OrderBook> {

    override fun map() = OrderBook(
        orders = orders.map { OrderBook.Item(it.map()) },
        offers = orders.map { OrderBook.Item(it.map()) }
    )
}