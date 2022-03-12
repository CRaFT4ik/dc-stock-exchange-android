package ru.er_log.stock.android.features.home.profile

import java.math.BigDecimal

data class Transaction(
    val uid: String,
    val price: BigDecimal,
    val amount: BigDecimal,
    val timestamp: Long,
    val type: Type,
    val inProgress: Boolean
) {
    enum class Type { SELL, BUY }
}