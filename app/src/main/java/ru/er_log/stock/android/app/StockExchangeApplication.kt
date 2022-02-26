package ru.er_log.stock.android.app

import android.app.Application
import android.util.Log
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ru.er_log.stock.android.BuildConfig
import ru.er_log.stock.android.di.appModule
import ru.er_log.stock.data.di.dataModule
import ru.er_log.stock.data.di.domainModule

class StockExchangeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@StockExchangeApplication)
            modules(appModule + domainModule + dataModule)
        }
    }
}