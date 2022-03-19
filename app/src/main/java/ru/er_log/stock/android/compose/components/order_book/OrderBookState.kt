package ru.er_log.stock.android.compose.components.order_book

import androidx.compose.ui.geometry.Offset
import ru.er_log.stock.domain.models.`in`.OrderBook
import java.math.BigDecimal
import java.util.*

/**
 * State for order book chart.
 */
data class OrderBookState(
    private val orderBook: OrderBook = OrderBook(TreeSet(), TreeSet())
) {
    // Each must be sorted by price ACS.
    val orders: SortedSet<OrderBook.Item> get() = orderBook.orders
    val offers: SortedSet<OrderBook.Item> get() = orderBook.offers

    private val ordersMin: BigDecimal get() = orders.firstOrNull()?.price ?: BigDecimal.ZERO
    private val ordersMax: BigDecimal get() = orders.lastOrNull()?.price ?: BigDecimal.ZERO
    private val offersMin: BigDecimal get() = offers.firstOrNull()?.price ?: BigDecimal.ZERO
    private val offersMax: BigDecimal get() = offers.lastOrNull()?.price ?: BigDecimal.ZERO

    val priceMin: BigDecimal get() = ordersMin
    val priceMax: BigDecimal get() = offersMax
    val priceAvg: BigDecimal get() = (ordersMax + offersMin) / BigDecimal.valueOf(2)

    private val maxAmount: BigDecimal? get() = run {
        val max = maxOf(ordersMaxAmount, offersMaxAmount)
        return if (max > BigDecimal.ZERO) max else null
    }

    val offersMaxAmount =
        offers.fold(BigDecimal.ZERO) { acc, item -> acc + item.amount }

    val ordersMaxAmount =
        orders.fold(BigDecimal.ZERO) { acc, item -> acc + item.amount }

    val amountLines = run {
        val amountStep = maxAmount?.let { it / BigDecimal.valueOf(6) } ?: BigDecimal.ZERO
        mutableListOf<BigDecimal>().apply {
            if (amountStep > BigDecimal.ZERO) {
                for (i in 1..6) add(amountStep * i.toBigDecimal())
            }
        }
    }

    fun amountYOffset(chartHeight: Float, amount: BigDecimal): Float {
        return maxAmount?.let {
            (chartHeight.toBigDecimal() * amount / it).toFloat()
        } ?: 0f
    }

    fun chartOrders(
        chartWidth: Int,
        chartHeight: Int,
        xOffset: Float = 0f,
        yOffset: Float = 0f
    ): List<Offset> = chart(
        orders,
        ordersMin..ordersMax,
        chartWidth,
        chartHeight,
        xOffset,
        yOffset,
        reversed = true
    )

    fun chartOffers(
        chartWidth: Int,
        chartHeight: Int,
        xOffset: Float = 0f,
        yOffset: Float = 0f
    ): List<Offset> = chart(
        offers,
        offersMin..offersMax,
        chartWidth,
        chartHeight,
        xOffset,
        yOffset,
        reversed = false
    )

    private fun chart(
        items: SortedSet<OrderBook.Item>,
        priceRange: ClosedRange<BigDecimal>,
        chartWidth: Int,
        chartHeight: Int,
        xOffset: Float = 0f,
        yOffset: Float = 0f,
        reversed: Boolean = false
    ): List<Offset> {
        val result = mutableListOf<Offset>()

        val range = if (reversed) chartWidth downTo 1 else 0 until chartWidth
        var yAccAmount: BigDecimal = BigDecimal.ZERO

        var prevItem = when (reversed) {
            true -> OrderBook.Item(priceMax, BigDecimal.ZERO)
            else -> OrderBook.Item(BigDecimal.ZERO, BigDecimal.ZERO)
        }

        for (x in range) {
            val xPrice = priceMin + (priceMax - priceMin) *
                    (x.toDouble() / chartWidth).toBigDecimal()

            if (xPrice !in priceRange) continue

            val nextItem = OrderBook.Item(xPrice, BigDecimal.ZERO)
            val amountPartSet = when (reversed) {
                true -> items.subSet(nextItem, prevItem)
                else -> items.subSet(prevItem, nextItem)
            }

            yAccAmount = amountPartSet.fold(yAccAmount) { acc, item -> acc + item.amount }
            // Because SortedList.subSet excludes second value.
            if (x == range.last) {
                when (reversed) {
                    true -> items.firstOrNull()?.let { yAccAmount += it.amount }
                    else -> items.lastOrNull()?.let { yAccAmount += it.amount }
                }
            }

            val y = maxAmount?.let { chartHeight.toBigDecimal() * yAccAmount / it } ?: BigDecimal.ZERO
            result.add(Offset(xOffset + x.toFloat(), yOffset + chartHeight - y.toFloat()))

            prevItem = nextItem
        }

        return result
    }
}