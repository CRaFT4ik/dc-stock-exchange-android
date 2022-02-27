package ru.er_log.stock.android.base

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ru.er_log.stock.android.BuildConfig
import ru.er_log.stock.android.base.di.AppModule
import ru.er_log.stock.data.di.DataModule
import ru.er_log.stock.data.di.DomainModule
import ru.er_log.stock.data.di.modules

class StockExchangeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@StockExchangeApplication)
            modules(listOf(AppModule(), DomainModule(), DataModule()).modules())
        }
    }
}