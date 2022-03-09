package ru.er_log.stock.domain.models.exchange

import java.math.BigDecimal

data class Lot(
    val price: BigDecimal,
    val amount: BigDecimal,
    val timestampCreated: Long,
    val owner: Owner
) {
    data class Owner(
        val name: String
    )
}
