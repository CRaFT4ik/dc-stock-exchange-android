package ru.er_log.stock.android.compose.components.order_book

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import ru.er_log.stock.domain.models.`in`.OrderBook
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random

internal class OrderBookPreviewProvider {

    private fun provideItems(minPrice: Int, maxPrice: Int): SortedSet<OrderBook.Item> {
        val lots = sortedSetOf(OrderBook.Item.PriceAscComparator)
        repeat(100) {
            lots.add(
                OrderBook.Item(
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

    @Composable
    fun provideState(
        orders: SortedSet<OrderBook.Item> = provideItems(20000, 30000),
        offers: SortedSet<OrderBook.Item> = provideItems(30000, 40000)
    ) = remember {
        val orderBook = OrderBook(orders, offers)
        OrderBookState(orderBook)
    }
}