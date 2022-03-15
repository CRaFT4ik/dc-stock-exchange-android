package ru.er_log.stock.domain.models.`in`

data class Transaction(
    val uid: String,
    val lot: Lot,
    val timestamp: Long,
    val type: Type,
    val isPending: Boolean
) {
    enum class Type {
        UNKNOWN,
        OFFER,
        ORDER
    }
}