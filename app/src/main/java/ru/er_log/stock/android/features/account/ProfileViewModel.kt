package ru.er_log.stock.android.features.account

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.er_log.stock.android.base.utils.StockMessage
import ru.er_log.stock.domain.models.`in`.Lot
import ru.er_log.stock.domain.models.`in`.Transaction
import ru.er_log.stock.domain.models.`in`.UserCard
import ru.er_log.stock.domain.models.`in`.UserInfo
import ru.er_log.stock.domain.usecases.AccountUseCases
import ru.er_log.stock.domain.usecases.ExchangeUseCases
import java.math.BigDecimal

class ProfileViewModel(
    private val accountUseCases: AccountUseCases,
    private val exchangeUseCases: ExchangeUseCases
) : ViewModel() {

    private val userStub = UserCard(
        userInfo = UserInfo("...", "..."),
        userBalance = BigDecimal.ZERO,
        statistics = UserCard.TransactionStatistics(0, 0, 0, 0)
    )

    private val _userCard = MutableStateFlow<UserCard>(userStub)
    private val _transactions: MutableStateFlow<List<Transaction>> = MutableStateFlow(listOf())

    fun userCard(scope: CoroutineScope) = _userCard.apply {
        accountUseCases.getUserCard(Unit, viewModelScope) {
            it.onSuccess { v -> delay(200); emit(v) }
            it.onFailure { v -> StockMessage.appErrors.emit(v) }
        }
    }.asStateFlow()

    fun transactions(scope: CoroutineScope) = _transactions.apply {
        val page = 0
        accountUseCases.getOperations(page, scope) {
            it.onSuccess { v -> delay(200); emit(v) }
            it.onFailure { v -> StockMessage.appErrors.emit(v) }
        }
    }.asStateFlow()

    fun onCreateOrder(order: Lot) {
        exchangeUseCases.createOrder(order)
    }

    fun onCreateOffer(offer: Lot) {
        exchangeUseCases.createOffer(offer)
    }

    override fun onCleared() {
        Log.d("CRTag", "On cleared: " + this::class.simpleName)
        super.onCleared()
    }
}