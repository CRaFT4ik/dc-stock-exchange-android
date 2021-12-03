package ru.er_log.stock.domain.models

data class Deal(
    val lotPurchase: Lot,
    val lotSale: Lot,
    val timestampCreated: Long
)
