package ru.er_log.stock.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.er_log.stock.data.network.ExchangeService
import ru.er_log.stock.data.network.NetworkResult
import ru.er_log.stock.data.network.makeRequest
import ru.er_log.stock.domain.boundaries.requests.LotCreationRequest
import ru.er_log.stock.domain.models.ActiveLots
import ru.er_log.stock.domain.models.Deal
import ru.er_log.stock.domain.repositories.ExchangeRepository

internal class ExchangeRepositoryImpl(
    private val api: ExchangeService
) : ExchangeRepository {

    override fun fetchActiveLots(): Flow<ActiveLots> = flow {
        when (val response = makeRequest { api.fetchActiveLots() }) {
            is NetworkResult.Failure -> throw Exception(response.errorMessage, response.t)
            is NetworkResult.Success -> emit(response.value.map())
        }
    }

    override fun fetchDeals(): Flow<List<Deal>> = flow {
        when (val response = makeRequest { api.fetchDeals() }) {
            is NetworkResult.Failure -> throw Exception(response.errorMessage, response.t)
            is NetworkResult.Success -> emit(response.value.map())
        }
    }

    override fun createPurchaseLot(request: LotCreationRequest): Flow<Unit> = flow {
        when (val response = makeRequest { api.createPurchaseLot(request) }) {
            is NetworkResult.Failure -> throw Exception(response.errorMessage, response.t)
            is NetworkResult.Success -> emit(Unit)
        }
    }

    override fun createSaleLot(request: LotCreationRequest): Flow<Unit> = flow {
        when (val response = makeRequest { api.createSaleLot(request) }) {
            is NetworkResult.Failure -> throw Exception(response.errorMessage, response.t)
            is NetworkResult.Success -> emit(Unit)
        }
    }
}