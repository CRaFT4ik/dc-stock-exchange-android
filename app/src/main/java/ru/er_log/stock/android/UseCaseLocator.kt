package ru.er_log.stock.android

import ru.er_log.stock.data.RepoLocator
import ru.er_log.stock.domain.usecases.AuthUseCase

object UseCaseLocator {
    fun authUseCase(): AuthUseCase = AuthUseCase(RepoLocator.authRepository)
}