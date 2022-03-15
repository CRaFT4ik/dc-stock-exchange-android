package ru.er_log.stock.android.features.order_book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import ru.er_log.stock.android.compose.components.order_book.OrderBookState
import ru.er_log.stock.domain.models.`in`.Lot
import ru.er_log.stock.domain.usecases.ExchangeUseCases

class OrderBookViewModel(
    private val exchangeUseCases: ExchangeUseCases
) : ViewModel() {

    private val _lotsPurchase: MutableStateFlow<List<Lot>> = MutableStateFlow(listOf())
    val lotsPurchase = _lotsPurchase.asStateFlow()

    private val _lotsSale: MutableStateFlow<List<Lot>> = MutableStateFlow(listOf())
    val lotsSale = _lotsSale.asStateFlow()

    private val _errors: MutableSharedFlow<Throwable> = MutableSharedFlow()
    val errors = _errors.asSharedFlow()

    fun loadActiveLots() {
        exchangeUseCases.fetchActiveLots(Int, viewModelScope) {
            it.onSuccess { v ->
                _lotsPurchase.value = v.lotOrders
                _lotsSale.value = v.lotOffers
            }
            it.onFailure { e -> _errors.emit(e) }
        }
    }

    private val _orderBookState: MutableSharedFlow<OrderBookState> = MutableSharedFlow()
    val orderBookState = _orderBookState.asSharedFlow().apply {
        exchangeUseCases.fetchActiveLots(50, viewModelScope) {
            it.onSuccess { v ->
                val orderBookState = OrderBookState(
                    orders = sortedSetOf(v.lotOrders, Order))
                _orderBookState.emit() v.lot
            }
        }
    }
}