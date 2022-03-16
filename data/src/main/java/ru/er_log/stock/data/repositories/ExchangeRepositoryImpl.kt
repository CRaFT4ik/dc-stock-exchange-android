package ru.er_log.stock.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.er_log.stock.data.network.NetworkResult
import ru.er_log.stock.data.network.api.v1.exchange.ExchangeService
import ru.er_log.stock.data.network.api.v1.exchange.map
import ru.er_log.stock.data.network.makeRequest
import ru.er_log.stock.domain.models.`in`.Lot
import ru.er_log.stock.domain.repositories.ExchangeRepository

internal class ExchangeRepositoryImpl(
    private val exchangeService: ExchangeService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ExchangeRepository {

    override suspend fun fetchOrderBook(limit: Int) = withContext(dispatcher) {
        when (val response = makeRequest { exchangeService.fetchOrdersBook(limit) }) {
            is NetworkResult.Failure -> throw Exception(response.errorMessage, response.t)
            is NetworkResult.Success -> response.value.map()
        }
    }

    override suspend fun createOrder(lot: Lot) {
        when (val response = makeRequest { exchangeService.createOrder(lot.map()) }) {
            is NetworkResult.Failure -> throw Exception(response.errorMessage, response.t)
            is NetworkResult.Success -> {}
        }
    }

    override suspend fun createOffer(lot: Lot) {
        when (val response = makeRequest { exchangeService.createOffer(lot.map()) }) {
            is NetworkResult.Failure -> throw Exception(response.errorMessage, response.t)
            is NetworkResult.Success -> {}
        }
    }
}