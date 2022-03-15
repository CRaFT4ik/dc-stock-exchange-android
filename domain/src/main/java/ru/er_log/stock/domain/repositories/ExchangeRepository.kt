package ru.er_log.stock.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.er_log.stock.data.network.api.v1.exchange.LotCreationRequest
import ru.er_log.stock.domain.models.ActiveLots
import ru.er_log.stock.domain.models.exchange.Deal

interface ExchangeRepository {

    suspend fun fetchActiveLots(count: Int): Flow<ActiveLots>

    suspend fun fetchDeals(): List<Deal>

    suspend fun createPurchaseLot(request: LotCreationRequest)

    suspend fun createSaleLot(request: LotCreationRequest)
}