package ru.er_log.stock.data.network.api.v1.account

import ru.er_log.stock.data.network.api.Mappable
import ru.er_log.stock.domain.models.`in`.UserCard
import ru.er_log.stock.domain.models.`in`.UserInfo
import java.math.BigDecimal

internal data class UserCardDto(
    val userInfo: UserInfoDto,
    val userBalance: BigDecimal,
    val transactionStatistics: TransactionStatisticsDto
) : Mappable<UserCard> {

    internal data class UserInfoDto(
        val userName: String,
        val userEmail: String
    ) : Mappable<UserInfo> {
        override fun map() = UserInfo(userName, userEmail)
    }

    internal data class TransactionStatisticsDto(
        val ordersCompleted: Long,
        val ordersActive: Long,
        val offersCompleted: Long,
        val offersActive: Long
    ) : Mappable<UserCard.TransactionStatistics> {
        override fun map() = UserCard.TransactionStatistics(
            ordersCompleted, ordersActive, offersCompleted, offersActive
        )
    }

    override fun map(): UserCard {
        return UserCard(userInfo.map(), userBalance, transactionStatistics.map())
    }
}