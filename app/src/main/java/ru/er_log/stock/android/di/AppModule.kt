package ru.er_log.stock.android.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.er_log.stock.android.PreferencesStorage
import ru.er_log.stock.android.presentation.auth.AuthViewModel
import ru.er_log.stock.android.presentation.exchange.ExchangeViewModel
import ru.er_log.stock.data.repositories.AuthDataStorage

/**
 * App module, that includes UI components, platform-specific storages, etc.
 */
val appModule = module {

    viewModel<AuthViewModel> {
        AuthViewModel(authUseCase = get())
    }

    viewModel<ExchangeViewModel> {
        ExchangeViewModel(exchangeUseCases = get())
    }

    factory<AuthDataStorage> {
        PreferencesStorage(context = get())
    }
}