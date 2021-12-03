package ru.er_log.stock.domain.usecases

import ru.er_log.stock.domain.repositories.ExchangeRepository

class ExchangeUseCase(private val repository: ExchangeRepository) {

    fun activeLots() = repository.fetchActiveLots()

    fun deals() = repository.fetchDeals()
}