package ru.er_log.stock.data.network.api.v1.exchange

import com.squareup.moshi.Json
import ru.er_log.stock.domain.models.`in`.OrderBook

internal data class OrderBookDto(
    @Json(name = "orders")
    val orders: List<LotDto>,
    @Json(name = "offers")
    val offers: List<LotDto>
)

internal fun OrderBookDto.map() = OrderBook(
    orders = orders.map { OrderBook.Item(it.price, it.amount) }
        .toSortedSet(OrderBook.Item.PriceAscComparator),
    offers = offers.map { OrderBook.Item(it.price, it.amount) }
        .toSortedSet(OrderBook.Item.PriceAscComparator)
)