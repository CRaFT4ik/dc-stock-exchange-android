package ru.er_log.stock.android.ui

import android.app.Application
import ru.er_log.stock.android.PreferencesStorage

class CurrentApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initSingletons()
    }

    private fun initSingletons() {
        PreferencesStorage.updateContext(applicationContext)
    }
}