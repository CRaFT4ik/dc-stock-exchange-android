package ru.er_log.stock.android.features.exchange.order_book.widget

import ru.er_log.stock.domain.models.exchange.OrderBookItem
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random

internal class OrderBookPreviewProvider {

    fun provide(minPrice: Int, maxPrice: Int): SortedSet<OrderBookItem> {
        val lots = sortedSetOf(OrderBookItem.PriceComparator)
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