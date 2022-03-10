package ru.er_log.stock.android.features.home.exchange.order_book.widget

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import ru.er_log.stock.domain.models.exchange.OrderBookItem
import java.math.BigDecimal
import java.util.*

/**
 * State for order book chart.
 */
data class OrderBookState(
    // Must be sorted by price ACS.
    val ordersState: MutableState<SortedSet<OrderBookItem>>,
    // Must be sorted by price ACS.
    val offersState: MutableState<SortedSet<OrderBookItem>>
) {
    private val orders: SortedSet<OrderBookItem> get() = ordersState.value
    private val offers: SortedSet<OrderBookItem> get() = offersState.value

    private val ordersMin: BigDecimal get() = orders.firstOrNull()?.price ?: BigDecimal.ZERO
    private val ordersMax: BigDecimal get() = orders.lastOrNull()?.price ?: BigDecimal.ZERO
    private val offersMin: BigDecimal get() = offers.firstOrNull()?.price ?: BigDecimal.ZERO
    private val offersMax: BigDecimal get() = offers.lastOrNull()?.price ?: BigDecimal.ZERO

    val priceMin: BigDecimal get() = ordersMin
    val priceMax: BigDecimal get() = offersMax
    val priceAvg: BigDecimal get() = (ordersMax + offersMin) / BigDecimal.valueOf(2)

    private val maxAmount get() = maxOf(ordersMaxAmount, offersMaxAmount)

    val offersMaxAmount by derivedStateOf {
        offers.fold(BigDecimal.ZERO) { acc, item -> acc + (item.amount * item.price) }
    }

    val ordersMaxAmount by derivedStateOf {
        orders.fold(BigDecimal.ZERO) { acc, item -> acc + (item.amount * item.price) }
    }

    val amountLines by derivedStateOf {
        val amountStep = maxAmount / BigDecimal.valueOf(6)
        mutableListOf<BigDecimal>().apply {
            for (i in 1..6) add(amountStep * i.toBigDecimal())
        }
    }

    fun amountYOffset(chartHeight: Float, amount: BigDecimal): Float =
        (chartHeight.toBigDecimal() * amount / maxAmount).toFloat()

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
        items: SortedSet<OrderBookItem>,
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
            true -> OrderBookItem(priceMax, BigDecimal.ZERO)
            else -> OrderBookItem(BigDecimal.ZERO, BigDecimal.ZERO)
        }

        for (x in range) {
            val xPrice = priceMin + (priceMax - priceMin) *
                    (x.toDouble() / chartWidth).toBigDecimal()

            if (xPrice !in priceRange) continue

            val nextItem = OrderBookItem(xPrice, BigDecimal.ZERO)
            val amountPartSet = when (reversed) {
                true -> items.subSet(nextItem, prevItem)
                else -> items.subSet(prevItem, nextItem)
            }

            yAccAmount = amountPartSet.fold(yAccAmount) { acc, item -> acc + (item.amount * item.price) }
            // Because SortedList.subSet excludes second value.
            if (x == range.last) {
                when (reversed) {
                    true -> items.firstOrNull()?.let { yAccAmount += it.price * it.amount }
                    else -> items.lastOrNull()?.let { yAccAmount += it.price * it.amount }
                }
            }

            val y = chartHeight.toBigDecimal() * yAccAmount / maxAmount
            result.add(Offset(xOffset + x.toFloat(), yOffset + chartHeight - y.toFloat()))

            prevItem = nextItem
        }

        return result
    }
}