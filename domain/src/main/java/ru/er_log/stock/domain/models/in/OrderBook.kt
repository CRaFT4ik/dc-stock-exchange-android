package ru.er_log.stock.domain.models.`in`

import java.math.BigDecimal
import java.util.*
import kotlin.Comparator

data class OrderBook(
    val orders: SortedSet<Item>,
    val offers: SortedSet<Item>
) {
    data class Item(
        val price: BigDecimal,
        val amount: BigDecimal
    ) : Comparable<Item> {
        override fun equals(other: Any?): Boolean {
            if (other == null || other !is Item) return false
            return price.compareTo(other.price) == 0
        }

        override fun hashCode(): Int {
            return price.hashCode()
        }

        object PriceAscComparator : Comparator<Item> {
            override fun compare(item1: Item?, item2: Item?): Int {
                if (item1 == null || item2 == null) {
                    throw NullPointerException()
                }
                return item1.price.compareTo(item2.price)
            }
        }

        object PriceDescComparator : Comparator<Item> {
            override fun compare(item1: Item?, item2: Item?): Int {
                if (item1 == null || item2 == null) {
                    throw NullPointerException()
                }
                return item2.price.compareTo(item1.price)
            }
        }

        override fun compareTo(other: Item): Int {
            return price.compareTo(other.price)
        }
    }
}

