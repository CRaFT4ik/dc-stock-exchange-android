package ru.er_log.stock.android.base.utils

import kotlinx.coroutines.flow.MutableSharedFlow

object StockMessage {

    data class Message(
        val t: Throwable
    )

    val appErrors = MutableSharedFlow<Throwable>()

}
