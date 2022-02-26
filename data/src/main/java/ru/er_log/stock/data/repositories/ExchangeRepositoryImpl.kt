package ru.er_log.stock.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ru.er_log.stock.data.network.ExchangeService
import ru.er_log.stock.data.network.NetworkResult
import ru.er_log.stock.data.network.makeRequest
import ru.er_log.stock.domain.api.v1.requests.LotCreationRequest
import ru.er_log.stock.domain.models.ActiveLots
import ru.er_log.stock.domain.models.Deal
import ru.er_log.stock.domain.repositories.ExchangeRepository

internal class ExchangeRepositoryImpl(
    private val exchangeService: ExchangeService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ExchangeRepository {

    override suspend fun fetchActiveLots(): Flow<ActiveLots> = withContext(dispatcher) {
        flow {
            when (val response = makeRequest { exchangeService.fetchActiveLots() }) {
                is NetworkResult.Failure -> throw Exception(response.errorMessage, response.t)
                is NetworkResult.Success -> emit(response.value.map())
            }
        }
    }

    override suspend fun fetchDeals(): List<Deal> = withContext(dispatcher) {
        when (val response = makeRequest { exchangeService.fetchDeals() }) {
            is NetworkResult.Failure -> throw Exception(response.errorMessage, response.t)
            is NetworkResult.Success -> response.value.map()
        }
    }

    override suspend fun createPurchaseLot(request: LotCreationRequest) {
        when (val response = makeRequest { exchangeService.createPurchaseLot(request) }) {
            is NetworkResult.Failure -> throw Exception(response.errorMessage, response.t)
            is NetworkResult.Success -> Unit
        }
    }

    override suspend fun createSaleLot(request: LotCreationRequest) {
        when (val response = makeRequest { exchangeService.createSaleLot(request) }) {
            is NetworkResult.Failure -> throw Exception(response.errorMessage, response.t)
            is NetworkResult.Success -> Unit
        }
    }
}