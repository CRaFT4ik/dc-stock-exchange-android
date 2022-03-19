package ru.er_log.stock.domain.repositories

import ru.er_log.stock.domain.models.`in`.Lot
import ru.er_log.stock.domain.models.`in`.OrderBook

interface ExchangeRepository {

    suspend fun getOrderBook(limit: Int): OrderBook

    suspend fun createOrder(lot: Lot)

    suspend fun createOffer(lot: Lot)
}