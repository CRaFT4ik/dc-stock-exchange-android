package ru.er_log.stock.domain.usecases

import ru.er_log.stock.domain.boundaries.requests.LotCreationRequest
import ru.er_log.stock.domain.repositories.ExchangeRepository

class ExchangeUseCase(private val repository: ExchangeRepository) {

    fun activeLots() = repository.fetchActiveLots()

    fun deals() = repository.fetchDeals()

    fun createPurchaseLot(request: LotCreationRequest) = repository.createPurchaseLot(request)

    fun createSaleLot(request: LotCreationRequest) = repository.createSaleLot(request)
}