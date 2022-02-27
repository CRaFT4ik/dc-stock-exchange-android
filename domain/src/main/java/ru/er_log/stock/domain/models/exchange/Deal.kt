package ru.er_log.stock.domain.models.exchange

data class Deal(
    val lotPurchase: Lot,
    val lotSale: Lot,
    val timestampCreated: Long
)
