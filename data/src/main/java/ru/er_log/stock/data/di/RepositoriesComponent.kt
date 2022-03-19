package ru.er_log.stock.data.di

import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import ru.er_log.stock.data.repositories.AccountRepositoryImpl
import ru.er_log.stock.data.repositories.AuthRepositoryImpl
import ru.er_log.stock.data.repositories.ExchangeRepositoryImpl
import ru.er_log.stock.domain.repositories.AccountRepository
import ru.er_log.stock.domain.repositories.AuthRepository
import ru.er_log.stock.domain.repositories.ExchangeRepository

internal class RepositoriesComponent : KoinModuleComponent() {

    override fun Module.provide() {
        val reposDispatcher = Dispatchers.IO

        single<AuthRepository> {
            AuthRepositoryImpl(
                userDao = get(),
                authService = get(),
                authStore = get(),
                dispatcher = reposDispatcher
            )
        }

        single<AccountRepository> {
            AccountRepositoryImpl(accountService = get(), dispatcher = reposDispatcher)
        }

        single<ExchangeRepository> {
            ExchangeRepositoryImpl(exchangeService = get(), dispatcher = reposDispatcher)
        }
    }
}