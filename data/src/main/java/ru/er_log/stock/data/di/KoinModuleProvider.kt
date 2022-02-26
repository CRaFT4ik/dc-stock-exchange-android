package ru.er_log.stock.data.di

import org.koin.core.module.Module

interface KoinModuleProvider {
    fun module(): Module
}

abstract class KoinModuleComponent {
    protected abstract fun Module.provide()

    fun attachTo(module: Module) {
        module.apply { provide() }
    }
}

fun Module.provide(comp: KoinModuleComponent) {
    comp.attachTo(this)
}

fun Collection<KoinModuleProvider>.modules(): List<Module> {
    return fold(mutableListOf()) { acc, el -> acc.add(el.module()); acc }
}