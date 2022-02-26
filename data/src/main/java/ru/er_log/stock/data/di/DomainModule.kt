package ru.er_log.stock.data.di

import org.koin.dsl.module
import ru.er_log.stock.domain.usecases.AuthUseCases
import ru.er_log.stock.domain.usecases.ExchangeUseCases

val domainModule = module {

    factory<AuthUseCases> {
        AuthUseCases(authRepository = get())
    }

    factory<ExchangeUseCases> {
        ExchangeUseCases(exchangeRepository = get())
    }
}