package ru.er_log.stock.android.base.utils

import kotlinx.coroutines.flow.MutableSharedFlow

object StockMessage {

    val appErrors = MutableSharedFlow<Throwable>()
}
