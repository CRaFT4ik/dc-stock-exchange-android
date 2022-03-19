package ru.er_log.stock.domain.models.`in`

import java.math.BigDecimal

data class Lot(
    val price: BigDecimal,
    val amount: BigDecimal
)
