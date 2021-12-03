package ru.er_log.stock.android.ui.exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import ru.er_log.stock.android.UseCaseLocator
import ru.er_log.stock.domain.boundaries.requests.LotCreationRequest
import ru.er_log.stock.domain.models.Deal
import ru.er_log.stock.domain.models.Lot
import java.math.BigDecimal

class ExchangeViewModel : ViewModel() {

    private val _feedback: MutableStateFlow<String> = MutableStateFlow("")
    val feedback = _feedback.asStateFlow()

    private val _errors: MutableSharedFlow<Throwable> = MutableSharedFlow()
    val errors = _errors.asSharedFlow()

    private val exchangeUseCase = UseCaseLocator.exchangeUseCase()

    fun createLot(price: BigDecimal, isPurchase: Boolean) {
        val request = LotCreationRequest(price)
        val useCase = if (isPurchase) {
            exchangeUseCase.createPurchaseLot(request)
        } else {
            exchangeUseCase.createSaleLot(request)
        }

        useCase
            .catch { _errors.emit(it) }
            .onEach {
                _feedback.value = "Successfully created"
            }
            .launchIn(viewModelScope)
    }
}