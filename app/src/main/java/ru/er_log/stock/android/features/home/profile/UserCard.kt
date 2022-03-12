package ru.er_log.stock.android.features.home.profile

import ru.er_log.stock.domain.models.auth.UserProfile
import java.math.BigDecimal

data class UserCard(
    val userProfile: UserProfile,
    val userBalance: BigDecimal,
    val statistics: TransactionStatistics
) {
    companion object {
        val empty = UserCard(
            userProfile = UserProfile.mock(),
            userBalance = BigDecimal.ZERO,
            TransactionStatistics.empty
        )
    }

    data class TransactionStatistics(
        val ordersCompleted: Int,
        val ordersActive: Int,
        val offersCompleted: Int,
        val offersActive: Int
    ) {
        companion object {
            val empty = TransactionStatistics(
                offersActive = 0,
                offersCompleted = 0,
                ordersActive = 0,
                ordersCompleted = 0,
            )
        }
    }
}