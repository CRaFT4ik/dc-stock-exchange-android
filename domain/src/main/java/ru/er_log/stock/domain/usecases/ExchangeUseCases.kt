package ru.er_log.stock.domain.usecases

import kotlinx.coroutines.flow.catch
import ru.er_log.stock.data.network.api.v1.exchange.LotCreationRequest
import ru.er_log.stock.domain.models.ActiveLots
import ru.er_log.stock.domain.models.exchange.Deal
import ru.er_log.stock.domain.models.`in`.Lot
import ru.er_log.stock.domain.models.order_book.OrderBookItem
import ru.er_log.stock.domain.repositories.ExchangeRepository

class ExchangeUseCases(private val exchangeRepository: ExchangeRepository) {

    val fetchActiveLots: UseCase<Int, ActiveLots> by lazy { FetchActiveLotsUseCase() }
    val fetchDeals: UseCase<Unit, List<Deal>> by lazy { FetchDealsUseCase() }
    val createPurchaseLot: UseCase<LotCreationRequest, Unit> by lazy { CreatePurchaseLotUseCase() }
    val createSaleLot: UseCase<LotCreationRequest, Unit> by lazy { CreateSaleLotUseCase() }

    private inner class FetchActiveLotsUseCase : UseCaseFlow<Int, ActiveLots>() {
        override suspend fun run(params: Int, onEach: suspend (Result<ActiveLots>) -> Unit) {
            exchangeRepository.fetchActiveLots(params)
                .catch { onEach(Result.failure(it)) }
                .collect { activeLots ->
                    fun List<Lot>.map() = map {
                        OrderBookItem(price = it.price, amount = it.amount)
                    }.toSortedSet(OrderBookItem.PriceAscComparator)

                    val orders = activeLots.lotOrders.map()
                    val offers = activeLots.lotOffers.map {
                        OrderBookItem(price = it.price, amount = it.amount)
                    }.toSortedSet(OrderBookItem.PriceAscComparator)

                    onEach(Result.success(activeLots))
                }
        }
    }

    private inner class FetchDealsUseCase : UseCaseValue<Unit, List<Deal>>() {
        override suspend fun run(params: Unit): Result<List<Deal>> {
            return Result.success(exchangeRepository.fetchDeals())
        }
    }

    private inner class CreatePurchaseLotUseCase : UseCaseValue<LotCreationRequest, Unit>() {
        override suspend fun run(params: LotCreationRequest): Result<Unit> {
            return Result.success(exchangeRepository.createPurchaseLot(params))
        }
    }

    private inner class CreateSaleLotUseCase : UseCaseValue<LotCreationRequest, Unit>() {
        override suspend fun run(params: LotCreationRequest): Result<Unit> {
            return Result.success(exchangeRepository.createSaleLot(params))
        }
    }
}