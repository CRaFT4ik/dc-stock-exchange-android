package ru.er_log.stock.android.compose.components.order_book

import ru.er_log.stock.domain.models.order_book.OrderBookItem
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random

internal class OrderBookPreviewProvider {

    fun provide(minPrice: Int, maxPrice: Int): SortedSet<OrderBookItem> {
        val lots = sortedSetOf(OrderBookItem.PriceAscComparator)
        repeat(100) {
            lots.add(
                OrderBookItem(
                    price = BigDecimal.valueOf(
                        Random.nextDouble(
                            minPrice.toDouble(),
                            maxPrice.toDouble()
                        )
                    ),
                    amount = BigDecimal.valueOf(Random.nextDouble(0.01, 50.0))
                )
            )
        }
        return lots
    }
}