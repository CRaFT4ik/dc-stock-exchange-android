package ru.er_log.stock.domain.api

interface Mappable<T> {
    fun map(): T
}

fun <A> Collection<Mappable<A>>.map(): List<A> = map { it.map() }