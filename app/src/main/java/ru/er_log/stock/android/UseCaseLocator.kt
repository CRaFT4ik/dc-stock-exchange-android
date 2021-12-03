package ru.er_log.stock.android

import ru.er_log.stock.data.RepoLocator
import ru.er_log.stock.domain.usecases.AuthUseCase
import ru.er_log.stock.domain.usecases.ExchangeUseCase

object UseCaseLocator {

    private val repoLocator = RepoLocator(PreferencesStorage)

    fun authUseCase(): AuthUseCase = AuthUseCase(repoLocator.authRepo)

    fun exchangeUseCase(): ExchangeUseCase = ExchangeUseCase(repoLocator.exchangeRepo)
}