package ru.er_log.stock.android.ui.exchange.active_lots

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import ru.er_log.stock.android.UseCaseLocator
import ru.er_log.stock.domain.models.Lot

class ActiveLotsViewModel : ViewModel() {

    private val _lotsPurchase: MutableStateFlow<List<Lot>> = MutableStateFlow(listOf())
    val lotsPurchase = _lotsPurchase.asStateFlow()

    private val _lotsSale: MutableStateFlow<List<Lot>> = MutableStateFlow(listOf())
    val lotsSale = _lotsSale.asStateFlow()

    private val _errors: MutableSharedFlow<Throwable> = MutableSharedFlow()
    val errors = _errors.asSharedFlow()

    private val exchangeUseCase = UseCaseLocator.exchangeUseCase()

    fun loadActiveLots() {
        exchangeUseCase.activeLots()
            .catch { _errors.emit(it) }
            .onEach {
                _lotsPurchase.value = it.lotPurchases
                _lotsSale.value = it.lotSales
            }
            .launchIn(viewModelScope)
    }
}