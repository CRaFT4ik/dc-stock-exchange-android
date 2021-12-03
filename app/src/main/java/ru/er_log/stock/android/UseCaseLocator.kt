package ru.er_log.stock.android

import ru.er_log.stock.data.RepoLocator
import ru.er_log.stock.domain.usecases.AuthUseCase

object UseCaseLocator {

    private val repoLocator = RepoLocator(PreferencesStorage)

    fun authUseCase(): AuthUseCase = AuthUseCase(repoLocator.authRepository)
}