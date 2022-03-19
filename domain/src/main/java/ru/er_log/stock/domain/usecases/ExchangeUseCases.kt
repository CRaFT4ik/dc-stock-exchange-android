package ru.er_log.stock.domain.usecases

import ru.er_log.stock.domain.models.`in`.Lot
import ru.er_log.stock.domain.models.`in`.OrderBook
import ru.er_log.stock.domain.repositories.ExchangeRepository

class ExchangeUseCases(private val exchangeRepository: ExchangeRepository) {

    val getOrderBook: UseCase<Int, OrderBook> by lazy { ObserveOrderBookUseCase() }
    val createOrder: UseCase<Lot, Unit> by lazy { CreateOrderUseCase() }
    val createOffer: UseCase<Lot, Unit> by lazy { CreateOfferUseCase() }

    private inner class ObserveOrderBookUseCase : UseCaseRepeatable<Int, OrderBook>(delayMs = 10000) {
        override suspend fun run(limit: Int): Result<OrderBook> {
            return Result.success(exchangeRepository.getOrderBook(limit))
        }
    }

    private inner class CreateOrderUseCase : UseCaseValue<Lot, Unit>() {
        override suspend fun run(params: Lot): Result<Unit> {
            return Result.success(exchangeRepository.createOrder(params))
        }
    }

    private inner class CreateOfferUseCase : UseCaseValue<Lot, Unit>() {
        override suspend fun run(params: Lot): Result<Unit> {
            return Result.success(exchangeRepository.createOffer(params))
        }
    }
}