package ru.er_log.stock.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.er_log.stock.domain.api.v1.exchange.LotCreationRequest
import ru.er_log.stock.domain.models.exchange.ActiveLots
import ru.er_log.stock.domain.models.exchange.Deal

interface ExchangeRepository {

    suspend fun fetchActiveLots(): Flow<ActiveLots>

    suspend fun fetchDeals(): List<Deal>

    suspend fun createPurchaseLot(request: LotCreationRequest)

    suspend fun createSaleLot(request: LotCreationRequest)
}