package ru.er_log.stock.android.base.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.scope.Scope
import org.koin.dsl.module
import ru.er_log.stock.android.features.splash.SplashViewModel
import ru.er_log.stock.android.base.utils.UserPrefsStore
import ru.er_log.stock.android.features.account.ProfileViewModel
import ru.er_log.stock.android.features.auth.login.LoginViewModel
import ru.er_log.stock.android.features.order_book.OrderBookViewModel
import ru.er_log.stock.android.features.settings.SettingsViewModel
import ru.er_log.stock.data.di.KoinModuleProvider

internal class AppModule : KoinModuleProvider {

    override fun module(): Module = module {
        provideViewModels()
        providePrefsStore()
    }

    private fun Module.provideViewModels() {
        viewModel { SplashViewModel(userPrefsStore = get()) }
        viewModel { LoginViewModel(authUseCases = get()) }
        viewModel { OrderBookViewModel(exchangeUseCases = get()) }
        viewModel { ProfileViewModel(accountUseCases = get(), exchangeUseCases = get()) }
        viewModel { SettingsViewModel(authUseCases = get(), userPrefsStore = get()) }
    }

    private fun Module.providePrefsStore() {
        single { UserPrefsStore(providePreferencesDataStore("user-prefs")) }
    }

    private fun Scope.providePreferencesDataStore(name: String): DataStore<Preferences> {
        val appContext: Context = get<Context>().applicationContext
        return PreferenceDataStoreFactory.create(
            corruptionHandler = null,
            migrations = listOf(),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        ) {
            appContext.preferencesDataStoreFile(name)
        }
    }
}