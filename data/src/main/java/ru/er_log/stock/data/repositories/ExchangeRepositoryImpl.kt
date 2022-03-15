package ru.er_log.stock.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ru.er_log.stock.data.network.api.v1.account.AccountService
import ru.er_log.stock.data.network.NetworkResult
import ru.er_log.stock.data.network.makeRequest
import ru.er_log.stock.data.network.api.v1.exchange.LotCreationRequest
import ru.er_log.stock.domain.models.ActiveLots
import ru.er_log.stock.domain.models.exchange.Deal
import ru.er_log.stock.domain.repositories.ExchangeRepository

internal class ExchangeRepositoryImpl(
    private val accountService: AccountService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ExchangeRepository {

    override suspend fun fetchActiveLots(count: Int): Flow<ActiveLots> = withContext(dispatcher) {
        flow {
            when (val response = makeRequest { accountService.fetchTransactions(count) }) {
                is NetworkResult.Failure -> throw Exception(response.errorMessage, response.t)
                is NetworkResult.Success -> emit(response.value.map())
            }
        }
    }

    override suspend fun fetchDeals(): List<Deal> = withContext(dispatcher) {
        when (val response = makeRequest { accountService.fetchDeals() }) {
            is NetworkResult.Failure -> throw Exception(response.errorMessage, response.t)
            is NetworkResult.Success -> response.value.map()
        }
    }

    override suspend fun createPurchaseLot(request: LotCreationRequest) {
        when (val response = makeRequest { accountService.createPurchaseLot(request) }) {
            is NetworkResult.Failure -> throw Exception(response.errorMessage, response.t)
            is NetworkResult.Success -> Unit
        }
    }

    override suspend fun createSaleLot(request: LotCreationRequest) {
        when (val response = makeRequest { accountService.createSaleLot(request) }) {
            is NetworkResult.Failure -> throw Exception(response.errorMessage, response.t)
            is NetworkResult.Success -> Unit
        }
    }
}