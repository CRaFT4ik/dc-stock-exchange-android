package ru.er_log.stock.android.features.home.exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import ru.er_log.stock.domain.api.v1.exchange.LotCreationRequest
import ru.er_log.stock.domain.usecases.ExchangeUseCases
import java.math.BigDecimal

class ExchangeViewModel(
    private val exchangeUseCases: ExchangeUseCases
) : ViewModel() {

    private val _feedback: MutableSharedFlow<String> = MutableSharedFlow()
    val feedback = _feedback.asSharedFlow()

    private val _errors: MutableSharedFlow<Throwable> = MutableSharedFlow()
    val errors = _errors.asSharedFlow()

    fun createLot(price: BigDecimal, isPurchase: Boolean) {
        val request = LotCreationRequest(price, BigDecimal.ONE)
        when (isPurchase) {
            true -> exchangeUseCases.createPurchaseLot(request, viewModelScope, ::onCreateResult)
            else -> exchangeUseCases.createSaleLot(request, viewModelScope, ::onCreateResult)
        }
    }

    private suspend fun onCreateResult(result: Result<Unit>) {
        result.onSuccess { _feedback.emit("Successfully created") }
        result.onFailure { _errors.emit(it) }
    }
}