package ru.er_log.stock.data.network.api

internal interface Mappable<T> {
    fun map(): T
}

internal fun <A> Collection<Mappable<A>>.map(): List<A> = map { it.map() }