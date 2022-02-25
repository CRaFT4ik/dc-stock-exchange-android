package ru.er_log.stock.domain.usecases

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import ru.er_log.stock.domain.api.v1.requests.LotCreationRequest
import ru.er_log.stock.domain.models.ActiveLots
import ru.er_log.stock.domain.models.Deal
import ru.er_log.stock.domain.repositories.ExchangeRepository

class ExchangeUseCases(private val exchangeRepository: ExchangeRepository) {

    val fetchActiveLots: UseCase<Unit, ActiveLots> by lazy { FetchActiveLotsUseCase() }
    val fetchDeals: UseCase<Unit, List<Deal>> by lazy { FetchDealsUseCase() }
    val createPurchaseLot: UseCase<LotCreationRequest, Unit> by lazy { CreatePurchaseLotUseCase() }
    val createSaleLot: UseCase<LotCreationRequest, Unit> by lazy { CreateSaleLotUseCase() }

    private inner class FetchActiveLotsUseCase : UseCaseFlow<Unit, ActiveLots>() {
        override suspend fun run(params: Unit, onEach: suspend (Result<ActiveLots>) -> Unit) {
            exchangeRepository.fetchActiveLots()
                .catch { onEach(Result.failure(it)) }
                .collect { onEach(Result.success(it)) }
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