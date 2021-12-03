package ru.er_log.stock.android

import android.app.Application

class CurrentApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initSingletons()
    }

    private fun initSingletons() {
        PreferencesStorage.updateContext(applicationContext)
    }
}