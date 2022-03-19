package ru.er_log.stock.domain.models.`in`

import java.math.BigDecimal

data class UserCard(
    val userInfo: UserInfo,
    val userBalance: BigDecimal,
    val statistics: TransactionStatistics
) {
    data class TransactionStatistics(
        val ordersCompleted: Long,
        val ordersActive: Long,
        val offersCompleted: Long,
        val offersActive: Long
    )
}