package ru.er_log.stock.android.ui.exchange.deals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import ru.er_log.stock.android.UseCaseLocator
import ru.er_log.stock.domain.models.Deal
import ru.er_log.stock.domain.models.Lot

class CompletedDealsViewModel : ViewModel() {

    private val _deals: MutableStateFlow<List<Deal>> = MutableStateFlow(listOf())
    val deals = _deals.asStateFlow()

    private val _errors: MutableSharedFlow<Throwable> = MutableSharedFlow()
    val errors = _errors.asSharedFlow()

    private val exchangeUseCase = UseCaseLocator.exchangeUseCase()

    fun loadCompletedDeals() {
        exchangeUseCase.deals()
            .catch { _errors.emit(it) }
            .onEach {
                _deals.value = it
            }
            .launchIn(viewModelScope)
    }
}