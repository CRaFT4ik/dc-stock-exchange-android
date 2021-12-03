package ru.er_log.stock.data

import ru.er_log.stock.data.repositories.AuthRepositoryImpl
import ru.er_log.stock.data.repositories.AuthTokenStorage
import ru.er_log.stock.domain.repositories.AuthRepository

class RepoLocator(storage: AuthTokenStorage) {
    private val netServices = NetServices(storage)

    val authRepository: AuthRepository by lazy { AuthRepositoryImpl(netServices.authService) }
}