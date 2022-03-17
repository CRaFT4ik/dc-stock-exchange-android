package ru.er_log.stock.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.TypeQualifier
import org.koin.core.scope.Scope
import ru.er_log.stock.data.storages.AuthDataStore
import ru.er_log.stock.data.storages.database.StockDatabase
import ru.er_log.stock.data.storages.database.daos.UserDao

internal class StoragesComponent : KoinModuleComponent() {

    override fun Module.provide() {
        single { provideStockDatabase() }
        provideDAOs()

        single { AuthDataStore(providePreferencesDataStore("auth")) }
    }

    private fun Module.provideDAOs() {
        factory<UserDao> { get<StockDatabase>().userDao() }
    }

    private fun Scope.provideStockDatabase(): StockDatabase {
        return Room.databaseBuilder(
            get<Context>().applicationContext,
            StockDatabase::class.java, "stock-database"
        ).build()
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