package ru.er_log.stock.data

import ru.er_log.stock.data.repositories.AuthRepositoryImpl
import ru.er_log.stock.data.repositories.AuthDataStorage
import ru.er_log.stock.data.repositories.ExchangeRepositoryImpl
import ru.er_log.stock.domain.repositories.AuthRepository
import ru.er_log.stock.domain.repositories.ExchangeRepository

class RepoLocator(storage: AuthDataStorage) {
    private val netServices = NetServices(storage)

    val authRepo: AuthRepository by lazy { AuthRepositoryImpl(netServices.authService) }

    val exchangeRepo: ExchangeRepository by lazy { ExchangeRepositoryImpl(netServices.exchangeService) }
}