package ru.er_log.stock.android.features.order_book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.er_log.stock.android.compose.components.order_book.OrderBookState
import ru.er_log.stock.domain.usecases.ExchangeUseCases

class OrderBookViewModel(
    private val exchangeUseCases: ExchangeUseCases
) : ViewModel() {

    private val _orderBookState: MutableSharedFlow<OrderBookState> = MutableSharedFlow()
    val orderBookState = _orderBookState.apply {
        exchangeUseCases.fetchOrderBook(500, viewModelScope) {
            it.onSuccess { v -> emit(OrderBookState(v)) }
        }
    }.asSharedFlow()
}