package ru.er_log.stock.android.presentation.exchange.deals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import ru.er_log.stock.domain.models.Deal
import ru.er_log.stock.domain.usecases.ExchangeUseCases

class CompletedDealsViewModel(
    private val exchangeUseCases: ExchangeUseCases
) : ViewModel() {

    private val _deals: MutableStateFlow<List<Deal>> = MutableStateFlow(listOf())
    val deals = _deals.asStateFlow()

    private val _errors: MutableSharedFlow<Throwable> = MutableSharedFlow()
    val errors = _errors.asSharedFlow()

    fun loadCompletedDeals() {
        exchangeUseCases.fetchDeals(Unit, viewModelScope) {
            it.onSuccess { v -> _deals.value = v }
            it.onFailure { e -> _errors.emit(e) }
        }
    }
}