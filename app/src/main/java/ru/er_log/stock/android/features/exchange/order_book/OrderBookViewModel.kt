package ru.er_log.stock.android.features.exchange.order_book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import ru.er_log.stock.domain.models.exchange.Lot
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
        exchangeUseCases.fetchActiveLots(Unit, viewModelScope) {
            it.onSuccess { v ->
                _lotsPurchase.value = v.lotPurchases
                _lotsSale.value = v.lotSales
            }
            it.onFailure { e -> _errors.emit(e) }
        }
    }
}