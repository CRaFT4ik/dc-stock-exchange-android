package ru.er_log.stock.data

import ru.er_log.stock.data.repositories.AuthRepositoryImpl
import ru.er_log.stock.domain.repositories.AuthRepository

object RepoLocator {
    val authRepository: AuthRepository by lazy { AuthRepositoryImpl(NetServices.authService) }
}