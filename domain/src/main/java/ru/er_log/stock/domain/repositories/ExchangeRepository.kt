package ru.er_log.stock.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.er_log.stock.domain.api.v1.requests.LotCreationRequest
import ru.er_log.stock.domain.models.ActiveLots
import ru.er_log.stock.domain.models.Deal

interface ExchangeRepository {

    suspend fun fetchActiveLots(): Flow<ActiveLots>

    suspend fun fetchDeals(): List<Deal>

    suspend fun createPurchaseLot(request: LotCreationRequest)

    suspend fun createSaleLot(request: LotCreationRequest)
}