package ru.er_log.stock.data.di

import org.koin.core.module.Module
import org.koin.dsl.module
import ru.er_log.stock.domain.usecases.AccountUseCases
import ru.er_log.stock.domain.usecases.AuthUseCases
import ru.er_log.stock.domain.usecases.ExchangeUseCases

class DomainModule : KoinModuleProvider {

    override fun module(): Module = module {
        provideUseCases()
    }

    private fun Module.provideUseCases() {
        factory<AuthUseCases> {
            AuthUseCases(authRepository = get())
        }

        factory<AccountUseCases> {
            AccountUseCases(accountRepository = get())
        }

        factory<ExchangeUseCases> {
            ExchangeUseCases(exchangeRepository = get())
        }
    }
}