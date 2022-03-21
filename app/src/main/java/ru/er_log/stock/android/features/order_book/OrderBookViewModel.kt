package ru.er_log.stock.android.features.order_book

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import ru.er_log.stock.android.compose.components.order_book.OrderBookState
import ru.er_log.stock.domain.usecases.ExchangeUseCases

class OrderBookViewModel(
    private val exchangeUseCases: ExchangeUseCases
) : ViewModel() {

    private val _orderBookState = MutableStateFlow(value = OrderBookState())

    fun orderBookState(scope: CoroutineScope): StateFlow<OrderBookState> {
        return _orderBookState.apply {
            exchangeUseCases.getOrderBook(500, scope) {
                it.onSuccess { v -> delay(500); emit(OrderBookState(v)) }
            }
        }.asStateFlow()
    }

    override fun onCleared() {
        Log.d("CRTag", "On cleared: " + this::class.simpleName)
        super.onCleared()
    }
}