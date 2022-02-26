package ru.er_log.stock.data.di

import org.koin.core.module.Module
import org.koin.dsl.module

class DataModule : KoinModuleProvider {

    private val components: List<KoinModuleComponent> = listOf(
        RepositoriesComponent(),
        NetworkComponent()
    )

    override fun module(): Module = module {
        components.forEach { provide(it) }
    }
}