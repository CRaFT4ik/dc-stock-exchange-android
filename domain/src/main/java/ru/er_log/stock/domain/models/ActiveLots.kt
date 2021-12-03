package ru.er_log.stock.domain.models

data class ActiveLots(
    val lotPurchases: List<Lot>,
    val lotSales: List<Lot>
)
