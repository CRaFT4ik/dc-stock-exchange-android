package ru.er_log.stock.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.er_log.stock.domain.api.v1.requests.LotCreationRequest
import ru.er_log.stock.domain.models.ActiveLots
import ru.er_log.stock.domain.models.Deal

interface ExchangeRepository {

    fun fetchActiveLots(): Flow<ActiveLots>

    fun fetchDeals(): Flow<List<Deal>>

    fun createPurchaseLot(request: LotCreationRequest): Flow<Unit>

    fun createSaleLot(request: LotCreationRequest): Flow<Unit>
}