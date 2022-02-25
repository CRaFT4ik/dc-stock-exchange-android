package ru.er_log.stock.android

import ru.er_log.stock.data.RepoLocator
import ru.er_log.stock.domain.usecases.AuthUseCases
import ru.er_log.stock.domain.usecases.ExchangeUseCases

object UseCaseLocator {

    private val repoLocator = RepoLocator(PreferencesStorage)

    fun authUseCase(): AuthUseCases = AuthUseCases(repoLocator.authRepo)

    fun exchangeUseCase(): ExchangeUseCases = ExchangeUseCases(repoLocator.exchangeRepo)
}