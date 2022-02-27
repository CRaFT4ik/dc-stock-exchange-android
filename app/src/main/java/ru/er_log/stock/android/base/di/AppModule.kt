package ru.er_log.stock.android.base.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.er_log.stock.android.base.storages.PreferencesStorage
import ru.er_log.stock.android.features.auth.AuthViewModel
import ru.er_log.stock.android.features.exchange.ExchangeViewModel
import ru.er_log.stock.data.di.KoinModuleProvider
import ru.er_log.stock.data.repositories.AuthDataStorage

/**
 * App module, that includes UI components, platform-specific storages, etc.
 */
internal class AppModule : KoinModuleProvider {

    override fun module(): Module = module {
        provideViewModels()
        provideStorages()
    }

    private fun Module.provideViewModels() {
        viewModel<AuthViewModel> {
            AuthViewModel(authUseCase = get())
        }

        viewModel<ExchangeViewModel> {
            ExchangeViewModel(exchangeUseCases = get())
        }
    }

    private fun Module.provideStorages() {
        factory<AuthDataStorage> {
            PreferencesStorage(context = get())
        }
    }
}