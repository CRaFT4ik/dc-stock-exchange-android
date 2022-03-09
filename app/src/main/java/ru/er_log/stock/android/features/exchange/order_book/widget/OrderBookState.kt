package ru.er_log.stock.android.features.exchange.order_book.widget

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import ru.er_log.stock.domain.models.exchange.OrderBookItem
import java.math.BigDecimal
import java.math.RoundingMode
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

    private val ordersMin: BigDecimal
        get() = (orders.firstOrNull()?.price ?: BigDecimal.ZERO).autoScale()
    private val ordersMax: BigDecimal
        get() = (orders.lastOrNull()?.price ?: BigDecimal.ZERO).autoScale()
    private val offersMin: BigDecimal
        get() = (offers.firstOrNull()?.price ?: BigDecimal.ZERO).autoScale()
    private val offersMax: BigDecimal
        get() = (offers.lastOrNull()?.price ?: BigDecimal.ZERO).autoScale()

    val priceMin: BigDecimal get() = ordersMin
    val priceMax: BigDecimal get() = offersMax
    val priceAvg: BigDecimal get() = ((ordersMax + offersMin) / BigDecimal.valueOf(2)).autoScale()

    private val maxAmount by derivedStateOf {
        val ordersMax =
            orders.fold(BigDecimal.ZERO) { acc, item -> acc + (item.amount * item.price) }
        val offersMax =
            offers.fold(BigDecimal.ZERO) { acc, item -> acc + (item.amount * item.price) }
        maxOf(ordersMax, offersMax)
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
        items: SortedSet<OrderBookItem>,
        priceRange: ClosedRange<BigDecimal>,
        chartWidth: Int,
        chartHeight: Int,
        xOffset: Float = 0f,
        yOffset: Float = 0f,
        reversed: Boolean = false
    ): List<Offset> {
        val result = mutableListOf<Offset>()

        val range = if (reversed) chartWidth downTo 0 else 0 until chartWidth
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

            yAccAmount = (nextItem.price * nextItem.amount) +
                    amountPartSet.fold(yAccAmount) { acc, item -> acc + (item.amount * item.price) }

            val y = chartHeight.toBigDecimal() * yAccAmount / maxAmount
            result.add(Offset(xOffset + x.toFloat(), yOffset + chartHeight - y.toFloat()))

            prevItem = nextItem
        }
        return result
    }

    private fun BigDecimal.autoScale(): BigDecimal {
        val scale = when {
            setScale(0, RoundingMode.DOWN).compareTo(BigDecimal.ZERO) == 0 -> 5
            compareTo(BigDecimal.TEN) < 0 -> 3
            else -> 2
        }
        return setScale(scale, RoundingMode.HALF_EVEN)
    }
}