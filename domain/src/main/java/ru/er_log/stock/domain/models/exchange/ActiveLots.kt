package ru.er_log.stock.domain.models.exchange

data class ActiveLots(
    val lotPurchases: List<Lot>,
    val lotSales: List<Lot>
)
