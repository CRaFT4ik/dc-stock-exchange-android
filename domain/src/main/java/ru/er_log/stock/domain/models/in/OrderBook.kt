package ru.er_log.stock.domain.models.`in`

data class OrderBook(
    val orders: List<Item>,
    val offers: List<Item>
) {
    data class Item(val lot: Lot) {
        val price get() = lot.price
        val amount get() = lot.amount

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

        override fun equals(other: Any?): Boolean {
            if (other == null || other !is Item) return false
            return price.compareTo(other.price) == 0
        }

        override fun hashCode(): Int {
            return price.hashCode()
        }
    }
}

