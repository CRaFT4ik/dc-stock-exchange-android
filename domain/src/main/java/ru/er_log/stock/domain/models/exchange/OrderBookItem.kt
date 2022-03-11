package ru.er_log.stock.domain.models.exchange

import java.math.BigDecimal

data class OrderBookItem(
    val price: BigDecimal,
    val amount: BigDecimal
) {
    object PriceAscComparator : Comparator<OrderBookItem> {
        override fun compare(lot1: OrderBookItem?, lot2: OrderBookItem?): Int {
            if (lot1 == null || lot2 == null) {
                throw NullPointerException()
            }
            return lot1.price.compareTo(lot2.price)
        }
    }

    object PriceDescComparator : Comparator<OrderBookItem> {
        override fun compare(lot1: OrderBookItem?, lot2: OrderBookItem?): Int {
            if (lot1 == null || lot2 == null) {
                throw NullPointerException()
            }
            return lot2.price.compareTo(lot1.price)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is OrderBookItem) return false
        return price.compareTo(other.price) == 0
    }

    override fun hashCode(): Int {
        return price.hashCode()
    }
}
