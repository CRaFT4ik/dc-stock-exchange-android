package ru.er_log.stock.android.base.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.er_log.stock.android.features.account.ProfileViewModel
import ru.er_log.stock.android.features.auth.AuthViewModel
import ru.er_log.stock.android.features.auth.login.LoginViewModel
import ru.er_log.stock.android.features.order_book.OrderBookViewModel
import ru.er_log.stock.data.di.KoinModuleProvider

internal class AppModule : KoinModuleProvider {

    override fun module(): Module = module {
        provideViewModels()
    }

    private fun Module.provideViewModels() {
        viewModel { AuthViewModel(authUseCases = get()) }
        viewModel { LoginViewModel(authUseCases = get()) }
        viewModel { ProfileViewModel(accountUseCases = get(), exchangeUseCases = get()) }
        viewModel { OrderBookViewModel(exchangeUseCases = get()) }
    }
}